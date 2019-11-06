package com.alanger.ioquiero.getTariff.models;

import android.graphics.drawable.Drawable;

public class Enterprise {

    private String name;
    private String phone;
    private Drawable logo;

    public Enterprise() {
        this.name = "";
        this.phone = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Drawable getLogo() {
        return logo;
    }

    public void setLogo(Drawable logo) {
        this.logo = logo;
    }
}
