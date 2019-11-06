package com.alanger.ioquiero.getTariff.view.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alanger.ioquiero.R;
import com.alanger.ioquiero.getTariff.models.Place;

import java.util.List;


public class RViewAdapterPlace
        extends RecyclerView.Adapter<RViewAdapterPlace.ViewHolder>
        implements View.OnClickListener{


    private static String TAG = RViewAdapterPlace.class.getSimpleName();

    private View.OnClickListener onClickListener;

    private static Context ctx;
    private static List<Place> placeList;

    public RViewAdapterPlace(List<Place> placeList) {
        this.placeList = placeList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_main_place_item,null,false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }


    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Place placeTemp = placeList.get(position);

        try{
            String lugar[] = placeTemp.getFormatted_address().split(",");
            holder.fmpi_tViewCalle.setText(""+lugar[0]);
            holder.fmpi_tViewReference.setText(""+lugar[1]);

        }catch (Exception e){
            Log.d(TAG,"Exception:"+e.toString());
            holder.fmpi_tViewCalle.setText(placeTemp.getFormatted_address());
            holder.fmpi_tViewReference.setText("");
        }

    }


    @Override
    public int getItemCount() {
        return placeList.size();
    }



    @Override
    public void onClick(View v) {
        if(onClickListener!=null){
            onClickListener.onClick(v);
        }
    }

    public void setOnClicListener(View.OnClickListener listener){
        this.onClickListener=listener;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView fmpi_tViewCalle;
        TextView fmpi_tViewReference;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fmpi_tViewCalle = itemView.findViewById(R.id.fmpi_tViewCalle);
            fmpi_tViewReference = itemView.findViewById(R.id.fmpi_tViewReference);
        }
    }
}
