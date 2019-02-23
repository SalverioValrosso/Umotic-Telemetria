package org.altervista.umotic.umotic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class AppInfoFragment extends Fragment {
    private static final String TAG = "AppInfoFragment";
    Button b;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_app_info, container, false);

        getActivity().setTitle(R.string.appinfo);

        return v;
    }

    @Override
    public void onDetach() {
        getActivity().setTitle(R.string.settingsActivityTitle);
        super.onDetach();
    }
}
