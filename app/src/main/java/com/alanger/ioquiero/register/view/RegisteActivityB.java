package com.alanger.ioquiero.register.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.alanger.ioquiero.R;
import com.alanger.ioquiero.register.presenter.RegisterPresenterB;
import com.alanger.ioquiero.register.presenter.RegisterPresenterImplA;
import com.alanger.ioquiero.register.presenter.RegisterPresenterImplB;
import com.alanger.ioquiero.views.ActivityMain;
import com.alanger.ioquiero.views.ActivityRecuperar;


public class RegisteActivityB extends Activity implements RegisterViewB {


    private String email;
    private String password;

    private RegisterPresenterB presenter;

    //contexto
    private Context ctx;
    private ProgressBar createB_progressBar;

    //boton enter
    private Button createB_btnCreateUser;
    private TextView createB_tViewRecoverPassword;

    //campos de acceso
    private EditText createB_eTextPhone;
    //animacion  de boton
    private Animation animBtn;

    private Animation animLayout;
    private ConstraintLayout createB_constCombo;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ctx =  RegisteActivityB.this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createaccount_b);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        declaration();
        defineEvents();
        enterAnimation();

    }

    void declaration(){

        Bundle b = getIntent().getExtras();


        email = b.getString("email");
        password = b.getString("password");

        presenter = new RegisterPresenterImplB(this);
        createB_btnCreateUser = findViewById(R.id.createB_btnCreateUser);
        createB_eTextPhone  = findViewById(R.id.createB_eTextPhone);
        createB_tViewRecoverPassword    = findViewById(R.id.createB_tViewRecoverPassword);

        animBtn     = android.view.animation.AnimationUtils.loadAnimation(getBaseContext(),R.anim.press_btn);
        animLayout  = android.view.animation.AnimationUtils.loadAnimation(getBaseContext(),R.anim.left_in);
        createB_constCombo  = findViewById(R.id.createB_constCombo);
        createB_progressBar = findViewById(R.id.createB_progressBar);

        defaultAttributes();
    }

    void defaultAttributes(){
        handler.post(
                () -> {
                    createB_constCombo.setVisibility(View.INVISIBLE);
                    hideProgressBar();
                }
        );

    }

    void defineEvents(){
        createB_btnCreateUser.setOnClickListener(v -> {
            v.startAnimation(animBtn);
            handler.postDelayed(
                    () -> {
                        presenter.intentRegister(
                                email,
                                password,
                                createB_eTextPhone.getText().toString()
                                );

                    },200
            );
        });



        createB_tViewRecoverPassword.setOnClickListener(v -> {
            handler.post(() -> v.startAnimation(animBtn));
            goRecoverPassword();

        });

    }


    private void enterAnimation() {

        handler.postDelayed(
                () -> {
                    createB_constCombo.startAnimation(animLayout);
                    createB_constCombo.setVisibility(View.VISIBLE);
                    enableInputs();
                },500
        );
    }


    @Override
    public void onResume() {
        super.onResume();
        createB_btnCreateUser.setClickable(true);
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        moveTaskToBack(true);
    }

    @Override
    public void goRecoverPassword() {
        Intent i = new Intent(this, ActivityRecuperar.class);
        startActivity(i);
    }

    @Override
    public void goVerifyPhone(String id) {
        handler.post(
                ()->{
                    Toast.makeText(ctx,"Welcome "+id,Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getBaseContext(), VerifyTokenActivity.class);
                    intent.putExtra("id",id);
                    startActivity(intent);
                }
        );

    }

    @Override
    public void enableInputs() {
        handler.post(
                () -> {
                    createB_eTextPhone.setEnabled(true);
                    createB_btnCreateUser.setEnabled(true);
                    createB_constCombo.setEnabled(true);
                }
        );

    }

    @Override
    public void disableInputs() {
        handler.post(
                () -> {
                    createB_eTextPhone.setEnabled(false);
                    createB_btnCreateUser.setEnabled(false);
                    createB_constCombo.setEnabled(false);
                }
        );

    }

    @Override
    public void hideProgressBar() {
        handler.post(
                () -> {
                    createB_progressBar.setVisibility(View.INVISIBLE);
                }
        );

    }

    @Override
    public void showProgressBar() {
        handler.post(
                () -> {
                    createB_progressBar.setVisibility(View.VISIBLE);
                }
        );
    }

    @Override
    public void registerError(String error) {
        handler.post(
                () -> {
                    Toast.makeText(ctx,error,Toast.LENGTH_LONG).show();
                    hideProgressBar();
                    enableInputs();
                }
        );

    }

}
