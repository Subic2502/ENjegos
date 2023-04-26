package com.gorskivijenac.enjegos.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "eNjegos.db";
    private final Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Nema onCreate jer importujem bazu
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Nema onUpgrade jer importujem bazu
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Nema onDowngrade jer importujem bazu
    }

    public SQLiteDatabase getReadableDatabase() {
        SQLiteDatabase db = super.getReadableDatabase();
        copyDatabaseFromAssets(db);
        return db;
    }

    public SQLiteDatabase getWritableDatabase() {
        SQLiteDatabase db = super.getWritableDatabase();
        copyDatabaseFromAssets(db);
        return db;
    }

    private void copyDatabaseFromAssets(SQLiteDatabase db) {
        try {
            InputStream inputStream = context.getAssets().open(DATABASE_NAME);
            File outputFile = context.getDatabasePath(DATABASE_NAME);
            OutputStream outputStream = new FileOutputStream(outputFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}