package com.alanger.ioquiero.pedido.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.alanger.ioquiero.R;
import com.alanger.ioquiero.models.Product;

import java.util.List;


public class RViewProductListAdapter
        extends RecyclerView.Adapter<RViewProductListAdapter.ViewHolder>
        implements View.OnClickListener{

    private View.OnClickListener onClickListener;

    private Context ctx;
    private List<Product> productList;

    public RViewProductListAdapter(Context ctx, List<Product> productList) {
        this.productList = productList;
        this.ctx = ctx;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_paso1_product_item,null,false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }


    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Product productTemp = productList.get(position);
        holder.tViewCantidad.setText(""+productTemp.getCant());
        holder.tViewName.setText(""+productTemp.getName());



    }

    private void showDialogMenu(){
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(ctx);
        builderSingle.setIcon(R.mipmap.ic_launcher_round);
        builderSingle.setTitle("Opciones");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_list_item_1);

        arrayAdapter.add("Editar");
        arrayAdapter.add("Eliminar");




        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = arrayAdapter.getItem(which);

            }
        });
        builderSingle.show();

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }



    @Override
    public void onClick(View v) {
        if(onClickListener!=null){
            onClickListener.onClick(v);
        }
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView tViewCantidad;
        AppCompatTextView tViewName;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tViewCantidad = itemView.findViewById(R.id.fp1pi_cant);
            tViewName = itemView.findViewById(R.id.fp1pi_name);


        }
    }
}
