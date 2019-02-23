package org.altervista.umotic.umotic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Settings extends AppCompatActivity {
    Button general, kill, infoVersion, info, contactUs, sendNot;
    String email;
    Context c;
    String TAG2="KILL";

    private static final String TAG ="Settings";
    SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, getClass().getSimpleName() + ":entered onCreate()");
        super.onCreate(savedInstanceState);
        setTitle(R.string.settingsActivityTitle);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.primary)));

        general = (Button)findViewById(R.id.btn_general);
        kill = (Button) findViewById(R.id.kill);
        contactUs = (Button)findViewById(R.id.btn_contact_us);
        info = (Button)findViewById(R.id.btn_info);
        infoVersion = (Button)findViewById(R.id.btn_info_version);
        sharedPreferences = getSharedPreferences("dataUser", MODE_PRIVATE);

        email = sharedPreferences.getString("email", "");



        /*

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.umotic_icon)
                .setContentTitle("My notification")
                .setContentText("Much longer text that cannot fit one line...")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, mBuilder.build());
*/



    }

    /*public void notificationSettings(View view){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        NotificationFragment fragment = new NotificationFragment();
        fragment.setRetainInstance(true);
        transaction.replace(R.id.containerS,fragment);
        transaction.addToBackStack("NotificationFragment");
        transaction.commit();
        getSupportFragmentManager().executePendingTransactions();

    }*/





    public void privacyInfo(View view){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        PrivacyFragment fragment = new PrivacyFragment();
        fragment.setRetainInstance(true);
        transaction.replace(R.id.containerS,fragment);
        transaction.addToBackStack("PrivacyFragment");
        transaction.commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    public void infoVersion(View view){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        InfoVersion fragment = new InfoVersion();
        fragment.setRetainInstance(true);
        transaction.replace(R.id.containerS,fragment);
        transaction.addToBackStack("InfoVersion");
        transaction.commit();
        getSupportFragmentManager().executePendingTransactions();
    }



    public void generalSettings(View view){

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        GeneralFragment fragment = new GeneralFragment();
        fragment.setRetainInstance(true);
        transaction.add(R.id.containerS,fragment);
        transaction.addToBackStack("GeneralFragment");
        transaction.commit();
        getSupportFragmentManager().executePendingTransactions();
    }


    public void infoSettings(View view){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        AppInfoFragment fragment = new AppInfoFragment();
        fragment.setRetainInstance(true);
        transaction.add(R.id.containerS,fragment);
        transaction.addToBackStack("FragmentInfo");
        transaction.commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    public void contactUs(View view){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        ContactUs fragment = new ContactUs();
        fragment.setRetainInstance(true);
        transaction.replace(R.id.containerS,fragment);
        transaction.addToBackStack("ContactUs");
        transaction.commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    public void selectImage(View view){

    }

    public void sendReport(View view){

    }


    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount()>0){
            getSupportFragmentManager().popBackStack();
            //super.onBackPressed();
        }else{
            finish();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(getSupportFragmentManager().getBackStackEntryCount()>0){
                    getSupportFragmentManager().popBackStack();
                    //super.onBackPressed();
                }else{
                    finish();
                }
                break;
        }
        return true;
    }

    @Override
    public void onStart() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onStart()");
        super.onStart();

    }


    @Override
    public void onResume() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onResume()");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onPause()");
        super.onPause();
    }

    protected void onStop() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onStop()");
        super.onStop();
    }


    protected void onDestroy() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onDestroy()");
        super.onDestroy();
    }


}