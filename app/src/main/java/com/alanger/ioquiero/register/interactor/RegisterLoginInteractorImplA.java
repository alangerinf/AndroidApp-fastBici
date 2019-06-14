package com.alanger.ioquiero.register.interactor;

import com.alanger.ioquiero.register.presenter.RegisterPresenterA;
import com.alanger.ioquiero.register.repository.RegisterRepositoryA;
import com.alanger.ioquiero.register.repository.RegisterRepositoryImplA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterLoginInteractorImplA implements RegisterInteractorA {

    private RegisterPresenterA presenter;
    private RegisterRepositoryA repository;

    public RegisterLoginInteractorImplA(RegisterPresenterA presenter) {
        this.presenter = presenter;
        repository = new RegisterRepositoryImplA(presenter);
    }
/*
    @Override
    public void (String user, String password) {
        repository.signIn(user,password);
    }
*/
    @Override
    public void verifyData(String email, String password1, String password2) {
        if(password1.equals(password2)){//si las contraseñas  coinciden
            Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
            if(matcher.find()){
                repository.verifyData(email);
            }else {
                presenter.verifyError("Email Invalido");
            }
        }else {
            presenter.verifyError("Las contraseñas no coinciden");
        }

    }
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

}
