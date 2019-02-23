package org.altervista.umotic.umotic;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;

import static android.content.Context.MODE_PRIVATE;

public class BackgroundWorker extends AsyncTask<String,Void,String> {
    public String resultAuto="";
    private String json = "";
    private ArrayList<String[]> data,rilevations;
    private String result = "";
    private String password,user = "";
    private String email_utente;
    private String TAG="";
    private static final String internet= "Not Internet";
    //tagParent è utilizzato per tener traccia dell'Activity che ha chiamato CreateCarFragment e di conseguenza
    // effettuare un intent al MainActivity o al CarsActivity.

    private String tagParent="";

    Context context;

    public BackgroundWorker(Context ctx) {
        context = ctx;
    }
    private ProgressDialog progressDialog;

    public ArrayList<String[]> getRilevation(){
        return rilevations;
    }

    @Override
    protected String doInBackground(String... params) {
        if(InternetConnection.haveInternetConnection(context.getApplicationContext())) {
            TAG=params[0];
            switch (TAG) {
                case "LoginActivity":
                    login(params);
                    break;
                case "SignUpActivity":
                    register(params);
                    break;
                case "CreateCarFragment":
                    tagParent= params[6];
                    addCar(params);
                    break;
                case "CarsActivity":
                case "SecondWelcomeActivity":
                    getCar();
                    break;
                case "ProfileActivity":
                    updateUserData(params);
                    break;
                case "deleteCar":
                    deleteCar(params);
                    break;
                case "UpdateCarFragment":
                    updateCarData(params);
                    break;
                case "forgotPassw":
                    getEmail(params);
                    break;

                case "updateRilevations":
                    getRilevations();
                    break;

                case "sendData":
                    setData(params);
                    break;

                case "KILL":
                    kill();
                    break;

                default:
                    return null;
            }
        }else{
            return internet;
        }
        return result;

    }


    private void connectionDb(URL url, String post_data) {
        this.result="";
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));


            result = bufferedReader.readLine();
            //----------- DELETING char at 0 and last char
            result = result.substring(1, result.length() - 1);
            //-----------------------------------------------

            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
        }
    }

    private void login(String... params) {

        try {
            String login_url = "http://umotic.altervista.org/login.php";
            String user_name = params[1];
            String password = params[2];
            String check = params[3];

            URL url = new URL(login_url);

            String post_data = URLEncoder.encode("username_utente", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8") + "&"
                             + URLEncoder.encode("password_utente", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");


            connectionDb(url, post_data);

            String[] dataArray = result.split(",");
            result = dataArray[0];
            dataArray = Arrays.copyOf(dataArray, dataArray.length + 1);

            //salva i dati di accesso in una SharedPreferences
            if (result.equals("logintrue")) {
                dataArray[9] = check;
                saveDataUser(dataArray);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void register(String... params) {

        try {
            String registration_url = "http://umotic.altervista.org/register.php";
            String nome_utente = params[1];
            String cognome_utente = params[2];
            String dataDiNascita_utente = params[3];
            String email_utente = params[4];
            String cf_utente = params[5];
            String username_utente = params[6];
            String password_utente = params[7];

            String post_data = URLEncoder.encode("nome_utente", "UTF-8") + "=" + URLEncoder.encode(nome_utente, "UTF-8") + "&"
                    + URLEncoder.encode("cognome_utente", "UTF-8") + "=" + URLEncoder.encode(cognome_utente, "UTF-8") + "&"
                    + URLEncoder.encode("dataDiNascita_utente", "UTF-8") + "=" + URLEncoder.encode(dataDiNascita_utente, "UTF-8") + "&"
                    + URLEncoder.encode("email_utente", "UTF-8") + "=" + URLEncoder.encode(email_utente, "UTF-8") + "&"
                    + URLEncoder.encode("cf_utente", "UTF-8") + "=" + URLEncoder.encode(cf_utente, "UTF-8") + "&"
                    + URLEncoder.encode("username_utente", "UTF-8") + "=" + URLEncoder.encode(username_utente, "UTF-8") + "&"
                    + URLEncoder.encode("password_utente", "UTF-8") + "=" + URLEncoder.encode(password_utente, "UTF-8");

            URL url = new URL(registration_url);
            connectionDb(url, post_data);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void updateUserData(String... params){

        try {

            String updateUser_url = "http://umotic.altervista.org/updateDataUser.php";
            String id = params[1];
            String nome_utente = params[3];
            String cognome_utente = params[4];
            String dataDiNascita_utente = params[5];
            String email_utente = params[6];
            String cf_utente = params[7];
            String password_utente = params[8];

            URL url = new URL(updateUser_url);

            String post_data = URLEncoder.encode("id_utente", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8") + "&"
                    + URLEncoder.encode("nome_utente", "UTF-8") + "=" + URLEncoder.encode(nome_utente, "UTF-8") + "&"
                    + URLEncoder.encode("cognome_utente", "UTF-8") + "=" + URLEncoder.encode(cognome_utente, "UTF-8") + "&"
                    + URLEncoder.encode("dataDiNascita_utente", "UTF-8") + "=" + URLEncoder.encode(dataDiNascita_utente, "UTF-8") + "&"
                    + URLEncoder.encode("email_utente", "UTF-8") + "=" + URLEncoder.encode(email_utente, "UTF-8") + "&"
                    + URLEncoder.encode("cf_utente", "UTF-8") + "=" + URLEncoder.encode(cf_utente, "UTF-8") + "&"
                    + URLEncoder.encode("password_utente", "UTF-8") + "=" + URLEncoder.encode(password_utente, "UTF-8");


            connectionDb(url, post_data);

            if (result.equals("updateUserTrue")) {
                SharedPreferences dataUser = context.getSharedPreferences("dataUser", MODE_PRIVATE);
                Boolean check = dataUser.getBoolean("check", false);

                params = Arrays.copyOf(params, params.length + 1);
                params[9] = String.valueOf(check);
                saveDataUser(params);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addCar(String... params){

            try {
                String addAuto_url = "http://umotic.altervista.org/addAuto.php";
                String licence_car = params[1];
                String brand_car = params[2];
                String model_car = params[3];
                String alias_car = params[4];
                String id_user = params[5];

                URL url = new URL(addAuto_url);

                String post_data = URLEncoder.encode("targa_auto", "UTF-8") + "=" + URLEncoder.encode(licence_car, "UTF-8") + "&"
                        + URLEncoder.encode("marca_auto", "UTF-8") + "=" + URLEncoder.encode(brand_car, "UTF-8") + "&"
                        + URLEncoder.encode("modello_auto", "UTF-8") + "=" + URLEncoder.encode(model_car, "UTF-8") + "&"
                        + URLEncoder.encode("alias_auto", "UTF-8") + "=" + URLEncoder.encode(alias_car, "UTF-8") + "&"
                        + URLEncoder.encode("id_utente", "UTF-8") + "=" + URLEncoder.encode(id_user, "UTF-8");


                connectionDb(url, post_data);

                if(result.equals("addautotrue")) {
                    //select the car automatically
                    SharedPreferences dataUser = context.getSharedPreferences("dataUser", MODE_PRIVATE);
                    String usernameFile=dataUser.getString("username","");
                    SharedPreferences preferences = context.getSharedPreferences("dataCar_"+usernameFile, MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();

                    editor.putString("targa", licence_car.toUpperCase());
                    editor.putString("model", model_car);
                    editor.putString("brand", brand_car);
                    editor.putString("alias", alias_car);
                    editor.apply();

                    if(tagParent.equals("CarsActivity")||TAG.equals("UpdateCarFragment")||TAG.equals("CarsActivity")){
                        getCar();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    private void deleteCar(String... params) {

        try {
            String deleteCar_url = "http://umotic.altervista.org/deleteCar.php";
            String targa_auto = params[1];
            String id_utente = params[2];
            URL url = new URL(deleteCar_url);

            String post_data = URLEncoder.encode("targa_auto", "UTF-8") + "=" + URLEncoder.encode(targa_auto, "UTF-8") + "&"
                    + URLEncoder.encode("id_utente", "UTF-8") + "=" + URLEncoder.encode(id_utente, "UTF-8");


            connectionDb(url, post_data);

            if (result.equals("deleteTrue")) {
                SharedPreferences dataUser = context.getSharedPreferences("dataUser", MODE_PRIVATE);
                String usernameFile=dataUser.getString("username","");
                SharedPreferences p = context.getSharedPreferences("dataCar_"+usernameFile, MODE_PRIVATE);
                //if the car is used for analisys then delete
                if (p.getString("targa", "").equals(targa_auto)) {
                    SharedPreferences.Editor editor = p.edit();
                    editor.clear();
                    editor.apply();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getCar() {

        try {
            String getAuto_url = "http://umotic.altervista.org/getAuto.php";
            SharedPreferences dataUser = context.getSharedPreferences("dataUser", MODE_PRIVATE);
            String id_utente = dataUser.getString("id",null);

            data = new ArrayList<>();

            if (id_utente != null) {
                URL url = new URL(getAuto_url);
                String post_data = URLEncoder.encode("id_utente", "UTF-8") + "=" + URLEncoder.encode(id_utente, "UTF-8");//+"&"
                       // +URLEncoder.encode("check", "UTF-8") + "=" + URLEncoder.encode(check, "UTF-8");

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                //l'utente avrà per forza un'auto
                json = sb.toString();
                JSONArray jsonArray = new JSONArray(json);
                for(int i=0; i<jsonArray.length();i++){
                    JSONObject json_data= jsonArray.getJSONObject(i);
                    String[] s= new String[4];
                    if(i==0){
                        result=json_data.getString("Result");
                        if(!result.equals("getAutotrue")){
                            break;
                        }
                        continue;
                    }
                    s[0]= json_data.getString("Targa_Auto");
                    s[1] = json_data.getString("Marca_Auto");
                    s[2] = json_data.getString("Modello_Auto");
                    s[3] = json_data.getString("Alias_Auto");
                    data.add(s);
                }


                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
            }

        } catch (Exception e) {
            e.printStackTrace();
            result = null;
        }
    }

    private void getRilevations() {

        try {
            String getAuto_url = "http://umotic.altervista.org/getRilevations.php";
            SharedPreferences dataUser = context.getSharedPreferences("dataUser", MODE_PRIVATE);
            String usernameFile=dataUser.getString("username","");
            String id_utente = dataUser.getString("id","");


            SharedPreferences dataCar = context.getSharedPreferences("dataCar_"+usernameFile, MODE_PRIVATE);

            String targa = dataCar.getString("targa",null);

            rilevations = new ArrayList<>();

            if (id_utente != null) {
                URL url = new URL(getAuto_url);
                String post_data = URLEncoder.encode("id_utente", "UTF-8") + "=" + URLEncoder.encode(id_utente, "UTF-8")+"&"
                        +URLEncoder.encode("targa_auto", "UTF-8") + "=" + URLEncoder.encode(targa, "UTF-8");
                // +URLEncoder.encode("check", "UTF-8") + "=" + URLEncoder.encode(check, "UTF-8");

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                json = sb.toString();
                JSONArray jsonArray = new JSONArray(json);
                for(int i=0; i<jsonArray.length();i++){
                    JSONObject json_data= jsonArray.getJSONObject(i);
                    String[] s= new String[6];
                    if(i==0){
                        result=json_data.getString("Result");
                    }else{
                        if(result.equals("getRilevazionetrue")){
                            s[0]= json_data.getString("ID_Utente");
                            s[1] = json_data.getString("Targa_Auto");
                            s[2] = json_data.getString("Device_Time");
                            s[3] = json_data.getString("AVG_Speed_OBD");
                            s[4] = json_data.getString("AVG_Consumi");
                            s[5] = json_data.getString("Distanza_Percorsa");
                            rilevations.add(s);
                        }else{
                            s[0]= "0";
                            s[1] = "0";
                            s[2] = "0";
                            s[3] = "0.0";
                            s[4] = "0.0";
                            s[5] = "0.0";
                            rilevations.add(s);
                            break;
                        }
                    }
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
            }

        } catch (Exception e) {
            e.printStackTrace();
            result = null;
        }
    }


    private void kill() {

        try {

            SharedPreferences dataUser = context.getSharedPreferences("dataUser", MODE_PRIVATE);
            String id = dataUser.getString("id",null);
            URL url = new URL("http://umotic.altervista.org/kill.php");

            String post_data = URLEncoder.encode(       "id_utente", "UTF-8") + "=" + URLEncoder.encode(      id, "UTF-8");

            connectionDb(url, post_data);

            Log.i("killer",result+" "+post_data);


        } catch (Exception e) {
            Log.i("killer","ERRORR");
        }


    }


    void setData(String... parms){

        SharedPreferences dataUser = context.getSharedPreferences("dataUser", MODE_PRIVATE);

        String usernameFile=dataUser.getString("username","");
        String id_utente = dataUser.getString("id","");


        SharedPreferences dataCar = context.getSharedPreferences("dataCar_"+usernameFile, MODE_PRIVATE);

        String targa = dataCar.getString("targa",null);


        try {

            URL url = new URL("http://umotic.altervista.org/stats.php");

            String post_data =
                          URLEncoder.encode(       "data", "UTF-8") + "=" + URLEncoder.encode(      parms[1], "UTF-8") + "&"
                        + URLEncoder.encode(      "data1", "UTF-8") + "=" + URLEncoder.encode(      parms[2], "UTF-8") + "&"
                        + URLEncoder.encode(      "data2", "UTF-8") + "=" + URLEncoder.encode(      parms[3], "UTF-8") + "&"
                        + URLEncoder.encode(      "data3", "UTF-8") + "=" + URLEncoder.encode(      parms[4], "UTF-8") + "&"
                        + URLEncoder.encode(      "data4", "UTF-8") + "=" + URLEncoder.encode(      parms[5], "UTF-8") + "&"
                        + URLEncoder.encode(       "time", "UTF-8") + "=" + URLEncoder.encode(      parms[6], "UTF-8") + "&"
                        + URLEncoder.encode(      "targa", "UTF-8") + "=" + URLEncoder.encode(         targa, "UTF-8") + "&"
                        + URLEncoder.encode(  "id_utente", "UTF-8") + "=" + URLEncoder.encode(     id_utente, "UTF-8");

            connectionDb(url, post_data);

            Log.i("TRA","restlt: "+result+" postData: "+post_data);



        } catch (Exception e) {
            Log.i("TRA","ERRORR");
            result="null";
        }


    }




    private void updateCarData(String... params) {

        try {

            SharedPreferences dataUser = context.getSharedPreferences("dataUser", MODE_PRIVATE);
            String id = dataUser.getString("id", null);
            String updateCar_url = "http://umotic.altervista.org/updateCar.php";
            String targa_auto = params[3];
            String alias_auto = params[4];


            URL url = new URL(updateCar_url);

            String post_data =
                      URLEncoder.encode("id_utente", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8") + "&"
                    + URLEncoder.encode("targa_auto", "UTF-8") + "=" + URLEncoder.encode(targa_auto, "UTF-8") + "&"
                    + URLEncoder.encode("alias_auto", "UTF-8") + "=" + URLEncoder.encode(alias_auto, "UTF-8");

            Log.i("TRA2",result+" ££ "+post_data);

            connectionDb(url, post_data);

            String usernameFile=dataUser.getString("username","");
            SharedPreferences dataCar = context.getSharedPreferences("dataCar_"+usernameFile, MODE_PRIVATE);
            String car = dataCar.getString("targa", null);

            if(car!=null && result.equals("updateCarTrue") && car.equals(targa_auto)){
                saveCarUser(params);
            }

            getCar();
        } catch (Exception e) {
            e.printStackTrace();
            result = null;
        }
    }

    private void getEmail(String... params) {

        try {
            String getAuto_url = "http://umotic.altervista.org/getEmail.php";
            email_utente= params[1];

            if (email_utente != null) {
                URL url = new URL(getAuto_url);
                String post_data = URLEncoder.encode("email_utente", "UTF-8") + "=" + URLEncoder.encode(email_utente, "UTF-8");

                connectionDb(url, post_data);

                String[] a = result.split("@");
                if(a.length>=2) {
                    result = a[0];
                    password = a[1];
                    user=a[2];
                }else{
                    result = a[0];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = null;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected void onPreExecute(String type) {
        if(InternetConnection.haveInternetConnection(context.getApplicationContext())) {
            progressDialog = new ProgressDialog(context,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);

            switch (type) {
                case "LoginActivity":
                case "forgotPassw":
                case "SecondWelcomeActivity":
                    progressDialog.setMessage(context.getString(R.string.enter_login_account));
                    progressDialog.show();
                    break;

                case "SignUpActivity":
                    progressDialog.setMessage(context.getString(R.string.creating_account));
                    progressDialog.show();
                    break;

                case "ProfileActivity":
                    progressDialog.setMessage(context.getString(R.string.updating_account));
                    progressDialog.show();
                    break;

                case "CarUpdateActivity":
                    progressDialog.setMessage(context.getString(R.string.updating_account));
                    progressDialog.show();
                    break;

                case "UpdateCarFragment":
                    progressDialog.setMessage(context.getString(R.string.updating_car));
                    progressDialog.show();
                    break;


                default:
                    progressDialog.cancel();
                    break;
            }

        }else{
            Toast.makeText(context, context.getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPostExecute(String result) {


            if(progressDialog!=null){
                progressDialog.dismiss();
            }


            switch(result){
                case internet:
                    Toast.makeText(context, context.getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
                    break;
                case "logintrue":
                    Intent intent = new Intent(context, SecondWelcomeActivity.class);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                    break;
                case "loginfalse":
                    Toast.makeText(context, context.getString(R.string.valid_login), Toast.LENGTH_SHORT).show();
                    break;
                case "controlUsernamefalse":
                    Toast.makeText(context, context.getString(R.string.control_Username_false), Toast.LENGTH_SHORT).show();
                    break;
                case "controlEmailfalse":
                    Toast.makeText(context, context.getString(R.string.control_Email_false), Toast.LENGTH_SHORT).show();
                    break;
                case "controlCFfalse":
                    Toast.makeText(context, context.getString(R.string.control_CF_false), Toast.LENGTH_SHORT).show();
                    break;
                case "registertrue":
                    Toast.makeText(context, context.getString(R.string.register_do), Toast.LENGTH_SHORT).show();
                    Intent i= new Intent (context,LoginActivity.class);
                    if(TAG.equals("CarsActivity")){
                        ((Activity)context).finish();
                    }
                    context.startActivity(i);
                    break;
                case "registerfalse":
                    Toast.makeText(context, context.getString(R.string.register_not_do), Toast.LENGTH_SHORT).show();
                    break;
                case "addautotrue":
                    Toast.makeText(context, context.getString(R.string.car_added), Toast.LENGTH_SHORT).show();
                    if(!tagParent.equals("CarsActivity")&&!TAG.equals("UpdateCarFragment")&&!TAG.equals("CarsActivity")) {
                        //chiude l'activity appartenente al fragment
                        SharedPreferences dataUser = context.getSharedPreferences("dataUser", MODE_PRIVATE);
                        String usernameFile=dataUser.getString("username","");
                        SharedPreferences flag= context.getSharedPreferences("flag_"+usernameFile,MODE_PRIVATE);
                        SharedPreferences.Editor editor= flag.edit();
                        editor.putInt("flag",0);
                        editor.apply();
                        ((Activity) context).finish();
                        Intent p = new Intent(context, MainActivity.class);
                        context.startActivity(p);
                    }
                    break;
                case "addautofalse":
                    Toast.makeText(context, context.getString(R.string.car_not_added), Toast.LENGTH_SHORT).show();
                    break;
                case "getAutotrue":
                    //Se l'activity chiamante è SecondWelcomeActivity allora dopo la creazione dell'auto viene chiamato il MainActivity
                    if(!TAG.equals("SecondWelcomeActivity")){

                        if(TAG.equals("CreateCarFragment")||TAG.equals("UpdateCarFragment")){
                            ((Activity) context).finish();
                        }

                        Intent p = new Intent(context, CarsActivity.class);
                        p.putExtra("listCar", data);
                        context.startActivity(p);

                    }else{
                        String[] s= data.get(0);
                        SharedPreferences dataUser = context.getSharedPreferences("dataUser", MODE_PRIVATE);
                        String usernameFile=dataUser.getString("username","");
                        SharedPreferences p = context.getSharedPreferences("dataCar_"+usernameFile, MODE_PRIVATE);
                        SharedPreferences.Editor editor= p.edit();
                        editor.clear();
                        editor.putString("brand", s[1]);
                        editor.putString("model", s[2]);
                        editor.putString("targa", s[0]);
                        editor.putString("alias", s[3]);
                        editor.apply();

                        Intent k = new Intent(context, MainActivity.class);
                        context.startActivity(k);
                        ((Activity) context).finish();
                    }
                    break;

                case "getAutofalse":
                    SharedPreferences dataUser = context.getSharedPreferences("dataUser", MODE_PRIVATE);
                    String usernameFile=dataUser.getString("username","");
                    SharedPreferences flag= context.getSharedPreferences("flag_"+usernameFile,MODE_PRIVATE);

                    SharedPreferences.Editor editor= flag.edit();
                    editor.putInt("flag",1);
                    editor.apply();
                    Intent t = new Intent(context, SecondWelcomeActivity.class);
                    context.startActivity(t);
                    ((Activity)context).finish();
                    break;


                case "getRilevazionetrue":
                    break;

                case "getRilevazionefalse":
                    break;
                case "updateUserFalse":
                    Toast.makeText(context, context.getString(R.string.profile_not_update), Toast.LENGTH_SHORT).show();
                    break;
                case "updateUserTrue":
                    Toast.makeText(context, context.getString(R.string.profile_update), Toast.LENGTH_SHORT).show();
                    break;
                case "deleteTrue":
                    Toast.makeText(context, context.getString(R.string.car_deleted), Toast.LENGTH_SHORT).show();
                    break;
                case "updateCarTrue":
                    Toast.makeText(context, context.getString(R.string.edit_car), Toast.LENGTH_SHORT).show();
                    break;
                case "updateCarFalse":
                    Toast.makeText(context, context.getString(R.string.edit_not_car), Toast.LENGTH_SHORT).show();
                    break;
                case "getEmailfalse":
                    Toast.makeText(context,context.getString(R.string.valid_email), Toast.LENGTH_SHORT).show();
                    Intent r = new Intent(context, LoginActivity.class);
                    context.startActivity(r);
                case "getEmailtrue":
                    new ForgotPassword(user,email_utente,password,context);
                    break;
                case "license_exist":
                    Toast.makeText(context, context.getString(R.string.control_Car_false), Toast.LENGTH_SHORT).show();
                    break;
                case "kill1":
                     Toast.makeText(context, context.getString(R.string.data_deleted), Toast.LENGTH_SHORT).show();
                     break;
                case "kill0":
                     Toast.makeText(context, context.getString(R.string.data_deleted_failed), Toast.LENGTH_SHORT).show();
                     break;
                default:
                    break;
            }
    }


    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    private void saveDataUser(String... params) {
        SharedPreferences dataUser = context.getSharedPreferences("dataUser", MODE_PRIVATE);
        String id = params[1];
        String username_utente = params[2];
        String nome_utente = params[3];
        String cognome_utente = params[4];
        String dataDiNascita_utente = params[5];
        String email_utente = params[6];
        String cf_utente = params[7];
        String password_utente = params[8];
        String check = params[9];

        SharedPreferences.Editor editor = dataUser.edit();
        editor.clear();
        editor.putString("id", id);
        editor.putString("username", username_utente);
        editor.putString("name", nome_utente);
        editor.putString("surname", cognome_utente);
        editor.putString("data", dataDiNascita_utente);
        editor.putString("email", email_utente);
        editor.putString("cf", cf_utente);
        editor.putString("password", password_utente);
        editor.putBoolean("termsCondition",true);

        if (check.equals("true")) {
            editor.putBoolean("check", true);
        } else {
            editor.putBoolean("check", false);
        }

        editor.apply();
    }

    private void saveCarUser(String... params) {
        SharedPreferences dataUser = context.getSharedPreferences("dataUser", MODE_PRIVATE);
        String usernameFile=dataUser.getString("username","");
        SharedPreferences dataCar = context.getSharedPreferences("dataCar_"+usernameFile, MODE_PRIVATE);
        String marca = params[1];
        String modello = params[2];
        String targa = params[3];
        String alias = params[4];


        SharedPreferences.Editor editor = dataCar.edit();
        editor.clear();
        editor.putString("brand", marca);
        editor.putString("model", modello);
        editor.putString("targa", targa);
        editor.putString("alias", alias);
        editor.apply();
    }



}