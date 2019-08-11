package com.alanger.ioquiero.bandeja;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.alanger.ioquiero.R;
import com.alanger.ioquiero.bandeja.adapters.RViewAdapterProduct;
import com.alanger.ioquiero.models.Product;
import com.alanger.ioquiero.pedido.main.ResumenFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrderDetailActivity extends AppCompatActivity {


    static WebView mWebView;

    static RViewAdapterProduct adapter;
    static List<Product> productList;
    static RecyclerView odai_rView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_detail_activity);

        productList = new ArrayList<>();

        Product product1 = new Product();
        product1.setCant(5);
        product1.setName("Papelotes");

        Product product2 = new Product();
        product2.setCant(12);
        product2.setName("Lapiceros");


        productList.add(product1);
        productList.add(product2);

        Log.d("hola","tamaño1 "+productList.size());

        odai_rView = (RecyclerView) findViewById(R.id.oda_rView);
        adapter = new RViewAdapterProduct(productList);
        Log.d("hola","tamaño2 "+adapter.getItemCount());

        odai_rView.setAdapter(adapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mWebView = findViewById(R.id.oda_webView);

        mWebView.addJavascriptInterface(new OrderDetailActivity.WebAppInterface(this), "Android");
        WebSettings ws = mWebView.getSettings();
        ws.setJavaScriptEnabled(true);

        mWebView.setWebChromeClient(new WebChromeClient());

        ws.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        mWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        mWebView.setInitialScale(0);

        LoadMap("-8.1141364","-79.0239832","-8.103876","-79.018018");

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // finish();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.order_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cancelar) {
            Toast.makeText(this,"Cancelar",Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class WebAppInterface {
        Context mContext;

        /** Instantiate the interface and set the context */
        WebAppInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void js_DatosDriving(final String dist) {
            /*
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Distancia( dist );
                }
            });
            */
        }
    }

    private void LoadMap(String lat1,String lng1,String lat2, String lng2){
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

}
