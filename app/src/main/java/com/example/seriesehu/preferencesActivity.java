package com.example.seriesehu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class preferencesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        if (findViewById(R.id.fragment_container) != null){
            if(savedInstanceState!=null)
                return;

            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new preferencesFragment()).commit();
        }

    }

}

/*
BotonCas.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
        Locale nuevaloc = new Locale("en");
        Locale.setDefault(nuevaloc);
        Configuration config = new Configuration();
        config.locale = nuevaloc;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        finish();
        startActivity(getIntent());
        }
        });*/
