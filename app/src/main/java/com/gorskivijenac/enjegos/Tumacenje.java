package com.gorskivijenac.enjegos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import com.gorskivijenac.enjegos.database.DatabaseHelper;

public class Tumacenje extends AppCompatActivity {

    private SQLiteDatabase mDatabase;
    private DatabaseHelper mDbHelper;

    TextView naslov;
    TextView tumacenje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tumacenje);

        naslov = findViewById(R.id.tumacenje_naslov);
        tumacenje = findViewById(R.id.fragment_citaj_tablet_tumacenjeTextview);

        Intent intent = getIntent();
        int brojStiha = Integer.parseInt(intent.getStringExtra("brojStiha"));
        String textStiha = findTextStiha(brojStiha);
        int idTumacenja = findIDTumacenja(brojStiha);
        String textTumacenja = findTumacenjeFromID(idTumacenja);


        System.out.println("brojStiha:" + brojStiha + "textStiha: \n" + textStiha + "idTumacenja: " + idTumacenja + "textTumacenja: " + textTumacenja);

        naslov.setText("Tumacenje za stih:\n"+textStiha);
        tumacenje.setText(textTumacenja);


    }

    private String findTumacenjeFromID(int brojStiha){
        mDbHelper = new DatabaseHelper(this);

        mDatabase = mDbHelper.getReadableDatabase();

        String[] kolone = {"textTumacenja"};
        String selection = "t.ID = ?";
        String[] selectionArgs = { String.valueOf(brojStiha) };
        Cursor cursor = mDatabase.query("Tumacenje t", kolone, selection, selectionArgs, null, null, null);



        cursor.moveToNext();
        String textTumacenja = cursor.getString(cursor.getColumnIndexOrThrow(kolone[0]));

        cursor.close();
        mDatabase.close();
        return textTumacenja;
    }
    private String findTextStiha(int brojStiha){
        mDbHelper = new DatabaseHelper(this);

        mDatabase = mDbHelper.getReadableDatabase();

        String[] kolone = {"textStiha"};
        String selection = "s.brojStiha = ?";
        String[] selectionArgs = { String.valueOf(brojStiha) };
        Cursor cursor = mDatabase.query("Stih s", kolone, selection, selectionArgs, null, null, null);



        cursor.moveToNext();
        String textStiha = cursor.getString(cursor.getColumnIndexOrThrow(kolone[0]));

        cursor.close();
        mDatabase.close();
        return textStiha;
    }
    private int findIDTumacenja(int brojStiha){
        mDbHelper = new DatabaseHelper(this);

        mDatabase = mDbHelper.getReadableDatabase();

        String[] kolone = {"IDTumacenja"};
        String selection = "s.brojStiha = ?";
        String[] selectionArgs = { String.valueOf(brojStiha) };
        Cursor cursor = mDatabase.query("Stih s", kolone, selection, selectionArgs, null, null, null);



        cursor.moveToNext();
        int idStiha = cursor.getInt(cursor.getColumnIndexOrThrow(kolone[0]));

        cursor.close();
        mDatabase.close();
        return idStiha;
    }
}