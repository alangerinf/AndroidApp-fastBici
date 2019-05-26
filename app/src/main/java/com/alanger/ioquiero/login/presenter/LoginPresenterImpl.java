package com.alanger.ioquiero.login.presenter;

import com.alanger.ioquiero.login.interactor.LoginInteractor;
import com.alanger.ioquiero.login.interactor.LoginInteractorImpl;
import com.alanger.ioquiero.login.view.LoginView;

public class LoginPresenterImpl implements LoginPresenter{

    private LoginView loginView;
    private LoginInteractor interactor;

    public LoginPresenterImpl(LoginView loginView) {
        this.loginView = loginView;
        interactor = new LoginInteractorImpl(this);
    }

    @Override
    public void signIn(String user, String password) {
        loginView.disableInputs();
        loginView.showProgressBar();
        interactor.signIn(user,password);
    }

    @Override
    public void loginSuccess() {
        loginView.goHome();
        loginView.enableInputs();
        loginView.hideProgressBar();
    }

    @Override
    public void loginError(String error) {
        loginView.enableInputs();
        loginView.hideProgressBar();
        loginView.loginError(error);
    }
}
