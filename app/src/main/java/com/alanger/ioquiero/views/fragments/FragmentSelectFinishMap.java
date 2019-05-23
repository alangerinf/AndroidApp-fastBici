package com.alanger.ioquiero.views.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alanger.ioquiero.R;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.alanger.ioquiero.app.AppController;
import com.alanger.ioquiero.views.ActivityGoalDetail;
import com.alanger.ioquiero.views.ActivityMain;
import com.alanger.ioquiero.views.Configuracion;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentSelectFinishMap.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentSelectFinishMap#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressLint("ValidFragment")
public class FragmentSelectFinishMap extends Fragment implements OnMapReadyCallback, GoogleMap.CancelableCallback {
    // TODO: Rename parameter arguments, choose names that match

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private GoogleMap mMap;
    private MapView mapView;


    static ConstraintLayout cLayoutDataPicker;

    static ConstraintLayout cLBtnContinuar;
    static FloatingActionButton fab;

    private OnFragmentInteractionListener mListener;

    ActivityMain activityMain;


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

    @SuppressLint("ValidFragment")
    public FragmentSelectFinishMap(ActivityMain activityMain) {
        this.activityMain = activityMain;
        activityMain.setTitle("FastBici");
    }

    String TAG = FragmentSelectFinishMap.class.getSimpleName();
    double lat, lng, latInicio, lngInicio;
    String direccion, numero;
    final Handler handler = new Handler();
    TextView txt_direccion;
    ImageView marker_pedir;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentSelectFinishMap.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentSelectFinishMap newInstance(ActivityMain activityMain, String param1, String param2) {
        FragmentSelectFinishMap fragment = new FragmentSelectFinishMap(activityMain);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        txt_direccion = (TextView) getView().findViewById(R.id.tViewLlegada);
        txt_direccion.setText("hola");
        cLayoutDataPicker = (ConstraintLayout) getView().findViewById(R.id.cLayoutDataPicker);
        cLayoutDataPicker.setVisibility(View.INVISIBLE);

        mostrarDataPicker();

        cLBtnContinuar = getView().findViewById(R.id.cLBtnContinuar);
        final Animation animBtn =
                android.view.animation.AnimationUtils.loadAnimation(getContext(), R.anim.press_btn);

        fab = getView().findViewById(R.id.floatingActionButton2);

        fab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.startAnimation(animBtn);
                       // activityMain.startActivityForResult(new Intent(activityMain,SearchSite.class),111);
                        Toast.makeText(getContext(), "click", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        cLBtnContinuar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        // Toast.makeText(getContext(),"click",Toast.LENGTH_SHORT).show();

                        Handler handler = new Handler();
                        handler.post(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        v.startAnimation(animBtn);
                                    }
                                }
                        );
                        ocultarDataPicker();
                        handler.postDelayed(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        openDetail();
                                    }
                                },180
                        );
                    }
                }
        );
    }

    private void mostrarDataPicker(){
        final Animation animLeftIn =
                android.view.animation.AnimationUtils.loadAnimation(getContext(), R.anim.left_in);
        final Animation animLeftOut =
                android.view.animation.AnimationUtils.loadAnimation(getContext(), R.anim.left_out);

        Handler handler = new Handler();
        handler.post(
                new Runnable() {
                    @Override
                    public void run() {
                        cLayoutDataPicker.setVisibility(View.VISIBLE);
                        cLayoutDataPicker.startAnimation(animLeftIn);
                    }
                }
        );
    }

    private void ocultarDataPicker(){
        final Animation animLeftIn =
                android.view.animation.AnimationUtils.loadAnimation(getContext(), R.anim.left_in);
        final Animation animLeftOut =
                android.view.animation.AnimationUtils.loadAnimation(getContext(), R.anim.left_out);
        cLayoutDataPicker.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.post(
                new Runnable() {
                    @Override
                    public void run() {
                        cLayoutDataPicker.startAnimation(animLeftOut);
                        cLayoutDataPicker.setVisibility(View.INVISIBLE);
                    }
                }
        );
    }

    void openDetail() {
        Intent intent = new Intent(getContext(), ActivityGoalDetail.class);
        startActivity(intent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            activityMain.overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_back_out);
        else {
            // Swap without transition
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_maps, container, false);

        //Si usas getActivity estas suponiendo que la vista se buscara en el layout cargado por la Activity.
        //SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        return rootView;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onCancel() {

    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    int LOCATION_REQUEST_CODE = 678;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "ONMAP READY");

/*

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getContext(), R.raw.mapstyle_night));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
*/
        mMap = googleMap;

        lat = -8.1190123;
        lng = -79.0362434;
        posicionarMarker();

        VerificandoPermiso();
        Log.d(TAG, "INCIANDO CAMARA");


        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Mostrar diálogo explicativo
            } else {
                // Solicitar permiso
                ActivityCompat.requestPermissions(
                        getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_REQUEST_CODE);
            }
        }


        mMap.getUiSettings().setMyLocationButtonEnabled(true);

// Marcadores

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                try {
                    if (mMap.getCameraPosition().target != null) {
                        isDrag(mMap.getCameraPosition().target.latitude, mMap.getCameraPosition().target.longitude);

                    } else {
                        txt_direccion.setText("Utils.NoPrecisa");
                    }
                } catch (Exception e) {
                    //algo salio mal
                    txt_direccion.setText("Utils.NoPrecisa");
                }
            }
        });


        mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
               // btn_pedir.setVisibility(View.INVISIBLE);
                ocultarDataPicker();
                txt_direccion.setText("Utils.BuscandoDireccion");
                Log.d(TAG,"INCIANDO CAMARA MOVE");
            }
        });
        mMap.setOnCameraMoveCanceledListener(new GoogleMap.OnCameraMoveCanceledListener() {
            @Override
            public void onCameraMoveCanceled() {
                txt_direccion.setText("Utils.termino moverse");
                Log.d(TAG,"INCIANDO CAMARA CANCEL");
            }
        });

    }
    private void isDrag(double cor_lat, double cor_lng) {
        lat = cor_lat;
        lng = cor_lng;
        BuscarDireccion();
    }
    private void posicionarMarker() {
        LatLng posicion = new LatLng(lat, lng);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(posicion).zoom(16.0f).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mMap.animateCamera(cameraUpdate, 500, this);
    }

    private void VerificandoPermiso() {
        Toast.makeText(activityMain,"permiso",Toast.LENGTH_SHORT).show();
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //si el permiso no está habilitado, solicitamos el permiso
            ActivityCompat.requestPermissions(activityMain,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        mMap.setMyLocationEnabled(true);

        runnableMyLocation.run();
    }
    Runnable runnableMyLocation = new Runnable() {
        @Override
        public void run() {
            try {
                Location loc = mMap.getMyLocation();
                if (loc != null) {
                    lat = loc.getLatitude();
                    lng = loc.getLongitude();

                    latInicio = lat;
                    lngInicio = lng;

                    Log.d("MAPS: ","lat: "+latInicio);
                    Log.d("MAPS: ","lon: "+lngInicio);


                    if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mMap.setMyLocationEnabled(true);

                    posicionarMarker();

                    txt_direccion.setText("Utils.BuscandoDireccion");

                    BuscarDireccion();
               //     runnable_coor.run();

                    handler.removeCallbacks(runnableMyLocation);
                } else {
                    handler.postDelayed(runnableMyLocation, 100);
                }
            } catch (Exception e) {
                //algo salio mal
                handler.postDelayed(runnableMyLocation, 100);
            }
        }
    };




    private void BuscarDireccion() {

        String apiKey = Configuracion.API_KEY;

        String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lng + "&key=" + apiKey;
        Log.d(TAG,"buscando: "+url);
        mostrarDataPicker();
        JsonObjectRequest sr = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
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
                                    numero = long_name;
                                }

                                if (Type.equalsIgnoreCase("route")) {
                                    direccion = long_name;
                                    procede = 1;
                                }
                            }

                            txt_direccion.setText(direccion + " " + numero);
                            if (procede == 1) {
                             //   btn_pedir.setVisibility(View.VISIBLE);
                            //    CargarUnidades();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG,"jsonE: "+e.toString());
                            Toast.makeText(getContext(),"jsonE: "+e.toString(),Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                txt_direccion.setText("Utils.NoPrecisa");
                Log.d(TAG,"voleyE "+error.toString());
                Toast.makeText(getContext(),"jsonE: "+error.toString(),Toast.LENGTH_LONG).show();
            }
        });

        AppController.getInstance().addToRequestQueue(sr);
    }



}
