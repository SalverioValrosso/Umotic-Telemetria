package org.altervista.umotic.umotic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class PrivacyFragment extends Fragment {
    private static final String TAG = "PrivacyFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_privacy, container, false);

        getActivity().setTitle(R.string.termsPrivacy);

        return view;
    }

    @Override
    public void onDetach() {
        getActivity().setTitle(R.string.appinfo);
        super.onDetach();
    }
}
