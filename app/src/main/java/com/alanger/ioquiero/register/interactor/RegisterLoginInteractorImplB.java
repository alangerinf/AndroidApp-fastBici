package com.alanger.ioquiero.register.interactor;

import com.alanger.ioquiero.R;
import com.alanger.ioquiero.register.presenter.RegisterPresenterB;
import com.alanger.ioquiero.register.repository.RegisterRepositoryB;
import com.alanger.ioquiero.register.repository.RegisterRepositoryImplB;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterLoginInteractorImplB implements RegisterInteractorB {

    private RegisterPresenterB presenter;
    private RegisterRepositoryB repository;

    public RegisterLoginInteractorImplB(RegisterPresenterB presenter) {
        this.presenter = presenter;
        repository = new RegisterRepositoryImplB(presenter);
    }
/*
    @Override
    public void (String user, String password) {
        repository.signIn(user,password);
    }
*/
    @Override
    public void intentRegister(String email, String password, String phone) {
        if(phone.length()==9 && phone.charAt(0)=='9'){
            repository.registerUser(email,password,phone);
        }else {
            presenter.registerError("Número de telefono invalido");
        }
    }

}
