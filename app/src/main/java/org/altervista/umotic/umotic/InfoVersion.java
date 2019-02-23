package org.altervista.umotic.umotic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class InfoVersion extends Fragment {
    private static final String TAG = "InfoVersion";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_info_version, container, false);

        getActivity().setTitle(R.string.verInfo);
        return v;
    }

    @Override
    public void onDetach() {
        getActivity().setTitle(R.string.appinfo);
        super.onDetach();
    }
}


