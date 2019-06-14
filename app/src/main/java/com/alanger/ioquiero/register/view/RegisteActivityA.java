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
import com.alanger.ioquiero.register.presenter.RegisterPresenterA;
import com.alanger.ioquiero.register.presenter.RegisterPresenterImplA;
import com.alanger.ioquiero.views.ActivityRecuperar;


public class RegisteActivityA extends Activity implements RegisterViewA {


    private RegisterPresenterA presenter;

    //contexto
    private Context ctx;
    private ProgressBar createA_progressBar;

    //boton enter
    private Button createA_btnNext;
    private TextView createA_tViewRecoverPassword;

    //campos de acceso
    private EditText createA_eTextEmail, createA_eTextPassword1,createA_eTextPassword2 ;
    //animacion  de boton
    private Animation animBtn;
    //boton de ver password
    private ImageView createA_iViewPassword1,createA_iViewPassword2;

    private Animation animLayout;
    private ConstraintLayout createA_constCombo;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ctx =  RegisteActivityA.this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createaccount_a);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        declaration();
        defineEvents();
        enterAnimation();

    }

    void declaration(){
        presenter = new RegisterPresenterImplA(this);
        createA_eTextEmail      = findViewById(R.id.createA_eTextEmail);
        createA_eTextPassword1      = findViewById(R.id.createA_eTextPassword1);
        createA_eTextPassword2  = findViewById(R.id.createA_eTextPassword2);

        createA_iViewPassword1 = findViewById(R.id.createA_iViewPassword1);
        createA_iViewPassword2 = findViewById(R.id.createA_iViewPassword2);
        createA_constCombo  = findViewById(R.id.createA_constCombo);

        animBtn     = android.view.animation.AnimationUtils.loadAnimation(getBaseContext(),R.anim.press_btn);
        animLayout  = android.view.animation.AnimationUtils.loadAnimation(getBaseContext(),R.anim.left_in);

        createA_progressBar = findViewById(R.id.createA_progressBar);
        createA_btnNext = findViewById(R.id.createA_btnNext);
        createA_tViewRecoverPassword = findViewById(R.id.createA_tViewRecoverPassword);
        defaultAttributes();
    }

    void defaultAttributes(){
        handler.post(
                () -> {
                    createA_constCombo.setVisibility(View.INVISIBLE);
                    hideProgressBar();
                }
        );

    }

    void defineEvents(){
        createA_btnNext.setOnClickListener(v -> {
            v.startAnimation(animBtn);
            handler.postDelayed(
                    () -> {
                        presenter.verifyData(
                                createA_eTextEmail.getText().toString(),
                                createA_eTextPassword1.getText().toString(),
                                createA_eTextPassword2.getText().toString()
                                );

                    },200
            );
        });

        createA_iViewPassword1.setOnClickListener(v -> {
            v.startAnimation(animBtn);
            if(createA_eTextPassword1.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                createA_eTextPassword1.setInputType( InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }else {
                createA_eTextPassword1.setInputType( InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD );
            }
            createA_eTextPassword1.setSelection(createA_eTextPassword1.getText().length());
        });

        createA_iViewPassword2.setOnClickListener(v -> {
            v.startAnimation(animBtn);
            if(createA_eTextPassword2.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                createA_eTextPassword2.setInputType( InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }else {
                createA_eTextPassword2.setInputType( InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD );
            }
            createA_eTextPassword2.setSelection(createA_eTextPassword2.getText().length());
        });

        createA_tViewRecoverPassword.setOnClickListener(v -> {
            handler.post(() -> v.startAnimation(animBtn));
            goRecoverPassword();

        });

    }


    private void enterAnimation() {

        handler.postDelayed(
                () -> {
                    createA_constCombo.startAnimation(animLayout);
                    createA_constCombo.setVisibility(View.VISIBLE);
                    enableInputs();
                },500
        );
    }


    @Override
    public void onResume() {
        super.onResume();
        createA_btnNext.setClickable(true);
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
    public void goRegisterB() {
        Intent i = new Intent(this, RegisteActivityB.class);
        i.putExtra("email",createA_eTextEmail.getText().toString());
                i.putExtra("password",createA_eTextPassword1.getText().toString());
        startActivity(i);
    }

    @Override
    public void enableInputs() {
        handler.post(
                () -> {
                    createA_btnNext.setEnabled(true);
                    createA_eTextEmail.setEnabled(true);
                    createA_eTextPassword1.setEnabled(true);
                    createA_eTextPassword2.setEnabled(true);
                    createA_iViewPassword1.setEnabled(true);
                    createA_iViewPassword2.setEnabled(true);
                    createA_constCombo.setEnabled(true);
                }
        );

    }

    @Override
    public void disableInputs() {
        handler.post(
                () -> {
                    createA_btnNext.setEnabled(false);
                    createA_eTextEmail.setEnabled(false);
                    createA_eTextPassword1.setEnabled(false);
                    createA_eTextPassword2.setEnabled(false);
                    createA_iViewPassword1.setEnabled(false);
                    createA_iViewPassword2.setEnabled(false);
                    createA_constCombo.setEnabled(false);
                }
        );

    }

    @Override
    public void hideProgressBar() {
        handler.post(
                () -> {
                    createA_progressBar.setVisibility(View.INVISIBLE);
                }
        );

    }

    @Override
    public void showProgressBar() {
        handler.post(
                () -> {
                    createA_progressBar.setVisibility(View.VISIBLE);
                }
        );
    }

    @Override
    public void loginError(String error) {
        handler.post(
                () -> {
                    Toast.makeText(ctx,error,Toast.LENGTH_LONG).show();
                    hideProgressBar();
                    enableInputs();
                }
        );

    }

}
