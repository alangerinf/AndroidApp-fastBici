package com.alanger.ioquiero.register.repository;

import com.alanger.ioquiero.RegisterMutation;
import com.alanger.ioquiero.register.presenter.RegisterPresenterA;
import com.alanger.ioquiero.register.presenter.RegisterPresenterB;
import com.alanger.ioquiero.volskayaGraphql.GraphqlClient;
import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Mutation;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import javax.annotation.Nonnull;

public class RegisterRepositoryImplB implements RegisterRepositoryB {


    private RegisterPresenterB presenter;


    public RegisterRepositoryImplB(RegisterPresenterB presenter) {
        this.presenter = presenter;
    }



    @Override
    public void registerUser(String email, String password, String phone) {


        GraphqlClient.getMyApolloClient()

                .mutate(
                        RegisterMutation.builder().email(email).pass(password).phone(phone).build()
                )
                .enqueue(
                        new ApolloCall.Callback<RegisterMutation.Data>() {
                            @Override
                            public void onResponse(@Nonnull Response<RegisterMutation.Data> response) {
                                RegisterMutation.Data data = response.data();
                                String successCode = "00";

                                RegisterMutation.VolskayaResponse volskayaResponse = data.register().volskayaResponse();

                                if(volskayaResponse.responseCode().equals(successCode)){
                                    presenter.verifyDataSuccess(data.register().id());

                                }else {
                                    presenter.registerError(volskayaResponse.responseMessage());
                                }

                            }

                            @Override
                            public void onFailure(@Nonnull ApolloException e) {


                            }
                        }


                );
    }



/*
    @Override
    public void signIn(String user, String password) {

        GraphqlClient.getMyApolloClient()
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
                            presenter.verifyError( volskayaResponse.responseCode()+": "+ volskayaResponse.responseMessage());
                        }
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {
                        presenter.verifyError(e.toString());
                    }
                });

    }
    */


}
