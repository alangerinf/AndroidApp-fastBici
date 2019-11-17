package com.alanger.ioquiero.register.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.alanger.ioquiero.R;
import com.alanger.ioquiero.views.ActivityMain;

public class VerifyTokenActivity extends Activity implements VerifyTokenView{


    AppCompatButton btnCheckCode;
    ProgressBar progressBar;
    TextView ck_tViewSendCode;
    EditText ck_eTextCode;

    String id;

    Handler handler = new Handler();


    String TAG =VerifyTokenActivity.class.getSimpleName();

    Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_token);




        Bundle b = getIntent().getExtras();
        id = b.getString("id");


        declare();
        event();
    }

    private void event() {
        btnCheckCode.setOnClickListener(v -> {
                checkCode();
        });
        ck_tViewSendCode.setOnClickListener(
                v->{
                    sendCode();
                }
        );

    }//055864

    private void checkCode() {

        String code = ck_eTextCode.getText().toString();
        Log.d(TAG,"id:"+id+" "+"code:"+code);
        showProgressBar();
        //consulta
        /*GraphqlClient.getMyApolloClient()
                .query(
                        CheckCodeQuery
                                .builder()
                                .id(id)
                                .code(code)
                                .build()
                )
                .enqueue(new ApolloCall.Callback<CheckCodeQuery.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<CheckCodeQuery.Data> response) {
                        CheckCodeQuery.Data data  = response.data();
                        String successCode = "00";
                        String responseCode = data.checkCode().responseCode();
                        String responseMessage = data.checkCode().responseMessage();
                        String message = data.checkCode().message();
                        if(responseCode.equals(successCode)){
                            goToMain();
                        }else {
                            handler.post(() -> Toast.makeText(ctx, responseMessage, Toast.LENGTH_SHORT).show());

                        }
                        hideProgressBar();

                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {
                        handler.post(() -> Toast.makeText(ctx,e.toString(),Toast.LENGTH_SHORT).show());
                        hideProgressBar();

                    }


                });*/

    }

    private void sendCode() {


    }

    private void declare() {
        btnCheckCode = findViewById(R.id.ck_btnCheckCode);
        progressBar = findViewById(R.id.ck_progressBar);
        ck_tViewSendCode = findViewById(R.id.ck_tViewSendCode);
        ck_eTextCode = findViewById(R.id.ck_eTextCode);
    }


    @Override
    public void enableInputs() {
        handler.post(()->{
            boolean flag = true;
            btnCheckCode.setEnabled(flag);
            ck_tViewSendCode.setEnabled(flag);
        });


    }

    @Override
    public void disableInputs() {
        handler.post(() -> {
            boolean flag = false;
            btnCheckCode.setEnabled(flag);
            ck_tViewSendCode.setEnabled(flag);
        });

    }

    @Override
    public void hideProgressBar() {
        handler.post(()->
                progressBar.setVisibility(View.GONE)
                );

    }

    @Override
    public void showProgressBar() {
        handler.post(()->
                progressBar.setVisibility(View.VISIBLE)
        );

    }

    @Override
    public void goToMain() {
        Intent i = new Intent(this, ActivityMain.class);
        startActivity(i);
    }

}
