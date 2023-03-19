package com.example.seriesehu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class crearNuevaLista extends AppCompatActivity {

    int[] iconos;
    int imagenActual;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_nueva_lista);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String idioma = prefs.getString("idiomapref","es");
        cambiarIdioma(idioma);

        int imagenActual = 0;

        iconos = new int[] {R.drawable.question_mark, R.drawable.food,
                R.drawable.shopping_cart,R.drawable.shopping_basket, R.drawable.shopping_cart_plus};


    }

    public void cambiarfoto(View view){


        ImageView newListImage = (ImageView) findViewById(R.id.newListImage);
        imagenActual++;
        if(imagenActual >= iconos.length){
            imagenActual = 0;
        }
        int nuevaimagen = iconos[imagenActual];

        newListImage.setImageResource(nuevaimagen);
    }

    public void onBackPressed() {
        startActivity(new Intent(crearNuevaLista.this, MainActivity.class));
        finish();
    }

    public void crearLista(View view){

        EditText newListNametextbox = findViewById(R.id.newListNametextbox);
        String nombreLista = newListNametextbox.getText().toString();

        ImageView newListImage = (ImageView) findViewById(R.id.newListImage);
        int foto = newListImage.getImageAlpha();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(crearNuevaLista.this);
        String usuario = prefs.getString("nombre","test");

        if(nombreLista.equals("")){

            Toast.makeText(getApplicationContext(), getString(R.string.CrearListaVacia), Toast.LENGTH_SHORT).show();

        }else{
            database GestorBD = new database(crearNuevaLista.this, "ShopList", null, 1);
            SQLiteDatabase bd = GestorBD.getWritableDatabase();


            String[] columnas = new String[] {"usuario","nombreLista"};
            String [] argumentos = new String[] {usuario, nombreLista};
            Cursor c2 = bd.query("Listas",columnas,"usuario=? AND nombreLista=?",argumentos, null,null,null);

            if(c2.getCount()>0) {
                Toast.makeText(getApplicationContext(), getString(R.string.CrearListError), Toast.LENGTH_SHORT).show();
            }
            else {

                bd.execSQL("INSERT INTO Listas ('usuario', 'nombreLista','foto') " +
                        "VALUES ('" + usuario + "','" + nombreLista + "','"+ foto + "')");
                Toast.makeText(getApplicationContext(), getString(R.string.CrearListaOk), Toast.LENGTH_LONG).show();
                Cursor c = bd.rawQuery("SELECT * FROM Usuarios", null);

                Log.d("CREARLista", Integer.toString(c.getCount()));
                bd.close();

                startActivity(new Intent(crearNuevaLista.this, MainActivity.class));
                finish();
            }
        }

    }





    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        EditText newListNametextbox = (EditText) findViewById(R.id.newListNametextbox);
        String nombreLista = newListNametextbox.getText().toString();
        savedInstanceState.putString("nombreLista", nombreLista);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        String nombreLista = savedInstanceState.getString("nombreLista");
        EditText newListNametextbox = (EditText) findViewById(R.id.newListNametextbox);
        newListNametextbox.setText(nombreLista);
    }

    protected void cambiarIdioma(String idioma){
        Log.d("miConfig",idioma);
        Locale nuevaloc = new Locale(idioma);
        Locale.setDefault(nuevaloc);
        Configuration configuration = getBaseContext().getResources().getConfiguration();
        configuration.setLocale(nuevaloc);
        configuration.setLayoutDirection(nuevaloc);

        Context context = getBaseContext().createConfigurationContext(configuration);
        getBaseContext().getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
    }
}