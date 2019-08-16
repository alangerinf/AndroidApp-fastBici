package com.alanger.ioquiero.getTariff.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.alanger.ioquiero.Configurations;
import com.alanger.ioquiero.GetPrice_Query;
import com.alanger.ioquiero.R;
import com.alanger.ioquiero.app.AppController;
import com.alanger.ioquiero.directionhelpers.FetchURL;
import com.alanger.ioquiero.views.ActivityMain;
import com.alanger.ioquiero.views.Utils;
import com.alanger.ioquiero.volskayaGraphql.GraphqlClient;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.exception.ApolloException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import javax.annotation.Nonnull;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Administrador on 09/09/2017.
 */
public class FragmentTariff extends Fragment implements OnMapReadyCallback, TariffView, GoogleMap.CancelableCallback {

    private static String TAG = FragmentTariff.class.getSimpleName();
    private static Polyline currentPolyline;

    private static final int PERMISION_REQUEST_GPS=100;

    private static boolean isFirstIdle = false;

    private static int STATUS = 0;

    private static Context ctx;
    private static GoogleMap mMap;
    private static Geocoder geocode;
    private static double LatTemp =0, LonTemp =0, CurrentLat =0, CurrentLon =0, latStart, lonStart, latFinish, lonFinish;;

    private static Button btnSetStart, btnSetFinish, btPedir;
    private static ConstraintLayout btnRestart;
    private static TextView tViewAddressStart, tViewAddressFinish, tViewMensaje;
    private static TextView tViewKilometers,tViewPriceEntero,tViewPriceDecimal;




    private static ConstraintLayout clViewKm,clPrecio;

    private static Marker markerStart =null, markerFinish =null;

    private static String direccion, number;

    private static SharedPreferences pref;

    private static final Handler handler = new Handler();

    private static LottieAnimationView lottieMarker;
    private static ConstraintLayout red_pointer;

    private static ActivityMain activityMain;

    @Override
    public void verifyPermission() {
        String[] PERMISSIONS = {
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
        };
        if(
                ContextCompat.checkSelfPermission(ctx,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                ||
                ContextCompat.checkSelfPermission(ctx,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
          ){
                requestPermissions(
                        PERMISSIONS,
                        PERMISION_REQUEST_GPS);
                return;
        }else {
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.setMyLocationEnabled(true);
            runnable.run();
        }

    }

    @Override
    public void openTariffResult() {
        Intent i = new Intent(ctx, ActivityShowTariffResult.class);
        i.putExtra("latStart", String.valueOf(latStart) );
        i.putExtra("lonStart", String.valueOf(lonStart) );
        i.putExtra("latFinish", String.valueOf(latFinish) );
        i.putExtra("lonFinish", String.valueOf(lonFinish) );
        startActivity(i);
    }

    @Override
    public void enableInputs() {
        if(markerStart ==null) {
            btnSetStart.setVisibility(View.VISIBLE);
            btnSetStart.setClickable(true);
            btnSetStart.setFocusable(true);
        }else{
            btnSetFinish.setClickable(true);
            btnSetFinish.setFocusable(true);
            btnSetFinish.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void disableInputs() {
        btnSetStart.setClickable(false);
        btnSetStart.setFocusable(false);
        btnSetFinish.setClickable(false);
        btnSetFinish.setFocusable(false);
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onCancel() {

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public FragmentTariff(ActivityMain activityMain) {
        this.activityMain = activityMain;
        activityMain.setTitle("FastBici");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void defaultAttributes(){

        clViewKm.setVisibility(View.INVISIBLE);
        clPrecio.setVisibility(View.GONE);
        handler.post(
                () -> {
                    tViewMensaje.setVisibility(View.VISIBLE);
                    tViewMensaje.setText("Arrastra el mapa y marca el punto de origen");
                    STATUS=0;
                    tViewAddressStart.setText(getString(R.string.lugar_de_partida));
                    tViewAddressFinish.setText(getString(R.string.lugar_de_llegada));
                    mMap.clear();
                    btnSetStart.setVisibility(View.INVISIBLE);
                    btnSetFinish.setVisibility(View.INVISIBLE);
                    btPedir.setVisibility(View.INVISIBLE);
                    btnRestart.setVisibility(View.INVISIBLE);
                    setVisibleMarker(View.VISIBLE);

                    markerStart = null;
                    markerFinish = null;
                    tViewMensaje.setText("Arrastra el mapa y marca el punto de origen");
                }
        );
    }


    void setVisibleMarker(int opVisible){
        lottieMarker.setVisibility(opVisible);
        red_pointer.setVisibility(opVisible);
    }


    void returnToSetFinish(){
        mMap.clear();

        LatLng posicion = new LatLng(latStart, lonStart);

        CameraPosition cameraPosition = new CameraPosition.Builder().target(posicion).zoom(14).bearing(0).tilt(0).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mMap.animateCamera(cameraUpdate, 500, this);

        tViewMensaje.setVisibility(View.VISIBLE);
        if(markerFinish !=null) {
            markerFinish.remove();
        }

        setVisibleMarker(View.VISIBLE);


        if(markerStart !=null) {
            markerStart.remove();
        }

        LatLng LatLng = new LatLng(latStart, lonStart);

        markerStart = mMap.addMarker(new MarkerOptions()
                .position(LatLng)
                .title("Marker")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_start)));


        tViewMensaje.setText("Arrastra el mapa y marca el punto de Destino");
      //  Toast.makeText(ctx,"AHORA ARRASTRA EL MAPA Y UBICA EL DESTINO",Toast.LENGTH_SHORT).show();
        STATUS=1;

    }

    private void declareEvents(){
        btnSetStart.setOnClickListener(v -> {
            if(markerStart !=null) {
                markerStart.remove();
            }
            latStart = mMap.getCameraPosition().target.latitude;
            lonStart = mMap.getCameraPosition().target.longitude;

            LatLng LatLng = new LatLng(latStart, lonStart);

            markerStart = mMap.addMarker(new MarkerOptions()
                    .position(LatLng)
                    .title("Marker")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_start)));

            btnSetStart.setVisibility(View.INVISIBLE);

            btnRestart.setVisibility(View.VISIBLE);

            tViewMensaje.setText("Arrastra el mapa y marca el punto de Destino");
         //   Toast.makeText(ctx,"AHORA ARRASTRA EL MAPA Y UBICA EL DESTINO",Toast.LENGTH_SHORT).show();
            STATUS=1;
        });
        btnSetFinish.setOnClickListener(v -> {

            latFinish = mMap.getCameraPosition().target.latitude;
            lonFinish = mMap.getCameraPosition().target.longitude;
/*
            if(latStart==latFinish && lonStart==lonFinish){

                Snackbar snackbar = Snackbar.make(root, "Las ubicaciones no pueden ser las mismas", Snackbar.LENGTH_LONG);
                snackbar.show();

                returnToSetFinish();
            }else {
  */
              //  Toast.makeText(ctx,""+(latStart-latFinish)+" "+(lonStart==lonFinish) , Toast.LENGTH_LONG).show();


                returnToSetFinish();

                tViewMensaje.setVisibility(View.GONE);
                if(markerFinish !=null) {
                    markerFinish.remove();
                }

                LatLng LatLngFinish = new LatLng(latFinish, lonFinish);
                markerFinish = mMap.addMarker(new MarkerOptions()
                        .position(LatLngFinish)
                        .title("Marker")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_finish)));
                btnSetFinish.setVisibility(View.INVISIBLE);
                //btPedir.setVisibility(View.VISIBLE);
            setVisibleMarker(View.INVISIBLE);
                STATUS=2;
                vistaPeriferica();

                if(isConnectedToInternetToUpdate()){
                    new FetchURL(getActivity()).execute(getUrl(markerStart.getPosition(), markerFinish.getPosition(), "walking"), "walking");
                    getPriceFromServer(latStart,lonStart,latFinish,lonFinish);
                }else{
                    Snackbar snackbar2 = Snackbar.make(root, "No se pudo Conectar", Snackbar.LENGTH_INDEFINITE);
                    snackbar2.setAction("Reintentar", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getPriceFromServer(latStart, lonStart, latFinish, lonFinish);
                        }
                    });
                    snackbar2.show();
                }
            //}


        });



        btPedir.setOnClickListener(v -> {

            String uriGoogleMaps = "http://maps.google.com/?mode=walking%26saddr="+latStart+","+lonStart+"%26daddr="+latFinish+","+lonFinish;
            //String uriGoogleMaps = "http://maps.google.com/?mode=walking%26saddr=-8.1158903,-79.0356704%26daddr=-8.1179977,-79.0358920";
            String phone = "51973446468";

            Uri uri = Uri.parse("https://api.whatsapp.com/send?phone="+phone+"&text=" + uriGoogleMaps +
                    "\n\nHola FastBici,quiero%20un%20delivery%20con%20este%20recorrido"
            );
            //    uri = Uri.parse("smsto:" + "98*********7");
            Log.d(TAG, "" + uri.toString());
                  Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri.toString()));
                   startActivity(myIntent);
        });

        btnRestart.setOnClickListener(v -> {
            defaultAttributes();
            verifyPermission();
        });

    }

    void showSnackBackError(String txt){
        Snackbar snackbar = Snackbar.make(root, txt, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.redLight));
        TextView tv = (TextView)snackBarView.findViewById(R.id.snackbar_text);
        tv.setTextSize(18f);
        tv.setTextColor(Color.WHITE);
        snackbar.show();
    }


    boolean isConnectedToInternetToUpdate() {
        //verificamos si tenemos internet
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            // Si hay conexión a Internet en este momento
            //Toast.makeText(getBaseContext(),"Conectando...",Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
    }

    private static Handler h = new Handler();

    private void getPriceFromServer(double latStart, double lonStart, double latFinish, double lonFinish) {

        showProgressDialog();

        GraphqlClient.getMyApolloClient()
                .query(
                        GetPrice_Query
                                .builder()
                                .latStart(latStart)
                                .lonStart(lonStart)
                                .latEnd(latFinish)
                                .lonEnd(lonFinish)
                                .build()
                )
                .enqueue(new ApolloCall.Callback<GetPrice_Query.Data>() {

                    @Override
                    public void onResponse(@Nonnull com.apollographql.apollo.api.Response<GetPrice_Query.Data> response) {
                        hideProgressDialog();
                        GetPrice_Query.Data data = response.data();

                        final String successCode = "00";
                        final String errorCommonCode="01";//error comun
                        final String errorSinCoverturaCode="02";//cuadno no se puede llegar al lugar
                        final String errorLimitDistanceCode="03"; //cuando se exede el limite de  distancia
                        final String errorDistanceZeroCode="04"; //cuando se exede el limite de  distancia

                        GetPrice_Query.GetPrice resp =  data.getPrice();

                        GetPrice_Query.VolskayaResponse volskayaResponse = resp.volskayaResponse();

                        switch (volskayaResponse.responseCode()) {
                            case successCode:
                                h.post(
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                clViewKm.setVisibility(View.VISIBLE);
                                                tViewKilometers.setText("" + (float) (resp.distance() / 1000.0) + " km");
                                                //  tViewKilometers.setText(resp.);
                                                //tViewKilometers.setText(""+data.getPrice().);
                                                btPedir.setVisibility(View.VISIBLE);
                                                clPrecio.setVisibility(View.VISIBLE);
                                                tViewPriceEntero.setText("" + resp.price().intValue());
                                            }
                                        }
                                );
                                break;

                            case errorCommonCode:
                                h.post(
                                        new Runnable() {
                                            @Override
                                            public void run() {

                                                Snackbar snackbar = Snackbar.make(root, "Ocurrio un Error Desconocido", Snackbar.LENGTH_LONG);
                                                snackbar.show();
                                            }
                                        }
                                );
                                break;
                            case errorSinCoverturaCode:
                                h.post(
                                        new Runnable() {
                                            @Override
                                            public void run() {

                                                showSnackBackError("Delivery Fuera de la Covertura");

                                                returnToSetFinish();
                                            }
                                        }
                                );
                                break;
                            case errorLimitDistanceCode:
                                h.post(
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                showSnackBackError("Distancia Maxima Superada");

                                                returnToSetFinish();
                                            }
                                        }
                                );
                                break;
                            case errorDistanceZeroCode:
                                h.post(
                                        new Runnable() {
                                            @Override
                                            public void run() {

                                                showSnackBackError("No se supero la distancia minima");

                                                returnToSetFinish();
                                            }
                                        }
                                );
                                break;
                            default:

                                Toast.makeText(ctx,"errorCode:"+volskayaResponse.responseCode(),Toast.LENGTH_LONG).show();
                                break;

                        }



                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {
                      hideProgressDialog();
                        h.post(
                                new Runnable() {
                                    @Override
                                    public void run() {

                                        showSnackBackError("No se pudo Conectar",
                                                new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        getPriceFromServer(latStart, lonStart, latFinish, lonFinish);
                                                    }
                                                },
                                                "Reintentar"
                                                );


                                    }
                                }
                        );

                    }
                });

    }


    void showSnackBackError(String txt,View.OnClickListener funcion,String action){
        Snackbar snackbar = Snackbar.make(root, txt, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.redLight));
        TextView tv = (TextView)snackBarView.findViewById(R.id.snackbar_text);
        tv.setTextSize(18f);
        tv.setTextColor(Color.WHITE);
        snackbar.setAction(action,funcion);
        snackbar.show();
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }
/*
    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }
  */
    public static void drawRoute(Object... values){
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }
    public void vistaPeriferica(){

        float bearing = mMap.getCameraPosition().bearing;
        float tilt = mMap.getCameraPosition().tilt;
        Double latMiddle= (latStart+latFinish)/2.0d;
        Double lonMiddle= (lonStart+lonFinish)/2.0d;


        Double distanceLat = (latFinish-latStart)>0?latFinish-latStart:latStart-latFinish;
        Double distanceLon = (lonFinish-lonStart)>0?lonFinish-lonStart:lonStart-lonFinish;

       // Double distance = Math.sqrt(Math.pow(distanceLon,2)+Math.pow(distanceLat,2));

        LatLng posicion = new LatLng(latMiddle, lonMiddle);

        CameraPosition cameraPosition = new CameraPosition.Builder().target(posicion).zoom(14).bearing(bearing).tilt(tilt).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mMap.animateCamera(cameraUpdate, 500, this);
    }

    static View root;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.tarifario_urbano, container, false);

        //Si usas getActivity estas suponiendo que la vista se buscara en el layout cargado por la Activity.
        //SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);



        return root;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ctx = getContext();
        pref = this.getActivity().getSharedPreferences(Utils.nameSesion, MODE_PRIVATE);
        geocode = new Geocoder(ctx, Locale.getDefault());
        btnSetStart = (Button)getView().findViewById(R.id.btnSetStart);
        btnSetFinish = (Button)getView().findViewById(R.id.btnSetFinish);
        btPedir = (Button)getView().findViewById(R.id.btnPedir);
        btnRestart = getView().findViewById(R.id.btnRestart);
        tViewAddressStart = (TextView)getView().findViewById(R.id.tViewAddressStart);
        tViewAddressFinish = (TextView)getView().findViewById(R.id.tViewAddressFinish);
        tViewMensaje = (TextView)getView().findViewById(R.id.tViewMensaje);
        lottieMarker = getView().findViewById(R.id.lottieMarker);
        red_pointer = getView().findViewById(R.id.red_pointer);

        clViewKm = getView().findViewById(R.id.clViewKm);
        tViewKilometers = getView().findViewById(R.id.tViewKilometers);
        tViewPriceEntero = getView().findViewById(R.id.tViewPriceEntero);
        tViewPriceDecimal = getView().findViewById(R.id.tViewPriceDecimal);
        clPrecio = getView().findViewById(R.id.clPrecio);
        declareEvents();
        defaultAttributes();
       // verifyPermission();

    }
    private void positionLastLoc(){
        LatLng center = new LatLng(CurrentLat, CurrentLon);
        CameraPosition cameraPosition;
        cameraPosition = new CameraPosition.Builder().target(center).zoom(16.0f).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mMap.moveCamera(cameraUpdate);
    }

    public void setCurrentLoc(Location loc){
        CurrentLat = loc.getLatitude();
        CurrentLon = loc.getLongitude();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                Location loc = mMap.getMyLocation();
                if(loc!=null) {
                    setCurrentLoc(loc);
                    if (ActivityCompat.checkSelfPermission(ctx, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mMap.setMyLocationEnabled(true);
                    positionLastLoc();
                    handler.removeCallbacks(runnable);
                }else{
                    handler.postDelayed(runnable, 100);
                }
            }catch (Exception e){
                //algo salio mal
                handler.postDelayed(runnable, 100);
                Toast.makeText(ctx,"salio Mal",Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
    private void posicionarMarker() {
        LatLng posicion = new LatLng(lat, lng);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(posicion).zoom(16.0f).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mMap.animateCamera(cameraUpdate, 500, this);
    }
    Double lat, lng ;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        lat = -8.1190123;
        lng = -79.0362434;
        verifyPermission();

        posicionarMarker();



        mMap.setOnCameraMoveStartedListener(i -> {
            // btn_pedir.setVisibility(View.INVISIBLE);
            
            btnSetStart.setVisibility(View.INVISIBLE);
            btnSetFinish.setVisibility(View.INVISIBLE);
            Log.d(TAG,"INCIANDO CAMARA MOVE");
        });

        mMap.setOnCameraIdleListener(() ->{
                    try {
                        Location loc = mMap.getMyLocation();
                        loc.setLongitude(mMap.getCameraPosition().target.longitude);
                        loc.setLatitude(mMap.getCameraPosition().target.latitude);
                        updateLocInView(loc);
                    }catch (Exception e){

                    }
                    if(!isFirstIdle){//si es la primera  ves q se inicia el mapa
                        isFirstIdle=true;
                    }else {//si ya se inicio el mapa hace rato

                        //tViewAddressStart.setText(direccion);
                        tViewAddressStart.setVisibility(View.VISIBLE);
                        enableInputs();


                    }

                    Log.d(TAG,"setOnCameraIdleListener");
                }
                );


    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        Toast.makeText(ctx,"PERMISION_REQUEST_GPS:"+requestCode,Toast.LENGTH_LONG).show();
        switch (requestCode) {
            case PERMISION_REQUEST_GPS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Log.d(TAG,"permision request noooo ok");
                   verifyPermission();
                }
                Log.d(TAG,"permision request ok");
                defaultAttributes();
                verifyPermission();

                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private OnFragmentInteractionListener mListener;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    private void updateLocInView(Location loc){
        String text = ("Buscando tu dirección");
        if(STATUS==0){
            tViewAddressStart.setText(text);
        }else {
            if(STATUS==1){
                tViewAddressFinish.setText(text);
            }
        }
        LatTemp = loc.getLatitude();
        LonTemp = loc.getLongitude();
        new AsyncSearchAddress().execute();
    }

    private void showProgressDialog(){
        ConstraintLayout cl  = getView().findViewById(R.id.progress_dialog );
        h.post(new Runnable() {
            @Override
            public void run() {

                cl.setVisibility(View.VISIBLE);
                cl.setClickable(true);
                cl.setFocusable(true);
            }
        });

    }
    private void hideProgressDialog(){
        ConstraintLayout cl  = getView().findViewById(R.id.progress_dialog );


        h.post(new Runnable() {
            @Override
            public void run() {
                cl.setVisibility(View.INVISIBLE);
                cl.setClickable(false);
                cl.setFocusable(false);
            }
        });

    }


    class AsyncSearchAddress extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(String... args) {
            if(LatTemp !=0 && LonTemp != 0) {

                CurrentLat = LatTemp;
                CurrentLon = LonTemp;
                String apiKey = Configurations.API_KEY;
                String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + LatTemp + "," + LonTemp + "&key=" + apiKey;
                Log.d(TAG,"buscando: "+url);

                JsonObjectRequest sr = new JsonObjectRequest(url, null,
                        response -> {
                            try {
                                JSONArray results = response.getJSONArray("results");
                                JSONObject zero = results.getJSONObject(0);
                                JSONArray address_components = zero.getJSONArray("address_components");

                                // direccion = Utils.NoPrecisa;
                                int procede = 0;

                                for (int i = 0; i < address_components.length(); i++) {
                                    JSONObject zero2 = address_components.getJSONObject(i);
                                    String long_name = zero2.getString("long_name");
                                    JSONArray mtypes = zero2.getJSONArray("types");
                                    String Type = mtypes.getString(0);

                                    if (Type.equalsIgnoreCase("street_number")) {
                                        number = long_name;
                                    }

                                    if (Type.equalsIgnoreCase("route")) {
                                        direccion = long_name;
                                        procede = 1;
                                    }
                                }

                                if(STATUS==0){
                                    tViewAddressStart.setText(direccion + " " + number);
                                }else {
                                    if(STATUS==1){
                                        tViewAddressFinish.setText(direccion + " " + number);
                                    }
                                }

                                if (procede == 1) {
                                    //   btn_pedir.setVisibility(View.VISIBLE);
                                    //    CargarUnidades();

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d(TAG,"jsonE: "+e.toString());
                          //      Toast.makeText(getContext(),"jsonE: "+e.toString(),Toast.LENGTH_LONG).show();
                            }

                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if(STATUS==0){
                            tViewAddressStart.setText("No se pudo Obtener");
                        }else {
                            if(STATUS==1){
                                tViewAddressFinish.setText("No se pudo Obtener");
                            }
                        }
                        Log.d(TAG,"voleyE "+error.toString());
                    //    Toast.makeText(getContext(),"jsonE: "+error.toString(),Toast.LENGTH_LONG).show();
                    }
                });

                AppController.getInstance().addToRequestQueue(sr);
               // }
            }
            return null;
        }



    }
}
