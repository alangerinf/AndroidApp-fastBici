package com.alanger.ioquiero.register.presenter;

public interface RegisterPresenterA {

    void verifyDataSuccess();
    void verifyError(String error);
    void verifyData(String email,String pass1,String pass2);

}
