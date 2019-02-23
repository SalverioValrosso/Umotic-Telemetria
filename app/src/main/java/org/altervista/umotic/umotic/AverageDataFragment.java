package org.altervista.umotic.umotic;


import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.altervista.umotic.AdapterEvents.AdapterEventWidget;
import org.altervista.umotic.AdapterEvents.EventAdapter;
import org.altervista.umotic.AdapterEvents.EventItem;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class AverageDataFragment extends Fragment implements Runnable{
    private ArrayList<EventItem> mExampleList;
    private RecyclerView mRecyclerView;
    private AdapterEventWidget mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button sync;
    private ImageView speedAlert, gasAlert,distanceAlert;
    private TextView speedtxt, disttxt, gastxt;
    private ArrayList<String[]> listRilevations;
    private static final String TAGR = "updateRilevations";
    private Thread thread;
    private boolean running;
    Context context;
    SharedPreferences alert;
    SwipeRefreshLayout swipeContainer;
    ProgressBar p,p1,p2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_average_data, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycleEvents);
        speedAlert = v.findViewById(R.id.alert_icon);
        gasAlert = v.findViewById(R.id.alert_icon3);
        distanceAlert = v.findViewById(R.id.distance_icon);
        distanceAlert.setVisibility(View.INVISIBLE);
        speedtxt = v.findViewById(R.id.textView12);
        disttxt =v.findViewById(R.id.textView13);
        gastxt = v.findViewById(R.id.textView14);

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                if(InternetConnection.haveInternetConnection(getActivity().getApplicationContext())) {
                    if(thread==null||!thread.isAlive()) {
                        start();
                    }else{
                        swipeContainer.setRefreshing(false);
                    }
                }else{
                    swipeContainer.setRefreshing(false);
                    Toast.makeText(getActivity(), getString(R.string.internet_connection),Toast.LENGTH_SHORT).show();
                }
            }
        });


        p =  v.findViewById(R.id.progressBar );
        p1 = v.findViewById(R.id.progressBar1);
        p2 = v.findViewById(R.id.progressBar2);

        alert = getContext().getSharedPreferences("alert",MODE_PRIVATE);

        sync=(Button) v.findViewById(R.id.btn_start);
        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (BluetoothAdapter.getDefaultAdapter() == null) {
                    Toast.makeText(getContext(), getString(R.string.bt_not_available), Toast.LENGTH_LONG).show();
                }else{
                    Intent i= new Intent(getActivity(),RealTimeData.class);
                    startActivity(i);
                }
            }
        });


        return v;
    }



    public void start(){
        if(!running){
            running=true;
            thread= new Thread(this,"MyRunnableThread");
            thread.start();
        }
    }
    @Override
    public void run() {
        Looper.prepare();
        if(InternetConnection.haveInternetConnection(getActivity().getApplicationContext())) {
            BackgroundWorker backgroundWorker = new BackgroundWorker(getActivity());
            backgroundWorker.execute(TAGR);
            swipeContainer.setRefreshing(false);
            while (running) {
                try {
                    listRilevations = backgroundWorker.getRilevation();
                    if (listRilevations != null && listRilevations.size() > 0 && listRilevations.get(0).length == 6) {
                        p.setVisibility(View.INVISIBLE);
                        p1.setVisibility(View.INVISIBLE);
                        p2.setVisibility(View.INVISIBLE);
                        stop();
                    } else {
                        speedAlert.setImageResource(R.drawable.alert_icon_alpha);
                        gasAlert.setImageResource(R.drawable.alert_icon_alpha);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }else{
            swipeContainer.setRefreshing(false);
            speedAlert.setImageResource(R.drawable.alert_icon_alpha);
            gasAlert.setImageResource(R.drawable.alert_icon_alpha);
            Toast.makeText(getActivity(), getString(R.string.internet_connection),Toast.LENGTH_SHORT).show();
            stop();
        }
    }

    public void stop(){
        if(running&& listRilevations != null && listRilevations.size() > 0 && listRilevations.get(0).length == 6) {
            running = false;
            thread = null;
            int size = listRilevations.size();
            String[] s = listRilevations.get(size - 1);
            listRilevations = null;

            speedtxt.setText(s[3] + " Km/h");
            gastxt.setText(s[4] + " Km/l");
            disttxt.setText(s[5] + " Km");

            if (Float.parseFloat(s[3]) != 0 && Float.parseFloat(s[5]) != 0)
                setColor(Float.parseFloat(s[3]), Float.parseFloat(s[4]));
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        stop();
    }

    @Override
    public void onStart(){
        super.onStart();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<EventItem>>() {}.getType();
        mExampleList = gson.fromJson(json, type);

        if (mExampleList == null) {
            mExampleList = new ArrayList<>();
        }

        buildRecyclerView();

    }


    @Override
    public void onResume() {
        super.onResume();
        if(InternetConnection.haveInternetConnection(getActivity().getApplicationContext())) {
            start();
        }else{
            p.setVisibility(View.INVISIBLE);
            p1.setVisibility(View.INVISIBLE);
            p2.setVisibility(View.INVISIBLE);
            speedAlert.setImageResource(R.drawable.alert_icon_alpha);
            gasAlert.setImageResource(R.drawable.alert_icon_alpha);
            Toast.makeText(getActivity(), getString(R.string.internet_connection),Toast.LENGTH_SHORT).show();
        }
    }



    void setColor(float velocita,float consumo){
        SharedPreferences.Editor editor = alert.edit();
        editor.clear();
        editor.putString("flag", "g");



        speedAlert.setImageResource(R.drawable.alert_icon_g);
        gasAlert.setImageResource(R.drawable.alert_icon_g);


        if(velocita>130){
            speedAlert.setImageResource(R.drawable.alert_icon_r);
            editor.putString("flag", "r");
        }



        if(consumo>25){
            gasAlert.setImageResource(R.drawable.alert_icon_r);
            editor.putString("flag", "r");
        }


        editor.apply();

    }

    private void buildRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new AdapterEventWidget(mExampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }


}
