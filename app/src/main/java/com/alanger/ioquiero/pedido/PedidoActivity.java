package com.alanger.ioquiero.pedido;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;

import com.alanger.ioquiero.R;
import com.alanger.ioquiero.RegisterMutation;
import com.alanger.ioquiero.RegisterOrderMutation;
import com.alanger.ioquiero.models.Pedido;
import com.alanger.ioquiero.models.Product;
import com.alanger.ioquiero.pedido.main.CustomViewPager;
import com.alanger.ioquiero.pedido.main.PageViewModel;
import com.alanger.ioquiero.pedido.main.Paso1Fragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alanger.ioquiero.pedido.main.Paso2Fragment;
import com.alanger.ioquiero.pedido.main.ResumenFragment;
import com.alanger.ioquiero.pedido.main.SectionsPagerAdapter;
import com.alanger.ioquiero.type.AddressFieldsInput;
import com.alanger.ioquiero.type.CoordinateFieldsInput;
import com.alanger.ioquiero.type.FinalClientFieldsInput;
import com.alanger.ioquiero.type.ProductFieldsInput;
import com.alanger.ioquiero.type.RouteFieldsInput;
import com.alanger.ioquiero.volskayaGraphql.GraphqlClient;
import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class PedidoActivity extends AppCompatActivity implements
        Paso1Fragment.OnFragmentInteractionListener ,
        Paso2Fragment.OnFragmentInteractionListener,
        ResumenFragment.OnFragmentInteractionListener
{

    private static String TAG = PedidoActivity.class.getSimpleName()+"Log";


    private Pedido PEDIDO;

    private boolean flag1;
    private boolean flag2;




    MaterialButton btnDone;
    MaterialButton btnNext;
    MaterialButton btnBack;



    public static CustomViewPager viewPager;
    static SectionsPagerAdapter sectionsPagerAdapter;
    static Context ctx;


    ImageView iViewOk1;
    ImageView iViewOk2;
    ImageView iViewOk3;

    ProgressBar progressBar1;
    ProgressBar progressBar2;
    ProgressBar progressBar3;


    TextView tViewPaso1;
    TextView tViewPaso2;
    TextView tViewPaso3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);

        declare();
    }

    private void declare() {
        PEDIDO = new Pedido();
        PageViewModel.init();
        PageViewModel.set(PEDIDO);

        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        //TabLayout tabs = findViewById(R.id.tabs);
        //tabs.setupWithViewPager(viewPager);
        viewPager.setAllowedSwipeDirection(CustomViewPager.SwipeDirection.none);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
              //  Log.d(TAG,"<onPageScrolled>"+"position:"+position+" positionOffset:"+positionOffset+" positionOffsetPixels"+positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG,"<onPageSelected>"+"position:"+position);

                switch (position){
                    case 0:
                        tViewPaso1.setTextColor(Color.parseColor(colorActive));
                        tViewPaso2.setTextColor(Color.parseColor(colorNoActive));
                        tViewPaso3.setTextColor(Color.parseColor(colorNoActive));

                        disableBtnDone();
                        if(flag1){
                            viewPager.setAllowedSwipeDirection(CustomViewPager.SwipeDirection.right);
                            progressBar1.setVisibility(View.INVISIBLE);
                            iViewOk1.setVisibility(View.VISIBLE);
                            enableBtnNext();
                            disableBtnBack();
                        }else {
                            disableBtnBack();
                            disableBtnNext();
                            viewPager.setAllowedSwipeDirection(CustomViewPager.SwipeDirection.none);
                            progressBar1.setVisibility(View.VISIBLE);
                            iViewOk1.setVisibility(View.INVISIBLE);
                        }
                        break;
                    case 1:
                        tViewPaso1.setTextColor(Color.parseColor(colorNoActive));
                        tViewPaso2.setTextColor(Color.parseColor(colorActive));
                        tViewPaso3.setTextColor(Color.parseColor(colorNoActive));
                        disableBtnDone();
                        if(flag2){
                            viewPager.setAllowedSwipeDirection(CustomViewPager.SwipeDirection.all);
                            progressBar2.setVisibility(View.INVISIBLE);
                            iViewOk2.setVisibility(View.VISIBLE);
                            enableBtnNext();
                            enableBtnBack();
                        }else {
                            disableBtnNext();
                            enableBtnBack();
                            viewPager.setAllowedSwipeDirection(CustomViewPager.SwipeDirection.left);
                            progressBar2.setVisibility(View.VISIBLE);
                            iViewOk2.setVisibility(View.INVISIBLE);
                        }
                        break;
                    case 2:
                        tViewPaso1.setTextColor(Color.parseColor(colorNoActive));
                        tViewPaso2.setTextColor(Color.parseColor(colorNoActive));
                        tViewPaso3.setTextColor(Color.parseColor(colorActive));

                        //if(flag2){
                            viewPager.setAllowedSwipeDirection(CustomViewPager.SwipeDirection.left);
                            progressBar3.setVisibility(View.VISIBLE);
                            iViewOk3.setVisibility(View.INVISIBLE);
                            enableBtnBack();
                            disableBtnNext();
                            enableBtnDone();
                        /*}else {
                            viewPager.setPagingEnabled(false);
                            progressBar2.setVisibility(View.VISIBLE);
                            iViewOk2.setVisibility(View.INVISIBLE);
                        }*/
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
           //     Log.d(TAG,"<onPageScrollStateChanged>"+"state:"+state);
            }
        });

        ctx = this;

        iViewOk1 = findViewById(R.id.iViewOk1);
        iViewOk2 = findViewById(R.id.iViewOk2);
        iViewOk3 = findViewById(R.id.iViewOk3);

        iViewOk1.setVisibility(View.INVISIBLE);
        iViewOk2.setVisibility(View.INVISIBLE);
        iViewOk3.setVisibility(View.INVISIBLE);

        tViewPaso1 = findViewById(R.id.tViewPaso1);
        tViewPaso2 = findViewById(R.id.tViewPaso2);
        tViewPaso3 = findViewById(R.id.tViewPaso3);

        progressBar1 = findViewById(R.id.progressBar1);
        progressBar2 = findViewById(R.id.progressBar2);
        progressBar3 = findViewById(R.id.progressBar3);

        btnNext = findViewById(R.id.btnNext);
        btnDone = findViewById(R.id.btnDone);
        btnBack = findViewById(R.id.btnBack);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int page =viewPager.getCurrentItem();
                if(page>0){
                    viewPager.setCurrentItem(page-1,true);
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int page =viewPager.getCurrentItem();
                if(page<2){
                    viewPager.setCurrentItem(page+1,true);
                    enableBtnBack();
                }
            }
        });
/*
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pedido pedido = PageViewModel.getPedido();

                CoordinateFieldsInput corA = new CoordinateFieldsInput(pedido.getLatA(),pedido.getLonA());
                AddressFieldsInput adrA = new AddressFieldsInput(pedido.getNameAddressA(),pedido.getRefA(),corA);

                CoordinateFieldsInput corB = new CoordinateFieldsInput(pedido.getLatB(),pedido.getLonB());
                AddressFieldsInput adrB = new AddressFieldsInput(pedido.getNameAddressB(),pedido.getRefB(),corB);

                RouteFieldsInput route = new RouteFieldsInput(adrA,adrB);


                FinalClientFieldsInput finalClientFieldsInput = new FinalClientFieldsInput(pedido.getNameCliente(),pedido.getTelefono());


                List<ProductFieldsInput> productFieldsInputList = new ArrayList<>();

                for(Product p : pedido.getProductList()){
                    ProductFieldsInput temp = new ProductFieldsInput(p.getName(),p.getCant());
                    productFieldsInputList.add(temp);
                }

                GraphqlClient.getMyApolloClient()

                        .mutate(
                                RegisterOrderMutation.builder()
                                        .route(route)
                                        .clientId(pedido.getClientID())
                                        .finalClient(finalClientFieldsInput)
                                        .distance(pedido.getDistance())
                                        .price(pedido.getPrice())
                                        .generalDescription(pedido.getDescripcion())
                                        .products(productFieldsInputList)
                                        .build()
                        )
                        .enqueue(
                                new ApolloCall.Callback<RegisterOrderMutation.Data>() {
                                    @Override
                                    public void onResponse(@Nonnull Response<RegisterOrderMutation.Data> response) {
                                        RegisterOrderMutation.Data data = response.data();
                                        String successCode = "00";

                                        RegisterOrderMutation.VolskayaResponse volskayaResponse = data.registerOrder().volskayaResponse();

                                        if(volskayaResponse.responseCode().equals(successCode)){
                                            Log.d("asdas","1");
                                       //     presenter.verifyDataSuccess(data.register().user().id());

                                        }else {
                                            Log.d("asdas","2");
                                            //     presenter.verifyDataSuccess(data.register().user().id());
                                            //                                            presenter.registerError(volskayaResponse.responseMessage());
                                        }

                                    }

                                    @Override
                                    public void onFailure(@Nonnull ApolloException e) {


                                    }
                                }


                        );
            }
        });

*/
        defaultAtributtes();

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    }
                    assert imm != null;
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    onWindowFocusChanged(true);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    // Shows the system bars by removing all the flags
// except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }



    void enableBtnNext(){
        btnNext.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        btnNext.setFocusable(true);
        btnNext.setClickable(true);
        btnNext.setVisibility(View.VISIBLE);
    }

    void enableBtnDone(){
        btnDone.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        btnDone.setFocusable(true);
        btnDone.setClickable(true);
        btnDone.setVisibility(View.VISIBLE);
    }

    void disableBtnDone(){
        btnDone.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        btnDone.setFocusable(true);
        btnDone.setClickable(true);
        btnDone.setVisibility(View.INVISIBLE);
    }



    void enableBtnBack(){
        btnBack.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setFocusable(true);
        btnBack.setClickable(true);

    }

    void disableBtnNext(){
        btnNext.setFocusable(false);
        btnNext.setClickable(false);
        btnNext.setBackgroundColor(getResources().getColor(R.color.gray));
        if(viewPager.getCurrentItem()==2){
            btnNext.setVisibility(View.INVISIBLE);
        }else {
            btnNext.setVisibility(View.VISIBLE);
        }
    }

    void disableBtnBack(){
        btnBack.setFocusable(false);
        btnBack.setBackgroundColor(getResources().getColor(R.color.gray));
        btnBack.setClickable(false);
        if(viewPager.getCurrentItem()==0){
            btnBack.setVisibility(View.INVISIBLE);
        }else {
            btnBack.setVisibility(View.VISIBLE);
        }
    }

    String colorActive = "#009E0F";
    String colorNoActive = "#757575";


    void defaultAtributtes(){
       // viewPager.setCurrentItem(0,true);
        disableBtnNext();
        disableBtnBack();
        btnBack.setVisibility(View.INVISIBLE);
        progressBar1.setVisibility(View.VISIBLE);
        progressBar2.setVisibility(View.INVISIBLE);
        progressBar3.setVisibility(View.INVISIBLE);

        tViewPaso1.setTextColor(Color.parseColor(colorActive));
        tViewPaso2.setTextColor(Color.parseColor(colorNoActive));
        tViewPaso3.setTextColor(Color.parseColor(colorNoActive));

    }

    @Override
    public void onFragmentInteractionPaso2(boolean flag, String nombre, String telefono, String refB) {
        Log.d(TAG,"paso 2 interaction");
        this.flag2 = flag;
        PEDIDO.setNameCliente(nombre);
        PEDIDO.setTelefono(telefono);
        PEDIDO.setRefB(refB);
        PageViewModel.set(PEDIDO);
        enableBtnBack();
        if(flag2){
            progressBar2.setVisibility(View.INVISIBLE);
            iViewOk2.setVisibility(View.VISIBLE);
            viewPager.setAllowedSwipeDirection(CustomViewPager.SwipeDirection.all);
            enableBtnNext();
//            enableBtnBack();

        }else {
            disableBtnNext();
  //          disableBtnBack();
            progressBar2.setVisibility(View.VISIBLE);
            iViewOk2.setVisibility(View.INVISIBLE);
            viewPager.setAllowedSwipeDirection(CustomViewPager.SwipeDirection.left);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteractionPaso1(boolean flag, String refA, String productos) {
        Log.d(TAG,"paso 1 interaction");
        this.flag1 = flag;
        PEDIDO.setRefA(refA);
        PEDIDO.setProductList(productos);
        PageViewModel.set(PEDIDO);

        if(flag1){
            progressBar1.setVisibility(View.INVISIBLE);
            iViewOk1.setVisibility(View.VISIBLE);
            viewPager.setAllowedSwipeDirection(CustomViewPager.SwipeDirection.right);
            enableBtnNext();
            disableBtnBack();
        }else {
            disableBtnNext();
            disableBtnBack();
            progressBar1.setVisibility(View.VISIBLE);
            iViewOk1.setVisibility(View.INVISIBLE);
            viewPager.setAllowedSwipeDirection(CustomViewPager.SwipeDirection.none);
        }
    }
}