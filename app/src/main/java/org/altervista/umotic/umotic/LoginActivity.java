package org.altervista.umotic.umotic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements ForgotPasswordDialog.ForgotPasswDialogListener{
    private static final String TAG = "LoginActivity";
    private static final String TAG2 = "forgotPassw";
    private String username, password;
    private SharedPreferences preferences;
    private SharedPreferences preferencesCar;
    @BindView(R.id.input_user)
    EditText _usernameText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.btn_login)
    Button _loginButton;
    @BindView(R.id.link_signup)
    TextView _signupLink;
    @BindView(R.id.remember_me)
    CheckBox mCheckBoxRemember;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, getClass().getSimpleName() + ":entered onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        if(savedInstanceState!=null){
            _usernameText.setText(savedInstanceState.getString("user"));
            _passwordText.setText(savedInstanceState.getString("pass"));
            if (savedInstanceState.getBoolean("remember_me")) {
                mCheckBoxRemember.setChecked(true);
            }else{
                mCheckBoxRemember.setChecked(false);
            }
        }

            //get user data for automatic login
            preferences = getSharedPreferences("dataUser", MODE_PRIVATE);
            String usernameFile=preferences.getString("username","");
            preferencesCar = getSharedPreferences("dataCar_"+usernameFile, MODE_PRIVATE);

            //remember_me
            if (preferences.getBoolean("check", false)) {
                _usernameText.setText(preferences.getString("username",""));
                _passwordText.setText(preferences.getString("password",""));
                mCheckBoxRemember.setChecked(true);
                if(InternetConnection.haveInternetConnection(getApplicationContext())) {
                    if(preferencesCar.getString("targa",null)!=null){
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Intent intent = new Intent(this, SecondWelcomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }else{
                    Toast.makeText(this, getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
                }

            }

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("user",username);
        outState.putString("pass",password);
        if (mCheckBoxRemember.isChecked()) {
            outState.putBoolean("remember_me",true);
        }else{
            outState.putBoolean("remember_me",false);
        }
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

    @Override
    public void onStop() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onStop()");
        super.onStop();
    }


    @Override
    public void onDestroy() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onDestroy()");
        super.onDestroy();
    }

    public boolean validate() {
        boolean valid = true;

        username = _usernameText.getText().toString().trim();
        password = _passwordText.getText().toString().trim();

        if (username.isEmpty() || username.length() < 3) {
            _usernameText.setError(getString(R.string.valid_email));
            valid = false;
        } else {
            _usernameText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 25) {
            _passwordText.setError(getString(R.string.four_twentyfive_character));
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    public void sendEmail(View view) {
        ForgotPasswordDialog forgotPasswordDialog = new ForgotPasswordDialog();
        forgotPasswordDialog.show(getSupportFragmentManager(), getString(R.string.forgotPassword_title));
    }

    @Override
    public void applyText(String email) {
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(TAG2,email);
    }

    public void onLogin(View view) {

        String check = "false";
        if (mCheckBoxRemember.isChecked()) {
            check = "true";
        }

        if (!validate()) {
            Toast.makeText(this, getString(R.string.valid_login), Toast.LENGTH_SHORT).show();
        } else {

            if(InternetConnection.haveInternetConnection(getApplicationContext())) {
                BackgroundWorker backgroundWorker = new BackgroundWorker(this);
                backgroundWorker.onPreExecute(TAG);
                backgroundWorker.execute(TAG, username, password, check);
            }else{
                Toast.makeText(this, getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            }
        }
    }


}
