package com.alanger.ioquiero.pedido.main;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alanger.ioquiero.R;
import com.google.android.material.textfield.TextInputEditText;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Paso2Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Paso2Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Paso2Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    TextInputEditText fp2_tietNombre;
    TextInputEditText fp2_tietTelefono;
    TextInputEditText fp2_tietRefB;
   // TextInputEditText fp2_tietDescripcion;



    public Paso2Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Paso2Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Paso2Fragment newInstance(String param1, String param2) {
        Paso2Fragment fragment = new Paso2Fragment();
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

        declare();
        events();
    }

    private void events() {
        fp2_tietNombre.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if(!s.equals("") ) {
                    onButtonPressed();
                }
            }



            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {

            }
        });

        fp2_tietTelefono.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if(!s.equals("") ) {
                    onButtonPressed();
                }
            }



            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {

            }
        });

        /*
        fp2_tietDescripcion.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if(!s.equals("") ) {
                    onButtonPressed();
                }
            }



            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {

            }
        });
        */
    }

    private void declare() {
        fp2_tietNombre = getView().findViewById(R.id.fp2_tietNombre);
        fp2_tietTelefono = getView().findViewById(R.id.fp2_tietTelefono);
        fp2_tietRefB = getView().findViewById(R.id.fp2_tietRefB);
       // fp2_tietDescripcion = getView().findViewById(R.id.fp2_tietDescripcion);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_paso2, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {
        if (mListener != null) {
            String nombre = fp2_tietNombre.getText().toString();
            String telefono = fp2_tietTelefono.getText().toString();
            String refB  = fp2_tietRefB.getText().toString();
           // String descripcion = fp2_tietDescripcion.getText().toString();

            boolean flag = true;

            if(nombre.equals("")||telefono.equals("")){
                flag= false;
            }
            mListener.onFragmentInteractionPaso2(flag,nombre,telefono,refB);
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
        void onFragmentInteractionPaso2(boolean flag, String nombre,String telefono,String refB);
    }
}
