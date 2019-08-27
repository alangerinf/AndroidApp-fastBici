package com.alanger.ioquiero.getTariff.view;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class Place {

    private String lat;
    private String lon;
    private JSONArray types;
    private String formatted_address;


    public Place() {
        this.lat = "";
        this.lon = "";
        this.types = new JSONArray();
        this.formatted_address = "Sin Direccion";
    }

    public Place(String lat, String lon, JSONArray types, String formatted_address) {
        this.lat = lat;
        this.lon = lon;
        this.types = types;
        this.formatted_address = formatted_address;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public JSONArray getTypes() {
        return types;
    }

    public void setTypes(JSONArray types) {
        this.types = types;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }
}
