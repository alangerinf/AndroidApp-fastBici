package com.alanger.ioquiero.register.presenter;

public interface RegisterPresenterB {

    void verifyDataSuccess(String id);
    void registerError(String error);
    void intentRegister(String email, String pass1, String pass2);

}
