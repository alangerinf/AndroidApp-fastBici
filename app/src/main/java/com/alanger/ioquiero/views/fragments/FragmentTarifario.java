package com.alanger.ioquiero.views.fragments;

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
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alanger.ioquiero.R;
import com.alanger.ioquiero.views.ActivityMain;
import com.alanger.ioquiero.views.Utils;
import com.alanger.ioquiero.views.tarifario_urbano_precio;
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

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Administrador on 09/09/2017.
 */
@SuppressLint("ValidFragment")
public class FragmentTarifario extends Fragment implements OnMapReadyCallback {
    Context ctx;
    private GoogleMap mMap;
    Geocoder geocode;
    double lat=0,lng=0,latActual=0, lngActual=0,lat1,lng1,lat2,lng2;;
    private CheckBox chk_corp;
    Button btn1,btn2;
    ImageButton btn_cancelar;
    TextView txt_direccion,txt_mensaje;
    Marker m1=null,m2=null;

    String direccion;
    int exito=0;

    SharedPreferences pref;

    final Handler handler = new Handler();


    ActivityMain activityMain;
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public FragmentTarifario(ActivityMain activityMain) {
        this.activityMain = activityMain;
        activityMain.setTitle("Tarifario");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        ctx = getContext();
        pref = this.getActivity().getSharedPreferences(Utils.nameSesion, MODE_PRIVATE);
        geocode = new Geocoder(ctx, Locale.getDefault());
        chk_corp = (CheckBox)getView().findViewById(R.id.chk_corp);
        btn1 = (Button)getView().findViewById(R.id.btn1);
        btn2 = (Button)getView().findViewById(R.id.btn2);
        btn_cancelar = (ImageButton) getView().findViewById(R.id.btnclear);

        txt_direccion = (TextView)getView().findViewById(R.id.txt_direccion);

        txt_mensaje = (TextView)getView().findViewById(R.id.txt_mensaje);
        txt_mensaje.setText("Arrastra el mapa y marca el punto de origen");

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(m1!=null) {
                    m1.remove();
                }
                lat1 = mMap.getCameraPosition().target.latitude;
                lng1 = mMap.getCameraPosition().target.longitude;

                LatLng LatLng = new LatLng(lat1, lng1);

                m1 = mMap.addMarker(new MarkerOptions()
                        .position(LatLng)
                        .title("Marker")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_cliente)));

                btn1.setVisibility(View.INVISIBLE);

                btn_cancelar.setVisibility(View.VISIBLE);

                txt_mensaje.setText("Arrastra el mapa y marca el punto de Destino");
                Toast.makeText(ctx,"AHORA ARRASTRA EL MAPA Y UBICA EL DESTINO",Toast.LENGTH_SHORT).show();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(m2!=null) {
                    m2.remove();
                }
                lat2 = mMap.getCameraPosition().target.latitude;
                lng2 = mMap.getCameraPosition().target.longitude;

                LatLng LatLng2 = new LatLng(lat2, lng2);
                m2 = mMap.addMarker(new MarkerOptions()
                        .position(LatLng2)
                        .title("Marker")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_conductor)));

                String corp = "no";
                if(chk_corp.isChecked()){
                    corp = "si";
                }
                Intent i = new Intent(ctx, tarifario_urbano_precio.class);
                i.putExtra("lat1", String.valueOf(lat1) );
                i.putExtra("lng1", String.valueOf(lng1) );
                i.putExtra("lat2", String.valueOf(lat2) );
                i.putExtra("lng2", String.valueOf(lng2) );
                i.putExtra("corp", corp );
                startActivity(i);
            }
        });

        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                btn1.setVisibility(View.VISIBLE);
                btn2.setVisibility(View.INVISIBLE);

                btn_cancelar.setVisibility(View.INVISIBLE);

                m1 = null;
                m2 = null;

                txt_mensaje.setText("Arrastra el mapa y marca el punto de origen");

                VerificandoPermiso();
            }
        });

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


    private void posicionActual(){
        LatLng center = new LatLng(latActual,lngActual);
        CameraPosition cameraPosition;
        cameraPosition = new CameraPosition.Builder().target(center).zoom(16.0f).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mMap.moveCamera(cameraUpdate);
    }

    private void VerificandoPermiso() {
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //si el permiso no está habilitado, solicitamos el permiso
            ActivityCompat.requestPermissions(activityMain,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        mMap.setMyLocationEnabled(true);

        runnable.run();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                Location loc = mMap.getMyLocation();
                if(loc!=null) {
                    latActual = loc.getLatitude();
                    lngActual = loc.getLongitude();

                    if (ActivityCompat.checkSelfPermission(ctx, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mMap.setMyLocationEnabled(false);

                    posicionActual();

                    handler.removeCallbacks(runnable);
                }else{
                    handler.postDelayed(runnable, 100);
                }
            }catch (Exception e){
                //algo salio mal
                handler.postDelayed(runnable, 100);
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
                    isDrag(loc.target.latitude, loc.target.longitude);
                }
            }
        });

        VerificandoPermiso();
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

    private void isDrag(double cor_lat, double cor_lng){
        txt_direccion.setText("Buscando tu dirección");
        lat = cor_lat;
        lng = cor_lng;
        new BuscarDireccion().execute();
    }

    class BuscarDireccion extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            exito=0;
            btn1.setVisibility(View.INVISIBLE);
            btn2.setVisibility(View.INVISIBLE);
        }

        protected String doInBackground(String... args) {
            if(lat!=0 && lng != 0) {
                if(latActual != lat & lngActual != lng) {

                    latActual = lat;
                    lngActual = lng;
                    Address ad;
                    try {
                        List<Address> addresses = geocode.getFromLocation(lat, lng, 1);
                        ad = addresses.get(0);
                        //direccion = ad.getThoroughfare();
                        direccion = ad.getAddressLine(0);
                        exito=1;

                    } catch (IOException e) {
                        direccion = "Problemas con el internet...";
                    }
                }
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            txt_direccion.setText(direccion);
            if(exito==1){
                if(m1==null) {
                    btn1.setVisibility(View.VISIBLE);
                }else{
                    btn2.setVisibility(View.VISIBLE);
                }
            }
        }
/*
        public int getR(float ini, float fin,int val){
            int total =255;
            float rango = fin-ini;//100
            int rangoColor=1024;//valores distintos
            int valorParseado= ((val*rangoColor))/((int)rango);
            int iteracion = (int) valorParseado/total;

            int resp;

            switch (iteracion){
                default:
                    resp=0;

                    break;
                case 1:

                    resp =  255-((valorParseado) %255);
                    break;

            }
            return resp;
        }
        public int getG(float ini, float fin,int val){
            int total =255;
            float rango = fin-ini;//100
            int rangoColor=1024;//valores distintos
            int valorParseado= ((val*rangoColor))/((int)rango);
            int iteracion = (int) valorParseado/total;

            int resp;

            switch (iteracion){
                default:
                    resp=255;
                    break;
                case 0:
                    resp = ((valorParseado) %total);
                    break;
                case 3:
                    resp=255;
                    resp = resp - ((valorParseado) %total);
                    break;
            }
            return resp;
        }
        public int getB(float ini, float fin,int val){
            int total =255;
            float rango = fin-ini;//100
            int rangoColor=1024;//valores distintos
            int valorParseado= ((val*rangoColor))/((int)rango);
            int iteracion = (int) valorParseado/total;

            int resp;

            switch (iteracion){
                default:
                    resp=0;
                    break;
                case 2:
                    resp = ((valorParseado) %total);
                    break;
                case 3:
                    resp=255;

                    break;
            }
            return resp;
        }
        */
    }
}
