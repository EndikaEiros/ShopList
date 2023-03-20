package com.example.seriesehu;

import static java.security.AccessController.getContext;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Locale;

public class VerTareas extends AppCompatActivity {

    ListView listaItems;
    Button newItem;

    ArrayList<String> Items;
    ArrayAdapter<String> eladaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_tareas);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String idioma = prefs.getString("idiomapref","es");
        cambiarIdioma(idioma);


        listaItems = findViewById(R.id.listaItems);
        newItem = findViewById( R.id.btn_addNewItem);

        Items = new ArrayList<String>();

        String usuario = prefs.getString("nombre","test");
        String nombreLista = prefs.getString("listaActual","test");


        database GestorBD = new database(VerTareas.this, "ShopList2", null, 1);
        SQLiteDatabase bd = GestorBD.getWritableDatabase();

        String[] columnas = new String[] {"item"};
        String [] argumentos = new String[] {usuario,nombreLista};

        Log.d("BBDD", argumentos[0] +"  " + argumentos[1]);
        //bd.execSQL("INSERT INTO tareas ('usuario', 'nombreLista','item') " + "VALUES ('" + "agayabea" + "','" + nombreLista + "','"+ "test" + "')");

        Cursor c2 = bd.query("tareas", columnas,"usuario=? AND nombreLista=?",argumentos, null,null,null);

        if(c2.getCount()>0){

            if (c2.moveToFirst()) {
                do {
                    Items.add( c2.getString(c2.getColumnIndexOrThrow("item")) );
                } while (c2.moveToNext());
            }

        }else{
            Toast.makeText(this, R.string.ListaItemsVacia, Toast.LENGTH_SHORT).show();
        }




        ArrayAdapter eladaptador = new ArrayAdapter( this , android.R.layout. simple_list_item_1 , Items);
        listaItems.setAdapter(eladaptador);
        listaItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                Log.i( "etiqueta" , ((TextView)view). getText (). toString ()+ ", " +position+ ", " +id);

                Context context = getApplicationContext();

                String item = Items.get(position);

                String [] argumentos = new String[] {usuario, nombreLista, item};
                bd.delete("tareas","usuario=? AND nombreLista=? AND item=?",argumentos);


                Items.remove(position);
                listaItems.setAdapter(eladaptador);
                eladaptador.notifyDataSetChanged();
                return true;
            }
        });

        c2.close();
        bd.close();

    }

    public void añadirTarea(View view) {
        Intent intent = new Intent(VerTareas.this, addItem.class);
        startActivityIntent.launch(intent);
    }

    public void borrarLista(View view) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String usuario = prefs.getString("nombre","test");
        String listaActual = prefs.getString("listaActual","test");

        database GestorBD = new database(VerTareas.this, "ShopList", null, 1);
        SQLiteDatabase bd = GestorBD.getWritableDatabase();

        String [] argumentos = new String[] {usuario, listaActual};

        bd.delete("Listas","usuario=? AND nombreLista LIKE ?",argumentos);

        bd.close();

        startActivity(new Intent(VerTareas.this, MainActivity.class));
        finish();

    }

    public void mandarSMS(View view) {

        String message = "";
        int i =0;
        do {
            message = message + "- " + Items.get(i) + "\n";
            i++;
        } while(i < Items.size());


        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_APP_MESSAGING);

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("message", message);
        clipboard.setPrimaryClip(clip);

        Toast.makeText(this, R.string.smsClipboard, Toast.LENGTH_SHORT).show();

        startActivity(intent);
    }

    public void Notificacion(View view) {

        NotificationManager elManager = (NotificationManager) this.getSystemService(this.NOTIFICATION_SERVICE);
        NotificationCompat.Builder elBuilder = new NotificationCompat.Builder(this,"notifySimple");
        elBuilder.setSmallIcon(R.drawable.icono)
                .setContentTitle(getString(R.string.TituloNotificacion))
                .setContentText(getString(R.string.NotificacionContenido))
                .setVibrate(new long[] {50,50,50,50})
                .setAutoCancel(true);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel elCanal = new NotificationChannel("notifySimple","Canal notificación simple",NotificationManager.IMPORTANCE_DEFAULT);
            elCanal.setDescription("Canal notificación simple");
            elCanal.enableLights(true);
            elCanal.setLightColor(Color.RED);
            elCanal.setVibrationPattern(new long[]{50, 50, 50, 50});
            elCanal.enableVibration(true);
            elManager.createNotificationChannel(elCanal);
        }
        elManager.notify(11, elBuilder.build());

        NotificationCompat.Builder b = new NotificationCompat.Builder(this );

    }

    ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK){
                String item = result.getData().getStringExtra("item");
                Items.add(item);
                Context context = getApplicationContext();
                ArrayAdapter eladaptador = new ArrayAdapter( context , android.R.layout. simple_list_item_1 , Items);
                listaItems.setAdapter(eladaptador);
                eladaptador.notifyDataSetChanged();

            }
        }
    });


    public void onBackPressed() {
        startActivity(new Intent(VerTareas.this, MainActivity.class));
        finish();
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