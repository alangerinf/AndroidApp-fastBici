package com.alanger.ioquiero.login.interactor;

import com.alanger.ioquiero.login.presenter.LoginPresenter;
import com.alanger.ioquiero.login.repository.LoginRepository;
import com.alanger.ioquiero.login.repository.LoginRepositoryImpl;

public class LoginInteractorImpl implements LoginInteractor {

    private LoginPresenter presenter;
    private LoginRepository repository;

    public LoginInteractorImpl(LoginPresenter presenter) {
        this.presenter = presenter;
        repository = new LoginRepositoryImpl(presenter);
    }

    @Override
    public void signIn(String user, String password) {
        repository.signIn(user,password);
    }

}
