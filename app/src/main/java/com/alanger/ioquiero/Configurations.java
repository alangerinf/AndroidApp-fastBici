package com.alanger.ioquiero;

public class Configurations {

    public static final String phone = "51973446468";
    public static final String API_KEY = "AIzaSyA9GmKAlfax_6Khs5JvUxJk0zhDzpCJyKw";

    public static class CodeMaps{
        public static String OK = "OK";
        public static String ZERO_RESULTS = "ZERO_RESULTS";
        public static String OVER_QUERY_LIMIT = "OVER_QUERY_LIMIT";
        public static String INVALID_REQUEST = "INVALID_REQUEST";
        public static String UNKNOWN_ERROR = "UNKNOWN_ERROR";
    }


    public static String getUrlSearchPlaces(String text,String lat,String lon){

        return "https://maps.googleapis.com/maps/api/place/textsearch/json?location="+lat+","+lon+"&radius=10000&region=pe&query="+text+"&key="+API_KEY;

    }

}
