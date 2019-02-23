package org.altervista.umotic.umotic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ErrorInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_info);


    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle(R.string.malfunctions);
    }
}
