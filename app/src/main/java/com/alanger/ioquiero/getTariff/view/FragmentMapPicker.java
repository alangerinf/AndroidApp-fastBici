package com.alanger.ioquiero.getTariff.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alanger.ioquiero.Configurations;
import com.alanger.ioquiero.GetPriceQuery;
import com.alanger.ioquiero.R;
import com.alanger.ioquiero.app.AppController;
import com.alanger.ioquiero.directionhelpers.FetchURL;
import com.alanger.ioquiero.fragment.Failed;
import com.alanger.ioquiero.fragment.Success;
import com.alanger.ioquiero.getTariff.models.Place;
import com.alanger.ioquiero.getTariff.view.adapters.RViewAdapterPlace;
import com.alanger.ioquiero.getTariff.view.unused.ActivityShowTariffResult;
import com.alanger.ioquiero.type.CoordinateInput;
import com.alanger.ioquiero.views.ActivityMain;
import com.alanger.ioquiero.volskayaGraphql.GraphqlClient;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.exception.ApolloException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;

/**
 * Created by Administrador on 09/09/2017.
 */
public class FragmentMapPicker extends Fragment implements OnMapReadyCallback, TariffView, GoogleMap.CancelableCallback {

    private static String TAG = FragmentMapPicker.class.getSimpleName();
    private static Polyline currentPolyline;

    private static final int PERMISION_REQUEST_GPS = 100;

    private static boolean isFirstIdle = false;

    private static int STATUS = 0;

    private static Context ctx;
    private static GoogleMap mMap;
    private static Geocoder geocode;
    private static double LatTemp = 0, LonTemp = 0, //ubicacion temporal para algun paso de data

    CurrentLat = 0, CurrentLon = 0,//ubicacion actual
            latStart, lonStart, latFinish, lonFinish; //lo q se seteo


    private static Button btnSetStart, btnSetFinish, btnPedir;
    private static ConstraintLayout clPedir;
    private static ExtendedFloatingActionButton btnRestart;
    private static TextView tViewAddressStart, tViewAddressFinish, tViewMensaje;
    private static TextView tViewPriceEntero;

    public static ConstraintLayout clSearch;
    public static boolean clSearchIsVisible = false;

    private static TextView tViewKilometers, tViewMin, tViewCo2;


    private static Marker markerStart = null, markerFinish = null;
    private static ImageView markerTo, marketFrom;

    private static String direccion, number;

    private static final Handler handler = new Handler();


    private static ActivityMain activityMain;

    private static FloatingActionButton fabDrawer;
    FloatingActionButton fAButtonClearText;
    EditText eTextSearch;

    ProgressBar progress_search;


    List<Place> placeList;
    RViewAdapterPlace rViewAdapterPlace;
    RecyclerView rViewPlaces;


    private FusedLocationProviderClient fusedLocationClient;


    @Override
    public void verifyPermission() {

        Log.d(TAG, "falg1");

        if (ContextCompat.checkSelfPermission(ctx,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            Log.d(TAG, "falg2");
            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                Log.d(TAG, "falg4");

                //Prompt the user once explanation has been shown
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISION_REQUEST_GPS);

            } else {

                Log.d(TAG, "falg5");
                // No explanation needed, we can request the permission.
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISION_REQUEST_GPS);
            }

        } else {

            Log.d(TAG, "falg3");

            LocationManager manager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
            boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (statusOfGPS) {
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                mMap.setMyLocationEnabled(true);
                runnableSetCurrentLocation.run();
            } else {
                showGPSDisabledAlertToUser();
            }


        }

    }

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx, R.style.AlertDialogTheme);
        alertDialogBuilder.setMessage("Su GPS se encuentra desactivado, ¿Desea Activarlo?")
                .setCancelable(false)
                .setPositiveButton("Activar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        verifyPermission();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();

        alert.show();
    }


    @Override
    public void openTariffResult() {
        Intent i = new Intent(ctx, ActivityShowTariffResult.class);
        i.putExtra("latStart", String.valueOf(latStart));
        i.putExtra("lonStart", String.valueOf(lonStart));
        i.putExtra("latFinish", String.valueOf(latFinish));
        i.putExtra("lonFinish", String.valueOf(lonFinish));
        startActivity(i);
    }

    @Override
    public void enableInputs() {
        if (markerStart == null) {
            btnSetStart.setVisibility(View.VISIBLE);
            btnSetStart.setClickable(true);
            btnSetStart.setFocusable(true);
        } else {
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

    public FragmentMapPicker(ActivityMain activityMain) {
        this.activityMain = activityMain;
        activityMain.setTitle("FastBici");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void defaultAttributes() {
        markerTo.setVisibility(View.INVISIBLE);
        marketFrom.setVisibility(View.VISIBLE);


        clSearch.setVisibility(View.INVISIBLE);

        tViewAddressFinish.setAlpha(0.3f);

        handler.post(
                () -> {
                    tViewMensaje.setVisibility(View.VISIBLE);
                    tViewMensaje.setText("Arrastra el mapa y marca el punto de origen");
                    STATUS = 0;
                    tViewAddressStart.setText(getString(R.string.donde_recogemos));
                    tViewAddressFinish.setText(getString(R.string.a_donde_vamos));
                    btnSetStart.setVisibility(View.INVISIBLE);
                    btnSetFinish.setVisibility(View.INVISIBLE);
                    clPedir.setVisibility(View.GONE);
                    btnRestart.setVisibility(View.INVISIBLE);

                    markerStart = null;
                    markerFinish = null;
                    mMap.clear();
                }
        );
    }


    void returnToSetFinish() {
        mMap.clear();

        LatLng posicion = new LatLng(latStart, lonStart);

        CameraPosition cameraPosition = new CameraPosition.Builder().target(posicion).zoom(14).bearing(0).tilt(0).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mMap.animateCamera(cameraUpdate, 500, this);

        tViewMensaje.setVisibility(View.VISIBLE);
        if (markerFinish != null) {
            markerFinish.remove();
        }

        markerTo.setVisibility(View.VISIBLE);
        marketFrom.setVisibility(View.INVISIBLE);


        if (markerStart != null) {
            markerStart.remove();
        }

        LatLng LatLng = new LatLng(latStart, lonStart);

        markerStart = mMap.addMarker(new MarkerOptions()
                .position(LatLng)
                .title("¿Donde Recogemos?")
                .anchor(0.5f, 0.5f)
                .icon(bitmapDescriptorFromVector(0, getContext(), R.drawable.ic_from)));

        markerStart.showInfoWindow();

        tViewMensaje.setText("Arrastra el mapa y marca el punto de Destino");
        //  Toast.makeText(ctx,"AHORA ARRASTRA EL MAPA Y UBICA EL DESTINO",Toast.LENGTH_SHORT).show();
        STATUS = 1;

    }


    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) ctx.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);


    }

    public void showKeyboard(View view) {
        ((InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    private void calculatePrice() {
        if (isConnectedToInternetToUpdate()) {
            new FetchURL(getActivity()).execute(getUrl(markerStart.getPosition(), markerFinish.getPosition(), "walking"), "walking");
            getPriceFromServer(latStart, lonStart, latFinish, lonFinish);
        } else {
            Snackbar snackbar2 = Snackbar.make(root, "No se pudo Conectar", Snackbar.LENGTH_INDEFINITE);
            snackbar2.setAction("Reintentar", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getPriceFromServer(latStart, lonStart, latFinish, lonFinish);
                }
            });
            snackbar2.show();
        }
    }


    Runnable runSearch = new Runnable() {
        @Override
        public void run() {

            progress_search.setVisibility(View.VISIBLE);

            AppController.getInstance().cancelPendingRequests(TAG);


            String text = eTextSearch.getText().toString().replaceAll(" ", "+");
            Log.d(TAG, "buscando direcciones:" + text);

            if (text.length() > 0) {

                fAButtonClearText.setVisibility(View.VISIBLE);
                fAButtonClearText.setClickable(true);
                fAButtonClearText.setFocusable(true);

                URL myURL = null;
                try {
                    myURL = new URL(Configurations.getUrlSearchPlaces(text, String.valueOf(CurrentLat), String.valueOf(CurrentLon)));

                } catch (MalformedURLException e) {
                    Log.d(TAG, e.toString());
                    e.printStackTrace();
                }

                Log.d(TAG, myURL.toString());
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                        myURL.toString(), null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(TAG, response.toString());
                                Log.d(TAG, "***************************************");

                                try {


                                    JSONArray results = response.getJSONArray("results");
                                    for (int i = 0; i < results.length(); i++) {


                                        Place placeTemp = new Place(

                                        );

                                        JSONObject jsonTemp = results.getJSONObject(i);

                                        try {

                                            placeTemp.setLat(jsonTemp.getJSONObject("geometry").getJSONObject("location").getString("lat"));

                                        } catch (Exception e) {

                                        }
                                        try {
                                            placeTemp.setLon(jsonTemp.getJSONObject("geometry").getJSONObject("location").getString("lng"));
                                        } catch (Exception e) {

                                        }

                                        try {
                                            placeTemp.setTypes(jsonTemp.getJSONArray("types"));
                                        } catch (Exception e) {

                                        }
                                        try {
                                            placeTemp.setFormatted_address(jsonTemp.getString("formatted_address"));
                                        } catch (Exception e) {

                                        }


                                        boolean flag = true;

                                        for (int x = 0; x < placeTemp.getTypes().length(); x++) {
                                            if (placeTemp.getTypes().getString(x).equals("country")) {
                                                flag = false;
                                                break;
                                            } else {
                                                Log.d(TAG, "----->cat:" + placeTemp.getTypes().getString(x));
                                            }
                                        }

                                        double maxlat = -7.990756;
                                        double minlat = -8.224768;


                                        double maxlon = -78.896751;
                                        double minlon = -79.192724;

                                        double lat = Double.parseDouble(placeTemp.getLat());
                                        double lon = Double.parseDouble(placeTemp.getLon());

                                        if (maxlat < lat || minlat > lat) {
                                            flag = false;
                                        } else {
                                            if (maxlon < lon || minlon > lon) {
                                                flag = false;
                                            }
                                        }


                                        Log.d(TAG, "********");
                                        if (flag) {
                                            placeList.add(placeTemp);
                                            Log.d(TAG, "\n");
                                            Log.d(TAG, jsonTemp.toString());
                                        }
                                        Log.d(TAG, "********");


                                    }
                                    rViewAdapterPlace.notifyDataSetChanged();
                                    progress_search.setVisibility(View.GONE);
                                    handler.removeCallbacks(runSearch);
                                } catch (JSONException e) {
                                    handler.removeCallbacks(runSearch);
                                    progress_search.setVisibility(View.GONE);
                                    rViewAdapterPlace.notifyDataSetChanged();
                                    Toast.makeText(root.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "JSONException " + e.toString());
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(root.getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onErrorResponse " + error.toString());
                        error.printStackTrace();
                    }
                }
                );
                AppController.getInstance().addToRequestQueue(jsonObjReq);


            } else {
                fAButtonClearText.setVisibility(View.GONE);
            }

        }
    };

    private void showDialogSearch(int mode) {
        clSearchIsVisible = true;
        placeList = new ArrayList<>();
        rViewAdapterPlace = new RViewAdapterPlace(placeList);

        rViewAdapterPlace.setOnClicListener(v -> {
            int pos = rViewPlaces.getChildAdapterPosition(v);
            Place item = placeList.get(pos);
            Toast.makeText(ctx, item.getFormatted_address(), Toast.LENGTH_LONG).show();

            LatLng latLng = new LatLng(Double.valueOf(item.getLat()), Double.valueOf(item.getLon()));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)
                    .zoom(15.0f)
                    .build();
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            clSearch.setVisibility(View.GONE);
            showFabDrawer();


            if (STATUS == 2) { //si ya se registraron las  2 fechas
                clPedir.setVisibility(View.GONE);
                if (mode == 0) {
                    tViewAddressStart.setText(item.getFormatted_address());
                    markerStart.setPosition(latLng);
                    updateLatLngStart(latLng);
                }

                if (mode == 1) {
                    tViewAddressFinish.setText(item.getFormatted_address());
                    markerFinish.setPosition(latLng);
                    updateLatLngFinish(latLng);
                }

                btnSetStart.setVisibility(View.INVISIBLE);
                btnRestart.setVisibility(View.VISIBLE);
                calculatePrice();

            } else {
                handler.postDelayed(
                        () -> {
                            if (mode == 0) {
                                tViewAddressStart.setText(item.getFormatted_address());
                                setStart();
                            } else {
                                tViewAddressFinish.setText(item.getFormatted_address());
                                setFinish();
                            }
                        }, 200

                );

            }
        });


        rViewPlaces.setAdapter(rViewAdapterPlace);


        h.post(new Runnable() {
            @Override
            public void run() {

                clSearch.setVisibility(View.VISIBLE);
                clSearch.setClickable(true);
                clSearch.setFocusable(true);

            }
        });


        eTextSearch.setText("");

        if (mode == 0) {//si es de modificar el toogle inicio
            eTextSearch.setHint("Lugar de Recojo");

        } else {//si se estamodificando el toogle de final
            eTextSearch.setHint("Lugar de Entrega");
        }

        eTextSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                } else {
                    showKeyboard(v);
                }
            }
        });
        eTextSearch.requestFocus();


        fAButtonClearText.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                eTextSearch.setText("");
                fAButtonClearText.setVisibility(View.INVISIBLE);
                eTextSearch.requestFocus();
                showKeyboard(eTextSearch);
                progress_search.setVisibility(View.GONE);
                handler.removeCallbacks(runSearch);
            }
        });


        eTextSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                placeList.clear();
                rViewAdapterPlace.notifyDataSetChanged();
                handler.removeCallbacks(runSearch);

                String text = eTextSearch.getText().toString();


                if (text.equals(" ")) { //si tiene espacio adelante
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            eTextSearch.setText(eTextSearch.getText().toString().trim());
                        }
                    });
                }

                progress_search.setVisibility(View.GONE);
                if (!eTextSearch.getText().toString().equals("")) {
                    handler.postDelayed(
                            runSearch
                            , 1000);

                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        getView().findViewById(R.id.iViewClose).setOnClickListener(v -> {
            showFabDrawer();
            clSearch.setVisibility(View.GONE);
            clSearchIsVisible = false;
        });


    }

    private void updateLatLngStart(LatLng latLng) {
        latStart = latLng.latitude;
        lonStart = latLng.longitude;
    }

    private void updateLatLngFinish(LatLng latLng) {
        latFinish = latLng.latitude;
        lonFinish = latLng.longitude;
    }

    private void setStart() {


        tViewAddressFinish.setAlpha(1);
        tViewAddressFinish.setClickable(true);
        tViewAddressFinish.setFocusable(true);

        if (markerStart != null) {
            markerStart.remove();
        }

        LatLng latLng = new LatLng(
                mMap.getCameraPosition().target.latitude,
                mMap.getCameraPosition().target.longitude
        );
        updateLatLngStart(latLng);

        markerStart = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("¿Donde Recogemos?")
                .anchor(0.5f, 0.5f)
                .icon(bitmapDescriptorFromVector(0, getContext(), R.drawable.ic_from)));
        markerStart.showInfoWindow();

        markerTo.setVisibility(View.VISIBLE);
        marketFrom.setVisibility(View.INVISIBLE);

        btnSetStart.setVisibility(View.INVISIBLE);

        btnRestart.setVisibility(View.VISIBLE);

        tViewMensaje.setText("Arrastra el mapa y marca el punto de Destino");
        //   Toast.makeText(ctx,"AHORA ARRASTRA EL MAPA Y UBICA EL DESTINO",Toast.LENGTH_SHORT).show();
        STATUS = 1;
    }


    void setFinish() {
        LatLng latLngFinish = new LatLng(
                mMap.getCameraPosition().target.latitude,
                mMap.getCameraPosition().target.longitude
        );
        updateLatLngFinish(latLngFinish);
/*
            if(latStart==latFinish && lonStart==tlonFinish){

                Snackbar snackbar = Snackbar.make(root, "Las ubicaciones no pueden ser las mismas", Snackbar.LENGTH_LONG);
                snackbar.show();

                returnToSetFinish();
            }else {
  */
        //  Toast.makeText(ctx,""+(latStart-latFinish)+" "+(lonStart==lonFinish) , Toast.LENGTH_LONG).show();


        // returnToSetFinish();

        tViewMensaje.setVisibility(View.GONE);
        if (markerFinish != null) {
            markerFinish.remove();
        }

        markerFinish = mMap.addMarker(new MarkerOptions()
                .position(latLngFinish)
                .title("¿Donde vamos?")
                .icon(bitmapDescriptorFromVector(1, getContext(), R.drawable.ic_to)));
        markerFinish.showInfoWindow();

        btnSetFinish.setVisibility(View.INVISIBLE);
        //btnPedir.setVisibility(View.VISIBLE);
        //ovualtar los amrkers
        markerTo.setVisibility(View.INVISIBLE);
        marketFrom.setVisibility(View.INVISIBLE);

        STATUS = 2;
        vistaPeriferica();

        if (isConnectedToInternetToUpdate()) {
            new FetchURL(getActivity()).execute(getUrl(markerStart.getPosition(), markerFinish.getPosition(), "walking"), "walking");
            getPriceFromServer(latStart, lonStart, latFinish, lonFinish);
        } else {
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
    }

    private void hideFabDrawer() {
        fabDrawer.setVisibility(View.INVISIBLE);
    }

    private void showFabDrawer() {
        fabDrawer.setVisibility(View.VISIBLE);
    }


    @Override
    public void onResume() {
        super.onResume();
        btnPedir.setClickable(true);
        btnPedir.setFocusable(true);
    }

    private void declareEvents() {


        tViewAddressStart.setOnClickListener(v -> {
            hideFabDrawer();
            showDialogSearch(0);
        });

        tViewAddressFinish.setOnClickListener(v -> {
            hideFabDrawer();
            showDialogSearch(1);
        });

        tViewAddressFinish.setFocusable(false);
        tViewAddressFinish.setClickable(false);

        btnSetStart.setOnClickListener(v -> {

            setStart();

        });
        btnSetFinish.setOnClickListener(v -> {

            setFinish();

        });


        btnPedir.setOnClickListener(v -> {

            Intent i = new Intent(getActivity(), ActivityPedidoDetail.class);
            i.putExtra(ActivityPedidoDetail.EXTRA_LATSTART, latStart);
            i.putExtra(ActivityPedidoDetail.EXTRA_LONSTART, lonStart);
            i.putExtra(ActivityPedidoDetail.EXTRA_LATFINISH, latFinish);
            i.putExtra(ActivityPedidoDetail.EXTRA_LONFINISH, lonFinish);


            i.putExtra(ActivityPedidoDetail.EXTRA_ADDRESSSTART, tViewAddressStart.getText().toString());
            i.putExtra(ActivityPedidoDetail.EXTRA_ADDRESSFINISH, tViewAddressFinish.getText().toString());

            i.putExtra(ActivityPedidoDetail.EXTRA_PRICE, price);
            i.putExtra(ActivityPedidoDetail.EXTRA_CO2, co2);
            i.putExtra(ActivityPedidoDetail.EXTRA_KM, kilometers);
            i.putExtra(ActivityPedidoDetail.EXTRA_TIME, approximateTime);

            startActivity(i);
            // Check if we're running on Android 5.0 or higher
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            } else {
                // Swap without transition
            }

            btnPedir.setClickable(false);
            btnPedir.setFocusable(false);

            /*

            String uriGoogleMaps = "http://maps.google.com/?mode=walking%26saddr=" + latStart + "," + lonStart + "%26daddr=" + latFinish + "," + lonFinish;
            //String uriGoogleMaps = "http://maps.google.com/?mode=walking%26saddr=-8.1158903,-79.0356704%26daddr=-8.1179977,-79.0358920";
            String phone = Configurations.phone;

            Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=" + phone + "&text=" + uriGoogleMaps +
                    "\nHola, FastBici. Quiero un delivery con este recorrido" +
                    "\n*Precio:*" + " " + price +
                    "\n*Co2:*" + " " + co2 +
                    "\n*Tiempo Aprox:*" + " " + approximateTime +
                    ""
            );
            //    uri = Uri.parse("smsto:" + "98*********7");
            Log.d(TAG, "" + uri.toString());
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri.toString()));
            startActivity(myIntent);
            */
        });

        btnRestart.setOnClickListener(v -> {
            defaultAttributes();
            verifyPermission();
        });

    }

    void showSnackBackError(String txt) {
        Snackbar snackbar = Snackbar.make(root, txt, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.redLight));
        TextView tv = (TextView) snackBarView.findViewById(R.id.snackbar_text);
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
    double price;
    double co2;
    double kilometers;
    double approximateTime;
    private void getPriceFromServer(double latStart, double lonStart, double latFinish, double lonFinish) {
        CoordinateInput coordinateStartInput =
                CoordinateInput.builder()
                        .latitude(latStart)
                        .longitude(lonStart)
                        .build();

        CoordinateInput coordinateFinishInput =
                CoordinateInput
                        .builder()
                        .latitude(latFinish)
                        .longitude(lonFinish)
                        .build();

        showProgressDialog();
        GraphqlClient.getMyApolloClient()
                .query(
                        GetPriceQuery
                                .builder()
                                .coordinateStart(coordinateStartInput)
                                .coordinateFinish(coordinateFinishInput)
                                .build()
                )
                .enqueue(new ApolloCall.Callback<GetPriceQuery.Data>() {
                    @Override
                    public void onResponse(@Nonnull com.apollographql.apollo.api.Response<GetPriceQuery.Data> response) {
                        hideProgressDialog();
                        GetPriceQuery.Data data = response.data();


                        final String successCode = "00";
                        final String errorCommonCode = "01";//error comun
                        final String errorSinCoverturaCode = "02";//cuando no se puede llegar al lugar
                        final String errorLimitDistanceCode = "03"; //cuando se exede el limite de  distancia
                        final String errorDistanceZeroCode = "04"; //cuando se exede el limite de  distancia

                        assert data != null;
                        final GetPriceQuery.Value value = data.calculatePriceRoute().value();
                        final GetPriceQuery.VolskayaResponse volskayaResponse = data.calculatePriceRoute().volskayaResponse();
                        final Success success = volskayaResponse.fragments().success();
                        final Failed failed = volskayaResponse.fragments().failed();
                        if (success != null) {
                            h.post(
                                    () -> {
                                        clPedir.setVisibility(View.VISIBLE);
                                        assert value != null;
                                        co2 = value.co2Saved();
                                        kilometers = value.distance();
                                        price = value.price();
                                        approximateTime = value.approximateTime();

                                        String priceText = "S/" +   String.format("%s", price);
                                        tViewPriceEntero.setText(priceText);
                                        tViewKilometers.setText(String.format("%s", kilometers));
                                        tViewCo2.setText(String.format("%s", co2));
                                        tViewMin.setText(String.format("%s", approximateTime));
                                    }
                            );
                        } else {
                            if (failed != null) {
                                switch (failed.responseCode()) {
                                    case errorCommonCode:
                                        h.post(
                                                () -> {
                                                    markerTo.setVisibility(View.VISIBLE);
                                                    Snackbar snackbar = Snackbar.make(root, "Ocurrio un Error Desconocido", Snackbar.LENGTH_LONG);
                                                    snackbar.show();
                                                }
                                        );
                                        break;
                                    case errorSinCoverturaCode:
                                        h.post(
                                                () -> {
                                                    markerTo.setVisibility(View.VISIBLE);
                                                    showSnackBackError("Delivery Fuera de la Cobertura");
                                                    returnToSetFinish();
                                                }
                                        );
                                        break;
                                    case errorLimitDistanceCode:
                                        h.post(
                                                () -> {
                                                    markerTo.setVisibility(View.VISIBLE);
                                                    showSnackBackError("Distancia Máxima Superada");

                                                    returnToSetFinish();
                                                }
                                        );
                                        break;
                                    case errorDistanceZeroCode:
                                        h.post(
                                                () -> {
                                                    markerTo.setVisibility(View.VISIBLE);
                                                    showSnackBackError("No se supero la distancia minima");

                                                    returnToSetFinish();
                                                }
                                        );
                                        break;
                                    default:
                                        markerTo.setVisibility(View.VISIBLE);
                                        Toast.makeText(ctx, "errorCode:" + failed.responseCode(), Toast.LENGTH_LONG).show();
                                        break;
                                }

                            }
                        }
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {
                        hideProgressDialog();
                        h.post(
                                () -> {
                                    Log.d(TAG, "apollo Error: " + e.toString());
                                    showSnackBackError("No se pudo Conectar",
                                            v -> {
                                                markerTo.setVisibility(View.VISIBLE);
                                                getPriceFromServer(latStart, lonStart, latFinish, lonFinish);
                                            },
                                            "Reintentar"
                                    );
                                }
                        );

                    }
                });
    }


    void showSnackBackError(String txt, View.OnClickListener funcion, String action) {
        Snackbar snackbar = Snackbar.make(root, txt, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.redLight));
        TextView tv = (TextView) snackBarView.findViewById(R.id.snackbar_text);
        tv.setTextSize(18f);
        tv.setTextColor(Color.WHITE);
        snackbar.setAction(action, funcion);
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


    public static void drawRoute(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

    public void vistaPeriferica() {

        float bearing = mMap.getCameraPosition().bearing;
        float tilt = mMap.getCameraPosition().tilt;
        Double latMiddle = (latStart + latFinish) / 2.0d;
        Double lonMiddle = (lonStart + lonFinish) / 2.0d;


        Double distanceLat = (latFinish - latStart) > 0 ? latFinish - latStart : latStart - latFinish;
        Double distanceLon = (lonFinish - lonStart) > 0 ? lonFinish - lonStart : lonStart - lonFinish;

        // Double distance = Math.sqrt(Math.pow(distanceLon,2)+Math.pow(distanceLat,2));

        LatLng posicion = new LatLng(latMiddle, lonMiddle);

        CameraPosition cameraPosition = new CameraPosition.Builder().target(posicion).zoom(13).bearing(bearing).tilt(tilt).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mMap.animateCamera(cameraUpdate, 500, this);
    }

    static View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_map_picker, container, false);

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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(ctx);


        geocode = new Geocoder(ctx, Locale.getDefault());


        clSearch = getView().findViewById(R.id.clSearch);


        btnSetStart = (Button) getView().findViewById(R.id.btnSetStart);
        btnSetFinish = (Button) getView().findViewById(R.id.btnSetFinish);
        btnPedir = (Button) getView().findViewById(R.id.btnPedir);
        clPedir = getView().findViewById(R.id.clPedir);

        btnRestart = getView().findViewById(R.id.efabRestar);
        tViewAddressStart = (TextView) getView().findViewById(R.id.tViewAddressStart);
        tViewAddressFinish = (TextView) getView().findViewById(R.id.tViewAddressFinish);
        tViewMensaje = (TextView) getView().findViewById(R.id.tViewMensaje);

        markerTo = getView().findViewById(R.id.markerTo);
        marketFrom = getView().findViewById(R.id.markerFrom);


        tViewKilometers = getView().findViewById(R.id.tViewKilometers);
        tViewMin = getView().findViewById(R.id.tViewMin);
        tViewCo2 = getView().findViewById(R.id.tViewco2);

        tViewPriceEntero = getView().findViewById(R.id.tViewPrecio);

        fabDrawer = (FloatingActionButton) getActivity().findViewById(R.id.fabDrawer);
        progress_search = getView().findViewById(R.id.progress_search);

        fAButtonClearText = getView().findViewById(R.id.fAButtonClearText);
        eTextSearch = getView().findViewById(R.id.eTextSearch);


        rViewPlaces = getView().findViewById(R.id.rViewPlaces);

        declareEvents();
        defaultAttributes();
        // verifyPermission();

    }

    private void positionLastLoc() {
        LatLng center = new LatLng(CurrentLat, CurrentLon);
        CameraPosition cameraPosition;
        cameraPosition = new CameraPosition.Builder().target(center).zoom(16.0f).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mMap.moveCamera(cameraUpdate);
    }

    public void setCurrentLoc(Location loc) {
        CurrentLat = loc.getLatitude();
        CurrentLon = loc.getLongitude();
    }

    Runnable runnableSetCurrentLocation = new Runnable() {
        @Override
        public void run() {
            try {
                Log.d(TAG, "runnableSetCurrentLocation()");
                if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    verifyPermission();
                    return;
                }

                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(getActivity(), (OnSuccessListener<Location>) location -> {
                            // Got last known location. In some rare situations this can be null.

                            if (location != null) {

                                mMap.setMyLocationEnabled(true);
                                setCurrentLoc(location);
                                positionLastLoc();
                                finishMoveMapEvent();
                                handler.removeCallbacks(runnableSetCurrentLocation);
                            } else {
                                handler.postDelayed(runnableSetCurrentLocation, 100);
                            }


                        });


            } catch (Exception e) {
                //algo salio mal
                handler.postDelayed(runnableSetCurrentLocation, 100);
                Toast.makeText(ctx, "salio Mal", Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnableSetCurrentLocation);
    }

    private void posicionarMarker() {

        Log.d(TAG, "posicionarMarker()" + CurrentLat + "," + CurrentLon);
        LatLng posicion = new LatLng(CurrentLat, CurrentLon);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(posicion).zoom(16.0f).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mMap.animateCamera(cameraUpdate, 500, this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        verifyPermission();

        mMap.setOnCameraMoveStartedListener(i -> {
            // btn_pedir.setVisibility(View.INVISIBLE);

            btnSetStart.setVisibility(View.INVISIBLE);
            btnSetFinish.setVisibility(View.INVISIBLE);
            handler.removeCallbacks(runnableSerchAddress);

            Log.d(TAG, "INICIANDO CAMARA MOVE");
        });

        mMap.setOnCameraIdleListener(() -> {
                    finishMoveMapEvent();
                }
        );

    }


    void finishMoveMapEvent() {
        try {

            if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                verifyPermission();
                return;
            }

            double CameraLat = mMap.getCameraPosition().target.latitude;
            double CameraLong = mMap.getCameraPosition().target.longitude;

            Log.d(TAG, "setOnCameraIdleListener() " + CameraLat + " , " + CameraLong);

            Location loc = new Location("provider");

            loc.setLongitude(CameraLong);
            loc.setLatitude(CameraLat);

            updateLocInView(loc);


        } catch (Exception e) {
            Log.d(TAG, "setOnCameraIdleListener()" + e.toString());
        }
        if (!isFirstIdle) {//si es la primera  ves q se inicia el mapa
            isFirstIdle = true;
        } else {//si ya se inicio el mapa hace rato

            tViewAddressStart.setVisibility(View.VISIBLE);
            enableInputs();
        }

        Log.d(TAG, "setOnCameraIdleListener");
    }


    private BitmapDescriptor bitmapDescriptorFromVector(int mode, Context context, @DrawableRes int vectorDrawableResourceId) {
        Drawable vectorDrawable = ResourcesCompat.getDrawable(getResources(), vectorDrawableResourceId, null);
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Toast.makeText(ctx,"PERMISION_REQUEST_GPS:"+requestCode,Toast.LENGTH_LONG).show();
        switch (requestCode) {
            case PERMISION_REQUEST_GPS:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mMap.getUiSettings().setMyLocationButtonEnabled(true);
                    if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    Activity#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for Activity#requestPermissions for more details.
                        return;
                    }
                    mMap.setMyLocationEnabled(true);
                    runnableSetCurrentLocation.run();
                    Toast.makeText(ctx, "permiso concedido", Toast.LENGTH_LONG).show();
                } else {
                    handler.postDelayed(
                            this::verifyPermission
                            , 1000);
                }
                return;

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

    private void updateLocInView(Location loc) {
        String text = ("Buscando tu dirección");
        if (STATUS == 0) {
            tViewAddressStart.setText(text);
        } else {
            if (STATUS == 1) {
                tViewAddressFinish.setText(text);
            }
        }
        LatTemp = loc.getLatitude();
        LonTemp = loc.getLongitude();
        //new AsyncSearchAddress().execute();
        handler.post(runnableSerchAddress);
    }

    private void showProgressDialog() {
        ConstraintLayout cl = getView().findViewById(R.id.progress_dialog);
        h.post(new Runnable() {
            @Override
            public void run() {

                cl.setVisibility(View.VISIBLE);
                cl.setClickable(true);
                cl.setFocusable(true);
            }
        });

    }


    private void hideProgressDialog() {
        ConstraintLayout cl = getView().findViewById(R.id.progress_dialog);


        h.post(new Runnable() {
            @Override
            public void run() {
                cl.setVisibility(View.INVISIBLE);
                cl.setClickable(false);
                cl.setFocusable(false);
            }
        });

    }


    Runnable runnableSerchAddress = new Runnable() {
        @Override
        public void run() {

            int statusTemp = STATUS;

            if (LatTemp != 0 && LonTemp != 0) {

                CurrentLat = LatTemp;
                CurrentLon = LonTemp;
                String apiKey = Configurations.API_KEY;
                String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + LatTemp + "," + LonTemp + "&key=" + apiKey;
                Log.d(TAG, "buscando: " + url);

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

                                if (statusTemp == 0) {

                                    // tViewAddressStart.setText(direccion + " " + number);
                                    tViewAddressStart.setText(zero.getString("formatted_address"));
                                } else {
                                    if (statusTemp == 1) {
//                                                                tViewAddressFinish.setText(direccion + " " + number);
                                        tViewAddressFinish.setText(zero.getString("formatted_address"));
                                    }
                                }

                                if (procede == 1) {
                                    //   btn_pedir.setVisibility(View.VISIBLE);
                                    //    CargarUnidades();

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d(TAG, "jsonE: " + e.toString());
                                //      Toast.makeText(getContext(),"jsonE: "+e.toString(),Toast.LENGTH_LONG).show();
                            }

                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (statusTemp == 0) {
                            tViewAddressStart.setText("Reintentando");
                        } else {
                            if (statusTemp == 1) {
                                tViewAddressFinish.setText("Reintentando");
                            }
                        }
                        verifyPermission();
                        handler.postDelayed(runnableSerchAddress, 2000);


                        Log.d(TAG, "voleyE " + error.toString());
                        //    Toast.makeText(getContext(),"jsonE: "+error.toString(),Toast.LENGTH_LONG).show();
                    }
                });

                AppController.getInstance().addToRequestQueue(sr);
                // }
            }
        }
    };


    class AsyncSearchAddress extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(String... args) {
            if (LatTemp != 0 && LonTemp != 0) {

                CurrentLat = LatTemp;
                CurrentLon = LonTemp;
                String apiKey = Configurations.API_KEY;
                String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + LatTemp + "," + LonTemp + "&key=" + apiKey;
                Log.d(TAG, "buscando: " + url);

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

                                if (STATUS == 0) {
                                    // tViewAddressStart.setText(direccion + " " + number);
                                    tViewAddressStart.setText(zero.getString("formatted_address"));
                                } else {
                                    if (STATUS == 1) {
                                        // tViewAddressFinish.setText(direccion + " " + number);
                                        tViewAddressFinish.setText(zero.getString("formatted_address"));
                                    }
                                }

                                if (procede == 1) {
                                    //   btn_pedir.setVisibility(View.VISIBLE);
                                    //    CargarUnidades();

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d(TAG, "jsonE: " + e.toString());
                                //      Toast.makeText(getContext(),"jsonE: "+e.toString(),Toast.LENGTH_LONG).show();
                            }

                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (STATUS == 0) {
                            tViewAddressStart.setText("No se pudo Obtener");
                        } else {
                            if (STATUS == 1) {
                                tViewAddressFinish.setText("No se pudo Obtener");
                            }
                        }
                        Log.d(TAG, "voleyE " + error.toString());
                        //    Toast.makeText(getContext(),"jsonE: "+error.toString(),Toast.LENGTH_LONG).show();
                    }
                });

                AppController.getInstance().addToRequestQueue(sr);
                // }
            }
            return null;
        }


    }

    @Override
    public void onStart() {
        super.onStart();
        if (mMap != null) {
            verifyPermission();
        }

    }

}
