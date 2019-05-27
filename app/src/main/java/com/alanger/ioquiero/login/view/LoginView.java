package com.alanger.ioquiero.login.view;

public interface LoginView {

     void goRecoverPassword();
     void goHome();

     void enableInputs();
     void disableInputs();

     void hideProgressBar();
     void showProgressBar();

     void loginError(String error);

}
