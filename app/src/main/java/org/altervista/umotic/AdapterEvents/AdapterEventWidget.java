package org.altervista.umotic.AdapterEvents;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.altervista.umotic.umotic.R;

import java.util.ArrayList;


public class AdapterEventWidget extends RecyclerView.Adapter<AdapterEventWidget.ExampleViewHolder> implements View.OnClickListener, View.OnLongClickListener{
    public ArrayList<EventItem> mExampleList;

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onLongClick(View v) {

        return true;
    }

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextViewLine1;
        public TextView mTextViewLine2;
        public TextView mTextViewLine3;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            mTextViewLine1 = itemView.findViewById(R.id.textview_line1);
            mTextViewLine2 = itemView.findViewById(R.id.textview_line_2);
            mTextViewLine3 = itemView.findViewById(R.id.textview_line_3);
        }
    }

    public AdapterEventWidget(ArrayList<EventItem> exampleList) {
        mExampleList = exampleList;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item_widget, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        EventItem currentItem = mExampleList.get(position);

        holder.mTextViewLine1.setText(currentItem.getTitle());
        holder.mTextViewLine2.setText(currentItem.getData());
        holder.mTextViewLine3.setText(currentItem.getTime());
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


}