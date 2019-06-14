package com.alanger.ioquiero.register.repository;

import com.alanger.ioquiero.register.presenter.RegisterPresenterA;

public class RegisterRepositoryImplA implements RegisterRepositoryA {


    private RegisterPresenterA presenter;


    public RegisterRepositoryImplA(RegisterPresenterA presenter) {
        this.presenter = presenter;
    }

    @Override
    public void verifyData(String email) {
        if(true){
            presenter.verifyDataSuccess();
        }else {
            presenter.verifyError("eror en la verificacion del repositorio");
        }

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
