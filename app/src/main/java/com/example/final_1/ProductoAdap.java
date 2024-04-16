package com.example.final_1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProductoAdap extends RecyclerView.Adapter<ProductoAdap.ProductoViewHolder> {

    private Context context;
    private ArrayList<Producto> productoList;

    public ProductoAdap(Context context, ArrayList<Producto> productoList) {
        this.context = context;
        this.productoList = productoList;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.producto_item, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        final Producto producto = productoList.get(position);
        holder.textViewNombre.setText(producto.getNombre());
        holder.textViewCoste.setText(producto.getCoste() + " €");
        holder.textViewTransporte.setText(producto.getCantidad() + " uds");

        // Manejar clic en el botón Eliminar
        holder.buttonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Eliminar producto");
                builder.setMessage("¿Estás seguro de que deseas eliminar este producto?");
                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Eliminar producto de la base de datos
                        DBHelper DBHelper = new DBHelper(context);
                        DBHelper.deleteTask(producto.getId());

                        // Eliminar el producto de la lista y notificar al adaptador
                        productoList.remove(producto);
                        notifyDataSetChanged();

                    }
                });
                builder.setNegativeButton("No", null);
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productoList.size();
    }

    public class ProductoViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombre;
        TextView textViewCoste;
        TextView textViewTransporte;
        Button buttonEliminar;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.textViewProducto);
            textViewCoste = itemView.findViewById(R.id.textViewCoste);
            textViewTransporte = itemView.findViewById(R.id.textViewCant);
            buttonEliminar = itemView.findViewById(R.id.buttonEliminar); // Obtener la referencia al botón eliminar
        }
    }
}
