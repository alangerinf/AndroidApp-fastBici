package com.alanger.ioquiero.login.repository;
/*
import android.util.Log;
*/
import com.alanger.ioquiero.login.presenter.LoginPresenter;
/*
import com.alanger.ioquiero.volskayaGraphql.GraphqlClient;
import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import javax.annotation.Nonnull;
*/
public class LoginRepositoryImpl implements LoginRepository {


    private LoginPresenter presenter;


    public LoginRepositoryImpl(LoginPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void signIn(String user, String password) {

/*        GraphqlClient.getMyApolloClient()
                .query(
                        LoginQuery
                                .builder()
                                .email(user)
                                .pass(password)
                                .build()
                )
                .enqueue(new ApolloCall.Callback<LoginQuery.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<LoginQuery.Data> response) {
                        LoginQuery.Data data = response.data();

                        String successCode = "00";

                        LoginQuery.VolskayaResponse volskayaResponse = data.login().volskayaResponse();



                        
                        if(successCode.equals(  volskayaResponse.responseCode())){
                            presenter.loginSuccess();
                        }else {
                            presenter.loginError( volskayaResponse.responseCode()+": "+ volskayaResponse.responseMessage());
                        }
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {
                        presenter.loginError(e.toString());
                    }
                });*/
    }
}
