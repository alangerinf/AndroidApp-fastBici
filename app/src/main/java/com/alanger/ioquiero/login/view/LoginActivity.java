package com.alanger.ioquiero.login.view;

import android.app.Activity;
import android.content.Context;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alanger.ioquiero.R;
import com.alanger.ioquiero.login.presenter.LoginPresenter;
import com.alanger.ioquiero.login.presenter.LoginPresenterImpl;
import com.alanger.ioquiero.register.view.RegisteActivityA;
import com.alanger.ioquiero.views.ActivityMain;
import com.alanger.ioquiero.views.ActivityRecuperar;

import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends Activity implements LoginView{


    private LoginPresenter presenter;

    //contexto
    private Context ctx;
    private ProgressBar progressBar;

    //boton enter
    private Button btnLogin, btnCreateUser;
    private TextView tViewRecoverPassword;

    //campos de acceso
    private EditText eTextUser, eTextPassword;
    //animacion  de boton
    private Animation animBtn;
    //boton de ver password
    private ImageView iViewPasswordSetVisible;

    private Animation animLayout;
    private ConstraintLayout constCombo;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ctx =  LoginActivity.this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        declaration();
        defineEvents();
        enterAnimation();

    }

    void declaration(){


        presenter = new LoginPresenterImpl(this);
        btnLogin    = findViewById(R.id.login_btnLogin);
        btnCreateUser   = findViewById(R.id.login_btnCreateUser);
        tViewRecoverPassword    = findViewById(R.id.login_tViewRecoverPassword);
        eTextUser   = findViewById(R.id.login_eTextEmail);
        eTextPassword   = findViewById(R.id.login_eTextPassword);
        iViewPasswordSetVisible = findViewById(R.id.login_iViewPassword);
        animBtn     = android.view.animation.AnimationUtils.loadAnimation(getBaseContext(),R.anim.press_btn);
        animLayout  = android.view.animation.AnimationUtils.loadAnimation(getBaseContext(),R.anim.left_in);
        constCombo  = findViewById(R.id.login_constCombo);
        progressBar = findViewById(R.id.login_progressBar);


        defaultAttributes();
    }

    void defaultAttributes(){
        handler.post(
                () -> {
                    constCombo.setVisibility(View.INVISIBLE);
                    hideProgressBar();
                }
        );

    }

    void defineEvents(){
        btnLogin.setOnClickListener(v -> {
            v.startAnimation(animBtn);
            handler.postDelayed(
                    () -> {
                        presenter.signIn(
                                eTextUser.getText().toString(),
                                eTextPassword.getText().toString()
                                );

                    },200
            );
        });
        btnCreateUser.setOnClickListener(v->{
            v.startAnimation(animBtn);
            goRegister();
        });

        iViewPasswordSetVisible.setOnClickListener(v -> {
            v.startAnimation(animBtn);
            if(eTextPassword.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                eTextPassword.setInputType( InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }else {
                eTextPassword.setInputType( InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD );
            }
            eTextPassword.setSelection(eTextPassword.getText().length());
        });

        tViewRecoverPassword.setOnClickListener(v -> {
            handler.post(() -> v.startAnimation(animBtn));
            goRecoverPassword();

        });

    }

    private void enterAnimation() {

        handler.postDelayed(
                () -> {
                    constCombo.startAnimation(animLayout);
                    constCombo.setVisibility(View.VISIBLE);
                    enableInputs();
                },500
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        btnLogin.setClickable(true);
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
    public void goHome() {
        Intent intent = new Intent(getBaseContext(), ActivityMain.class);
        startActivity(intent);
    }

    @Override
    public void goRegister() {
        Intent intent = new Intent(getBaseContext(), RegisteActivityA.class);
        startActivity(intent);
    }

    @Override
    public void enableInputs() {
        handler.post(
                () -> {
                    btnLogin.setEnabled(true);
                    eTextUser.setEnabled(true);
                    eTextPassword.setEnabled(true);
                    eTextPassword.setEnabled(true);
                    eTextPassword.setEnabled(true);
                    iViewPasswordSetVisible.setEnabled(true);
                    constCombo.setEnabled(true);
                }
        );

    }

    @Override
    public void disableInputs() {
        handler.post(
                () -> {
                    btnLogin.setEnabled(false);
                    eTextUser.setEnabled(false);
                    eTextPassword.setEnabled(false);
                    eTextPassword.setEnabled(false);
                    eTextPassword.setEnabled(false);
                    iViewPasswordSetVisible.setEnabled(false);
                    constCombo.setEnabled(false);
                }
        );

    }

    @Override
    public void hideProgressBar() {
        handler.post(
                () -> {
                    progressBar.setVisibility(View.INVISIBLE);
                }
        );

    }

    @Override
    public void showProgressBar() {
        handler.post(
                () -> {
                    progressBar.setVisibility(View.VISIBLE);
                }
        );
    }

    @Override
    public void loginError(String error) {
        handler.post(
                () -> {
                    Toast.makeText(ctx,error,Toast.LENGTH_LONG).show();
                }
        );

    }
    public class isogram {
        public boolean  isIsogram(String str) {
            List<String> tempList = new ArrayList<>();
            for(int i=0;i<str.length();i++){
                String letraComparar = String.valueOf(str.charAt(i));
                for(String s:tempList){
                    if((s.toLowerCase()).equals(letraComparar.toLowerCase())){
                            return false;
                    }
                }
                tempList.add(letraComparar);
            }
            return true;
        }
    }

}
