package org.altervista.umotic.umotic;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

public class CarsActivity extends AppCompatActivity {
    private static final String TAG = "CarsActivity" ;
    FragmentTransaction transaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cars);
        setTitle(R.string.carsActivityTitle);
        Bundle m = getIntent().getExtras();
        m.putString("tag",TAG);

        if (savedInstanceState == null) {
            transaction = getSupportFragmentManager().beginTransaction();
            ListCarFragment fragment = new ListCarFragment();
            fragment.setArguments(m);
            fragment.setRetainInstance(true);
            transaction.add(R.id.container, fragment);
            transaction.addToBackStack("ListCarFragment");
            transaction.commit();
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
