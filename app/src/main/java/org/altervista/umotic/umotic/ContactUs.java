package org.altervista.umotic.umotic;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;
import static org.altervista.umotic.umotic.ProfileFragment.PICK_IMAGE;


public class ContactUs extends Fragment {
    private static final String TAG = "ContactUs";
    private int STORAGE_PERMISSION_CODE = 1;
    ImageView i1, i2, i3;
    Button send;
    String problem;
    EditText textProblem;
    private Uri imageUri;
    private String encoded_string, image_name;
    private Bitmap bitmap;
    private File file;
    private Uri file_uri;
    private String requestCommand;
    public CheckBox info;
    String model;
    String hw, bootloader, brand, display, manufacturer, tags;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_contact_us, container, false);

        //Riferimenti


        send = (Button) v.findViewById(R.id.button);
        send.setOnClickListener(sendReport);
        textProblem = (EditText)v.findViewById(R.id.editText);
        info = (CheckBox)v.findViewById(R.id.checkBox) ;






        getActivity().setTitle(R.string.contactUs);

        return v;
    }



    @Override
    public void onDetach() {
        getActivity().setTitle(R.string.appinfo);
        super.onDetach();
    }

    public View.OnClickListener sendReport = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            problem = textProblem.getText().toString();
            if(info.isChecked()){
                model = android.os.Build.MODEL;
                hw = Build.HARDWARE;
                bootloader = Build.BOOTLOADER;
                brand = Build.BRAND;
                display = Build.DISPLAY;
                manufacturer = Build.MANUFACTURER;
                tags = Build.TAGS;
                problem += "\n\nBrand: " + brand + "\nDevice model: " + model + "\nHardware info: " + hw + "\nBootloader: " + bootloader + "\nDisplay: " + display + "\nTags: " + tags;
            }



            if(problem.isEmpty()){
                textProblem.setError("Scrivere prima il problema");
            }else{
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"umoticandobd@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Reports");

                i.putExtra(Intent.EXTRA_TEXT   , problem);

                startActivity(Intent.createChooser(i, "Send mail..."));


            }}


    };











}
