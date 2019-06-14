package com.alanger.ioquiero.register.presenter;

import com.alanger.ioquiero.register.interactor.RegisterInteractorB;
import com.alanger.ioquiero.register.interactor.RegisterLoginInteractorImplB;
import com.alanger.ioquiero.register.view.RegisterViewB;

public class RegisterPresenterImplB implements RegisterPresenterB {

    private RegisterViewB registerViewB;
    private RegisterInteractorB interactor;

    public RegisterPresenterImplB(RegisterViewB registerViewB) {
        this.registerViewB = registerViewB;
        interactor = new RegisterLoginInteractorImplB(this);
    }


    @Override
    public void verifyDataSuccess(String id) {
        registerViewB.goVerifyPhone(id);
        registerViewB.enableInputs();
        registerViewB.hideProgressBar();
    }

    @Override
    public void registerError(String error) {
        registerViewB.enableInputs();
        registerViewB.hideProgressBar();
        registerViewB.registerError(error);
    }

    @Override
    public void intentRegister(String email, String pass1, String pass2) {
        interactor.intentRegister(email,pass1,pass2);
        registerViewB.disableInputs();
        registerViewB.showProgressBar();
    }
}
