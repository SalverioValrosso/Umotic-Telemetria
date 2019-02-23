package org.altervista.umotic.umotic;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class SecondWelcomeFragment extends Fragment {

    public SecondWelcomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_welcome, container, false);
        SharedPreferences userData= getContext().getSharedPreferences("dataUser",Context.MODE_PRIVATE);
        TextView txt= (TextView) v.findViewById(R.id.txt_welcome);
        String s= getActivity().getString(R.string.hello)+" "+ userData.getString("username","")+"!";
        txt.setText(s);
        return v;
    }

}
