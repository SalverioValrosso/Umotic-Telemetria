package org.altervista.umotic.AdapterEvents;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.aakira.expandablelayout.ExpandableLayout;
import com.github.aakira.expandablelayout.ExpandableLayoutListener;
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.Utils;

import org.altervista.umotic.AdapterRecyclerView.ItemClickListener;
import org.altervista.umotic.umotic.Item;
import org.altervista.umotic.umotic.R;

import java.util.List;



/**
 * Created by admin on 21/01/2019.
 */

class MyViewHolderWithChild extends RecyclerView.ViewHolder implements  View.OnClickListener{

    public TextView textView,textViewChild;
    public RelativeLayout button;
    public ExpandableLinearLayout expandableLayout;

    ItemClickListener itemClickListener;

    public MyViewHolderWithChild(View itemView) {
        super(itemView);

        textView = (TextView) itemView.findViewById(R.id.textView);
        textViewChild = (TextView) itemView.findViewById(R.id.textViewChild);
        button = (RelativeLayout) itemView.findViewById(R.id.button);
        expandableLayout = (ExpandableLinearLayout) itemView.findViewById(R.id.expandableLayout);

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

    List<Item> items;
    Context context;
    SparseBooleanArray expandState = new SparseBooleanArray();

    public MyAdapter(List<Item> items) {
        this.items = items;
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
        View view = inflater.inflate(R.layout.layout_with_child_event,parent,false);
        return new MyViewHolderWithChild(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        switch (holder.getItemViewType()){
            case 1:
            {
                final MyViewHolderWithChild viewHolder = (MyViewHolderWithChild)holder;
                final Item item = items.get(position);
                viewHolder.setIsRecyclable(false);
                viewHolder.textView.setText(item.getText());

                viewHolder.expandableLayout.setInRecyclerView(true);
                viewHolder.expandableLayout.setExpanded(expandState.get(position));
                viewHolder.expandableLayout.setListener(new ExpandableLayoutListenerAdapter() {

                    @Override
                    public void onPreOpen() {
                        changeRotate(viewHolder.button,0f,180f).start();
                        expandState.put(position,true);
                    }

                    @Override
                    public void onPreClose() {
                        changeRotate(viewHolder.button,180f,0f).start();
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
                        Toast.makeText(context, "With child click : "+items.get(position).getText(),Toast.LENGTH_LONG).show();
                    }

                });

            }
            break;
            default:
                break;
        }
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

