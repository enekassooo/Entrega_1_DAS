package com.example.final_1;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class Menu extends AppCompatActivity {

    private Button buttonListaProd;
    private Button buttonSpanish;
    private Button buttonFrench;
    private Button buttonEnglish;
    private String idioma = "es";
    private String M_usu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();

        // Obtener el nombre de usuario de la intención
        Intent intent = getIntent();
        M_usu = intent.getStringExtra("usuarioMenu");
        idioma = intent.getStringExtra("IDIOMA");

        // Imprimir el nombre de usuario en Logcat
        Log.d("MenuUsuario1", "Nombre de usuario: " + M_usu);

        //Cambiar idioma al darle a un boton
        if (extras != null) {
            idioma = extras.getString("IDIOMA");
        }
        Locale nuevaloc = new Locale(idioma);
        Locale.setDefault(nuevaloc);
        Configuration configuration = getBaseContext().getResources().getConfiguration();
        configuration.setLocale(nuevaloc);
        configuration.setLayoutDirection(nuevaloc);
        Context context = getBaseContext().createConfigurationContext(configuration);
        getBaseContext().getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());


        setContentView(R.layout.menu);

        mostrar_Img_De_Usu_Logueado(M_usu);

        // Inicializar los botones
        buttonListaProd = findViewById(R.id.buttonListaProd);
        buttonSpanish = findViewById(R.id.buttonSpanish);
        buttonFrench = findViewById(R.id.buttonFrench);
        buttonEnglish = findViewById(R.id.buttonEnglish);

        // Configurar el click listener para el botón Producto
        buttonListaProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para iniciar ListsDestinesActivity
                Intent intent = new Intent(Menu.this, ListaProductos.class);
                startActivity(intent);
            }
        });

        // Detecta el click en el boton español y cambia el idioma de la app
        Button cambiarIdioma_a_es = findViewById(R.id.buttonSpanish);
        cambiarIdioma_a_es.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idioma = "es";
                Intent intent = new Intent(Menu.this, Menu.class);
                intent.putExtra("IDIOMA", idioma);
                intent.putExtra("usuarioMenu", M_usu); // Añadir el nombre del usuario al intent
                finish(); // Finaliza la actividad actual
                startActivity(intent); // Inicia la nueva actividad con el nuevo idioma
                //Toast para indicar el cambio a ingles
                Toast.makeText(getApplicationContext(), "Cambiando idioma a español", Toast.LENGTH_LONG).show();
            }
        });

        // Detecta el click en el boton frances y cambia el idioma de la app
        Button cambiarIdioma_a_fr = findViewById(R.id.buttonFrench);
        cambiarIdioma_a_fr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idioma = "fr";
                Intent intent = new Intent(Menu.this, Menu.class);
                intent.putExtra("IDIOMA", idioma);
                intent.putExtra("usuarioMenu", M_usu); // Añadir el nombre del usuario al intent
                finish(); // Finaliza la actividad actual
                startActivity(intent); // Inicia la nueva actividad con el nuevo idioma
                //Toast para indicar el cambio a ingles
                Toast.makeText(getApplicationContext(), "Changer la langue en français", Toast.LENGTH_LONG).show();
            }
        });

        // Detecta el click en el boton ingles y cambia el idioma de la app
        Button cambiarIdioma_a_en = findViewById(R.id.buttonEnglish);
        cambiarIdioma_a_en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idioma = "en";
                Intent intent = new Intent(Menu.this, Menu.class);
                intent.putExtra("IDIOMA", idioma);
                intent.putExtra("usuarioMenu", M_usu); // Añadir el nombre del usuario al intent
                finish(); // Finaliza la actividad actual
                startActivity(intent); // Inicia la nueva actividad con el nuevo idioma
                //Toast para indicar el cambio a ingles
                Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void mostrar_Img_De_Usu_Logueado(String nombre){
        TextView textViewUser = findViewById(R.id.textViewFotoDesc);
        textViewUser.setText("Usuario: " + nombre);

        // URL del script para recuperar la imagen
        String url = "http://34.28.249.48:81/devolver_image.php"; // URL del script para recuperar la imagen
        String parametros = "usuario=" + Uri.encode(nombre);
        Log.d("Datos enviados", parametros);

        // Ejecutar la tarea asíncrona para recuperar la imagen
        new ObtenerImagenTask().execute(url, parametros);
    }

    private void mostrarImagen (Bitmap imagen){
        ImageView imageView = findViewById(R.id.imageViewFoto);
        imageView.setImageBitmap(imagen);
    }

    private class ObtenerImagenTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            String urlString = params[0];
            String parametros = params[1];
            Bitmap imagen = null;

            try {
                URL url = new URL(urlString);
                HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
                conexion.setRequestMethod("POST");
                conexion.setDoOutput(true);

                // Escribir los parámetros en la solicitud
                OutputStream outputStream = conexion.getOutputStream();
                outputStream.write(parametros.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
                outputStream.close();


                // Establecer la conexión HTTP y recibir la respuesta del servidor
                int responseCode = conexion.getResponseCode();

                // Verificar si la conexión fue exitosa (código de respuesta HTTP 200)
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Leer la respuesta del servidor
                    InputStream inputStream = conexion.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;

                    // Leer línea por línea y construir el StringBuilder
                    while ((line = bufferedReader.readLine()) != null) {
                        Log.d("Buff", line);
                        stringBuilder.append(line);
                    }

                    // Cerrar el BufferedReader y el InputStream
                    bufferedReader.close();
                    inputStream.close();

                    // Obtener el string de la respuesta del servidor
                    String base64String = stringBuilder.toString();
                    Log.d("Data", "Image: " + base64String);

                    // Decodificar la cadena base64 en un array de bytes
                    byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);

                    // Crear un Bitmap a partir de los bytes decodificados
                    imagen = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);


                } else {
                    // Si la conexión no fue exitosa, manejar el error adecuadamente
                    Log.d("Error", "Imagen no recibida correctamente");
                }

                conexion.disconnect();
            } catch (Exception e) {
                Log.e("Error", "Error al recuperar la imagen: " + e.getMessage());
            }

            return imagen;
        }

        @Override
        protected void onPostExecute(Bitmap imagen) {
            if (imagen != null) {
                mostrarImagen(imagen);
            } else {
                Log.e("Error", "No se pudo obtener la imagen o la imagen recibida es nula");
            }
        }
    }
}
