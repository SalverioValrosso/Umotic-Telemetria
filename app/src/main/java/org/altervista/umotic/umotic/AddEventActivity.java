package org.altervista.umotic.umotic;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.CalendarContract;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.altervista.umotic.AdapterEvents.EventAdapter;
import org.altervista.umotic.AdapterEvents.EventItem;
import org.altervista.umotic.AdapterRecyclerView.SwipeController;


import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AddEventActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    /*
    RecyclerView list;
    RecyclerView.LayoutManager layoutManager;
    List<Item> items = new ArrayList<>();

    FloatingActionButton floatingActionButton;
    Button addEvent;*/

    ArrayList<EventItem> mExampleList;
    private RecyclerView mRecyclerView;
    private EventAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    Context context;
    TextView data, orario;
    EditText titolo;

    String textTitolo;
    String textData;
    String textOrario;
    String giornoText;
    String meseText;
    String annoText;


    String day, y, m;
    Integer giorno, mese, anno, ora, minuto;

    Calendar c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        loadData();
        titolo = findViewById(R.id.titoloEvento);
        data = findViewById(R.id.dataEvento);
        orario = findViewById(R.id.orarioEvento);
        context  = getApplicationContext();

        c = Calendar.getInstance();




        buildRecyclerView();
        setInsertButton();

        setTitle(R.string.addEvent);

    }



    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<EventItem>>() {}.getType();
        mExampleList = gson.fromJson(json, type);



        if (mExampleList == null) {
            mExampleList = new ArrayList<>();
        }

    }

    private void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new EventAdapter(mExampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setInsertButton() {
        Button buttonInsert = findViewById(R.id.button7);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textData = data.getText().toString();
                textOrario = orario.getText().toString();
                textTitolo = titolo.getText().toString();


                if ((textData.isEmpty() ||textOrario.isEmpty() || textTitolo.isEmpty())||(textData.equals("") || textOrario.equals("") || textTitolo.equals(""))) {
                    Toast.makeText(getApplicationContext(),getString(R.string.event_validate),Toast.LENGTH_SHORT).show();
                } else {
                    String d = textData.replace("/", "");

                    int dim = d.length();

                    if (dim == 8) {
                        giornoText = d.substring(0, 2);
                        meseText = d.substring(2, 4);
                        annoText = d.substring(4, 8);

                    }

                    //CONVERSIONE DI GIORNO MESE E ANNO IN INTERO
                    anno = Integer.parseInt(d.substring(dim - 4, dim));
                    mese = Integer.parseInt(d.substring(2, 4));
                    giorno = Integer.parseInt(d.substring(0, 2));


                    insertItem(textTitolo, textData, textOrario);
                    setEvent(textTitolo, giorno, mese, anno, ora, minuto);

                    titolo.setText("");
                    data.setText("");
                    orario.setText("");

                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
        }
        return true;
    }

    private void insertItem(String textTitolo, String textData, String textOrario) {
        mExampleList.add(new EventItem(textTitolo, textData, textOrario));
        mAdapter.notifyItemInserted(mExampleList.size());
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mExampleList);
        editor.putString("task list", json);
        editor.apply();
    }

    public void setData(View view){

        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);


        Log.i("GIORNO GET", dayOfMonth + "");




        DatePickerDialog datePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd-MM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ITALY);

                day = dayOfMonth + "";
                m = month+ 1 + "";



                if(month + 1 < 10) {
                    m = "0" + m;
                }
                if(dayOfMonth < 10){
                    day = "0" + day;
                }


                data.setText(day + "/" + m + "/" + year);

                m = "";
                day = "";

            }
        }, dayOfMonth, month, year);

        Log.i("ANNO SETTATO", month + "");

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

        datePickerDialog.show();

    }

    public void orarioPicker(View view){
        DialogFragment timePicker = new TimePickerFragment();

        timePicker.show(getSupportFragmentManager(), "time picker");
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        if(minute < 10){
            orario.setText(hourOfDay + ":0" + minute);
        }else{
            orario.setText(hourOfDay + ":" + minute);
        }
        this.ora = hourOfDay;
        this.minuto = minute;

    }


    public void setEvent(String titolo, int day, int mnth, int year, int hrs, int min){
        Calendar beginCal = Calendar.getInstance();
        beginCal.set(year, mnth, day, hrs, min);

        Log.i("GIORNO", hrs + "");





        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra(CalendarContract.Events.TITLE, titolo);
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, "");
        intent.putExtra(CalendarContract.Events.STATUS, 1);
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginCal.getTimeInMillis());
        intent.putExtra(CalendarContract.Events.VISIBLE, 0);
        intent.putExtra(CalendarContract.Events.HAS_ALARM, 1);
        intent.putExtra(CalendarContract.Events.EVENT_COLOR, getResources().getColor(R.color.accent));
        startActivity(intent);


    }




    private void cleanPref(){
        SharedPreferences preferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        preferences.edit().remove("task list").commit();

    }




    public void deleteItem(View view){
        mAdapter.onLongClick(view);
    }
}