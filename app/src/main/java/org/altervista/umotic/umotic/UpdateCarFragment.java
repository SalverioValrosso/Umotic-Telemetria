package org.altervista.umotic.umotic;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateCarFragment extends Fragment {
    private static final String TAG = "UpdateCarFragment";
    private Bundle m;
    private EditText licenseText,modelText, brandText,customNameText;
    private Button updateCarButton, commit;
    private String license,model,brand,customName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_update_car, container, false);
        getActivity().setTitle(R.string.updateActivityTitle);
        m=this.getArguments();
        licenseText = (EditText) v.findViewById(R.id.input_lincense);
        modelText = (EditText) v.findViewById(R.id.input_car_model);
        brandText = (EditText) v.findViewById(R.id.input_car_brand);
        customNameText = (EditText) v.findViewById(R.id.input_custom_name);
        updateCarButton = (Button) v.findViewById(R.id.btn_UpdateCar);
        updateCarButton.setOnClickListener(updateCarButtonListener);
        commit = (Button) v.findViewById(R.id.btn_Commit);
        commit.setOnClickListener(commitButtonListener);

        licenseText.setEnabled(false);
        modelText.setEnabled(false);
        brandText.setEnabled(false);
        customNameText.setEnabled(false);
        commit.setVisibility(View.GONE);

        licenseText.setText(m.getString("targa"));
        modelText.setText(m.getString("model"));
        brandText.setText(m.getString("brand"));
        customNameText.setText(m.getString("alias"));

        return v;
    }


    public View.OnClickListener updateCarButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            customNameText.setEnabled(true);
            updateCarButton.setVisibility(View.GONE);
            commit.setVisibility(View.VISIBLE);

        }
    };

    public View.OnClickListener commitButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            update();
        }
    };


    private void update() {
        Log.d(TAG, "UpdateCar");

        if (!validate()) {
            onUpdateFailed();
        } else {
            onUpdateSuccess();
        }
    }

    public boolean validate() {
        boolean valid = true;

        license = licenseText.getText().toString().trim();
        model = modelText.getText().toString().trim();
        brand = brandText.getText().toString().trim();
        customName = customNameText.getText().toString().trim();



        if (license.isEmpty() || license.length()>7) {
            licenseText.setError(getResources().getText(R.string.licenseEmpty));
            valid = false;
        } else {
            licenseText.setError(null);
        }

        if (brand.isEmpty() || brand.length()>25) {
            brandText.setError(getResources().getText(R.string.wrongBrand));
            valid = false;
        } else {
            brandText.setError(null);
        }

        if (model.isEmpty()|| model.length()>25) {
            modelText.setError(getResources().getText(R.string.wrongModel));
            valid = false;
        } else {
            modelText.setError(null);
        }

        if(customName.length() == 0) {
            customName = brand;
        }else if (customName.length() > 25) {
            customNameText.setError(getResources().getText(R.string.nameTooLong));
            valid = false;
        } else {
            customNameText.setError(null);
        }
        return valid;
    }



    private void onUpdateSuccess() {

        BackgroundWorker backgroundWorker = new BackgroundWorker(getActivity());
        backgroundWorker.execute(TAG,brand,model,license,customName);
        Toast.makeText(getActivity(), getString(R.string.edit_car), Toast.LENGTH_SHORT).show();
    }

    public void onUpdateFailed() {
        Toast.makeText(getActivity(), getString(R.string.edit_not_car), Toast.LENGTH_LONG).show();
    }
    @Override
    public void onDetach() {
        getActivity().setTitle(R.string.carsActivityTitle);
        super.onDetach();
    }
}
