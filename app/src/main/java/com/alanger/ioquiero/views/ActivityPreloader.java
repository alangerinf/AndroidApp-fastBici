package com.alanger.ioquiero.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.alanger.ioquiero.R;
import com.alanger.ioquiero.WakeUpHerokuQuery;
import com.alanger.ioquiero.login.view.LoginActivity;
import com.alanger.ioquiero.models.SharedPreferencesManager;
import com.alanger.ioquiero.models.User;
import com.alanger.ioquiero.volskayaGraphql.GraphqlClient;
import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.exception.ApolloException;
import com.google.android.material.snackbar.Snackbar;

import javax.annotation.Nonnull;


public class ActivityPreloader extends Activity {

    static LottieAnimationView lAVbackground;
    static ImageView iViewLogoEmpresa;
    static TextView tViewlogo_p1;
    static TextView tViewlogo_p2;
    static Context ctx;

    static String TAG = ActivityPreloader.class.getSimpleName();

    Handler h = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preloader);
        declare();
        conectandoServer();
        startAnimations();
    }

    private void declare() {
        ctx = this;
        lAVbackground = (LottieAnimationView) findViewById(R.id.lottie);

        lAVbackground.enableMergePathsForKitKatAndAbove(true);
        iViewLogoEmpresa = findViewById(R.id.iViewLogoEmpresa);
        tViewlogo_p1 = findViewById(R.id.tViewlogo_p1);
        tViewlogo_p2 = findViewById(R.id.tViewlogo_p2);
    }


    void conectandoServer(){

        Log.d(TAG,"conectandoServer");

        GraphqlClient.getMyApolloClient()
                .query(
                        WakeUpHerokuQuery
                                .builder()
                                .build()
                )
                .enqueue(new ApolloCall.Callback<WakeUpHerokuQuery.Data>() {

                    @Override
                    public void onResponse(@Nonnull com.apollographql.apollo.api.Response<WakeUpHerokuQuery.Data> response) {

                        WakeUpHerokuQuery.Data data = response.data();
                        String resp =  data.wakeUpHeroku();
                        Log.d(TAG,resp);

                        h.post(()->{
                            Log.d(TAG,"Comprobación Exitosa del Servidor");

                        });



                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {

                        h.post(() ->{

                                    Log.d(TAG,"onFailure:"+e.toString());
                        }

                        );

                    }
                });
    }

    void startAnimations(){

        int timeWait=3000;

        final Animation animLayout =
                android.view.animation.AnimationUtils.loadAnimation(getBaseContext(), R.anim.fade_in);
/*
        Handler handler = new Handler();
        handler.postDelayed(
                () -> {
                    lAVbackground.setVisibility(View.VISIBLE);
                    lAVbackground.startAnimation(animLayout);
                }
        ,timeWait);
*/
        final Animation anim_rightFadeIn1 =
                android.view.animation.AnimationUtils.loadAnimation(getBaseContext(), R.anim.right_fade_in);

        Handler handler1 = new Handler();
        handler1.postDelayed(
                () -> {
                    iViewLogoEmpresa.startAnimation(anim_rightFadeIn1);
                    iViewLogoEmpresa.setVisibility(View.VISIBLE);
                }
                ,timeWait);
        final Animation anim_rightFadeIn2 =
                android.view.animation.AnimationUtils.loadAnimation(getBaseContext(), R.anim.top_fade_in);
        Handler handler2 = new Handler();
        handler2.postDelayed(
                () -> {
                    tViewlogo_p1.startAnimation(anim_rightFadeIn2);
                    tViewlogo_p1.setVisibility(View.VISIBLE);
                },timeWait+500
        );
        final Animation anim_rightFadeIn3 =
                android.view.animation.AnimationUtils.loadAnimation(getBaseContext(), R.anim.top_fade_in);
        Handler handler3 = new Handler();
        handler3.postDelayed(
                () -> {
                    tViewlogo_p2.startAnimation(anim_rightFadeIn3);
                    tViewlogo_p2.setVisibility(View.VISIBLE);
                },timeWait+600
        );

        Handler handler4 = new Handler();
        handler4.postDelayed(()->
            {
           //     User user = SharedPreferencesManager.getUser(ctx);
            //    if(user!=null){
                    startActivity(new Intent(this, ActivityMain.class));
            //    }else {
            //        startActivity(new Intent(this, LoginActivity.class));
            //    }
            },timeWait+2000);
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    // Shows the system bars by removing all the flags
// except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}
