package org.altervista.umotic.umotic;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;

public class CreateCarFragment extends Fragment /*implements View.OnClickListener implements AdapterView.OnItemSelectedListener*/ {
    private static final String TAG = "CreateCarFragment";
    private EditText licenseText;
    private EditText customNameText;
    private Button createCarButton;
    private String[] modelArray;
    private String[] brandArray;
    private String b,m;
    private Spinner brandSpinner, modelSpinner;
    private ArrayAdapter<String> adapterbrand,adapterModel;
    private String tagParent="";

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_create_car, container, false);
        getActivity().setTitle(R.string.createCarBtn);

        Bundle c = getArguments();
        tagParent= c.getString("tag");

        licenseText= (EditText) v.findViewById(R.id.input_lincense);
        brandSpinner = (Spinner) v.findViewById(R.id.spinner_brand);
        modelSpinner = (Spinner) v.findViewById(R.id.spinner_models);
        customNameText= (EditText) v.findViewById(R.id.input_custom_name);
        createCarButton= (Button) v.findViewById(R.id.btn_CreateCar);
        createCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnAddCar();
            }
        });
        brandArray = getResources().getStringArray(R.array.marcheAuto);

        adapterbrand = new ArrayAdapter<String>(getActivity(),R.layout.spinner_item,brandArray){
            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextColor(getResources().getColor(R.color.black));
                parent.setBackgroundResource(R.color.white);
                tv.setGravity(Gravity.CENTER);
                return view;
            }
        };

        //adapterbrand.setDropDownViewTheme(android.R.layout.sim);
        brandSpinner.setAdapter(adapterbrand);
        //brandSpinner.setPopupBackgroundResource(R.color.primary_dark);
        //modelSpinner.setPopupBackgroundResource(R.color.primary_dark);


        brandSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) adapterView.getChildAt(0)).setTextColor(getResources().getColor(R.color.black));
                String textBrand = brandSpinner.getSelectedItem().toString();
                switch(textBrand){
                    case "ABARTH":
                        modelArray = getResources().getStringArray(R.array.abarth);
                        selectBrand(textBrand);
                        break;
                    case "AC" :
                        modelArray = getResources().getStringArray(R.array.ac);
                        selectBrand(textBrand);
                        break;
                    case "AIXAM" :
                        modelArray = getResources().getStringArray(R.array.aixam);
                        selectBrand(textBrand);
                        break;
                    case "ALFA ROMEO" :
                        modelArray = getResources().getStringArray(R.array.alfa_romeo);
                        selectBrand(textBrand);
                        break;
                    case "ASTON MARTIN" :
                        modelArray = getResources().getStringArray(R.array.aston_martin);
                        selectBrand(textBrand);
                        break;
                    case "AUDI" :
                        modelArray = getResources().getStringArray(R.array.audi);
                        selectBrand(textBrand);
                        break;
                    case "AUSTIN ROVER" :
                        modelArray = getResources().getStringArray(R.array.austin_rover);
                        selectBrand(textBrand);
                        break;
                    case "AUTOBIANCHI" :
                        modelArray = getResources().getStringArray(R.array.autobianchi);
                        selectBrand(textBrand);
                        break;
                    case "BELLIER" :
                        modelArray = getResources().getStringArray(R.array.bellier);
                        selectBrand(textBrand);
                        break;
                    case "BENTLEY" :
                        modelArray = getResources().getStringArray(R.array.bentley);
                        selectBrand(textBrand);
                        break;
                    case "BMW" :
                        modelArray = getResources().getStringArray(R.array.bmw);
                        selectBrand(textBrand);
                        break;
                    case "BUGATTI" :
                        modelArray = getResources().getStringArray(R.array.bugatti);
                        selectBrand(textBrand);
                        break;
                    case "BUICK" :
                        modelArray = getResources().getStringArray(R.array.buick);
                        selectBrand(textBrand);
                        break;
                    case "CADILLAC" :
                        modelArray = getResources().getStringArray(R.array.cadillac);
                        selectBrand(textBrand);
                        break;
                    case "CASALINI" :
                        modelArray = getResources().getStringArray(R.array.casalini);
                        selectBrand(textBrand);
                        break;
                    case "CHATENET" :
                        modelArray = getResources().getStringArray(R.array.chatenet);
                        selectBrand(textBrand);
                        break;
                    case "CHEVROLET" :
                        modelArray = getResources().getStringArray(R.array.chevrolet);
                        selectBrand(textBrand);
                        break;
                    case "CHRYSLER" :
                        modelArray = getResources().getStringArray(R.array.chrysler);
                        selectBrand(textBrand);
                        break;
                    case "CITROEN" :
                        modelArray = getResources().getStringArray(R.array.citroen);
                        selectBrand(textBrand);
                        break;
                    case "CORVETTE" :
                        modelArray = getResources().getStringArray(R.array.corvette);
                        selectBrand(textBrand);
                        break;
                    case "DACIA" :
                        modelArray = getResources().getStringArray(R.array.dacia);
                        selectBrand(textBrand);
                        break;
                    case "DAEWOO" :
                        modelArray = getResources().getStringArray(R.array.daewoo);
                        selectBrand(textBrand);
                        break;
                    case "DAF" :
                        modelArray = getResources().getStringArray(R.array.daf);
                        selectBrand(textBrand);
                        break;
                    case "DAIHATSU" :
                        modelArray = getResources().getStringArray(R.array.daihatsu);
                        selectBrand(textBrand);
                        break;
                    case "DAIMLER" :
                        modelArray = getResources().getStringArray(R.array.daimler);
                        selectBrand(textBrand);
                        break;
                    case "DE TOMASO" :
                        modelArray = getResources().getStringArray(R.array.detomaso);
                        selectBrand(textBrand);
                        break;
                    case "DODGE" :
                        modelArray = getResources().getStringArray(R.array.dodge);
                        selectBrand(textBrand);
                        break;
                    case "DR" :
                        modelArray = getResources().getStringArray(R.array.dr);
                        selectBrand(textBrand);
                        break;
                    case "DS" :
                        modelArray = getResources().getStringArray(R.array.ds);
                        selectBrand(textBrand);
                        break;
                    case "FERRARI" :
                        modelArray = getResources().getStringArray(R.array.ferrari);
                        selectBrand(textBrand);
                        break;
                    case "FIAT" :
                        modelArray = getResources().getStringArray(R.array.fiat);
                        selectBrand(textBrand);
                        break;
                    case "FISKER" :
                        modelArray = getResources().getStringArray(R.array.fisker);
                        selectBrand(textBrand);
                        break;
                    case "FORD" :
                        modelArray = getResources().getStringArray(R.array.ford);
                        selectBrand(textBrand);
                        break;
                    case "GIOTTI VICTORIA" :
                        modelArray = getResources().getStringArray(R.array.giotti);
                        selectBrand(textBrand);
                        break;
                    case "GREAT WALL MOTOR" :
                        modelArray = getResources().getStringArray(R.array.great_wall_motor);
                        selectBrand(textBrand);
                        break;
                    case "HONDA" :
                        modelArray = getResources().getStringArray(R.array.honda);
                        selectBrand(textBrand);
                        break;
                    case "HUMMER" :
                        modelArray = getResources().getStringArray(R.array.hummer);
                        selectBrand(textBrand);
                        break;
                    case "HYUNDAI" :
                        modelArray = getResources().getStringArray(R.array.hyundai);
                        selectBrand(textBrand);
                        break;
                    case "INFINITI" :
                        modelArray = getResources().getStringArray(R.array.infiniti);
                        selectBrand(textBrand);
                        break;
                    case "INNOCENTI" :
                        modelArray = getResources().getStringArray(R.array.innocenti);
                        selectBrand(textBrand);
                        break;
                    case "ISUZU" :
                        modelArray = getResources().getStringArray(R.array.isuzu);
                        selectBrand(textBrand);
                        break;
                    case "IVECO" :
                        modelArray = getResources().getStringArray(R.array.iveco);
                        selectBrand(textBrand);
                        break;
                    case "JAGUAR" :
                        modelArray = getResources().getStringArray(R.array.jaguar);
                        selectBrand(textBrand);
                        break;
                    case "JEEP" :
                        modelArray = getResources().getStringArray(R.array.jeep);
                        selectBrand(textBrand);
                        break;
                    case "KIA" :
                        modelArray = getResources().getStringArray(R.array.kia);
                        selectBrand(textBrand);
                        break;
                    case "LADA" :
                        modelArray = getResources().getStringArray(R.array.lada);
                        selectBrand(textBrand);
                        break;
                    case "LAMBORGHINI" :
                        modelArray = getResources().getStringArray(R.array.lamborghini);
                        selectBrand(textBrand);
                        break;
                    case "LANCIA" :
                        modelArray = getResources().getStringArray(R.array.lancia);
                        selectBrand(textBrand);
                        break;
                    case "LAND ROVER" :
                        modelArray = getResources().getStringArray(R.array.landrover);
                        selectBrand(textBrand);
                        break;
                    case "LEXUS" :
                        modelArray = getResources().getStringArray(R.array.lexus);
                        selectBrand(textBrand);
                        break;
                    case "LIGIER" :
                        modelArray = getResources().getStringArray(R.array.ligier);
                        selectBrand(textBrand);
                        break;
                    case "LOTUS" :
                        modelArray = getResources().getStringArray(R.array.lotus);
                        selectBrand(textBrand);
                        break;
                    case "MAHINDRA" :
                        modelArray = getResources().getStringArray(R.array.mahindra);
                        selectBrand(textBrand);
                        break;
                    case "MASERATI" :
                        modelArray = getResources().getStringArray(R.array.maserati);
                        selectBrand(textBrand);
                        break;
                    case "MAYBACH" :
                        modelArray = getResources().getStringArray(R.array.maybach);
                        selectBrand(textBrand);
                        break;
                    case "MAZDA" :
                        modelArray = getResources().getStringArray(R.array.mazda);
                        selectBrand(textBrand);
                        break;
                    case "MCLAREN" :
                        modelArray = getResources().getStringArray(R.array.mclaren);
                        selectBrand(textBrand);
                        break;
                    case "MERCEDES" :
                        modelArray = getResources().getStringArray(R.array.mercedes);
                        selectBrand(textBrand);
                        break;
                    case "MG" :
                        modelArray = getResources().getStringArray(R.array.mg);
                        selectBrand(textBrand);
                        break;
                    case "MICROCAR" :
                        modelArray = getResources().getStringArray(R.array.microcar);
                        selectBrand(textBrand);
                        break;
                    case "MINAUTO":
                        modelArray = getResources().getStringArray(R.array.miniauto);
                        selectBrand(textBrand);
                        break;
                    case "MINI" :
                        modelArray = getResources().getStringArray(R.array.mini);
                        selectBrand(textBrand);
                        break;
                    case "MITSUBISHI" :
                        modelArray = getResources().getStringArray(R.array.mitsubishi);
                        selectBrand(textBrand);
                        break;
                    case "MOKE" :
                        modelArray = getResources().getStringArray(R.array.moke);
                        selectBrand(textBrand);
                        break;
                    case "MORGAN" :
                        modelArray = getResources().getStringArray(R.array.morgan);
                        selectBrand(textBrand);
                        break;
                    case "MUSTANG" :
                        modelArray = getResources().getStringArray(R.array.mustang);
                        selectBrand(textBrand);
                        break;
                    case "NISSAN" :
                        modelArray = getResources().getStringArray(R.array.nissan);
                        selectBrand(textBrand);
                        break;
                    case "OPEL" :
                        modelArray = getResources().getStringArray(R.array.opel);
                        selectBrand(textBrand);
                        break;
                    case "PEUGEOT" :
                        modelArray = getResources().getStringArray(R.array.peugeot);
                        selectBrand(textBrand);
                        break;
                    case "PIAGGIO" :
                        modelArray = getResources().getStringArray(R.array.piaggio);
                        selectBrand(textBrand);
                        break;
                    case "PONTIAC" :
                        modelArray = getResources().getStringArray(R.array.pontiac);
                        selectBrand(textBrand);
                        break;
                    case "PORSCHE" :
                        modelArray = getResources().getStringArray(R.array.porsche);
                        selectBrand(textBrand);
                        break;
                    case "RENAULT" :
                        modelArray = getResources().getStringArray(R.array.renault);
                        selectBrand(textBrand);
                        break;
                    case "ROLLS ROYCE" :
                        modelArray = getResources().getStringArray(R.array.rolls_royce);
                        selectBrand(textBrand);
                        break;
                    case "ROVER" :
                        modelArray = getResources().getStringArray(R.array.rover);
                        selectBrand(textBrand);
                        break;
                    case "SAAB" :
                        modelArray = getResources().getStringArray(R.array.saab);
                        selectBrand(textBrand);
                        break;
                    case "SANTANA" :
                        modelArray = getResources().getStringArray(R.array.santana);
                        selectBrand(textBrand);
                        break;
                    case "SEAT" :
                        modelArray = getResources().getStringArray(R.array.seat);
                        selectBrand(textBrand);
                        break;
                    case "SKODA" :
                        modelArray = getResources().getStringArray(R.array.skoda);
                        selectBrand(textBrand);
                        break;
                    case "SMART" :
                        modelArray = getResources().getStringArray(R.array.smart);
                        selectBrand(textBrand);
                        break;
                    case "SSANGYONG" :
                        modelArray = getResources().getStringArray(R.array.ssangyong);
                        selectBrand(textBrand);
                        break;
                    case "SUBARU" :
                        modelArray = getResources().getStringArray(R.array.subaru);
                        selectBrand(textBrand);
                        break;
                    case "SUZUKI" :
                        modelArray = getResources().getStringArray(R.array.suzuki);
                        selectBrand(textBrand);
                        break;
                    case "TALBOT" :
                        modelArray = getResources().getStringArray(R.array.talbot);
                        selectBrand(textBrand);
                        break;
                    case "TATA" :
                        modelArray = getResources().getStringArray(R.array.tata);
                        selectBrand(textBrand);
                        break;
                    case "TESLA" :
                        modelArray = getResources().getStringArray(R.array.tesla);
                        selectBrand(textBrand);
                        break;
                    case "TOYOTA" :
                        modelArray = getResources().getStringArray(R.array.toyota);
                        selectBrand(textBrand);
                        break;
                    case "UAZ" :
                        modelArray = getResources().getStringArray(R.array.uaz);
                        selectBrand(textBrand);
                        break;
                    case "VEM" :
                        modelArray =getResources().getStringArray(R.array.vem);
                        selectBrand(textBrand);
                        break;
                    case "VOLKSWAGEN" :
                        modelArray = getResources().getStringArray(R.array.volkswagen);
                        selectBrand(textBrand);
                        break;
                    case "VOLVO" :
                        modelArray = getResources().getStringArray(R.array.volvo);
                        selectBrand(textBrand);
                        break;
                    case "Other Brand" :
                        modelArray= getResources().getStringArray(R.array.other);
                        selectBrand(textBrand);
                        break;
                    default:
                        modelArray=new String[1];
                        modelArray[0]= getResources().getString(R.string.select_model);
                        selectBrand(textBrand);
                        b=textBrand;
                        break;
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}

        });




        modelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String textModel= modelSpinner.getSelectedItem().toString();
                ((TextView) adapterView.getChildAt(0)).setTextColor(getResources().getColor(R.color.black));
                if(!textModel.equals(getString(R.string.select_model))){
                    m=textModel;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        return v;
    }


    private void selectBrand(String text){
        b=text;
        adapterModel = new ArrayAdapter<String>(getActivity(),R.layout.spinner_item, modelArray){
            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setGravity(Gravity.CENTER);
                tv.setTextColor(getResources().getColor(R.color.black));
                parent.setBackgroundResource(R.color.white);


                return view;
            }
        };
        modelSpinner.setAdapter(adapterModel);
    }


    public boolean validate() {
        boolean valid = true;

        String license = licenseText.getText().toString().trim();
        //String modelArray = modelText.getText().toString().trim();
        String brand = brandSpinner.getSelectedItem().toString();
        String customName = customNameText.getText().toString().trim();


        if (license.isEmpty() || license.length()!=7) {
            licenseText.setError(getString(R.string.licenseEmpty));
            valid = false;
        } else {
            licenseText.setError(null);
        }



        TextView errorText = (TextView) brandSpinner.getSelectedView();
        if (brandSpinner.getSelectedItem().toString().equals(getResources().getString(R.string.select_brand))) {
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText(getString(R.string.wrongBrand));//changes the selected item text to this
            valid = false;
        } else {
            errorText.setError(null);
        }


        TextView errorTextm = (TextView) modelSpinner.getSelectedView();
        if (modelSpinner.getSelectedItem().toString().equals(getResources().getString(R.string.select_model))) {

            errorTextm.setError("");
            errorTextm.setTextColor(Color.RED);//just to highlight that this is an error
            errorTextm.setText(getString(R.string.wrongModel));//changes the selected item text to this
            valid = false;
        } else{
            errorTextm.setError(null);
        }


        if (customName.length() > 25) {
            customNameText.setError(getResources().getText(R.string.nameTooLong));
            valid = false;
        } else {
            customNameText.setError(null);
        }
        return valid;
    }

    public void OnAddCar() {
        if (validate()) {
            SharedPreferences preferences = this.getActivity().getSharedPreferences("dataUser", MODE_PRIVATE);
            String license = licenseText.getText().toString().trim();
            if(customNameText.getText().toString().isEmpty()){
                customNameText.setText(brandSpinner.getSelectedItem().toString());
            }

            String customName = customNameText.getText().toString().trim();
            String id = preferences.getString("id", null);

            BackgroundWorker backgroundWorker = new BackgroundWorker(getActivity());
            backgroundWorker.execute(TAG, license, b, m, customName, id,tagParent);
        }
    }

    @Override
    public void onDetach() {
        getActivity().setTitle(R.string.carsActivityTitle);
        super.onDetach();
    }
}
