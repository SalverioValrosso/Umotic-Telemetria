package org.altervista.umotic.umotic;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;


public class PrivacyWelcomeFragment extends Fragment {

    private static final String TAG ="PrivacyWelcomeFragment";
    private RadioButton radioButtonAccept,radioButtonDeny;
    private FloatingActionButton fb;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_privacy_welcome, container, false);
        fb= (FloatingActionButton) view.findViewById((R.id.floatingActionButton));
        radioButtonAccept= (RadioButton) view.findViewById(R.id.radioButtonAccept);
        radioButtonAccept.setOnClickListener(acceptListener);
        radioButtonDeny= (RadioButton) view.findViewById(R.id.radioButtonDeny);
        radioButtonDeny.setOnClickListener(denyListener);

        return view;
    }


    public View.OnClickListener acceptListener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           fb.show();
        }

    };
    public View.OnClickListener denyListener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            fb.hide();
        }

    };
}
