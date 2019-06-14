package com.alanger.ioquiero.register.view;

public interface RegisterViewB {

     void goRecoverPassword();
     void goVerifyPhone(String id);

     void enableInputs();
     void disableInputs();

     void hideProgressBar();
     void showProgressBar();

     void registerError(String error);

}
