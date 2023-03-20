package com.example.seriesehu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ArrayList<listaCompra> listaDatos;
    RecyclerView recycler;
    int[] iconos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String idioma = prefs.getString("idiomapref","es");
        cambiarIdioma(idioma);

        recycler = (RecyclerView) findViewById(R.id.recyclerListas);
        recycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        listaDatos = new ArrayList<listaCompra>();


        database GestorBD = new database(MainActivity.this, "ShopList", null, 1);
        SQLiteDatabase bd = GestorBD.getWritableDatabase();

        String usuario = prefs.getString("nombre","test");

        String[] columnas = new String[] {"usuario","nombreLista,foto"};
        String [] argumentos = new String[] {usuario};
        Cursor c2 = bd.query("Listas",columnas,"usuario=?",argumentos, null,null,null);
        //bd.delete("Listas",null,null);


        iconos = new int[] {R.drawable.question_mark, R.drawable.food,
                R.drawable.shopping_cart,R.drawable.shopping_basket, R.drawable.shopping_cart_plus};

        Log.d("BBDD", String.valueOf(c2.getCount()));
        if(c2.getCount()>0){

            if (c2.moveToFirst()) {
                do {
                    Log.d("BBDD", "nombreLista " + c2.getString(c2.getColumnIndexOrThrow("nombreLista")));
                    Log.d("BBDD", "numfoto foto " + c2.getInt(c2.getColumnIndexOrThrow("foto")));

                    listaDatos.add(new listaCompra( c2.getString(c2.getColumnIndexOrThrow("nombreLista")),iconos[c2.getInt(c2.getColumnIndexOrThrow("foto"))] ));
                } while (c2.moveToNext());
            }

        }else{
            Toast.makeText(this, R.string.MainActivityListaVacia, Toast.LENGTH_SHORT).show();
        }



        AdaptadorListView adapter = new AdaptadorListView(listaDatos);
        
        adapter.setOnclickLisrener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String listaActual = listaDatos.get(recycler.getChildAdapterPosition(view)).getNombre();

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor editor= prefs.edit();
                editor.putString("listaActual", listaActual);
                editor.apply();

                startActivity(new Intent(MainActivity.this, VerTareas.class));
                finish();
            }
        });
        
        recycler.setAdapter(adapter);
        c2.close();
        bd.close();
    }

    public void crearNuevaLista(View view){
        startActivity(new Intent(MainActivity.this, crearNuevaLista.class));
        finish();
    }

    public void onBackPressed() {
        DialogFragment dialog = new CerrarAppDialogLogIn();
        dialog.show(getSupportFragmentManager(), "CloseAppFragment");
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