package com.example.seriesehu;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class database extends SQLiteOpenHelper {
    public database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE Usuarios (" +
                "'codigo' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "'usuario' VARCHAR(255), " +
                "'contraseña' VARCHAR(255), " +
                "'email' VARCHAR(255), " +
                "'fechaNacimiento' DATE)");

        sqLiteDatabase.execSQL("CREATE TABLE Listas (" +
                "'usuario' VARCHAR(255), " +
                "'nombreLista' VARCHAR(255), " +
                "'foto' int)");

        sqLiteDatabase.execSQL("CREATE TABLE tareas (" +
                "'usuario' VARCHAR(255), " +
                "'nombreLista' VARCHAR(255), " +
                "'item' VARCHAR(255) )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}