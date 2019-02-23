package org.altervista.umotic.umotic;


import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.altervista.umotic.bluetooth.BluetoothConnectionService;
import org.altervista.umotic.bluetooth.Constants;
import org.altervista.umotic.bluetooth.DeviceListActivity;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static org.altervista.umotic.umotic.RealTimeFragment.Bluetooth.REQUEST_CONNECT_DEVICE_INSECURE;
import static org.altervista.umotic.umotic.RealTimeFragment.Bluetooth.REQUEST_CONNECT_DEVICE_SECURE;
import static org.altervista.umotic.umotic.RealTimeFragment.Bluetooth.REQUEST_ENABLE_BT;


/**
 * A simple {@link Fragment} subclass.
 */
public class RealTimeFragment extends Fragment implements onBackPressed{
    private static final String TAG = "RealTimeFragment";
    private static final String TAG1 = "sendData";
    String flag;
    private ProgressBar rpm;
    private TextView speedValue, RPMValue, distanceValue, temperatureValue, consumiValue;
    private Bluetooth bt;
    private Button stop,start;
    ImageView indicatoreSpeed,indicatoreFuel;
    private int a=0;
    private int row=0;
    private ArrayList<String[]> data;
    private ArrayList<Integer> speedAverage;
    private ArrayList<Double> fuelAverage;
    private TextView speedtext, fueltext;
    public RealTimeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_real_time, container, false);
        setHasOptionsMenu(true);


        data=new ArrayList<>();
        speedAverage= new ArrayList<>();
        fuelAverage= new ArrayList<>();
        rpm = (ProgressBar) v.findViewById(R.id.progressBar1);
        speedValue = (TextView) v.findViewById(R.id.speedValue);
        temperatureValue = (TextView) v.findViewById(R.id.EngineTempValue);
        RPMValue = (TextView) v.findViewById(R.id.RPMValue);
        distanceValue = (TextView) v.findViewById(R.id.PartialDistanceValue);
        consumiValue = (TextView) v.findViewById(R.id.consumiValue);
        start=(Button) v.findViewById(R.id.btn_start_sync);
        stop=(Button) v.findViewById(R.id.btn_stop_sync);
        speedtext = (TextView) v.findViewById(R.id.textView12);
        fueltext = (TextView) v.findViewById(R.id.textView14);
        indicatoreSpeed= (ImageView) v.findViewById(R.id.alert_icon);
        indicatoreFuel= (ImageView) v.findViewById(R.id.alert_icon3);
        speedtext.setText("0 Km/h");
        fueltext.setText("0.0 Km/l");

        bt= new Bluetooth();
        setBt();

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt.mConnectionService.stop();
                stop.setVisibility(View.GONE);
                start.setVisibility(View.VISIBLE);
                data.clear();
            }
        });


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBt();
            }
        });

        return v;
    }





    private void setBt(){
        SharedPreferences dataUser = getActivity().getSharedPreferences("dataUser", MODE_PRIVATE);
        String usernameFile=dataUser.getString("username","");
        SharedPreferences dataCar = getActivity().getSharedPreferences("dataCar_"+usernameFile, MODE_PRIVATE);
        if(dataCar.getString("targa",null)!=null) {
            if (!bt.mBluetoothAdapter.isEnabled()) {
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent, REQUEST_ENABLE_BT);

            } else if (bt.mConnectionService == null) {
                bt.setupChat();
            }

            //Scanning devices if Bluetooth is enabled
            if (bt.mBluetoothAdapter.isEnabled()) {
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
            }
        }else{
            Toast.makeText(getActivity(),getString(R.string.first_select_car), Toast.LENGTH_LONG).show();
            getActivity().finish();
        }
    }


    @Override
    public void onResume() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onResume()");
        super.onResume();
        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (bt.mConnectionService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (bt.mConnectionService.getState() == BluetoothConnectionService.STATE_NONE) {
                // Start the Bluetooth chat services
                bt.mConnectionService.start();

            }
        }
    }

    @Override
    public void onPause() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onPause()");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onStop()");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onDestroy()");
        super.onDestroy();
        if (bt.mConnectionService != null) {
            bt.mConnectionService.stop();
        }
    }

    @Override
    public void onDetach(){
        super.onDetach();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.title_sign_out)
                        .setMessage(R.string.mex_interrupt_journey)
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                if (bt.mConnectionService!=null&&bt.mBluetoothAdapter != null) {
                                    bt.mConnectionService.stop();
                                }
                                getActivity().finish();
                            }
                        }).create().show();
                break;
        }
        return true;
    }


    @TargetApi(26)
    public void setGraphs(String s) throws IOException, URISyntaxException {
        String[] tuple=s.split(",");
        data.add(tuple);

        /**
         *
         2019-02-09 17:15:40.816 23981-23981/org.altervista.umotic.umotic I/PU: GPS Time ->0
         2019-02-09 17:15:40.816 23981-23981/org.altervista.umotic.umotic I/PU:  Device Time ->1
         2019-02-09 17:15:40.816 23981-23981/org.altervista.umotic.umotic I/PU:  Longitude ->2
         2019-02-09 17:15:40.816 23981-23981/org.altervista.umotic.umotic I/PU:  Latitude ->3
         2019-02-09 17:15:40.816 23981-23981/org.altervista.umotic.umotic I/PU: GPS Speed (Meters/second) ->4
         2019-02-09 17:15:40.817 23981-23981/org.altervista.umotic.umotic I/PU:  Horizontal Dilution of Precision ->5
         2019-02-09 17:15:40.817 23981-23981/org.altervista.umotic.umotic I/PU:  Altitude ->6
         2019-02-09 17:15:40.817 23981-23981/org.altervista.umotic.umotic I/PU:  Bearing ->7
         2019-02-09 17:15:40.817 23981-23981/org.altervista.umotic.umotic I/PU:  G(x) ->8
         2019-02-09 17:15:40.817 23981-23981/org.altervista.umotic.umotic I/PU:  G(y) ->9
         2019-02-09 17:15:40.817 23981-23981/org.altervista.umotic.umotic I/PU:  G(z) ->10
         2019-02-09 17:15:40.817 23981-23981/org.altervista.umotic.umotic I/PU:  G(calibrated) ->11
         2019-02-09 17:15:40.818 23981-23981/org.altervista.umotic.umotic I/PU: Speed (OBD)(km/h) ->12
         2019-02-09 17:15:40.818 23981-23981/org.altervista.umotic.umotic I/PU: Engine RPM(rpm) ->13
         2019-02-09 17:15:40.818 23981-23981/org.altervista.umotic.umotic I/PU: Kilometers Per Litre(Instant)(kpl) ->14
         2019-02-09 17:15:40.818 23981-23981/org.altervista.umotic.umotic I/PU: Trip Distance(km) ->15
         2019-02-09 17:15:40.818 23981-23981/org.altervista.umotic.umotic I/PU: Turbo Boost & Vacuum Gauge(psi) ->16

         */



        if(tuple!=null&&tuple.length>15) {




            String rpmvalue=tuple[13];
            String speedvalue=tuple[12];
            String distvalue=tuple[15];
            String fuelvalue=tuple[14];
            String coolvalue=tuple[16];

            String deviceTime=tuple[1];





            int rm=0;
            try {


                rpmvalue=rpmvalue.substring(0,(rpmvalue.indexOf(".")));
                rm=Integer.parseInt(rpmvalue);

                speedvalue=speedvalue.substring(0,(speedvalue.indexOf(".")));
                distvalue=distvalue.substring(0,(distvalue.indexOf(".")+4));
                fuelvalue=fuelvalue.substring(0,(fuelvalue.indexOf(".")+2));
                coolvalue=coolvalue.substring(0,(coolvalue.indexOf(".")+2));


                BackgroundWorker b = new BackgroundWorker(getActivity());
                b.execute(TAG1, rpmvalue,speedvalue,distvalue,fuelvalue,coolvalue,deviceTime);


            }catch(IndexOutOfBoundsException e){
                rpmvalue="Load";
                rm=0;
            }


            //Toast.makeText(this, rp, Toast.LENGTH_SHORT).show();
            int mediaSpeed=0;
            double mediaConsumo=0.0;

            if(data.size()>1){


                Double ind_cons=0.0;
                Integer ind_speed=0;
                    try {
                        ind_speed = Integer.parseInt(speedvalue);
                    }catch (Exception e){
                        Log.i("PARSER Error","");
                    }


                if(!fuelvalue.equals("-")){
                    try {
                        ind_cons = Double.parseDouble(fuelvalue);
                    }catch (Exception e){
                        Log.i("PARSER Error","");

                    }

                    int v= 0;
                    speedAverage.add(ind_speed);
                    for(int i=0;i<speedAverage.size();i++){
                        v= v+ speedAverage.get(i);
                    }
                    //mediaAritmetica
                    mediaSpeed=v/speedAverage.size();
                    speedtext.setText(""+mediaSpeed+" "+getString(R.string.kmPerHour));
                    if(mediaSpeed>120){
                        indicatoreSpeed.setImageResource(R.drawable.alert_icon_r);
                    }else{
                        indicatoreSpeed.setImageResource(R.drawable.alert_icon_g);
                    }

                    double c=0.0;
                    fuelAverage.add(ind_cons);
                    for(int i=0;i<fuelAverage.size();i++){
                        c= c+ fuelAverage.get(i);
                    }
                    //mediaAritmetica
                    mediaConsumo=c/fuelAverage.size();
                    String result = String.format("%.1f Km/l", mediaConsumo);
                    fueltext.setText(result);
                    if(mediaConsumo>10){
                        indicatoreFuel.setImageResource(R.drawable.alert_icon_r);
                    }else{
                        indicatoreFuel.setImageResource(R.drawable.alert_icon_g);
                    }

                }else{
                    int v=0;
                    speedAverage.add(ind_speed);
                    for(int i=0;i<speedAverage.size();i++){
                        v= v+ speedAverage.get(i);
                    }
                    //mediaAritmetica
                    mediaSpeed=v/speedAverage.size();
                    speedtext.setText(""+mediaSpeed);
                    if(mediaSpeed>120){
                        indicatoreSpeed.setImageResource(R.drawable.alert_icon_r);
                    }else{
                        indicatoreFuel.setImageResource(R.drawable.alert_icon_g);
                    }
                }


                speedValue.setText(speedvalue);
                RPMValue.setText(rpmvalue);
                distanceValue.setText(distvalue);   // carburante usato Litri
                temperatureValue.setText(coolvalue);
                consumiValue.setText(fuelvalue); //flusso carburante/minuto
                rpm.setMax(4500);
                rpm.setMin(700);

                rpm.setProgress(rm);
            }
        }
        else{
            speedValue.setText("LOST");
            RPMValue.setText("LOST");
            distanceValue.setText("LOST");
            temperatureValue.setText("LOST");
            consumiValue.setText("LOST");
            rpm.setProgress(100);
            a++;
            a = a % 320;
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == RESULT_OK) {
                    bt.connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == RESULT_OK) {
                    bt.connectDevice(data, false);
                }
                break;

            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    bt.setupChat();
                    if (bt.mBluetoothAdapter.isEnabled()) {
                        // Launch the DeviceListActivity to see devices and do scan
                        Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
                    }

                } else {
                    // User did not enable Bluetooth or an error occurred
                    Toast.makeText(getActivity(), R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public void onBackPressed() {
        if(getActivity().getSupportFragmentManager().getBackStackEntryCount()>1){
            getActivity().getSupportFragmentManager().popBackStack();
        }else{
            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.title_sign_out)
                    .setMessage(R.string.mex_interrupt_journey)
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {

                            if (bt.mConnectionService!=null&&bt.mBluetoothAdapter != null) {
                                bt.mConnectionService.stop();
                            }
                           /*
                            navigateUpTo(new Intent(this, MainActivity.class));
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                            */

                            getActivity().finish();
                        }
                    }).create().show();
        }
    }

    public class Bluetooth{

        // Intent request codes
        static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
        static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
        static final int REQUEST_ENABLE_BT = 3;
        //flag for change optionsMenu
        private int state = 0;
        /**
         * Name of the connected device
         */
        private String mConnectedDeviceName = null;

        /**
         * Array adapter for the conversation thread
         */
        private ArrayList<String> mConnectionArrayAdapter;
        /**
         * Local Bluetooth adapter
         */
        private BluetoothAdapter mBluetoothAdapter;

        /**
         * Member object for the chat services
         */
        private BluetoothConnectionService mConnectionService = null;



        public Bluetooth(){

            // Get local Bluetooth adapter
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        }


        public int getState(){
            return state;
        }


        /**
         * Set up the UI and background operations for chat.
         */
        private void setupChat() {
            // Initialize the array adapter for the conversation thread
            mConnectionArrayAdapter = new ArrayList<>();
            // Initialize the BluetoothConnectionService to perform bluetooth connections
            mConnectionService = new BluetoothConnectionService(getActivity().getApplicationContext(), mHandler);
        }


        /**
         * The Handler that gets information back from the BluetoothConnectionService
         */
        private final Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                FragmentActivity activity = getActivity();
                switch (msg.what) {
                    case Constants.MESSAGE_STATE_CHANGE:
                        switch (msg.arg1) {
                            case BluetoothConnectionService.STATE_CONNECTED:
                                state = 3;
                                //RealTimeData.this.invalidateOptionsMenu();
                                mConnectionArrayAdapter.clear();
                                break;
                            case BluetoothConnectionService.STATE_CONNECTING:
                                break;
                            case BluetoothConnectionService.STATE_LISTEN:
                            case BluetoothConnectionService.STATE_NONE:
                                break;
                        }
                        break;
                    case Constants.MESSAGE_READ:
                        byte[] readBuf = (byte[]) msg.obj;
                        // construct a string from the valid bytes in the buffer4
                        String readMessage = new String(readBuf, 0, msg.arg1);
                        try {
                            setGraphs(readMessage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }

                        break;
                    case Constants.MESSAGE_DEVICE_NAME:
                        // save the connected device's name
                        mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                        if (null != activity) {
                            Toast.makeText(activity, getString(R.string.title_connected_to) + " " + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                            stop.setVisibility(View.VISIBLE);
                            start.setVisibility(View.GONE);
                        }
                        break;
                    case Constants.MESSAGE_TOAST:
                        if (null != activity) {
                            Toast.makeText(activity, msg.getData().getString(Constants.TOAST),
                                    Toast.LENGTH_SHORT).show();
                        }
                        break;

                }
            }
        };

        /**
         * Establish connection with other device
         *
         * @param data   An {@link Intent} with {@link DeviceListActivity#EXTRA_DEVICE_ADDRESS} extra.
         * @param secure Socket Security type - Secure (true) , Insecure (false)
         */

        private void connectDevice(Intent data, boolean secure) {
            // Get the device MAC address
            String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
            // Get the BluetoothDevice object
            BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
            // Attempt to connect to the device
            mConnectionService.connect(device, secure);
        }
    }

}
