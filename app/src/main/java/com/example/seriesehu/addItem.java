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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Locale;

public class addItem extends AppCompatActivity {

    Button btnAddItem;
    TextView ItemDescription;
    EditText ItemName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String idioma = prefs.getString("idiomapref","es");
        cambiarIdioma(idioma);


        btnAddItem = findViewById(R.id.btnAddItem);
        ItemName = findViewById(R.id.addItemEditText);
        ItemDescription = findViewById(R.id.ItemDescription);

        Intent intent = new Intent();
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nuevoItem = ItemName.getText().toString();
                intent.putExtra("item", nuevoItem);

                String usuario = prefs.getString("nombre","test");
                String nombreLista = prefs.getString("listaActual","test");

                database GestorBD = new database(addItem.this, "ShopList2", null, 1);
                SQLiteDatabase bd = GestorBD.getWritableDatabase();


                bd.execSQL("INSERT INTO tareas ('usuario', 'nombreLista','item') " +
                        "VALUES ('" + usuario + "','" + nombreLista + "','"+ nuevoItem + "')");
                Toast.makeText(getApplicationContext(), getString(R.string.ItemOk), Toast.LENGTH_LONG).show();

                bd.close();

                setResult(RESULT_OK,intent);
                finish();
            }
        });

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        EditText addItemEditText = (EditText) findViewById(R.id.addItemEditText);
        String nombreItem = addItemEditText.getText().toString();
        savedInstanceState.putString("nombreItem", nombreItem);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        String nombreItem = savedInstanceState.getString("nombreItem");
        EditText addItemEditText = (EditText) findViewById(R.id.addItemEditText);
        addItemEditText.setText(nombreItem);
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