package org.altervista.umotic.umotic;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity {


    private static final String TAG = "SignUpActivity";
    private EditText birthdayL;
    private String username, name, surname, birthday, email, cf, password,reEnterPassword;
    @BindView(R.id.input_name)
    EditText _nameText;
    @BindView(R.id.input_surname)
    EditText _surnameText;
    @BindView(R.id.input_email)
    EditText _emailText;
    @BindView(R.id.input_fiscalCode)
    EditText _cfText;
    @BindView(R.id.input_username)
    EditText _usernameText;
    @BindView(R.id.input_EnterPassword)
    EditText _passwordText;
    @BindView(R.id.input_reEnterPassword)
    EditText _reEnterPasswordText;
    @BindView(R.id.btn_signup)
    Button _signupButton;
    @BindView(R.id.link_login)
    TextView _loginLink;

    DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, getClass().getSimpleName() + ":entered onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MODE_CHANGED);
        birthdayL= (EditText) findViewById(R.id.input_birthday);

        if(savedInstanceState!=null){
            _usernameText.setText(savedInstanceState.getString("user"));
            _surnameText.setText(savedInstanceState.getString("surname"));
            birthdayL.setText(savedInstanceState.getString("birth"));
            _emailText.setText(savedInstanceState.getString("email"));
            _cfText.setText(savedInstanceState.getString("cf"));
            _nameText.setText(savedInstanceState.getString("name"));
            _passwordText.setText(savedInstanceState.getString("password"));
            _reEnterPasswordText.setText(savedInstanceState.getString("repeat_password"));
        }


        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });


        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });


    }

    @Override
    protected void onSaveInstanceState(Bundle outstate){
            super.onSaveInstanceState(outstate);
            outstate.putString("user",username);
            outstate.putString("surname",surname);
            outstate.putString("birth",birthday);
            outstate.putString("email",email);
            outstate.putString("cf",cf);
            outstate.putString("name",name);
            outstate.putString("password",password);
            outstate.putString("repeat_password",reEnterPassword);
    }

    private void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
        } else {
            onSignupSuccess();
        }

    }

    private void onSignupSuccess() {
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.onPreExecute(TAG);
            backgroundWorker.execute(TAG, name, surname, birthday, email, cf, username, password);
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), getString(R.string.registration_failed), Toast.LENGTH_LONG).show();
    }

    public boolean validate() {
        boolean valid = true;

        username = _usernameText.getText().toString().trim();
        name = _nameText.getText().toString().trim();
        surname = _surnameText.getText().toString().trim();
        birthday = birthdayL.getText().toString().trim();
        email = _emailText.getText().toString().trim();
        password = _passwordText.getText().toString().trim();
        cf = _cfText.getText().toString().trim();
        reEnterPassword = _reEnterPasswordText.getText().toString().trim();


        if (username.isEmpty() || username.length() < 3 || username.length()>15) {
            _usernameText.setError(getString(R.string.three_character));
            valid = false;
        } else {
            _usernameText.setError(null);
        }

        if (name.isEmpty() || name.length() < 3|| name.length()>40) {
            _nameText.setError(getString(R.string.three_forty_character));
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (surname.isEmpty() || surname.length() < 3 || surname.length()>25) {
            _surnameText.setError(getString(R.string.three_twentyfiv_character));
            valid = false;
        } else {
            _surnameText.setError(null);
        }



        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()||email.length()>40) {
            _emailText.setError(getString(R.string.valid_email));
            valid = false;
        } else {
            _emailText.setError(null);
        }


        if (password.isEmpty() || password.length() < 4 || password.length() > 25) {
            _passwordText.setError(getString(R.string.four_twentyfive_character));
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (!(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError(getString(R.string.valid_password));
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        if (cf.length() != 16) {
            _cfText.setError(getString(R.string.valid_fiscal_code));
            valid = false;
        } else {
            _cfText.setError(null);
        }
        return valid;
    }

    public void setBirthday(View view) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);


        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month += 1;
                String date = year + "-" + month + "-" + day;
                birthdayL.setText(date);
            }
        };


        DatePickerDialog dateDialog = new DatePickerDialog(SignUpActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, day);
        dateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dateDialog.getDatePicker().setMaxDate(new Date().getTime());
        dateDialog.show();

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