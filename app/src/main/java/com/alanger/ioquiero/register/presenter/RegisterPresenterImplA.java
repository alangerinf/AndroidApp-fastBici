package com.alanger.ioquiero.register.presenter;

import com.alanger.ioquiero.register.interactor.RegisterInteractorA;
import com.alanger.ioquiero.register.interactor.RegisterLoginInteractorImplA;
import com.alanger.ioquiero.register.view.RegisterViewA;

public class RegisterPresenterImplA implements RegisterPresenterA {

    private RegisterViewA registerViewA;
    private RegisterInteractorA interactor;

    public RegisterPresenterImplA(RegisterViewA registerViewA) {
        this.registerViewA = registerViewA;
        interactor = new RegisterLoginInteractorImplA(this);
    }


    @Override
    public void verifyDataSuccess() {
        registerViewA.goRegisterB();
        registerViewA.enableInputs();
        registerViewA.hideProgressBar();
    }

    @Override
    public void verifyError(String error) {
        registerViewA.enableInputs();
        registerViewA.hideProgressBar();
        registerViewA.loginError(error);
    }

    @Override
    public void verifyData(String email, String pass1, String pass2) {
        interactor.verifyData(email,pass1,pass2);
        registerViewA.disableInputs();
        registerViewA.showProgressBar();
    }
}
