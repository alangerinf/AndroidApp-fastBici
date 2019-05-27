package com.alanger.ioquiero.getTariff.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alanger.ioquiero.R;
import com.alanger.ioquiero.app.AppController;
import com.alanger.ioquiero.views.ActivityMain;
import com.alanger.ioquiero.views.Configuracion;
import com.alanger.ioquiero.views.Utils;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Administrador on 09/09/2017.
 */
@SuppressLint("ValidFragment")
public class FragmentTariff extends Fragment implements OnMapReadyCallback, TariffView {

    static private String TAG = FragmentTariff.class.getSimpleName();

    private boolean isFirstIdle = false;

    private Context ctx;
    private GoogleMap mMap;
    private Geocoder geocode;
    private double LatTemp =0, LonTemp =0, CurrentLat =0, CurrentLon =0, latStart, lonStart, latFinish, lonFinish;;

    private Button btnFinishStep1, btnCalcPrice;
    private ConstraintLayout btnRestart;
    private TextView tViewAddress,txt_mensaje;
    private Marker markerStart =null, markerFinish =null;

    private String direccion, number;

    private SharedPreferences pref;

    private final Handler handler = new Handler();

    private ActivityMain activityMain;

    @Override
    public void verifyPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //si el permiso no está habilitado, solicitamos el permiso
            ActivityCompat.requestPermissions(activityMain,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        mMap.setMyLocationEnabled(true);

        runnable.run();
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
            btnFinishStep1.setVisibility(View.VISIBLE);
            btnFinishStep1.setClickable(true);
            btnFinishStep1.setFocusable(true);
        }else{
            btnCalcPrice.setClickable(true);
            btnCalcPrice.setFocusable(true);
            btnCalcPrice.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void disableInputs() {
        btnFinishStep1.setClickable(false);
        btnFinishStep1.setFocusable(false);
        btnCalcPrice.setClickable(false);
        btnCalcPrice.setFocusable(false);
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public FragmentTariff(ActivityMain activityMain) {
        this.activityMain = activityMain;
        activityMain.setTitle("Tarifario");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    private void declaration(){

        ctx = getContext();
        pref = this.getActivity().getSharedPreferences(Utils.nameSesion, MODE_PRIVATE);
        geocode = new Geocoder(ctx, Locale.getDefault());
        btnFinishStep1 = (Button)getView().findViewById(R.id.btnFinishStep1);
        btnCalcPrice = (Button)getView().findViewById(R.id.btn2);
        btnRestart = getView().findViewById(R.id.btnRestart);
        tViewAddress = (TextView)getView().findViewById(R.id.txt_direccion);
        txt_mensaje = (TextView)getView().findViewById(R.id.txt_mensaje);

        txt_mensaje.setText("Arrastra el mapa y marca el punto de origen");

    }
    private void defaultAttributes(){
        handler.post(
                () -> {
                    mMap.clear();
                    btnFinishStep1.setVisibility(View.INVISIBLE);
                    btnCalcPrice.setVisibility(View.INVISIBLE);
                    btnRestart.setVisibility(View.INVISIBLE);

                    markerStart = null;
                    markerFinish = null;

                    txt_mensaje.setText("Arrastra el mapa y marca el punto de origen");
                }
        );
    }

    private void declareEvents(){
        btnFinishStep1.setOnClickListener(v -> {
            if(markerStart !=null) {
                markerStart.remove();
            }
            latStart = mMap.getCameraPosition().target.latitude;
            lonStart = mMap.getCameraPosition().target.longitude;

            LatLng LatLng = new LatLng(latStart, lonStart);

            markerStart = mMap.addMarker(new MarkerOptions()
                    .position(LatLng)
                    .title("Marker")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_cliente)));

            btnFinishStep1.setVisibility(View.INVISIBLE);

            btnRestart.setVisibility(View.VISIBLE);

            txt_mensaje.setText("Arrastra el mapa y marca el punto de Destino");
            Toast.makeText(ctx,"AHORA ARRASTRA EL MAPA Y UBICA EL DESTINO",Toast.LENGTH_SHORT).show();
        });
        btnCalcPrice.setOnClickListener(v -> {
            if(markerFinish !=null) {
                markerFinish.remove();
            }
            latFinish = mMap.getCameraPosition().target.latitude;
            lonFinish = mMap.getCameraPosition().target.longitude;

            LatLng LatLngFinish = new LatLng(latFinish, lonFinish);
            markerFinish = mMap.addMarker(new MarkerOptions()
                    .position(LatLngFinish)
                    .title("Marker")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_conductor)));

            openTariffResult();

        });

        btnRestart.setOnClickListener(v -> {
            defaultAttributes();
            verifyPermission();
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        declaration();
        declareEvents();
        defaultAttributes();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.tarifario_urbano, container, false);

        //Si usas getActivity estas suponiendo que la vista se buscara en el layout cargado por la Activity.
        //SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        return rootView;
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
    
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition loc) {
                if(loc!=null) {
                    Location location = new Location("googleProvider");
                    location.setLatitude(loc.target.latitude);
                    location.setLongitude(loc.target.longitude);
                    updateLocInView(location);
                }
            }
        });
        mMap.setOnCameraMoveStartedListener(i -> {
            // btn_pedir.setVisibility(View.INVISIBLE);
            
            btnFinishStep1.setVisibility(View.INVISIBLE);
            btnCalcPrice.setVisibility(View.INVISIBLE);
            Log.d(TAG,"INCIANDO CAMARA MOVE");
        });

        mMap.setOnCameraIdleListener(() ->{

                    if(!isFirstIdle){//si es la primera  ves q se inicia el mapa
                        isFirstIdle=true;
                    }else {//si ya se inicio el mapa hace rato

                        //tViewAddress.setText(direccion);
                        tViewAddress.setVisibility(View.VISIBLE);
                        enableInputs();
                    }

                    Log.d(TAG,"setOnCameraIdleListener");
                }
                );
        verifyPermission();
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
        tViewAddress.setText("Buscando tu dirección");
        LatTemp = loc.getLatitude();
        LonTemp = loc.getLongitude();
        new AsyncSearchAddress().execute();
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
                String apiKey = Configuracion.API_KEY;
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

                                tViewAddress.setText(direccion + " " + number);
                                if (procede == 1) {
                                    //   btn_pedir.setVisibility(View.VISIBLE);
                                    //    CargarUnidades();

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d(TAG,"jsonE: "+e.toString());
                                Toast.makeText(getContext(),"jsonE: "+e.toString(),Toast.LENGTH_LONG).show();
                            }

                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        tViewAddress.setText("Utils.NoPrecisa");
                        Log.d(TAG,"voleyE "+error.toString());
                        Toast.makeText(getContext(),"jsonE: "+error.toString(),Toast.LENGTH_LONG).show();
                    }
                });

                AppController.getInstance().addToRequestQueue(sr);
               // }
            }
            return null;
        }



    }
}
