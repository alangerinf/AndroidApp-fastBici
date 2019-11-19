package com.alanger.ioquiero.getTariff.view.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.alanger.ioquiero.R;
import com.alanger.ioquiero.getTariff.models.Enterprise;

import java.util.ArrayList;
import java.util.List;

public class DialogSelectEnterprise {

    private List<Enterprise> enterpriseList= null;

    private Context ctx;
    private UriOrderHelper uriOrderHelper;



    public DialogSelectEnterprise(Context ctx, UriOrderHelper uriOrderHelper) {
        this.enterpriseList= new ArrayList<>();
        this.ctx = ctx;
        this.uriOrderHelper = uriOrderHelper;

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


    private void showDialog(){
        Dialog dialog = new Dialog(ctx);
        dialog.setContentView(R.layout.activity_pedido_detail_dialog_select_enterprise);
        RecyclerView rView = dialog.findViewById(R.id.apddse_rView);
        RViewAdapterEnterprise adapter = new RViewAdapterEnterprise(ctx,enterpriseList);
        adapter.setOnClicListener(v->{
            int pos = rView.getChildAdapterPosition(v);
            Enterprise temp = enterpriseList.get(pos);
            Uri  x  =uriOrderHelper.makeUriByPhone(temp.getPhone());
            UriOrderHelper.buildWhatsAppRequest(ctx,x);

        });
        rView.setAdapter(adapter);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();


    }









}
