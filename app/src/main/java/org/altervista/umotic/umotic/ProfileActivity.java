package org.altervista.umotic.umotic;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG1 = "ProfileActivity";
    ProfileFragment fragment;
    FragmentTransaction transaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        transaction = getSupportFragmentManager().beginTransaction();
        fragment = new ProfileFragment();
        fragment.setRetainInstance(true);
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack("ProfileFragment");
        transaction.commit();
    }

    protected void onStart() {
        Log.i(TAG1, getClass().getSimpleName() + ":entered onStart()");
        super.onStart();
    }


    protected void onRestart() {
        Log.i(TAG1, getClass().getSimpleName() + ":entered onRestart()");
        super.onRestart();
    }


    protected void onResume() {
        Log.i(TAG1, getClass().getSimpleName() + ":entered onResume()");
        super.onResume();
    }

    protected void onPause() {
        Log.i(TAG1, getClass().getSimpleName() + ":entered onPause()");
        super.onPause();

    }


    protected void onStop() {
        Log.i(TAG1, getClass().getSimpleName() + ":entered onStop()");
        super.onStop();
    }



    protected void onDestroy() {
        Log.i(TAG1, getClass().getSimpleName() + ":entered onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG1, getClass().getSimpleName() + "");
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
      super.onRequestPermissionsResult(requestCode,permissions, grantResults);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount()>1){
            getSupportFragmentManager().popBackStack();
            //super.onBackPressed();
        }else{
           /*
            navigateUpTo(new Intent(this, MainActivity.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            */
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // getBackStackEntryCount()>1 because it close activity when ListCarFragment is view.
                if(getSupportFragmentManager().getBackStackEntryCount()>1){
                    getSupportFragmentManager().popBackStack();
                    //super.onBackPressed();
                }else{
                   /*
                    navigateUpTo(new Intent(this, MainActivity.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    */
                    finish();
                }
                break;
        }
        return true;
    }

}