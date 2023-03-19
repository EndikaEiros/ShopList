package com.example.seriesehu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.Locale;

public class preferencesActivity extends AppCompatActivity {

    @Override
    public void recreate() {
        finish();
        startActivity(getIntent());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (findViewById(R.id.fragment_container) != null){
            if(savedInstanceState!=null)
                return;
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new preferencesFragment()).commit();
        }


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences ( this );
        SharedPreferences.Editor editor= prefs.edit();

        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                editor.putBoolean( "displayColor" , false );
                //Log.d("miConfig","color: UI_MODE_NIGHT_NO" );
                break;

            case Configuration.UI_MODE_NIGHT_YES:
                editor.putBoolean( "displayColor" , true );
                //Log.d("miConfig","color: UI_MODE_NIGHT_YES" );
                break;
        }

        //String idioma = Locale.getDefault().getLanguage();
        //editor.putString( "lanpref" , "es" );
        //cambiarIdioma(idioma);

        editor.apply();
        //Boolean resultado= editor.commit();



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
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
    public void onBackPressed() {
        Log.d("loggerEHU", "Abrir Login");
        startActivity(new Intent(preferencesActivity.this, loginActivity.class));
        finish();
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
