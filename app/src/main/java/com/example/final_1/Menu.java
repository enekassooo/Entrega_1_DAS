package com.example.final_1;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class Menu extends AppCompatActivity {

    private Button buttonListaProd;
    private Button buttonSpanish;
    private Button buttonFrench;
    private Button buttonEnglish;
    private String idioma = "es";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();

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
                finish(); // Finaliza la actividad actual
                startActivity(intent); // Inicia la nueva actividad con el nuevo idioma
                //Toast para indicar el cambio a ingles
                Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
            }
        });


    }
}
