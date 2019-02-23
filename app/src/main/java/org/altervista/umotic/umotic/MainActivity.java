package org.altervista.umotic.umotic;

import android.app.AlertDialog;
import android.app.Notification.Builder;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


import static org.altervista.umotic.umotic.ProfileFragment.USERIMG;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";

    private DrawerLayout mDrawerLayout;
    private ImageView userPhoto;
    public TextView nameUser;
    android.support.v7.widget.Toolbar toolbar;
    NavigationView navHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //set toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toogle);
        toogle.syncState();


        navHeader = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navHeader.getHeaderView(0);

        userPhoto = (ImageView) headerView.findViewById(R.id.imgUser);
        nameUser = (TextView) headerView.findViewById(R.id.nameUser);


        SharedPreferences dataUser = getSharedPreferences("dataUser", Context.MODE_PRIVATE);
        String usernameFile=dataUser.getString("username","");
        nameUser.setText(usernameFile);



        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setCheckable(false);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        switch (menuItem.getItemId()) {
                            case R.id.nav_cars:
                                startCars();
                                break;
                            case R.id.nav_profile:
                                startProfile();
                                break;
                            case R.id.nav_settings:
                                startSettings();
                                break;
                            case R.id.nav_logout:
                                startLogout();
                                break;
                        }
                        return true;
                    }
                });


    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }

    private class DownloadImage extends AsyncTask<Void, Void, Bitmap> {
        SharedPreferences dataUser = getSharedPreferences("dataUser", Context.MODE_PRIVATE);
        String id = dataUser.getString("id", null);

        public DownloadImage() {

        }

        @Override
        protected Bitmap doInBackground(Void... voids) {

            String url = "http://umotic.altervista.org/profileImages/" + USERIMG + id;

            try {
                URLConnection connection = new URL(url).openConnection();
                    //connection.setConnectTimeout(1000*30);
                    //connection.setReadTimeout(1000*30);

                return BitmapFactory.decodeStream((InputStream) connection.getContent(), null, null);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                userPhoto.setImageBitmap(bitmap);
            }
        }
    }


    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.title_sign_out)
                    .setMessage(R.string.exit)
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            //MainActivity.super.onDestroy();
                            //fragment.onDestroy();
                            finish();
                        }
                    }).create().show();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_cars:
                startCars();
                break;
            case R.id.nav_profile:
                startProfile();
                break;
            case R.id.nav_settings:
                startSettings();
                break;
            case R.id.nav_logout:
                startLogout();
                break;
        }
        item.setChecked(false);
        mDrawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }


    @Override
    public  void onStart() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onStart()");
        super.onStart();
    }


    @Override
    public void onRestart() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onRestart()");
        super.onRestart();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, getClass().getSimpleName() + ":entered onResume()");

        SharedPreferences dataUser = getSharedPreferences("dataUser", Context.MODE_PRIVATE);
        String usernameFile=dataUser.getString("username","");
        SharedPreferences dataCar = getSharedPreferences("dataCar_"+usernameFile, MODE_PRIVATE);

        toolbar.setSubtitle(getResources().getString(R.string.selectedCar) + dataCar.getString("alias",""));
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.black));

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        AverageDataFragment fragment = new AverageDataFragment();
        fragment.setRetainInstance(true);
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack("AverageDataFragment");
        transaction.commit();
        new MainActivity.DownloadImage().execute();
    }


    @Override
    public  void onPause() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onPause()");
        super.onPause();
    }


    @Override
    public  void onStop() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onStop()");
        super.onStop();
    }


    @Override
    public  void onDestroy() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onDestroy()");
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //change icon bluetooth
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                item.setChecked(false);
                break;

        }
        return false;
    }

    private void startProfile() {
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    private void startSettings() {
        Intent intent = new Intent(MainActivity.this, Settings.class);
        startActivity(intent);
    }

    private void startCars() {

        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute("CarsActivity");
    }


    private void startLogout() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.message_sign_out)
                .setMessage(R.string.logout)
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        //MainActivity.super.onDestroy();
                        //fragment.onDestroy();
                        SharedPreferences prefs = getSharedPreferences("dataUser", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("check",false);
                        editor.apply();
                        finish();
                        startLogin();
                    }
                }).create().show();
    }

    private void startLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void assistentDialog(MenuItem item) {
        Intent intent = new Intent(this, AssistentActivity.class);
        startActivity(intent);
    }

    public void yourEvent(View v){
        Intent intent = new Intent(this, AddEventActivity.class);
        startActivity(intent);
    }
}

