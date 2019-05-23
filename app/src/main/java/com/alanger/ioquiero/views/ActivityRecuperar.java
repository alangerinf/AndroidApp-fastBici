package com.alanger.ioquiero.views;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import com.alanger.ioquiero.ApiPrueba;
import com.alanger.ioquiero.R;


public class ActivityRecuperar extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


    }


    public void pruebaApi(View view){
        ApiPrueba apiPrueba = new ApiPrueba(getBaseContext());
apiPrueba.download();



    }



}
