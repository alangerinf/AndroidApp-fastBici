package com.alanger.ioquiero;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.alanger.ioquiero.app.AppController;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.apollographql.apollo.ApolloClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class ApiPrueba {

    Context ctx;

    String TAG = ApiPrueba.class.getSimpleName();
 //   ProgressDialog progress;
    public static int status;
    public ApiPrueba(Context ctx){
        status=0;
        this.ctx = ctx;
    }

    public void download(){


        String BASE_URL = "<your_server>/graphql";

        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        ApolloClient apolloClient = ApolloClient.builder()
                .serverUrl(BASE_URL)
                .okHttpClient(okHttpClient)
                .build();






        status=1;
   //     progress = new ProgressDialog(ctx);
   //     progress.setCancelable(false);
   //     progress.setMessage("Intentando descargar Empresas");
   //     progress.show();
        JSONObject jsonObj= new JSONObject();
        try {
            jsonObj = new JSONObject (
                    "" +
                            "{\"operationName\":null,\"variables\":{},\"query\":\"{\\n  allUsers {\\n    email\\n  }\\n}\\n\"}" +
                            "");
        } catch (JSONException e) {
            e.printStackTrace();
        }



        //jsonObject2 is the payload to server here you can use JsonObjectRequest

        String url="https://volskayaforce.herokuapp.com/graphql";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST,url, jsonObj, new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d(TAG,"resp : "+response.toString());

                        try {
                            //TODO: Handle your response here
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        System.out.print(response);

                    }
                }, new com.android.volley.Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        error.printStackTrace();

                    }


                });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

}
