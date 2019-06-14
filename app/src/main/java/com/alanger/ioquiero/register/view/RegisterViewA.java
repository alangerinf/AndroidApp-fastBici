package com.alanger.ioquiero.register.view;

public interface RegisterViewA {

     void goRecoverPassword();
     void goRegisterB();

     void enableInputs();
     void disableInputs();

     void hideProgressBar();
     void showProgressBar();

     void loginError(String error);

}
