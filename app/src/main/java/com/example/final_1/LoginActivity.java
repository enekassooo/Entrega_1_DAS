package com.example.final_1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.net.Uri;
import android.util.Log;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class LoginActivity extends AppCompatActivity {

    // Variable de clase para almacenar el resultado de la foto en base64
    private String fotoEn64;
    private String usu_1;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Botón para iniciar sesión
        Button buttonLogin = findViewById(R.id.buttonIniciarSesion);

        // Configurar el click listener del botón
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarSesion();
            }
        });

        // Botón para registrarse
        Button buttonRegistro = findViewById(R.id.buttonRegistrarme);

        // Configurar el click listener del botón
        buttonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mostrar el diálogo de registro
                mostrarDialogoRegistro();
            }
        });


    }

    private ActivityResultLauncher<Intent> takePictureLauncher =
            registerForActivityResult(new
                    ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK &&
                        result.getData() != null) {
                    Bundle bundle = result.getData().getExtras();
                    ImageView elImageView = findViewById(R.id.imageViewCapturedImage);
                    Bitmap laminiatura = (Bitmap) bundle.get("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    laminiatura.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] fototransformada = stream.toByteArray();
                    fotoEn64 = Base64.encodeToString(fototransformada, Base64.DEFAULT);

                    // Mostrar la imagen en el ImageView
                    //elImageView.setImageBitmap(laminiatura);


                } else {
                    Log.d("TakePicture", "No photo taken");
                }
            });


    private void iniciarSesion() {
        // Obtener referencias a los campos de usuario y contraseña
        EditText editTextUsuario = findViewById(R.id.editTextUsuario);
        EditText editTextContrasena = findViewById(R.id.editTextContrasena);

        // Obtener los valores de usuario y contraseña
        String usuario = editTextUsuario.getText().toString();
        String contrasena = editTextContrasena.getText().toString();

        usu_1 = usuario;

        // Enviar solicitud al servidor para verificar la autenticación
        String url = "http://34.121.132.31:81/verificar_login.php"; // URL del script para verificar la autenticación
        String parametros = "usuario=" + Uri.encode(usuario) + "&contrasena=" + Uri.encode(contrasena);

        // Ejecutar la tarea asíncrona para enviar la solicitud
        new VerificarLoginTask().execute(url, parametros);

    }

    // Clase para enviar datos al servidor y verificar autenticación
    private class VerificarLoginTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            String urlString = params[0];
            String parametros = params[1];
            boolean autenticado = false;

            try {
                // Crear la conexión HTTP
                URL url = new URL(urlString);
                HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
                conexion.setRequestMethod("POST");
                conexion.setDoOutput(true);

                // Escribir los parámetros en la solicitud
                OutputStream outputStream = conexion.getOutputStream();
                outputStream.write(parametros.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
                outputStream.close();

                // Leer la respuesta del servidor
                BufferedReader reader = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("Autenticado")) {
                        autenticado = true;
                    }
                }
                reader.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return autenticado;
        }

        @Override
        protected void onPostExecute(Boolean autenticado) {
            if (autenticado) {
                String idioma = "es"; // O el idioma que corresponda
                Log.d("Usuario", usu_1);
                // Si el usuario está autenticado, iniciar la actividad del menú
                Intent intent = new Intent(LoginActivity.this, Menu.class);
                intent.putExtra("usuarioMenu", usu_1);
                intent.putExtra("IDIOMA", idioma); // Agregar el atributo idioma
                startActivity(intent);

            } else {
                // Si no está autenticado, mostrar un mensaje de error
                Toast.makeText(LoginActivity.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }
        }
    }



        private void mostrarDialogoRegistro() {
        // Inflar el diseño del diálogo de registro
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.diag_activity_registro, null);

        // Configurar el diálogo de alerta
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setTitle("Registro");

        // Configurar el botón de registro
        builder.setPositiveButton("Registrarme", (dialog, which) -> {
            // Obtener referencias a los campos de usuario y contraseña
            EditText editTextUsuario = dialogView.findViewById(R.id.editTextUsuarioRegistro);
            EditText editTextContrasena = dialogView.findViewById(R.id.editTextContrasenaRegistro);

            // Obtener los valores de usuario y contraseña
            String usuario = editTextUsuario.getText().toString();
            String contrasena = editTextContrasena.getText().toString();
            // Aquí puedes usar la variable `fotoEn64`


            // Imprimir el contenido de dir_img
            //Log.d("Registro", "Imagen en Base64: " + fotoEn64);

            // Guardar los datos del usuario en el servidor
            guardarDatosUsuario(usuario, contrasena, fotoEn64);
        });

        // Configurar el botón de foto
        Button buttonFoto = dialogView.findViewById(R.id.buttonCaptureImage);

        // Configurar el click listener del botón para lanzar la cámara
        buttonFoto.setOnClickListener(v -> {
            Intent intentFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureLauncher.launch(intentFoto);
        });

        // Configurar el botón de cancelar
        builder.setNegativeButton("Cancelar", null);

        // Mostrar el diálogo
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void guardarDatosUsuario(String usuario, String contrasena, String foto) {
        // URL de tu archivo PHP en el servidor
        String url = "http://34.121.132.31:81/procesar_registro.php";


        // Dentro del método enviarDatos()
        String parametros = "usuario=" + Uri.encode(usuario) + "&contrasena=" + Uri.encode(contrasena) + "&foto=" + Uri.encode(foto);
        Log.d("Datos enviados", parametros); // Imprime los datos enviados en el registro (Logcat)



        // Ejecutar la tarea asíncrona
        new EnviarDatosTask().execute(url, parametros);
    }

    // Clase para enviar datos al servidor en segundo plano
    private static class EnviarDatosTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            // Obtener la URL y los parámetros de los argumentos
            String urlString = params[0];
            String parametros = params[1];

            try {
                // Crear la conexión HTTP
                URL url = new URL(urlString);
                HttpURLConnection conexion = (HttpURLConnection) url.openConnection();

                // Configurar la conexión
                conexion.setRequestMethod("POST");
                conexion.setDoOutput(true);

                // Escribir los parámetros en la solicitud
                OutputStream outputStream = conexion.getOutputStream();
                outputStream.write(parametros.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
                outputStream.close(); // Asegúrate de cerrar el OutputStream

                // Obtener la respuesta del servidor (si es necesario)
                int responseCode = conexion.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Log.d("Bien", "Todo ok");
                } else {
                    Log.d("Mal", "Algo no ok");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
