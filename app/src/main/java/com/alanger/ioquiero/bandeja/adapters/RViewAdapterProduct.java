package com.alanger.ioquiero.bandeja.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alanger.ioquiero.R;
import com.alanger.ioquiero.models.Product;

import java.util.List;


public class RViewAdapterProduct
        extends RecyclerView.Adapter<RViewAdapterProduct.ViewHolder>
        implements View.OnClickListener{

    private View.OnClickListener onClickListener;

    private static Context ctx;
    private static List<Product> productList;

    public RViewAdapterProduct( List<Product> productList) {
        this.productList = productList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_detail_activity_item,null,false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }


    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Product productTemp = productList.get(position);
        holder.tViewCantidad.setText(""+productTemp.getCant());
        holder.tViewName.setText(""+productTemp.getName());

    }


    @Override
    public int getItemCount() {
        return productList.size();
    }



    @Override
    public void onClick(View v) {
        if(onClickListener!=null){
            onClickListener.onClick(v);
        }
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tViewCantidad;
        TextView tViewName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tViewCantidad = itemView.findViewById(R.id.odai_cant);
            tViewName = itemView.findViewById(R.id.odai_name);
        }
    }
}
