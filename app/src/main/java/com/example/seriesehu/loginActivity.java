package com.example.seriesehu;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

public class loginActivity extends AppCompatActivity {

    Button loginLoginButton, loginSingUpButton, loginCloseButton, settingsButton;
    EditText UsuarioTextbox, loginPassTextbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String idioma = prefs.getString("lanpref","es");
        cambiarIdioma(idioma);

        loginLoginButton = findViewById(R.id.loginLoginButton);
        loginSingUpButton = findViewById(R.id.loginSingUpButton);
        loginCloseButton = findViewById(R.id.loginCloseButton);
        settingsButton = findViewById(R.id.SettingsButton);

        UsuarioTextbox = findViewById(R.id.loginUsuarioTextbox);
        loginPassTextbox = findViewById(R.id.loginPassTextbox);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    public void onBackPressed() {
        DialogFragment dialog = new CerrarAppDialog();
        dialog.show(getSupportFragmentManager(), "CloseAppFragment");
    }

    public void cerrarApp(View V){

        DialogFragment dialog = new CerrarAppDialog();
        dialog.show(getSupportFragmentManager(), "CloseAppFragment");

    }

    public void abrirajustes(View view){
        Log.d("loggerEHU", "Abrir ajustes");
        startActivity(new Intent(loginActivity.this, preferencesActivity.class));
        finish();
    }

    public void crearCuenta(View view){
        Log.d("loggerEHU", "Abrir Login");
        startActivity(new Intent(loginActivity.this, SignUpActivity.class));
        finish();
    }

    public void login(View view){
        EditText loginUsuarioTextbox = (EditText) findViewById(R.id.loginUsuarioTextbox);
        String usuario = loginUsuarioTextbox.getText().toString();
        EditText loginPassTextbox = (EditText) findViewById(R.id.loginPassTextbox);
        String password = loginPassTextbox.getText().toString();

        database GestorBD = new database(loginActivity.this, "ShopList", null, 1);
        SQLiteDatabase bd = GestorBD.getWritableDatabase();
        String[] columnas = new String[] {"codigo"};
        String [] argumentos = new String[] {usuario,password};
        Cursor cursor = bd.query("Usuarios",columnas,"usuario=? AND contraseña=?",argumentos, null,null,null);

        //bd.delete("Usuarios",null,null);
        //bd.delete("Listas",null,null);
        //bd.delete("tareas",null,null);

        if(cursor.getCount()>0) {
            cursor.close();
            bd.close();
            Toast.makeText(getApplicationContext(), getString(R.string.loginOk), Toast.LENGTH_LONG).show();
            Log.d("miConfig", "contraseña bien");

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(loginActivity.this);
            SharedPreferences.Editor editor= prefs.edit();
            editor.putString("nombre", usuario);
            editor.apply();

            bd.close();

            startActivity(new Intent(loginActivity.this, MainActivity.class));
            finish();

        }
        else{
            Log.d("miConfig", "contraseña MAL");

            Toast.makeText(getApplicationContext(), getString(R.string.loginError), Toast.LENGTH_LONG).show();
        }

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

    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        EditText usuarioE = (EditText) findViewById(R.id.loginUsuarioTextbox);
        String usuario = usuarioE.getText().toString();
        savedInstanceState.putString("usuario", usuario);

        EditText passwordE = (EditText) findViewById(R.id.loginPassTextbox);
        String password = passwordE.getText().toString();
        savedInstanceState.putString("password", password);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String usuario = savedInstanceState.getString("usuario");
        EditText usuarioE = (EditText) findViewById(R.id.loginUsuarioTextbox);
        usuarioE.setText(usuario);

        String password = savedInstanceState.getString("password");
        EditText passwordE = (EditText) findViewById(R.id.loginPassTextbox);
        passwordE.setText(password);
    }

}