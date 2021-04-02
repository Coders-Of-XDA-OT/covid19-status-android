package com.vipul.covidstatus.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vipul.covidstatus.data.PerDistrictData;
import com.vipul.covidstatus.R;
import com.vipul.covidstatus.models.DistrictModel;

import java.text.NumberFormat;
import java.util.ArrayList;

public class DistrictAdapter extends RecyclerView.Adapter<DistrictAdapter.ViewHolder>{

    private Context mContext;
    private ArrayList<DistrictModel> arrayList;
    private DistrictAdapter.OnItemClickListner mListner;
    private static final String DISTRICT_NAME = "districtName";
    private static final String DISTRICT_CONFIRMED = "districtConfirmed";
    private static final String DISTRICT_ACTIVE = "districtActive";
    private static final String DISTRICT_DECEASED = "districtDeaceased";
    private static final String DISTRICT_NEW_CONFIRMED = "districtNewConfirmed";
    private static final String DISTRICT_NEW_RECOVERED = "districtNewRecovered";
    private static final String DISTRICT_NEW_DECEASED = "districtNewDeceased";
    private static final String DISTRICT_RECOVERED = "districtRecovered";

    public interface OnItemClickListner {
        void onItemClick(int position);
    }

    public void setOnItemClickListner(DistrictAdapter.OnItemClickListner listner) {
        mListner = listner;
    }

    public DistrictAdapter(Context context, ArrayList<DistrictModel> districtModelArrayList) {
        mContext = context;
        arrayList = districtModelArrayList;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.districtwise_list_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DistrictModel clickedItem = arrayList.get(position);
                Intent perDistrictIntent = new Intent(mContext, PerDistrictData.class);

                perDistrictIntent.putExtra(DISTRICT_NAME, clickedItem.getDistrict());
                perDistrictIntent.putExtra(DISTRICT_CONFIRMED, clickedItem.getConfirmed());
                perDistrictIntent.putExtra(DISTRICT_ACTIVE, clickedItem.getActive());
                perDistrictIntent.putExtra(DISTRICT_DECEASED, clickedItem.getDeceased());
                perDistrictIntent.putExtra(DISTRICT_NEW_CONFIRMED, clickedItem.getNewConfirmed());
                perDistrictIntent.putExtra(DISTRICT_NEW_RECOVERED, clickedItem.getNewRecovered());
                perDistrictIntent.putExtra(DISTRICT_NEW_DECEASED, clickedItem.getNewDeceased());;
                perDistrictIntent.putExtra(DISTRICT_RECOVERED, clickedItem.getRecovered());


                mContext.startActivity(perDistrictIntent);
            }
        });


        DistrictModel currentItem = arrayList.get(position);
        String districtName = currentItem.getDistrict();
        String districtTotal = currentItem.getConfirmed();
        int districtTotalInt = Integer.parseInt(districtTotal);
        holder.districtTotalCases.setText(NumberFormat.getInstance().format(districtTotalInt));
        holder.districtName.setText(districtName);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void filterList(ArrayList<DistrictModel> filteredList) {
        arrayList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView districtName, districtTotalCases;

        public ViewHolder(View itemView) {
            super(itemView);
            districtName = itemView.findViewById(R.id.district_name_textview);
            districtTotalCases = itemView.findViewById(R.id.district_confirmed_textview);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListner != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListner.onItemClick(position);
                        }
                    }
                }
            });

        }
    }
}
