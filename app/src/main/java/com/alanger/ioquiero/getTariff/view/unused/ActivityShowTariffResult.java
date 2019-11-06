package com.alanger.ioquiero.getTariff.view.unused;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alanger.ioquiero.R;
import com.alanger.ioquiero.app.AppController;
import com.alanger.ioquiero.views.Utils;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrador on 09/09/2017.
 */
public class ActivityShowTariffResult extends AppCompatActivity {
    Context ctx;
    private WebView mWebView;
    Double lat1,lng1,lat2,lng2;
    TextView txt1,txt2,txt_precio_referencial;

    private Double precio_base,p_Km,km_LDist;
    private int km_min,dist_Min,exito=0;

    Button btn_perdir_unidad;

    String precio,hora_noche_inicio,hora_noche_fin,add_urbano,url_precio;
    ProgressDialog progress;

    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.tarifario_urbano_precio);
        ctx = this;

        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Cargando datos");

        mWebView = (WebView) findViewById(R.id.wv_espera);
        mWebView.addJavascriptInterface(new WebAppInterface(this), "Android");
        WebSettings ws = mWebView.getSettings();
        ws.setJavaScriptEnabled(true);

        mWebView.setWebChromeClient(new WebChromeClient());

        ws.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        mWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        mWebView.setInitialScale(0);
        ws.setSupportZoom(false);
        ws.setBuiltInZoomControls(false);
        ws.setUseWideViewPort(false);

        txt1 = (TextView)findViewById(R.id.txt1);
        txt2 = (TextView)findViewById(R.id.txt2);
        txt_precio_referencial = (TextView) findViewById(R.id.txt_precio_referencial);

        Bundle extras = getIntent().getExtras();
        lat1 = Double.parseDouble(extras.getString("latStart"));
        lng1 = Double.parseDouble(extras.getString("lonStart"));
        lat2 = Double.parseDouble(extras.getString("latFinish"));
        lng2 = Double.parseDouble(extras.getString("lonFinish"));

        url_precio = Utils.UL0019;


        CargarDatosPrecioBase();
    }

    private void LoadMap(){
        mWebView.loadUrl("file:///android_asset/www/index.html");
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(mWebView, url);
                mWebView.loadUrl("javascript:Android.onData(initMap("+lat1+","+lng1+","+lat2+","+lng2+"))");
                mWebView.setVisibility(View.VISIBLE);
            }

        });
    }

    private void CargarDatosPrecioBase() {

        progress.show();
        StringRequest sr = new StringRequest(Request.Method.POST,
                Utils.UrlServer + url_precio,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.dismiss();
                        try {
                            JSONObject data = new JSONObject(response);
                            int success = data.getInt("success");
                            if(success==1){
                                precio_base = data.getDouble("precio_base");
                                km_min = data.getInt("km_minimo");
                                dist_Min = data.getInt("dist_minimo");
                                p_Km = data.getDouble("prec_km");
                                km_LDist = data.getDouble("km_dist_larga");
                                add_urbano = data.getString("add_urbano");
                                hora_noche_inicio = data.getString("hora_noche_inicio");
                                hora_noche_fin = data.getString("hora_noche_fin");
                                exito = 1;

                                LoadMap();
                            }else{

                                Toast.makeText(ctx,"No se puedo crear pedido",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();

                Toast.makeText(ctx,"No se puedo conectar con el servidor",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("lat-lat","asad");
                params.put("lat-lon","asad");
                params.put("lat-long","asad");
                params.put("lat-ini","asad");

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<String, String>();
                headers.put("Content-Type","application/x-www-form-urlencoded");
                return headers;
            }
        };

        AppController.getInstance().addToRequestQueue(sr);
    }

    class WebAppInterface {
        Context mContext;

        /** Instantiate the interface and set the context */
        WebAppInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void js_DatosDriving(final String dist) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Distancia( dist );
                }
            });
        }
    }

    private void Distancia(String distancia){
        Double dist;
        dist = Double.valueOf(distancia) / 1000;

        DecimalFormat df = new DecimalFormat("#.#");
        //dist = Double.valueOf(df.format(dist));
        //error
        txt1.setText(Double.valueOf((dist))+" KM \n Aprox.");
        precio = CalcularPrecio( dist );

        String valor = "PRECIO REFERENCIAL";
        if(HorarioNocturno(hora_noche_inicio,hora_noche_fin)) {
            precio = String.valueOf(Double.parseDouble(precio) + Double.parseDouble(add_urbano));
            valor = "PRECIO REFRENCIAL NOCTURNO";
        }

        txt2.setText("S/. "+precio);
        txt_precio_referencial.setText(valor);
        //btn_perdir_unidad.setVisibility(View.VISIBLE);
        progress.dismiss();
    }

    private boolean HorarioNocturno(String inicio, String fin){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        String h24 = "23:59:59",h0="00:00:00";
        Date hora_24=null,hora_0=null,inicio_noche = null,fin_noche=null, hora_actual=null;
        Boolean retorno=false;

        try {
            hora_24 = sdf.parse( h24 );
            hora_0 = sdf.parse( h0 );
            inicio_noche = sdf.parse( inicio );
            fin_noche = sdf.parse( fin );
            hora_actual = sdf.parse( sdf.format(cal.getTime()) );

            if( hora_actual.after(inicio_noche)  && hora_actual.before(hora_24) ) {
                retorno = true;
            }else if( hora_actual.after(hora_0)  && hora_actual.before(fin_noche)  ){
                retorno = true;
            }else{
                retorno = false;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return retorno;

    }

    public String CalcularPrecio(Double distancia){
        String importe = "0.0";
        Double d1 = distancia;
        Double pBase = precio_base; //precio base de los servicios minimos
        int kMin = km_min; //kilometraje de recorrido minimo para cobrar el precio base
        int distMin = dist_Min; //distancia minimo para cobrar 1 sol por km mas el precio base
        Double pKm = p_Km; // el precio a cobrar por cada km que exece a la distancia minima
        Double kmLDist = km_LDist;//minimo de km para largas distancias y sumar 1 por km

        if(distancia >= distMin) {
            importe = precioBase((double)distMin-1,pBase,kMin);
            if(distancia < kmLDist) {
                Double diferencia = Math.ceil(d1 - distMin);//redondeamos la diferencia en una unidad
                importe = String.valueOf(Math.ceil(Double.parseDouble(importe) + diferencia * pKm)); //calculamos el precio con el agregado del precio base
            }else{//calculamos precio con mayor distancia a 15
                Double diferencia = Math.ceil(d1 - distMin);//redondeamos la diferencia en una unidad
                Double kmMayor = diferencia - kmLDist;//la diferencia sumamos 1 * km
                importe = String.valueOf(Math.ceil(Double.parseDouble(importe) + (distMin+kMin) * pKm + kmMayor)); //calculamos el precio con el agregado del precio base
            }
        }else{
            importe = precioBase(d1,pBase,kMin);
        }
        return importe;
    }

    private String precioBase(Double d1,Double pBase,int kMin){
        String importe = "0.0";
        if (d1 <= kMin) {
            importe = String.valueOf( pBase );
        } else {
            d1 = Math.ceil(d1);
            d1 = d1 - kMin;
            importe = String.valueOf( pBase + d1 );//calculamos el precio restando el km minimmo para suma 1 por km
        }
        return importe;
    }

}
