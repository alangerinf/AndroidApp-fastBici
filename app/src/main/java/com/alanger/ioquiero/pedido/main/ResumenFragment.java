package com.alanger.ioquiero.pedido.main;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.alanger.ioquiero.R;
import com.alanger.ioquiero.getTariff.view.ActivityShowTariffResult;
import com.alanger.ioquiero.models.Pedido;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ResumenFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ResumenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResumenFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private PageViewModel pageViewModel;


    private OnFragmentInteractionListener mListener;

    public ResumenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ResumenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ResumenFragment newInstance(String param1, String param2) {
        ResumenFragment fragment = new ResumenFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    static TextView fpresumen_tViewDesc;
    static TextView fpresumen_tViewClienteName;
    static WebView mWebView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_resumen, container, false);

        fpresumen_tViewDesc = root.findViewById(R.id.fpresumen_tViewDesc);
        fpresumen_tViewClienteName = root.findViewById(R.id.fpresumen_tViewClienteName);
        mWebView = root.findViewById(R.id.fpresumen_webView);

        mWebView.addJavascriptInterface(new WebAppInterface(getContext()), "Android");
        WebSettings ws = mWebView.getSettings();
        ws.setJavaScriptEnabled(true);

        mWebView.setWebChromeClient(new WebChromeClient());

        ws.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        mWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        mWebView.setInitialScale(0);
        ws.setSupportZoom(false);
        ws.setBuiltInZoomControls(false);
        ws.setUseWideViewPort(false);


        pageViewModel.get().observe(this, new Observer<Pedido>() {
            @Override
            public void onChanged(Pedido pedido) {
                fpresumen_tViewDesc.setText(""+pedido.getDescripcion());
                fpresumen_tViewClienteName.setText("Preguntar por "+pedido.getNameCliente());
                LoadMap("-8.1141364","-79.0239832","-8.103876","-79.018018");
            }
        });



        return root;
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
}
