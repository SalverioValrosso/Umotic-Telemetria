package org.altervista.umotic.AdapterRecyclerView;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.Utils;
import org.altervista.umotic.umotic.Car;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import org.altervista.umotic.umotic.R;

/**
 * Created by admin on 21/01/2019.
 */

class MyViewHolderWithChild extends RecyclerView.ViewHolder implements  View.OnClickListener{

    public TextView textView,textViewChild;
    public RelativeLayout button;
    public ImageView delete,update;
    public CheckBox checkbox;
    public ExpandableLinearLayout expandableLayout;
    ItemClickListener itemClickListener;

    public MyViewHolderWithChild(View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.car_alias);
        textViewChild = (TextView) itemView.findViewById(R.id.textViewChild);
        button = (RelativeLayout) itemView.findViewById(R.id.button);
        expandableLayout = (ExpandableLinearLayout) itemView.findViewById(R.id.expandableLayout);
        checkbox= (CheckBox) itemView.findViewById(R.id.btn_select);
        itemView.setOnClickListener(this);
    }


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.OnClick(v,getAdapterPosition(),false);
    }
}

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public List<Car> items;
    Context context;
    SparseBooleanArray expandState = new SparseBooleanArray();
    ArrayList<MyViewHolderWithChild> viewG;
    public MyAdapter(List<Car> items) {
        this.items = items;
        viewG= new ArrayList<>();
        for(int i = 0; i<items.size();i++){
            expandState.append(i,false);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_with_child,parent,false);
        return new MyViewHolderWithChild(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {


        switch (holder.getItemViewType()){
            case 1:
            {
                final MyViewHolderWithChild viewHolder = (MyViewHolderWithChild)holder;
                final Car item = items.get(position);
                viewHolder.setIsRecyclable(false);
                viewHolder.textView.setText(item.getText());
                SharedPreferences dataUser = context.getSharedPreferences("dataUser", MODE_PRIVATE);
                String usernameFile=dataUser.getString("username","");
                SharedPreferences p = context.getSharedPreferences("dataCar_"+usernameFile, MODE_PRIVATE);
                String s= p.getString("targa","").toUpperCase();
                String t= item.getTarga();
                if(s.equals(t)){
                    viewHolder.checkbox.setChecked(true);
                }
                viewG.add(viewHolder);

                viewHolder.expandableLayout.setInRecyclerView(true);
                viewHolder.expandableLayout.setExpanded(expandState.get(position));
                viewHolder.expandableLayout.setListener(new ExpandableLayoutListenerAdapter() {


                    @Override
                    public void onPreOpen() {
                        changeRotate(viewHolder.button,180f,0f).start();
                        expandState.put(position,true);
                    }

                    @Override
                    public void onPreClose() {
                        changeRotate(viewHolder.button,0f,180f).start();
                        expandState.put(position,false);
                    }

                });

                viewHolder.button.setRotation(expandState.get(position)?180f:0f);
                viewHolder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       viewHolder.expandableLayout.toggle();
                    }
                });

                viewHolder.textViewChild.setText(items.get(position).getSubText());
                viewHolder.textViewChild.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context,""+items.get(position).getSubText(),Toast.LENGTH_LONG).show();
                    }
                });

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void OnClick(View view, int position, boolean isLongClick) {
                        //Toast.makeText(context, "With child click : "+items.get(position).getText(),Toast.LENGTH_LONG).show();
                    }

                });

                viewHolder.checkbox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences dataUser = context.getSharedPreferences("dataUser", MODE_PRIVATE);
                        String usernameFile=dataUser.getString("username","");
                        SharedPreferences preferences = context.getSharedPreferences("dataCar_"+usernameFile, MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();

                        String targa = (String) items.get(position).getTarga();
                        String model = (String) items.get(position).getModel();
                        String brand = (String) items.get(position).getBrand();
                        String alias = (String) items.get(position).getAlias();

                        editor.putString("targa", targa);
                        editor.putString("model", model);
                        editor.putString("brand", brand);
                        editor.putString("alias", alias);
                        editor.apply();
                        viewHolder.checkbox.setChecked(true);
                        for(int i=0; i<viewG.size();i++){
                            MyViewHolderWithChild a= viewG.get(i);
                            if(a!=viewHolder)
                                a.checkbox.setChecked(false);
                        }

                    }
                });
            }
            break;
            default:
                break;
        }
    }

    //serve per aggiornare l'arraylist di view
    public void setCheckboxView(int position){
        SharedPreferences dataUser = context.getSharedPreferences("dataUser", MODE_PRIVATE);
        String usernameFile=dataUser.getString("username","");
        SharedPreferences preferences = context.getSharedPreferences("dataCar_"+usernameFile, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();

        if(position==0){
            String targa = (String) items.get(1).getTarga();
            String model = (String) items.get(1).getModel();
            String brand = (String) items.get(1).getBrand();
            String alias = (String) items.get(1).getAlias();
            editor.putString("targa", targa);
            editor.putString("model", model);
            editor.putString("brand", brand);
            editor.putString("alias", alias);
            editor.apply();
            viewG.get(1).checkbox.setChecked(true);
        }else{
            String targa = (String) items.get(0).getTarga();
            String model = (String) items.get(0).getModel();
            String brand = (String) items.get(0).getBrand();
            String alias = (String) items.get(0).getAlias();
            editor.putString("targa", targa);
            editor.putString("model", model);
            editor.putString("brand", brand);
            editor.putString("alias", alias);
            editor.apply();
            viewG.get(0).checkbox.setChecked(true);
        }


    }

    public  ArrayList<MyViewHolderWithChild> getView(){
        return viewG;
    }
    private ObjectAnimator changeRotate(RelativeLayout button, float to, float from){
        ObjectAnimator animator = ObjectAnimator.ofFloat(button,"rotation",from,to);
        animator.setDuration(300);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}

