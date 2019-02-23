package org.altervista.umotic.umotic;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class SecondWelcomeActivity extends AppCompatActivity {
    public static final String TAG = "SecondWelcomeActivity";
    FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, getClass().getSimpleName() + ":entered onCreate()");
        setContentView(R.layout.activity_welcome);

        ProgressDialog progressDialog = new ProgressDialog(this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(this.getString(R.string.enter_login_account));
        progressDialog.show();

        SharedPreferences dataUser = getSharedPreferences("dataUser", MODE_PRIVATE);
        String usernameFile=dataUser.getString("username","");
        SharedPreferences flag= getSharedPreferences("flag_"+usernameFile,MODE_PRIVATE);

        if(flag.getInt("flag",0)==1){
            transaction = getSupportFragmentManager().beginTransaction();
            SecondWelcomeFragment fragment = new SecondWelcomeFragment();
            fragment.setRetainInstance(true);
            transaction.add(R.id.container, fragment);
            transaction.addToBackStack("SecondWelcomeFragment");
            transaction.commit();
            progressDialog.dismiss();
        }else{
            SharedPreferences preferencesCar = getSharedPreferences("dataCar_"+usernameFile, MODE_PRIVATE);

            if(preferencesCar.getString("targa",null)!=null){
                Intent intent= new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }else {
                BackgroundWorker backgroundWorker = new BackgroundWorker(this);
                //eseguire getCar() per verificare se ha delle macchine. Può capitare quando si cambia telefono.
                backgroundWorker.onPreExecute(TAG);
                backgroundWorker.execute(TAG);
            }
        }

    }


    protected void onStart() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onStart()");
        super.onStart();
    }


    protected void onRestart() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onRestart()");
        super.onRestart();
    }

    protected void onResume() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onResume()");
        super.onResume();
    }

    protected void onPause() {
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

    /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, getClass().getSimpleName() + "");
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i(TAG, getClass().getSimpleName() + "");
        super.onRequestPermissionsResult(requestCode,permissions, grantResults);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    */
    @Override
    public void onBackPressed() {
        final Intent intent= new Intent(this, LoginActivity.class);
        if(getSupportFragmentManager().getBackStackEntryCount()>1){
            getSupportFragmentManager().popBackStack();
            //super.onBackPressed();
        }else{
            new AlertDialog.Builder(this)
                    .setTitle(R.string.title_sign_out)
                    .setMessage(R.string.logout)
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            SharedPreferences prefs = getSharedPreferences("dataUser", MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putBoolean("check",false);
                            editor.apply();
                            startActivity(intent);
                            finish();
                        }
                    }).create().show();
        }
    }

/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // getBackStackEntryCount()>1 because it close activity when ListCarFragment is view.
                if(getSupportFragmentManager().getBackStackEntryCount()>1){
                    getSupportFragmentManager().popBackStack();
                    //super.onBackPressed();
                }else{
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.title_sign_out)
                            .setMessage(R.string.message_sign_out)
                            .setNegativeButton(android.R.string.no, null)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    //MainActivity.super.onDestroy();
                                    //fragment.onDestroy();
                                    SharedPreferences prefs = getSharedPreferences("dataUser", MODE_PRIVATE);
                                    String usernameFile=prefs.getString("username","");
                                    SharedPreferences prefs2 = getSharedPreferences("dataCar_"+usernameFile, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    SharedPreferences.Editor editor2 = prefs2.edit();
                                    editor.clear();
                                    editor.apply();
                                    editor2.clear();
                                    editor2.apply();
                                    finish();
                                }
                            }).create().show();
                }
                break;
        }
        return true;
    }
*/

    public void got(View view) {
        Bundle m = new Bundle();
        m.putString("tag",TAG);
        FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
        CreateCarFragment fragment = new CreateCarFragment();
        fragment.setArguments(m);
        fragment.setRetainInstance(true);
        transaction.add(R.id.container,fragment);
        transaction.addToBackStack("CreateCarFragment");
        transaction.commit();
    }
}
