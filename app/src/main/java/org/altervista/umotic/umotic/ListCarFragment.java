package org.altervista.umotic.umotic;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import org.altervista.umotic.umotic.R;
import org.altervista.umotic.AdapterRecyclerView.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class ListCarFragment extends Fragment {

    private static final String TAG = "ListCarFragment";
    private ArrayList<String[]> listCar;
    private List<Car> lstCarUser;
    private FloatingActionButton fb;
    private String tagParent="";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    SwipeController swipeController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, getClass().getSimpleName() + ":entered onCreate()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_list_car, container, false);
        getActivity().setTitle(R.string.listCarActivityTitle);
        fb= (FloatingActionButton) v.findViewById(R.id.addCar);
        fb.setOnClickListener(addCarListener);

        Bundle m = getArguments();
        tagParent= m.getString("tag");


        listCar = (ArrayList<String[]>) m.get("listCar");


        //User's car array
        if (listCar.size() > 0) {
            lstCarUser = new ArrayList<>();
            this.orderCar();
            for (String[] b : listCar) {
                lstCarUser.add(new Car(b[0].toUpperCase(),b[1].toUpperCase(),b[2].toUpperCase(),b[3].toUpperCase(), getContext()));
            }

            recyclerView = (RecyclerView) v.findViewById(R.id.listCar);
            //RecyclerView può eseguire diverse ottimizzazioni se sa a priorio che le
            // modifiche del contenuto non modificano le dimensioni della recyclerView
            recyclerView.setHasFixedSize(true);

            layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            final MyAdapter adapter = new MyAdapter(lstCarUser);

            recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
            recyclerView.setAdapter(adapter);

            swipeController = new SwipeController(new SwipeControllerActions() {
                @Override
                public void onRightClicked(final int position) {
                    // Show the removed item label
                    AlertDialog.Builder a_builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.Theme_AppCompat_DayNight_Dialog_Alert));
                    a_builder.setMessage(getActivity().getResources().getText(R.string.deleteCar)).setCancelable(false)
                            .setPositiveButton(getActivity().getResources().getText(R.string.yes), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (adapter.items.size() != 1) {
                                        SharedPreferences preferences = getActivity().getSharedPreferences("dataUser", MODE_PRIVATE);
                                        String id = preferences.getString("id", null);
                                        String targa = adapter.items.get(position).getTarga();
                                        String type = "deleteCar";

                                        //se elimina l'auto selezionata, ne seleziona un'altra(la prima in elenco).
                                        SharedPreferences dataUser = getActivity().getSharedPreferences("dataUser", MODE_PRIVATE);
                                        String usernameFile=dataUser.getString("username","");
                                        SharedPreferences p= getActivity().getSharedPreferences("dataCar_"+usernameFile,MODE_PRIVATE);
                                        String s= p.getString("targa","").toUpperCase();
                                        if(s.equals(targa)){
                                            adapter.setCheckboxView(position);
                                        }

                                        // Remove the item on remove/button click
                                        adapter.items.remove(position);
                                        adapter.notifyDataSetChanged();
                                        /*
                                            public final void notifyItemRemoved (int position)
                                                Notify any registered observers that the item previously located at position
                                                has been removed from the data set. The items previously located at and
                                                after position may now be found at oldPosition - 1.

                                                This is a structural change event. Representations of other existing items
                                                in the data set are still considered up to date and will not be rebound,
                                                though their positions may be altered.

                                            Parameters
                                                position : Position of the item that has now been removed
                                        */
                                        adapter.notifyItemRemoved(position);
                                        /*
                                            public final void notifyItemRangeChanged (int positionStart, int itemCount)
                                                Notify any registered observers that the itemCount items starting at
                                                position positionStart have changed. Equivalent to calling
                                                notifyItemRangeChanged(position, itemCount, null);.

                                                This is an item change event, not a structural change event. It indicates
                                                that any reflection of the data in the given position range is out of date
                                                and should be updated. The items in the given range retain the same identity.

                                            Parameters
                                                positionStart : Position of the first item that has changed
                                                itemCount : Number of items that have changed
                                        */

                                        adapter.notifyItemRangeChanged(position, adapter.getItemCount());

                                        BackgroundWorker backgroundWorker = new BackgroundWorker(getActivity());
                                        backgroundWorker.execute(type, targa, id);


                                    }else{
                                        Toast.makeText(getActivity(), getString(R.string.delete_last_car),Toast.LENGTH_LONG).show();
                                    }
                                }
                            }).setNegativeButton(getActivity().getResources().getText(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    a_builder.show();
                }

                @Override
                public void onLeftClicked(final int position) {
                    String targa = (String) adapter.items.get(position).getTarga();
                    String model = (String) adapter.items.get(position).getModel();
                    String brand = (String) adapter.items.get(position).getBrand();
                    String alias = (String) adapter.items.get(position).getAlias();

                    Bundle bundle = new Bundle();
                    bundle.putString("targa",targa);
                    bundle.putString("model",model);
                    bundle.putString("brand",brand);
                    bundle.putString("alias",alias);

                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    UpdateCarFragment fragment = new UpdateCarFragment();
                    fragment.setArguments(bundle);
                    fragment.setRetainInstance(true);
                    transaction.add(R.id.container,fragment);
                    transaction.addToBackStack("UpdateCarFragment");
                    transaction.commit();
                }

            }, getContext());

            ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
            itemTouchhelper.attachToRecyclerView(recyclerView);
            //Questa è una classe di utilità per aggiungere lo scorrimento per eliminare e trascinare il supporto su RecyclerView.
            //definisce lo scorrimento delle view all'interno della recyclerView
            recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                    swipeController.onDraw(c);
                }
            });
        }

        return v;
    }

    //save the car data
    public View.OnClickListener addCarListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //add Car
            Bundle p=new Bundle();
            p.putString("tag",tagParent);
            FragmentTransaction transaction= getActivity().getSupportFragmentManager().beginTransaction();
            CreateCarFragment fragment = new CreateCarFragment();
            fragment.setRetainInstance(true);
            fragment.setArguments(p);
            transaction.add(R.id.container,fragment);
            transaction.addToBackStack("CreateCarFragment");
            transaction.commit();
        }

    };

    public void orderCar() {
        Collections.sort(this.listCar, new Comparator<String[]>() {
            @Override
            public int compare(String[] t0, String[] t1) {
                return t0[3].toLowerCase().compareTo(t1[3].toLowerCase());
            }

        });
    }

    @Override
    public void onResume(){
        super.onResume();
        getActivity().setTitle(R.string.carsActivityTitle);
    }


}
