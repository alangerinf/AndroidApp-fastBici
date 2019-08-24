package com.alanger.ioquiero;

public class Configurations {

    public static final String phone = "51973446468";
    public static final String API_KEY = "AIzaSyBNAHJGZZE4hzvH5dHfHq3TOKe2HYP5Fks";


    public static class CodeMaps{
        public static String OK = "OK";
        public static String ZERO_RESULTS = "ZERO_RESULTS";
        public static String OVER_QUERY_LIMIT = "OVER_QUERY_LIMIT";
        public static String INVALID_REQUEST = "INVALID_REQUEST";
        public static String UNKNOWN_ERROR = "UNKNOWN_ERROR";
    }


    public static String getUrlSearchPlaces(String text,String lat,String lon){

        return "https://maps.googleapis.com/maps/api/place/textsearch/json?query="+text+"&location="+lat+","+lon+"&radius=20000&key="+API_KEY;

    }

}
