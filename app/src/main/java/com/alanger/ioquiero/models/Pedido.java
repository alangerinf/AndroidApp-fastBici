package com.alanger.ioquiero.models;

import java.util.ArrayList;
import java.util.List;

public class Pedido {

    private String clientID;



    private String productList;
    private String refA;
    private String refB;
    private String telefono;
    private String nameCliente;
    private String descripcion;

    private double LatA;
    private double LonA;
    private String nameAddressA;

    private double LatB;
    private double LonB;
    private String nameAddressB;

    private double distance;
    private double price;

    public Pedido() {
        this.productList = "";
        this.clientID = "";
        this.LatA=0;
        this.LonA=0;
        this.nameAddressA="";

        this.LatB=0;
        this.LonB=0;
        this.nameAddressB="";


        this.refA="";
        this.refB="";
        this.telefono="";
        this.nameCliente="";
        this.descripcion="";
    }
    public String getProductList() {
        return productList;
    }

    public void setProductList(String productList) {
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

    public double getLatA() {
        return LatA;
    }

    public void setLatA(double latA) {
        LatA = latA;
    }

    public double getLonA() {
        return LonA;
    }

    public void setLonA(double lonA) {
        LonA = lonA;
    }

    public String getNameAddressA() {
        return nameAddressA;
    }

    public void setNameAddressA(String nameAddressA) {
        this.nameAddressA = nameAddressA;
    }

    public double getLatB() {
        return LatB;
    }

    public void setLatB(double latB) {
        LatB = latB;
    }

    public double getLonB() {
        return LonB;
    }

    public void setLonB(double lonB) {
        LonB = lonB;
    }

    public String getNameAddressB() {
        return nameAddressB;
    }

    public void setNameAddressB(String nameAddressB) {
        this.nameAddressB = nameAddressB;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
