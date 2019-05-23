package com.alanger.ioquiero.views;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.text.InputType;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.alanger.ioquiero.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class ActivityLogin extends Activity {

    static private Button btnEnter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final Animation animBtn =
                android.view.animation.AnimationUtils.loadAnimation(getBaseContext(),R.anim.press_btn);

        final EditText eTextUser = findViewById(R.id.eTextUsuario);
        final EditText eTextPassword = findViewById(R.id.eTextPassword);
        btnEnter = (Button) findViewById(R.id.btnEnter);

        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animBtn);
                Handler handler = new Handler();
                handler.postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                btnEnter.setClickable(false);
                                Intent intent = new Intent(getBaseContext(), ActivityMain.class);
                                startActivity(intent);

                            }
                        },200
                );
            }
        });


        ImageView iViewPasswordSetVisible = findViewById(R.id.iViewPassword);
        iViewPasswordSetVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animBtn);
                if(eTextPassword.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    eTextPassword.setInputType( InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }else {
                    eTextPassword.setInputType( InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD );
                }
                eTextPassword.setSelection(eTextPassword.getText().length());
            }
        });


        final ConstraintLayout cl = (ConstraintLayout) findViewById(R.id.constCombo);
        cl.setVisibility(View.INVISIBLE);

        final Animation animLayout =
                android.view.animation.AnimationUtils.loadAnimation(getBaseContext(),R.anim.left_in);

        Handler handler = new Handler();
        handler.postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        cl.startAnimation(animLayout);
                        cl.setVisibility(View.VISIBLE);
                    }
                },200
        );

    }




    @Override
    public void onResume() {
        super.onResume();
        btnEnter.setClickable(true);
    }
    @Override
    public void onBackPressed() {

        //super.onBackPressed();
        moveTaskToBack(true);

    }

    public void openRecuperar(View view){
        Intent i = new Intent(this,ActivityRecuperar.class);
        startActivity(i);

    }



}
