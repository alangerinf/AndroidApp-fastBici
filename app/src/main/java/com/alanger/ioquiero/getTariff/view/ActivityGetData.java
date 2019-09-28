package com.alanger.ioquiero.getTariff.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_temporal);

        getExtras();

        declare();

        setDefaults();

        events();

    }

    private void events() {

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setClickable(false);
                v.setFocusable(false);


            }
        });

        iViewCloseDetailPedido.setOnClickListener(ev -> {
            onBackPressed();
        });

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
        latFinish = b.getDouble(EXTRA_LONFINISH);

        price = b.getDouble(EXTRA_PRICE);
        co2 = b.getDouble(EXTRA_CO2);
        km = b.getDouble(EXTRA_KM);
        time = b.getDouble(EXTRA_TIME);

    }
}
