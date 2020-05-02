package com.vipul.covidstatus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StatewiseAdapter extends RecyclerView.Adapter<StatewiseAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<StatewiseModel> arrayList;
    private OnItemClickListner mListner;

    public  interface OnItemClickListner{
        void onItemClick(int position);
    }

    public void setOnItemClickListner(OnItemClickListner listner){
        mListner = listner;
    }

    public StatewiseAdapter(Context context, ArrayList<StatewiseModel>statewiseModelArrayList){
        mContext = context;
        arrayList = statewiseModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.statewise_list_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StatewiseModel currentItem = arrayList.get(position);
        String stateName = currentItem.getState();
        String stateTotal = currentItem.getConfirmed();
        holder.stateTotalCases.setText(stateTotal);
        holder.stateName.setText(stateName);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView stateName, stateTotalCases;

        public ViewHolder(View itemView) {
            super(itemView);

            stateName = itemView.findViewById(R.id.state_name_textview);
            stateTotalCases = itemView.findViewById(R.id.state_confirmed_textview);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListner != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListner.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

}
