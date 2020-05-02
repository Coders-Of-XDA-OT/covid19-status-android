package com.vipul.covidstatus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StatewiseAdapter extends RecyclerView.Adapter<StatewiseAdapter.ViewHolder> {

    LayoutInflater layoutInflater;
    List<StatewiseModel>statewiseModel;

    public StatewiseAdapter(Context context, List<StatewiseModel>statewiseModel){
        this.layoutInflater = LayoutInflater.from(context);
        this.statewiseModel = statewiseModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.statewise_list_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.stateName.setText(statewiseModel.get(position).getState());
        holder.stateTotalCases.setText(statewiseModel.get(position).getConfirmed());

    }

    @Override
    public int getItemCount() {
        return statewiseModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView stateName, stateTotalCases;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            stateName = itemView.findViewById(R.id.state_name_textview);
            stateTotalCases = itemView.findViewById(R.id.state_confirmed_textview);
        }
    }

}
