package com.gorskivijenac.enjegos;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.gorskivijenac.enjegos.database.DatabaseHelper;
import com.gorskivijenac.enjegos.database.StihItem;
import com.gorskivijenac.enjegos.ui.citaj.StihAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StihoviPoKriterijumu extends AppCompatActivity {

    private SQLiteDatabase mDatabase;
    private DatabaseHelper mDbHelper;

    //----------------------------------------
    private RecyclerView recyclerView;
    private StihAdapter stihAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private String raspolozenje;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stihovi_po_kriterijumu);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        List<StihItem> listaStihova = new ArrayList<>();


        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("raspolozenje")) {
            raspolozenje =  intent.getStringExtra("raspolozenje");
            getStihoviFromRaspolozenje(listaStihova,raspolozenje);

            System.out.println("Raspolozenje: " + raspolozenje);
            System.out.println("Velicina:" + listaStihova.size());
            for(int j=0;j<listaStihova.size();j++){
                System.out.println(listaStihova.get(j));
            }
        }

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        stihAdapter = new StihAdapter(listaStihova);
        recyclerView.setAdapter(stihAdapter);
    }

    private void getStihoviFromRaspolozenje(List<StihItem> listaStihova,String raspolozenje) {
        listaStihova.clear();
        mDbHelper = new DatabaseHelper(this);

        mDatabase = mDbHelper.getReadableDatabase();

        String[] kolone = {"brojStiha","textStiha","IDPoglavlja"};
        String selection = "s.raspolozenje = ?";
        String[] selectionArgs = { String.valueOf(raspolozenje) };
        Cursor cursor = mDatabase.query("Stih s", kolone, selection, selectionArgs, null, null, null);

        int brojZaRedom = -1;
        String nazivPoglavlja;

        while (cursor.moveToNext()) {
            String brojStiha = cursor.getString(cursor.getColumnIndexOrThrow(kolone[0]));
            String textStiha = cursor.getString(cursor.getColumnIndexOrThrow(kolone[1]));
            int idPoglavlja = cursor.getInt(cursor.getColumnIndexOrThrow(kolone[2]));

            if(brojZaRedom == -1){
                nazivPoglavlja = getPoglavljeByID(idPoglavlja);
                brojZaRedom = Integer.parseInt(brojStiha);
                listaStihova.add(new StihItem("","--- Poglavlje:" + nazivPoglavlja + "---"));
            }else{
                if(brojZaRedom == Integer.parseInt(brojStiha)-1){
                    brojZaRedom = Integer.parseInt(brojStiha);
                }else{
                    nazivPoglavlja = getPoglavljeByID(idPoglavlja);
                    brojZaRedom = Integer.parseInt(brojStiha);
                    listaStihova.add(new StihItem("","--- Poglavlje:" + nazivPoglavlja + "---"));
                }
            }


            listaStihova.add(new StihItem(brojStiha,textStiha));
        }

        cursor.close();
        mDatabase.close();

    }
    private String getPoglavljeByID(int idPoglavlja) {
        mDbHelper = new DatabaseHelper(this);

        mDatabase = mDbHelper.getReadableDatabase();

        String[] kolone = {"nazivPoglavlja"};
        String selection = "p.ID = ?";
        String[] selectionArgs = { String.valueOf(idPoglavlja) };
        Cursor cursor = mDatabase.query("Poglavlje p", kolone, selection, selectionArgs, null, null, null);

        String nazivPoglavlja;

        cursor.moveToNext();
        nazivPoglavlja = cursor.getString(cursor.getColumnIndexOrThrow(kolone[0]));


        cursor.close();
        mDatabase.close();
        return nazivPoglavlja;
    }
}