package com.gorskivijenac.enjegos;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gorskivijenac.enjegos.database.DatabaseHelper;
import com.gorskivijenac.enjegos.database.StihItem;
import com.gorskivijenac.enjegos.ui.citaj.StihAdapter;

public class OznaciKaoOmiljeno extends AppCompatActivity {

    private SQLiteDatabase mDatabase;
    private DatabaseHelper mDbHelper;

    TextView textViewStiha;
    EditText editTextKomentar;
    Button buttonDodaj;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oznaci_kao_omiljeno);

        Intent intent = getIntent();
        int brojStihaZaOznaciti = Integer.parseInt(intent.getStringExtra("brojStiha"));

        String textStiha = findTextStiha(brojStihaZaOznaciti);
        textViewStiha = findViewById(R.id.oznaci_omiljeno_textView);
        textViewStiha.setText("Stih koji zelite da sacuvate kao omiljeni:\n" + textStiha);

        editTextKomentar = findViewById(R.id.oznaci_omiljeno_textedit);



        buttonDodaj = findViewById(R.id.oznaci_omiljeno_button);



        int idStiha = findIDStiha(brojStihaZaOznaciti);


        buttonDodaj.setOnClickListener(view -> {
            if(proveriPostojanjeOmiljenogCitata(idStiha)){
                Toast.makeText(this, "Taj citat sve vec dodali!", Toast.LENGTH_SHORT).show();
            }else{
                String komentar = editTextKomentar.getText().toString();
                oznaciKaoOmiljen(brojStihaZaOznaciti,textStiha,komentar);
                stampajBazuOmiljeno();
                //finish();
            }

        });



    }

    private void stampajBazuOmiljeno(){

        System.out.println("Uslo u stampanje");

        mDbHelper = new DatabaseHelper(this);

        mDatabase = mDbHelper.getReadableDatabase();

        Cursor cursor = mDatabase.query("Omiljeno", null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            System.out.println("Uslo u if");
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("ID"));
                @SuppressLint("Range") int idStiha = cursor.getInt(cursor.getColumnIndex("IDStiha"));
                @SuppressLint("Range") String textKomentara = cursor.getString(cursor.getColumnIndex("textKomentara"));

                System.out.println("ID: " + id + ", IDStiha: " + idStiha + ", textKomentara: " + textKomentara);
            } while (cursor.moveToNext());
        }

        cursor.close();
        mDatabase.close();
        System.out.println("Izaslo");

    }

    private int findIDStiha(int brojStiha){
        mDbHelper = new DatabaseHelper(this);

        mDatabase = mDbHelper.getReadableDatabase();

        String[] kolone = {"ID"};
        String selection = "s.brojStiha = ?";
        String[] selectionArgs = { String.valueOf(brojStiha) };
        Cursor cursor = mDatabase.query("Stih s", kolone, selection, selectionArgs, null, null, null);



        cursor.moveToNext();
        int idStiha = cursor.getInt(cursor.getColumnIndexOrThrow(kolone[0]));

        cursor.close();
        mDatabase.close();
        return idStiha;
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

    private void oznaciKaoOmiljen(int brojStiha, String textStiha, String textKomentara) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        StihItem stihItem = new StihItem(String.valueOf(brojStiha), textStiha);

        Gson gson = new Gson();
        String stihJson = gson.toJson(stihItem);

        editor.putString("stih-" + brojStiha+"komentar", stihJson);
        editor.apply();
    }


    private boolean proveriPostojanjeOmiljenogCitata(int idStiha) {
        mDbHelper = new DatabaseHelper(this);
        mDatabase = mDbHelper.getReadableDatabase();

        String[] columns = {"ID"};
        String selection = "IDStiha = ?";
        String[] selectionArgs = {String.valueOf(idStiha)};

        Cursor cursor = mDatabase.query("Omiljeno", columns, selection, selectionArgs, null, null, null);

        boolean exists = (cursor.getCount() > 0);

        cursor.close();
        mDatabase.close();

        return exists;
    }


}