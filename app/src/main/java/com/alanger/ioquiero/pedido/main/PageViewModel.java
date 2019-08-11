package com.alanger.ioquiero.pedido.main;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.alanger.ioquiero.models.Pedido;

public class PageViewModel extends ViewModel {

    private static MutableLiveData<Pedido> _pedido;
/*
     LiveData<Pedido> mText = Transformations.map(_pedido, new Function<Integer, String>() {
        @Override
        public String apply(Integer input) {
            return "Hello world from section: " + input;
        }
    });
*/
    private LiveData<Pedido> pedidoLiveData = Transformations.map(_pedido, new Function<Pedido, Pedido>() {
        @Override
        public Pedido apply(Pedido input) {
            return input;
        }
    });

    public static void set(Pedido index) {
        _pedido.setValue(index);
    }

    public LiveData<Pedido> get() {
        return pedidoLiveData;
    }


    public static Pedido getPedido() {
        return _pedido.getValue();
    }


    public static void init(){
        _pedido = new MutableLiveData<>();
    }

}