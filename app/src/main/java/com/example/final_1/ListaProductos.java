package com.example.final_1;
import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListaProductos extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductoAdap adapter;
    private ArrayList<Producto> productList;
    private static final String CHANNEL_ID = "ProductChannel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_productos);

        productList = new ArrayList<>(); // Inicializar lista de productos

        // Configurar el RecyclerView
        recyclerView = findViewById(R.id.recyclerViewProductos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductoAdap(this, productList);
        recyclerView.setAdapter(adapter);

        // Configurar el botón de añadir
        Button buttonAdd = findViewById(R.id.buttonAnadir);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoAñadir();
            }
        });

        // Cargar prodcuto existentes al iniciar la actividad
        cargarProductos();

    }

    private void mostrarDialogoAñadir() {
        // Inflar el diseño del diálogo personalizado
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.diag_anadir_producto, null);

        // Obtener referencias a los elementos del diálogo
        final EditText editTextProducto = dialogView.findViewById(R.id.editTextProducto);
        final EditText editTextCoste = dialogView.findViewById(R.id.editTextCoste);
        final EditText editTextCant = dialogView.findViewById(R.id.editTextCant);

        // Crear el diálogo de alerta
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setTitle(getString(R.string.id_añadir_producto));
        // Acceder al texto "Cancelar" desde los recursos de cadenas
        String textoAñadir = getResources().getString(R.string.id_anadir);
        builder.setPositiveButton(textoAñadir, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Obtener los datos ingresados por el usuario
                String n_Nombre = editTextProducto.getText().toString();
                String n_Coste = editTextCoste.getText().toString();
                int n_Cant = Integer.parseInt(editTextCant.getText().toString());

                long n_ProductoId = System.currentTimeMillis();

                // Crear un nuevo objeto producto con los datos ingresados
                Producto nuevoProducto = new Producto(n_ProductoId, n_Nombre, n_Coste, n_Cant);

                // Añadir el nuevo producto a la lista
                productList.add(nuevoProducto);

                // Notificar al adaptador del cambio en los datos
                adapter.notifyDataSetChanged();

                // Agregar el nuevo producto a la base de datos
                DBHelper DBHelper = new DBHelper(ListaProductos.this);
                long id = DBHelper.nuevoProducto(n_Nombre, String.valueOf(n_Coste), n_Cant);

                // Mostrar la notificación
                showNotification();
            }
        });
        // Acceder al texto "Cancelar" desde los recursos de cadenas
        String textoCancelar = getResources().getString(R.string.id_cancelar);

        builder.setNegativeButton(textoCancelar, null);

        // Mostrar el diálogo
        builder.create().show();
    }

    private void showNotification() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 11);
        } else {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.icono)
                    .setContentTitle("Nuevo producto añadido a tu carrito")
                    .setContentText("¡Añade todos los productos que necesites para que no se te olvide nada!")
                    .setVibrate(new long[]{0, 1000, 500, 1000})
                    .setAutoCancel(true);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Productos", NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }
            notificationManager.notify(0, builder.build());
        }
    }

    private void cargarProductos() {
        DBHelper DBHelper = new DBHelper(this);
        productList.clear(); // Limpiar la lista actual
        productList.addAll(DBHelper.getAllTasks()); // Agregar todos los producto de la base de datos
        adapter.notifyDataSetChanged(); // Notificar al adaptador de la actualización
    }
}

