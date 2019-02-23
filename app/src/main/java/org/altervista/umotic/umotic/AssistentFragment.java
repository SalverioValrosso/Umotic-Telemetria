package org.altervista.umotic.umotic;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class AssistentFragment extends Fragment {

    private CardView btnCarrAttr,btnACI,btnMec,btnOfficina,btnGomm, btnCarb;

    public AssistentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_assistent, container, false);

        SharedPreferences dataUser =  Objects.requireNonNull(getActivity()).getSharedPreferences("dataUser", MODE_PRIVATE);
        String usernameFile=dataUser.getString("username","");
        final SharedPreferences dataCar = Objects.requireNonNull(getActivity()).getSharedPreferences("dataCar_"+usernameFile, MODE_PRIVATE);

        btnCarrAttr = (CardView) v.findViewById(R.id.btn_CarrAttr);
        btnMec = (CardView) v.findViewById(R.id.btn_Meccanico);
        btnACI = (CardView) v.findViewById(R.id.btn_ACI);
        btnOfficina = (CardView) v.findViewById(R.id.btn_OfficinaAuto);
        btnGomm = (CardView) v.findViewById(R.id.btn_gomm);
        btnCarb = (CardView) v.findViewById(R.id.btn_carb);

        btnMec.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                try {
                    String address = getString(R.string.mechanic);
                    address = address.replace(' ', '+');
                    Intent geoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + address));
                    startActivity(geoIntent);
                } catch (Exception e) {
                    Log.e("Error", e.toString());
                }
            }
        });

        btnOfficina.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                try {
                    String address = getString(R.string.auto_repair) + dataCar.getString("brand","");
                    address = address.replace(' ', '+');
                    Intent geoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + address));
                    startActivity(geoIntent);
                } catch (Exception e) {
                    Log.e("Error", e.toString());
                }
            }
        });
        btnCarrAttr.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:0805439678"));
                    startActivity(intent);
                } catch (Exception e) {
                    Log.e("Error", e.toString());
                }
            }
        });

        btnACI.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:800116800"));
                    startActivity(intent);
                } catch (Exception e) {
                    Log.e("Error", e.toString());
                }
            }
        });

        btnGomm.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                try {
                    String address = getString(R.string.tire_repairer);
                    address = address.replace(' ', '+');
                    Intent geoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + address));
                    startActivity(geoIntent);
                } catch (Exception e) {
                    Log.e("Error", e.toString());
                }
            }
        });
        btnCarb.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                try {
                    String address = getString(R.string.fuel_dispenser);
                    address = address.replace(' ', '+');
                    Intent geoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + address));
                    startActivity(geoIntent);
                } catch (Exception e) {
                    Log.e("Error", e.toString());
                }
            }
        });
        return v;
    }

}
