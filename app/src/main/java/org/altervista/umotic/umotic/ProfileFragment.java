package org.altervista.umotic.umotic;


import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static android.support.v4.content.PermissionChecker.checkSelfPermission;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private int STORAGE_PERMISSION_CODE = 1;
    private String requestCommand;
    // variabile utilizzata per prendere un'immagine dalla galleria
    public static final int PICK_IMAGE = 100;
    // variabile utilizzata per settare il nome dell'immagine del profilo in maniera univoca per utente
    public static final String USERIMG = "photoUser_id";
    private Uri imageUri;
    private static final String TAG1 = "ProfileActivity";
    private EditText username, name, surname, email, cf, password, rPassword;
    private String usernameS, nameS, surnameS, emailS, birthdayS, cfS, passwordS, imgPath;
    private TextView data;
    private Button update, commit;
    private SharedPreferences dataUser;
    private ImageView userImg, addImage, takeImage;

    // variabili utilizzate per il salvataggio e la conseguente lettura dell'immagine dal server
    private String encoded_string, image_name;
    private Bitmap bitmap;
    private File file;
    private Uri file_uri;
    // variabile utilizzata per poter selezionare la data di nascita per l'Utente
    DatePickerDialog.OnDateSetListener mDateSetListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_profile, container, false);

        //to solve 'android.os.FileUriExposedException: file.jpg exposed beyond app through ClipData.Item.getUri()'  error
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        username = (EditText) v.findViewById(R.id.txt_User);
        surname = (EditText) v.findViewById(R.id.txt_Surname);
        name = (EditText) v.findViewById(R.id.txt_Name);
        email = (EditText) v.findViewById(R.id.txt_Mail);
        data = (TextView) v.findViewById(R.id.txt_Birth);
        data.setOnClickListener(setBirthday);
        cf = (EditText) v.findViewById(R.id.txt_Cf);
        password = (EditText) v.findViewById(R.id.txt_Password);
        rPassword = (EditText) v.findViewById(R.id.txt_Rpass);
        update = (Button) v.findViewById(R.id.btn_Update);
        update.setOnClickListener(updateButtonListener);
        commit = (Button) v.findViewById(R.id.btn_Commit);
        commit.setOnClickListener(commitButtonListener);
        // addImage --> prendere l'immagine dalla galleria
        addImage = (ImageView) v.findViewById(R.id.addImage);
        // takeImage --> prendere l'immagine dalla fotocamera
        takeImage = (ImageView) v.findViewById(R.id.takeImage);
        userImg = (ImageView) v.findViewById(R.id.imageView);


        username.setEnabled(false);
        name.setEnabled(false);
        surname.setEnabled(false);
        email.setEnabled(false);
        data.setEnabled(false);
        cf.setEnabled(false);
        password.setEnabled(false);
        rPassword.setVisibility(View.GONE);
        commit.setVisibility(View.GONE);
        update.setVisibility(View.VISIBLE);
        SharedPreferences prefs = Objects.requireNonNull(getActivity()).getSharedPreferences("dataUser", MODE_PRIVATE);

        // scaricare l'immagine del profilo all'apertura dell'activity
        new DownloadImage().execute();

        username.setText(prefs.getString("username", ""));
        name.setText(prefs.getString("name", ""));
        surname.setText(prefs.getString("surname", ""));
        data.setText(prefs.getString("data", ""));
        email.setText(prefs.getString("email", ""));
        cf.setText(prefs.getString("cf", ""));
        password.setText(prefs.getString("password", ""));

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCommand = "gallery";
                if(checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                }else requestRecordPermission();
            }
        });


        takeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCommand = "camera";
                if(checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    try {
                        openCamera();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else requestRecordPermission();
            }
        });

        return v;
    }



    private void refresh(){
        //refresh
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }
    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    private void openCamera() throws IOException {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        getFileUri();
        i.putExtra(MediaStore.EXTRA_OUTPUT, file_uri);
        startActivityForResult(i, 10);
    }

    private void getFileUri(){

        dataUser = getActivity().getSharedPreferences("dataUser", MODE_PRIVATE);
        String id = dataUser.getString("id", null);

        image_name = USERIMG + id;
        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + File.separator + image_name
        );

        file_uri = Uri.fromFile(file);

    }

    public static Bitmap handleSamplingAndRotationBitmap(Context context, Uri selectedImage)
            throws IOException {
        int MAX_HEIGHT = 1024;
        int MAX_WIDTH = 1024;

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream imageStream = context.getContentResolver().openInputStream(selectedImage);
        BitmapFactory.decodeStream(imageStream, null, options);
        imageStream.close();

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        imageStream = context.getContentResolver().openInputStream(selectedImage);
        Bitmap img = BitmapFactory.decodeStream(imageStream, null, options);

        img = rotateImageIfRequired(context, img, selectedImage);
        return img;
    }

    /**
     * Calculate an inSampleSize for use in a {@link BitmapFactory.Options} object when decoding
     * bitmaps using the decode* methods from {@link BitmapFactory}. This implementation calculates
     * the closest inSampleSize that will result in the final decoded bitmap having a width and
     * height equal to or larger than the requested width and height. This implementation does not
     * ensure a power of 2 is returned for inSampleSize which can be faster when decoding but
     * results in a larger bitmap which isn't as useful for caching purposes.
     *
     * @param options   An options object with out* params already populated (run through a decode*
     *                  method with inJustDecodeBounds==true
     * @param reqWidth  The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @return The value to be used for inSampleSize
     */
    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
            // with both dimensions larger than or equal to the requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down further
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    /**
     * Rotate an image if required.
     *
     * @param img           The image bitmap
     * @param selectedImage Image URI
     * @return The resulted Bitmap after manipulation
     */
    private static Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) throws IOException {

        InputStream input = context.getContentResolver().openInputStream(selectedImage);
        ExifInterface ei;
        if (Build.VERSION.SDK_INT > 23)
            ei = new ExifInterface(input);
        else
            ei = new ExifInterface(selectedImage.getPath());

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG1, getClass().getSimpleName() + "");
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            imgPath = getImagePath(imageUri);
            //Toast.makeText(this,imgPath,Toast.LENGTH_LONG).show();
            userImg.setImageURI(imageUri);
            saveImage(imgPath);
        } else if (requestCode == 10 && resultCode == RESULT_OK) {
            new Encode_image().execute();
        }
    }

    private class Encode_image extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        bitmap = handleSamplingAndRotationBitmap(getContext(),file_uri);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        bitmap.recycle();
                        byte[] array = stream.toByteArray();
                        encoded_string = Base64.encodeToString(array, 0);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
         return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            makeAddRequest();
        }
    }

    private void makeAddRequest() {

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest request = new StringRequest(Request.Method.POST, "http://umotic.altervista.org/addProfileImage.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams(){
                dataUser = getActivity().getSharedPreferences("dataUser", MODE_PRIVATE);
                String id = dataUser.getString("id", null);

                HashMap<String,String> map = new HashMap<>();
                map.put("encoded_string",encoded_string);
                map.put("image_name",image_name);
                map.put("id_utente",id);

                return map;
            }
        };
        requestQueue.add(request);
    }

    private class DownloadImage extends AsyncTask<Void,Void,Bitmap>{
        SharedPreferences dataUser = getActivity().getSharedPreferences("dataUser", MODE_PRIVATE);
        String id = dataUser.getString("id", null);

        public DownloadImage(){

        }

        @Override
        protected Bitmap doInBackground(Void... voids) {

            String url ="http://umotic.altervista.org/profileImages/"+USERIMG+id;

            try{
                URLConnection connection = new URL(url).openConnection();
                connection.setConnectTimeout(1000*30);
                connection.setReadTimeout(1000*30);

                return BitmapFactory.decodeStream((InputStream) connection.getContent(),null,null);
            }catch(Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap){
            super.onPostExecute(bitmap);
            if(bitmap != null){
                userImg.setImageBitmap(bitmap);
            }
        }
    }

    public void saveImage(String path){
        bitmap = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        bitmap.recycle();

        byte[] array = stream.toByteArray();
        encoded_string = Base64.encodeToString(array, 0);

        dataUser = getActivity().getSharedPreferences("dataUser", MODE_PRIVATE);
        String id = dataUser.getString("id", null);

        image_name = USERIMG+id;

        makeAddRequest();


    }

    public String getImagePath(Uri uri) {
        if(uri == null)
            return null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);

        if(cursor!=null){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }


    /**
     * if user clicks on "Available Update" button, the app go to SensorListActivity
     */
    public View.OnClickListener updateButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            name.setEnabled(true);
            surname.setEnabled(true);
            data.setEnabled(true);
            password.setEnabled(true);
            rPassword.setVisibility(View.VISIBLE);
            update.setVisibility(View.GONE);
            commit.setVisibility(View.VISIBLE);
        }
    };

    public View.OnClickListener commitButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG1, "Update");
            if (!validate()) {
                Toast.makeText(getContext(), getString(R.string.profile_not_update), Toast.LENGTH_LONG).show();
            } else {
                onUpdateSuccess();
            }
        }
    };


    private void onUpdateSuccess() {

        dataUser = getActivity().getSharedPreferences("dataUser", MODE_PRIVATE);
        String id = dataUser.getString("id", null);
        BackgroundWorker backgroundWorker = new BackgroundWorker(getActivity());
        backgroundWorker.onPreExecute(TAG1);
        backgroundWorker.execute(TAG1, id, usernameS, nameS, surnameS, birthdayS, emailS, cfS, passwordS);
        rPassword.setText("");
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    public boolean validate() {
        boolean valid = true;

        usernameS = username.getText().toString().trim();
        nameS = name.getText().toString().trim();
        surnameS = surname.getText().toString().trim();
        birthdayS = data.getText().toString().trim();
        emailS = email.getText().toString().trim();
        cfS = cf.getText().toString().trim();
        passwordS = password.getText().toString().trim();
        String reEnterPasswordS = rPassword.getText().toString().trim();


        if (usernameS.isEmpty() || usernameS.length() < 3 || usernameS.length()>15) {
            name.setError(getString(R.string.three_character));
            valid = false;
        } else {
            username.setError(null);
        }

        if (nameS.isEmpty() || nameS.length() < 3 || nameS.length()>40) {
            name.setError(getString(R.string.three_forty_character));
            valid = false;
        } else {
            name.setError(null);
        }

        if (surnameS.isEmpty() || surnameS.length() < 3 || surname.length()>25) {
            surname.setError(getString(R.string.three_twentyfiv_character));
            valid = false;
        } else {
            surname.setError(null);
        }


        if (emailS.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailS).matches() || emailS.length()>40) {
            email.setError(getString(R.string.valid_email));
            valid = false;
        } else {
            email.setError(null);
        }


        if (passwordS.isEmpty() || passwordS.length() < 4 || passwordS.length() > 25) {
            password.setError(getString(R.string.four_twentyfive_character));
            valid = false;
        } else {
            password.setError(null);
        }

        if (!(reEnterPasswordS.equals(passwordS))) {
            rPassword.setError(getString(R.string.valid_password));
            valid = false;
        } else {
            rPassword.setError(null);
        }



        if (cfS.length() != 16) {
            cf.setError(getString(R.string.valid_fiscal_code));
            valid = false;
        } else {
            cf.setError(null);
        }

        return valid;
    }

    public View.OnClickListener setBirthday= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);


            mDateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    month += 1;
                    String date = year + "-" + month + "-" + day;
                    data.setText(date);

                }
            };


            DatePickerDialog dateDialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, day);
            dateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dateDialog.getDatePicker().setMaxDate(new Date().getTime());
            dateDialog.show();

        }

    };


    private void requestRecordPermission() {
        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == STORAGE_PERMISSION_CODE && requestCommand.compareTo("gallery") == 0){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //Toast.makeText(this,"Permision granted",Toast.LENGTH_LONG).show();
                openGallery();
            }else{
                Toast.makeText(getActivity(),getString(R.string.permission_not_granted),Toast.LENGTH_LONG).show();
            }
        } else{
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //Toast.makeText(this,"Permision granted",Toast.LENGTH_LONG).show();
                try {
                    openCamera();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(getActivity(),getString(R.string.permission_not_granted),Toast.LENGTH_LONG).show();
            }
        }
    }
}
