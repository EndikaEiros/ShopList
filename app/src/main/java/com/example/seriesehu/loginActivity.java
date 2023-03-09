package com.example.seriesehu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

public class loginActivity extends AppCompatActivity {

    Button loginLoginButton;
    Button loginSingUpButton;
    Button loginCloseButton;
    //Button AJUSTES;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginLoginButton = findViewById(R.id.loginLoginButton);
        loginSingUpButton = findViewById(R.id.loginSingUpButton);
        loginCloseButton = findViewById(R.id.loginCloseButton);
        //loginLoginButton = findViewById(R.id.loginLoginButton);
    }

    @Override
    public void onBackPressed() {
        cerrarApp();
    }

    public void cerrarApp(){

        DialogFragment dialog = new CerrarAppDialog();
        dialog.show(getSupportFragmentManager(), "CloseAppFragment");

    }


}