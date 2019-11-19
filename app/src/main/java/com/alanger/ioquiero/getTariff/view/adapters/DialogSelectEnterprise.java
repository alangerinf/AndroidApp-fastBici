package com.alanger.ioquiero.getTariff.view.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Config;
import android.util.Log;
import android.widget.Adapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.alanger.ioquiero.Configurations;
import com.alanger.ioquiero.R;
import com.alanger.ioquiero.getTariff.models.Enterprise;

import java.util.ArrayList;
import java.util.List;

public class DialogSelectEnterprise {

    private List<Enterprise> enterpriseList= null;

    private Context ctx;
    private Uri uriMessage;



    public DialogSelectEnterprise(Context ctx, Uri uriMessage) {
        this.enterpriseList= new ArrayList<>();
        this.ctx = ctx;
        this.uriMessage = uriMessage;

        Enterprise enterprise1 = new Enterprise();
        enterprise1.setName("FastBici");
        enterprise1.setPhone("+51973446468");
        enterprise1.setLogo(ctx.getDrawable(R.drawable.ic_logo_fast));


        Enterprise enterprise2 = new Enterprise();
        enterprise2.setName("MuskBike");
        enterprise2.setPhone("+51977382412");
        enterprise2.setLogo(ctx.getDrawable(R.drawable.musklogo));


        if( getRandomIntegerBetweenRange(0,10)%2==0){
            enterpriseList.add(enterprise1);
            enterpriseList.add(enterprise2);
        }else {

            enterpriseList.add(enterprise2);
            enterpriseList.add(enterprise1);
        }

        showDialog();
    }


    public static int getRandomIntegerBetweenRange(double min, double max){
        int x = (int) ((int)( Math.random()*((max-min)+1) )+min);
        Log.d("numRand",""+x);
        return x;
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

    private void showDialog(){
        Dialog dialog = new Dialog(ctx);
        dialog.setContentView(R.layout.activity_pedido_detail_dialog_select_enterprise);
        RecyclerView rView = dialog.findViewById(R.id.apddse_rView);
        RViewAdapterEnterprise adapter = new RViewAdapterEnterprise(ctx,enterpriseList,uriMessage);
        adapter.setOnClicListener(v->{
            int pos = rView.getChildAdapterPosition(v);
            Enterprise temp = enterpriseList.get(pos);
           // Toast.makeText(ctx,"llamando a "+temp.getName(),Toast.LENGTH_LONG).show();
            String strURI = uriMessage.toString();
             Uri uri = Uri.parse(strURI.replaceAll("xxxxxxxxxx",temp.getPhone()));
            buildWhatsAppRequest(uri);
        });
        rView.setAdapter(adapter);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();


    }









}
