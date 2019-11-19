package com.alanger.ioquiero.getTariff.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alanger.ioquiero.R;
import com.alanger.ioquiero.getTariff.models.Enterprise;

import java.util.List;


public class RViewAdapterEnterprise
        extends RecyclerView.Adapter<RViewAdapterEnterprise.ViewHolder>
        implements View.OnClickListener{

    List<Enterprise> enterpriseList;
    Context ctx;

    private View.OnClickListener onClickListener;

    public RViewAdapterEnterprise(Context ctx, List<Enterprise> enterpriseList) {
        this.enterpriseList = enterpriseList;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_pedido_detail_dialog_select_enterprise_item,null,false);

        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Enterprise item = enterpriseList.get(position);
        holder.apddsei_tViewEntepriseName.setText(item.getName());
        holder.apddsei_iViewEnterprise.setImageDrawable(item.getLogo());




    }


    public void setOnClicListener(View.OnClickListener listener){
        this.onClickListener=listener;
    }

    @Override
    public void onClick(View v) {
        if(onClickListener!=null){
            onClickListener.onClick(v);
        }
    }

    @Override
    public int getItemCount() {
        return enterpriseList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView apddsei_iViewEnterprise;
        TextView apddsei_tViewEntepriseName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            apddsei_iViewEnterprise = itemView.findViewById(R.id.apddsei_iViewEnterprise);
            apddsei_tViewEntepriseName = itemView.findViewById(R.id.apddsei_tViewEntepriseName);
/*
            itemView.setOnClickListener(v->{
                buildWhatsAppRequest();
            });

 */
        }
    }

    private void buildWhatsAppRequest(Uri bodyMsg) {
        /*
        Intent whatsappIntent =  new Intent("android.intent.action.MAIN");
        whatsappIntent.setAction(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT,  bodyMsg + uriGoogleMaps);
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        */
        Intent whatsappIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(bodyMsg.toString()));

        ctx.startActivity(whatsappIntent);

    }
}
