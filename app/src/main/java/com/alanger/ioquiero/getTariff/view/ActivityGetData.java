package com.alanger.ioquiero.getTariff.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alanger.ioquiero.Configurations;
import com.alanger.ioquiero.R;
import com.google.android.material.button.MaterialButton;

public class ActivityGetData extends AppCompatActivity {

    public static String EXTRA_LATSTART = "latStart";
    public static String EXTRA_LONSTART = "lonStart";
    public static String EXTRA_LATFINISH = "latFinish";
    public static String EXTRA_LONFINISH = "lonFinish";
    public static String EXTRA_PRICE = "price";
    public static String EXTRA_CO2 = "co2";
    public static String EXTRA_KM = "km";
    public static String EXTRA_TIME = "timeAproximate";

    public static String EXTRA_ADDRESSSTART = "addressStart";
    public static String EXTRA_ADDRESSFINISH = "addressFinisht";


    private static double latStart;
    private static double lonStart;

    private static double latFinish;
    private static double lonFinish;

    private static String addressStart;
    private static String addressFinish;

    private static double price;
    private static double co2;
    private static double km;
    private static double time;

    TextView tViewAddressStart;
    TextView tViewAddressFinish;

    EditText eTextRefStart;
    EditText eTextRefFinish;

    EditText eTextProduct;

    EditText eTextClientName;
    EditText eTextClientPhone;

    TextView tViewkm, tViewTime, tViewPrice, tViewCO2;

    MaterialButton btnOk;

    ImageView iViewCloseDetailPedido;

    Context ctx;

    String TAG = ActivityGetData.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_temporal);

        ctx = this;

        getExtras();

        declare();

        setDefaults();

        events();

    }

    private void events() {

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(dataisValide()){
                    v.setClickable(false);
                    v.setFocusable(false);
                    String uriGoogleMaps = "http://maps.google.com/?mode=walking%26saddr=" + latStart + "," + lonStart + "%26daddr=" + latFinish + "," + lonFinish;
                    //String uriGoogleMaps = "http://maps.google.com/?mode=walking%26saddr=-8.1158903,-79.0356704%26daddr=-8.1179977,-79.0358920";
                    String phone = Configurations.phone;

                    /**
                    ENTREGA PEDIDO ENCARACOLADOS
                    HORA DE ENTREGA 12 AM
                    CLIENTE: KARINA FLORES
                    CELULAR: 948051691
                    DIRECCION: CALLE LOS BRILLANTES 627, RESIDENCIAL LOS BRILLANTES BLOCK H DPTO 404, URB SANTA INÉS
                    COBRAR: 99.00 SOLES INCLUIDO DELIVERY
                    COSTO DEL DELIVERY 9.00
                    OBSERVACION: CLIENTE PAGA CON 100.00 SOLES
                    UBICACION
                    */

                    String product = eTextProduct.getText().toString().trim();
                    Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=" + phone + "&text=" +
                            "\n*ENTREGA PEDIDO*"+
                            "\nNOMBRE:"+" "+eTextClientName.getText().toString().trim()+
                            "\nTELEFONO:"+" "+eTextClientPhone.getText().toString().trim()+
                            "\nDESCRIPCIÓN DEL PRODUCTO"+" "+product+
                            "\nTIEMPO APROXIMADO:"      +" "+time   +" MINUTOS"+
                            "\nCOSTO DEL DELIVERY:"     +" "+ price +" SOLES"+
                            "\n¿DONDE RECOJEMOS?:"     +" "+ addressStart +
                            "\n"+
                            (eTextRefStart.getText().toString().trim().equals("")?
                                    ""
                                    :
                                    "\n*REFERENCIA DE DONDE RECOJEMOS*"+
                                            "\n"+eTextRefStart.getText().toString().trim()

                            )+
                            "\n¿DONDE VAMOS?:"     +" "+ addressFinish +
                            (eTextRefFinish.getText().toString().trim().equals("")?
                                    "\n"
                                    :
                                    "\n*REFERENCIA DE DONDE VAMOS*"+
                                            "\n"+eTextRefFinish.getText().toString().trim()

                            )+
                            "\n"+
                                    uriGoogleMaps+
                            ""
                    );
                    //    uri = Uri.parse("smsto:" + "98*********7");
                    Log.d(TAG, "" + uri.toString());
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri.toString()));
                    startActivity(myIntent);
                }else {

                    Toast.makeText(ctx,"Faltan campos obligatorios(*)",Toast.LENGTH_LONG).show();

                }



            }
        });

        iViewCloseDetailPedido.setOnClickListener(ev -> {
            onBackPressed();
        });

    }

    private boolean dataisValide() {
        String phone = eTextClientPhone.getText().toString();
        String name = eTextClientName.getText().toString();
        String product = eTextProduct.getText().toString();

        if(phone.length()!=9){
            eTextClientPhone.setError("Ingrese un numero de 9 digitos");
        }
        if(name.equals("")){
            eTextClientName.setError("Ingrese su nombre");
        }

        if(product.equals("")){
            eTextProduct.setError("Ingrese un producto");
        }
        return !(phone.equals("") || name.equals("") || product.equals(""));
    }


    @Override
    protected void onResume() {
        super.onResume();
        btnOk.setClickable(true);
        btnOk.setFocusable(true);
    }

    private void setDefaults() {
        tViewAddressStart.setText(""+addressStart);
        tViewAddressFinish.setText(""+addressFinish);
        tViewCO2.setText(""+co2);
        tViewkm.setText(""+km);
        tViewPrice.setText(""+price);
        tViewTime.setText(""+time);
    }



    private void declare() {

        tViewAddressStart = findViewById(R.id.tViewAddressStart);
        tViewAddressFinish = findViewById(R.id.tViewAddressFinish);
        eTextRefStart = findViewById(R.id.eTextRefStart);
        eTextRefFinish = findViewById(R.id.eTextRefFinish);
        eTextProduct = findViewById(R.id.eTextProduct);
        eTextClientName = findViewById(R.id.eTextClientName);
        eTextClientPhone = findViewById(R.id.eTextClientPhone);

        tViewkm = findViewById(R.id.tViewkm);
        tViewTime = findViewById(R.id.tViewTime);
        tViewPrice = findViewById(R.id.tViewPrice);
        tViewCO2 = findViewById(R.id.tViewCO2);

        btnOk = findViewById(R.id.btnOk);
        iViewCloseDetailPedido =  findViewById(R.id.iViewClose_detail_pedido);
    }

    private void getExtras() {
    Bundle  b = getIntent().getExtras();

        addressStart = b.getString(EXTRA_ADDRESSSTART);
        addressFinish = b.getString(EXTRA_ADDRESSFINISH);

        latStart = b.getDouble(EXTRA_LATSTART);
        lonStart = b.getDouble(EXTRA_LONSTART);
        latFinish = b.getDouble(EXTRA_LATFINISH);
        lonFinish = b.getDouble(EXTRA_LONFINISH);

        price = b.getDouble(EXTRA_PRICE);
        co2 = b.getDouble(EXTRA_CO2);
        km = b.getDouble(EXTRA_KM);
        time = b.getDouble(EXTRA_TIME);

    }
}
