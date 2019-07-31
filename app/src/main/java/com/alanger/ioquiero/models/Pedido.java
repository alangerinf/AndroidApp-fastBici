package com.alanger.ioquiero.models;

import java.util.ArrayList;
import java.util.List;

public class Pedido {

    private List<Product> productList;
    private String refA;
    private String refB;
    private String telefono;
    private String nameCliente;
    private String descripcion;

    public Pedido() {
        this.productList = new ArrayList<>();
        this.refA="";
        this.refB="";
        this.telefono="";
        this.nameCliente="";
        this.descripcion="";
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public String getRefA() {
        return refA;
    }

    public void setRefA(String refA) {
        this.refA = refA;
    }

    public String getRefB() {
        return refB;
    }

    public void setRefB(String refB) {
        this.refB = refB;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNameCliente() {
        return nameCliente;
    }

    public void setNameCliente(String nameCliente) {
        this.nameCliente = nameCliente;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
