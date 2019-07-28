package com.alanger.ioquiero.models;

public class Product {

    private int cant;
    private String name;

    public Product() {
        this.cant=0;
        this.name="";
    }

    public Product(int cant, String name) {
        this.cant = cant;
        this.name = name;
    }

    public int getCant() {
        return cant;
    }

    public void setCant(int cant) {
        this.cant = cant;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
