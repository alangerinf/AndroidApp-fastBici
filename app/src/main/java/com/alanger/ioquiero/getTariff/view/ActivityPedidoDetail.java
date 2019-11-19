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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alanger.ioquiero.Configurations;
import com.alanger.ioquiero.R;
import com.alanger.ioquiero.getTariff.view.adapters.DialogSelectEnterprise;
import com.google.android.material.button.MaterialButton;

public class ActivityPedidoDetail extends AppCompatActivity {

    public static String EXTRA_LATSTART = "latStart";
    public static String EXTRA_LONSTART = "lonStart";
    public static String EXTRA_LATFINISH = "latFinish";
    public static String EXTRA_LONFINISH = "lonFinish";
    public static String EXTRA_PRICE = "price";
    public static String EXTRA_CO2 = "co2";
    public static String EXTRA_KM = "km";
    public static String EXTRA_TIME = "approximateTime";

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


    EditText eTextConCuantoPaga;
    Spinner spnQuienPaga;

    EditText eTextProduct, eTextObservations;

    EditText eTextClientName;
    EditText eTextClientPhone;



    TextView tViewkm, tViewTime, tViewPrice, tViewCO2;

    MaterialButton btnOk;

    ImageView iViewCloseDetailPedido;

    Context ctx;

    String TAG = ActivityPedidoDetail.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_detail);

        ctx = this;

        getExtras();

        declare();

        setDefaults();

        events();

    }

    private void buildWhatsAppRequest(Uri bodyMsg) {
        /*
        Intent whatsappIntent =  new Intent("android.intent.action.MAIN");
        whatsappIntent.setAction(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT,  bodyMsg + uriGoogleMaps);
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        */

        Intent whatsappIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(bodyMsg.toString()));

        try {
            startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ctx,"Whatsapp no esta instalado.",Toast.LENGTH_LONG).show();
        }
    }

    private void events() {

        btnOk.setOnClickListener(v -> {

            if(formIsValid()){
                String uriGoogleMaps = "http://maps.google.com/?mode=walking&saddr=" + latStart + "," + lonStart + "&daddr=" + latFinish + "," + lonFinish;
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

                /******************************* */

                Uri uriMensaje =
                        Uri.parse(
                        "https://api.whatsapp.com/send?phone=" + "xxxxxxxxxx" + "&text=" +
                        "\n*___________ ENTREGA PEDIDO ___________*"+
                        "\n*NOMBRE:*"                         +" "+eTextClientName.getText().toString().trim() +
                        "\n*TELEFONO:*"                       +" "+eTextClientPhone.getText().toString().trim() +
                        "\n*DESCRIPCIÓN DEL PRODUCTO:*"     +" "+product+
                        "\n*TIEMPO APROXIMADO:*"              +" "+time   +" MINUTOS"+
                        "\n*COSTO DEL DELIVERY:*"             +" "+ price +" SOLES" +
                        "\n"+
                        "\n*¿DONDE RECOJEMOS?:*"     +
                        "\n "+ addressStart +
                        "\n"+
                        (eTextRefStart.getText().toString().trim().isEmpty()?
                                ""
                                :
                                "*_REFERENCIA DE DONDE RECOJEMOS:_*"+
                                        "\n"+eTextRefStart.getText().toString().trim()

                        ) +
                        "\n*¿DONDE VAMOS?:*" +
                        "\n "+ addressFinish +
                        "\n" +
                        (eTextRefFinish.getText().toString().trim().isEmpty()?
                                ""
                                :
                                "*_REFERENCIA DE DONDE VAMOS:_*"+
                                        "\n"+eTextRefFinish.getText().toString().trim()

                        )+
                        "\n"+
                        (eTextObservations.getText().toString().trim().isEmpty()?
                                ""
                                :
                                "*OBSERVACIONES:*"+
                                        "\n"+eTextObservations.getText().toString().trim()
                        )+
                        "\n"+
                        (eTextConCuantoPaga.getText().toString().trim().isEmpty()?
                                ""
                                :
                        "*Paga con:* S/ "+ eTextConCuantoPaga.getText().toString().trim()+" ( *"+ spnQuienPaga.getSelectedItem().toString() +"* ) "
                        )+
                        "\n"+
                        uriGoogleMaps)
                        ;
                new DialogSelectEnterprise(ctx,uriMensaje);
                //buildWhatsAppRequest(uriMensaje);

            }else {
                Toast.makeText(ctx,"Faltan campos obligatorios(*)",Toast.LENGTH_LONG).show();
            }
        });

        iViewCloseDetailPedido.setOnClickListener(ev -> {
            onBackPressed();
        });

    }

    private boolean formIsValid() {
        String phone = eTextClientPhone.getText().toString();
        boolean isEmptyName = eTextClientName.getText().toString().isEmpty();
        boolean isEmptyProduct = eTextProduct.getText().toString().isEmpty();
        boolean isEmptyRefFinish = eTextRefFinish.getText().toString().isEmpty();
        boolean isEmptyRefStart = eTextRefStart.getText().toString().isEmpty();
        boolean isInvalidPhone = phone.length()!=9;

        if(isInvalidPhone){
            eTextClientPhone.setError("Ingrese un numero de 9 digitos");
        }
        if(isEmptyName){
            eTextClientName.setError("Ingrese su nombre");
        }
        if(isEmptyProduct){
            eTextProduct.setError("Ingrese un producto");
        }
        if(isEmptyRefStart) {
            eTextRefStart.setError("Ingrese una referencia de recojo");
        }
        if(isEmptyRefFinish) {
            eTextRefFinish.setError("Ingrese una referencia de donde vamos");
        }
        return !(isEmptyProduct || isEmptyName|| isEmptyRefFinish || isEmptyRefStart ||  isInvalidPhone);
    }


    @Override
    protected void onResume() {
        super.onResume();
       // btnOk.setClickable(true);
       // btnOk.setFocusable(true);
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

        eTextObservations = findViewById(R.id.eTextObservations);


        eTextConCuantoPaga = findViewById(R.id.eTextConCuantoPaga);
        spnQuienPaga = findViewById(R.id.spnQuienPaga);

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
