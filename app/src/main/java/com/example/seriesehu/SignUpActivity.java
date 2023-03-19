package com.example.seriesehu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

public class SignUpActivity extends AppCompatActivity {
    Calendar calendario = Calendar.getInstance();
    EditText SignUpUsername, signUpEmail, signUpFnacim, SignUpPassword1, SignUpPassword2 ;
    Button SignUpButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String idioma = prefs.getString("lanpref","es");
        cambiarIdioma(idioma);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int anyo, int mes, int dia) {

                EditText signUpFnacim = (EditText) findViewById(R.id.signUpFnacim);
                signUpFnacim.setText(Integer.toString(anyo)+"-"+Integer.toString(mes+1)+"-"+Integer.toString(dia));
            }
        };

        EditText signUpFnacim = (EditText) findViewById(R.id.signUpFnacim);
        signUpFnacim.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                new DatePickerDialog(SignUpActivity.this,date,calendario.get(Calendar.YEAR),calendario.get(Calendar.MONTH),calendario.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }
    public void onBackPressed() {
        Log.d("loggerEHU", "Abrir Login");
        startActivity(new Intent(SignUpActivity.this, loginActivity.class));
        finish();
    }

    public void CrearCuenta(View view){
        //EditText SignUpUsername, signUpEmail, signUpFnacim, SignUpPassword1, SignUpPassword2 ;

        EditText SignUpUsername = (EditText) findViewById(R.id.SignUpUsername);
        String usuario = SignUpUsername.getText().toString();

        EditText SignUpPassword1 = (EditText) findViewById(R.id.SignUpPassword1);
        String password1 = SignUpPassword1.getText().toString();

        EditText SignUpPassword2 = (EditText) findViewById(R.id.SignUpPassword2);
        String password2 = SignUpPassword2.getText().toString();

        EditText signUpEmail = (EditText)  findViewById(R.id.signUpEmail);
        String email = signUpEmail.getText().toString();

        EditText signUpFnacim = (EditText) findViewById(R.id.signUpFnacim);
        String fechaNac = signUpFnacim.getText().toString();

        if(usuario.equals("")||password1.equals("")||password2.equals("")||email.equals("")||fechaNac.equals("")){
            Toast.makeText(getApplicationContext(), getString(R.string.SignUpErrorFields), Toast.LENGTH_SHORT).show();
        }
        else {
            if(!password1.equals(password2)){
                Toast.makeText(getApplicationContext(), getString(R.string.SignUpErrorPassword), Toast.LENGTH_SHORT).show();
            }else{
                database GestorBD = new database(SignUpActivity.this, "ShopList", null, 1);
                SQLiteDatabase bd = GestorBD.getWritableDatabase();

                String[] columnas = new String[] {"codigo"};
                String [] argumentos = new String[] {usuario};
                Cursor c2 = bd.query("Usuarios",columnas,"usuario=?",argumentos, null,null,null);
                if(c2.getCount()>0) {
                    Toast.makeText(getApplicationContext(), getString(R.string.SignUpError), Toast.LENGTH_SHORT).show();
                }
                else {

                    bd.execSQL("INSERT INTO Usuarios ('usuario', 'contraseña','email','fechaNacimiento') " +
                            "VALUES ('" + usuario + "','" + password1 + "','"+ email + "','" + fechaNac + "')");
                    Toast.makeText(getApplicationContext(), getString(R.string.SignUpOk), Toast.LENGTH_LONG).show();
                    Cursor c = bd.rawQuery("SELECT * FROM Usuarios", null);
                    Log.d("CREARCUENTA", Integer.toString(c.getCount()));
                    bd.close();

                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SignUpActivity.this);
                    SharedPreferences.Editor editor= prefs.edit();
                    editor.putString("nombre", usuario);
                    editor.apply();

                    //Se abre la main activity
                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                    finish();
                }

            }
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

    //MANTENER DATOS EN HORIZONTAL
    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        //EditText SignUpUsername, signUpEmail, signUpFnacim, SignUpPassword1, SignUpPassword2 ;

        EditText SignUpUsername = (EditText) findViewById(R.id.SignUpUsername);
        String usuario = SignUpUsername.getText().toString();
        savedInstanceState.putString("usuario", usuario);

        EditText SignUpPassword1 = (EditText) findViewById(R.id.SignUpPassword1);
        String password1 = SignUpPassword1.getText().toString();
        savedInstanceState.putString("password1", password1);

        EditText SignUpPassword2 = (EditText) findViewById(R.id.SignUpPassword2);
        String password2 = SignUpPassword2.getText().toString();
        savedInstanceState.putString("password2", password2);

        EditText signUpEmail = (EditText)  findViewById(R.id.signUpEmail);
        String email = signUpEmail.getText().toString();
        savedInstanceState.putString("email", email);

        EditText signUpFnacim = (EditText) findViewById(R.id.signUpFnacim);
        String fechaNac = signUpFnacim.getText().toString();
        savedInstanceState.putString("fechaNacimiento", fechaNac);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        String usuario = savedInstanceState.getString("usuario");
        EditText SignUpUsername = (EditText) findViewById(R.id.SignUpUsername);
        SignUpUsername.setText(usuario);

        String password1 = savedInstanceState.getString("password1");
        EditText SignUpPassword1 = (EditText) findViewById(R.id.SignUpPassword1);
        SignUpPassword1.setText(password1);

        String password2 = savedInstanceState.getString("password2");
        EditText SignUpPassword2 = (EditText) findViewById(R.id.SignUpPassword2);
        SignUpPassword2.setText(password2);

        String email = savedInstanceState.getString("email");
        EditText signUpEmail = (EditText)  findViewById(R.id.signUpEmail);
        signUpEmail.setText(email);

        String fechaNac = savedInstanceState.getString("fechaNacimiento");
        EditText signUpFnacim = (EditText) findViewById(R.id.signUpFnacim);
        signUpFnacim.setText(fechaNac);
    }

}