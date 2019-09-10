package com.alanger.ioquiero.pedido.main;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.alanger.ioquiero.R;
import com.alanger.ioquiero.models.Product;
import com.alanger.ioquiero.pedido.PedidoActivity;
import com.alanger.ioquiero.pedido.adapters.RViewProductListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Paso1Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Paso1Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Paso1Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static Context ctx;
 //   private static RecyclerView rView;
 //   private static RViewProductListAdapter adapter;
    private static View fp1_rootView;

 //   private  static FloatingActionButton fp1_addProducts;
    private  static TextInputEditText fp1_tietRefA;
    private  static TextInputEditText fp1_tietDescripcion;
   // private  static TextInputEditText fp1_tietRefB;

    private static List<Product> productList  = new ArrayList<>();

    private static OnFragmentInteractionListener mListener;

    public Paso1Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Paso1Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Paso1Fragment newInstance(String param1, String param2) {
        Paso1Fragment fragment = new Paso1Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        productList =new ArrayList<>();
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
        this.ctx = getContext();

    }

    private void events() {
  /*      fp1_addProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // List<String> pro = new ArrayList<>();
               // pro.add("pro1");
               // pro.add("pro2");
              //  enviar(fp1_tietRefA.getText().toString(),fp1_tietRefB.getText().toString(),pro);
            //
                showDialogProduct(new Product(),true);
            }
        });
*/

    }

    private void declare() {
        //productList = new ArrayList<>();
        fp1_rootView = getView().findViewById(R.id.fp1_rootView);
      //  rView = getView().findViewById(R.id.fp1_rViewProductos);
      //  adapter = new RViewProductListAdapter(getContext(),productList);

      //  new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(rView);
      //  rView.setAdapter(adapter);

      //  fp1_addProducts = getView().findViewById(R.id.fp1_fabAddProducts);
        fp1_tietRefA = getView().findViewById(R.id.fp1_tietRefA);
      //  fp1_tietRefB = getView().findViewById(R.id.fp1_tietRefB);

        fp1_tietDescripcion = getView().findViewById(R.id.fp1_tietDescripcion);
        fp1_tietDescripcion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {



            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("paso1","esc");
                if(PedidoActivity.viewPager.getCurrentItem()==0){
                    enviar(fp1_tietRefA.getText().toString(),fp1_tietDescripcion.getText().toString());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_paso1, container, false);
        return view;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        //Toast.makeText(getContext(),"onViewCreated",Toast.LENGTH_SHORT).show();
        declare();
        events();
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void enviar(String refA, String productos) {
        if (mListener != null) {
            boolean flag = true;


            if(productos.length()==0){
                flag=false;
            }

            mListener.onFragmentInteractionPaso1(flag, refA, productos );
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteractionPaso1(boolean flag, String refA, String productos);

    }
/*
    private void showDialogProduct(Product product, boolean isNew){
        Dialog dialogClose;
        dialogClose = new Dialog(ctx);
        dialogClose.setContentView(R.layout.fragment_paso1_dialog_product);
        Button btnOk = (Button) dialogClose.findViewById(R.id.btnOk);
        Button btnCancel = (Button) dialogClose.findViewById(R.id.btnCancel);
        TextInputEditText fp1_tietCantidad = dialogClose.findViewById(R.id.fp1_tietCantidad);
        TextInputEditText fp1_tietNameProduct = dialogClose.findViewById(R.id.fp1_tietNameProduct);

        fp1_tietNameProduct.setText(""+product.getName());
        fp1_tietCantidad.setText(""+product.getCant());


        fp1_tietCantidad.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if(fp1_tietCantidad.getText().toString().equals("")||(Integer.valueOf(fp1_tietCantidad.getText().toString())==0&&fp1_tietCantidad.getText().toString().length()>1)){
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            fp1_tietCantidad.setText("0");
                            fp1_tietCantidad.setSelection(fp1_tietCantidad.getText().toString().length());
                        }
                    });
                }

                if((!fp1_tietCantidad.getText().toString().equals(""))&&String.valueOf(Integer.valueOf(fp1_tietCantidad.getText().toString())).length()!= fp1_tietCantidad.getText().toString().length() ){
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            fp1_tietCantidad.setText(String.valueOf(Integer.valueOf(fp1_tietCantidad.getText().toString())));
                            fp1_tietCantidad.setSelection(fp1_tietCantidad.getText().toString().length());
                        }
                    });

                }


            }
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {

            }
        });

        btnOk.setOnClickListener(v -> {
            if(Integer.valueOf(fp1_tietCantidad.getText().toString())==0 || fp1_tietNameProduct.getText().toString().equals("")){
                Toast.makeText(ctx,"Campos no Configurados",Toast.LENGTH_SHORT).show();
            }else {
                //insertar

                if(isNew){
                    product.setCant(Integer.valueOf(fp1_tietCantidad.getText().toString()));
                    product.setName(fp1_tietNameProduct.getText().toString());
                    productList.add(product);
                    adapter.notifyDataSetChanged();
                    enviar(fp1_tietRefA.getText().toString(),fp1_tietRefB.getText().toString(),productList);
                    Toast.makeText(ctx,"Se agregaron "+fp1_tietCantidad.getText().toString()+" "+fp1_tietNameProduct.getText().toString(),Toast.LENGTH_LONG).show();

                }else {
                    product.setCant(Integer.valueOf(fp1_tietCantidad.getText().toString()));
                    product.setName(fp1_tietNameProduct.getText().toString());
                    Toast.makeText(ctx,"Se cambio a "+fp1_tietCantidad.getText().toString()+" "+fp1_tietNameProduct.getText().toString(),Toast.LENGTH_LONG).show();

                }

                dialogClose.dismiss();
            }
        });

        btnCancel.setOnClickListener(v->{
            dialogClose.dismiss();
        });

        dialogClose.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogClose.show();
    }
    */

/*
    ItemTouchHelper.SimpleCallback itemTouchHelperCallBack = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final Product item = productList.remove(viewHolder.getAdapterPosition());
            final int index = viewHolder.getAdapterPosition();
            adapter.notifyDataSetChanged();
            enviar(fp1_tietRefA.getText().toString(),fp1_tietRefB.getText().toString(),productList);
            Snackbar snackbar = Snackbar.make(fp1_rootView,"Se Borró "+item.getCant()+" "+item.getName(),Snackbar.LENGTH_LONG);
            snackbar.setAction("Deshacer", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productList.add(index,item);
                    adapter.notifyDataSetChanged();
                    enviar(fp1_tietRefA.getText().toString(),fp1_tietRefB.getText().toString(),productList);
                }
            });
            snackbar.show();
        }
    };
*/
}
