package org.altervista.umotic.umotic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

public class FirstWelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_welcome);

        SharedPreferences prefs = getSharedPreferences("dataUser", MODE_PRIVATE);
        Boolean condition=prefs.getBoolean("termsCondition",false);

        if(savedInstanceState==null) {
            if (condition) {
                login();
            } else {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                FirstWelcomeFragment fragment = new FirstWelcomeFragment();
                fragment.setRetainInstance(true);
                transaction.add(R.id.containerS, fragment);
                transaction.addToBackStack("FirstWelcomeFragment");
                transaction.commit();
                getSupportFragmentManager().executePendingTransactions();

            }
        }
    }



    private void login(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void privacyAccept(View view){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        PrivacyWelcomeFragment fragment = new PrivacyWelcomeFragment();
        fragment.setRetainInstance(true);
        transaction.add(R.id.containerS,fragment);
        transaction.addToBackStack("PrivacyWelcomeFragment");
        transaction.commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    public void createAccount(View view){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        AccountMessageFragment fragment = new AccountMessageFragment();
        fragment.setRetainInstance(true);
        transaction.add(R.id.containerS, fragment);
        transaction.addToBackStack("CreateAccountFragment");
        transaction.commit();
        getSupportFragmentManager().executePendingTransactions();
    }


    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount()>1){
            getSupportFragmentManager().popBackStack();
        }else{
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // getBackStackEntryCount()>1 because it close activity when FirstWelcomeFragment is view.
                if(getSupportFragmentManager().getBackStackEntryCount()>1){
                    getSupportFragmentManager().popBackStack();

                }else{
                    finish();
                }
                break;
        }
        return true;
    }

    public void register(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public void alreadyRegistered(View view) {
        login();
    }
}
