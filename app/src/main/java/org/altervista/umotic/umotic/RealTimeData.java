package org.altervista.umotic.umotic;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

public class RealTimeData extends AppCompatActivity {
    RealTimeFragment fragment;
    private static final String TAG = "RealTimeData";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_time_data);
        setTitle(R.string.realTime);
        final ActionBar ab = getSupportActionBar();
        assert ab != null;

        //Make the action bar display an up arrow and set its drawable and color
        ab.setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = ResourcesCompat.getDrawable(
                getResources(),
                R.drawable.ic_close, //this is the <- arrow from android resources. Change this to the thing you want.
                null);
        assert upArrow != null;
        upArrow.setColorFilter(
                ContextCompat.getColor(
                        getApplicationContext(),
                        R.color.black // change this to the color you want (or remove the setColorFilter call)
                ),
                PorterDuff.Mode.SRC_ATOP);
        ab.setHomeAsUpIndicator(upArrow);

        SharedPreferences dataUser = getSharedPreferences("dataUser", Context.MODE_PRIVATE);
        String usernameFile=dataUser.getString("username","");
        SharedPreferences dataCar = getSharedPreferences("dataCar_"+usernameFile, MODE_PRIVATE);

        ab.setSubtitle(getResources().getString(R.string.selectedCar) + dataCar.getString("alias",""));

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        fragment = new RealTimeFragment();
        fragment.setRetainInstance(true);
        transaction.add(R.id.container, fragment);
        transaction.addToBackStack("RealTimeFragment");
        transaction.commit();

    }

    @Override
    public void onBackPressed() {
        tellFragments();
    }

    public void assistentDialog(MenuItem item) {
        Intent intent = new Intent(this, AssistentActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    private void tellFragments() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment f : fragments) {
            if (f != null && f instanceof RealTimeFragment)
                ((RealTimeFragment) f).onBackPressed();
        }
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
}


