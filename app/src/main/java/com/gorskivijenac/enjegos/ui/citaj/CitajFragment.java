package com.gorskivijenac.enjegos.ui.citaj;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.gorskivijenac.enjegos.R;
import com.gorskivijenac.enjegos.database.DatabaseHelper;
import com.gorskivijenac.enjegos.database.Poglavlje;
import com.gorskivijenac.enjegos.database.StihItem;

import java.util.ArrayList;
import java.util.List;


public class CitajFragment extends Fragment {

    Spinner spinner;

    private SQLiteDatabase mDatabase;
    private DatabaseHelper mDbHelper;
    private RecyclerView recyclerView;
    private StihAdapter stihAdapter;


    public CitajFragment() {
        // Required empty public constructor
    }


    public static CitajFragment newInstance(String param1, String param2) {
        CitajFragment fragment = new CitajFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_citaj, container, false);

        spinner = view.findViewById(R.id.fragment_citaj_spinner);
        List<Poglavlje> listaPoglavlja = new ArrayList<>();
        getPoglavlja(listaPoglavlja);

        ArrayAdapter<Poglavlje> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, listaPoglavlja);
        spinner.setAdapter(adapter);

        recyclerView = view.findViewById(R.id.fragment_citaj_recyclerview);

        List<StihItem> listaStihova = new ArrayList<>();

        Poglavlje oznacenoPoglavlje;

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Poglavlje oznacenoPoglavlje = (Poglavlje)adapterView.getSelectedItem();
                System.out.println(oznacenoPoglavlje);

                getStihoviFromPoglavlje(oznacenoPoglavlje,listaStihova);

                System.out.println(oznacenoPoglavlje);
                System.out.println("Velicina:" + listaStihova.size());
                for(int j=0;j<listaStihova.size();j++){
                    System.out.println(listaStihova.get(j));
                }

                forwardToAdapter(view,listaStihova);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //nista
            }
        });
        return view;
    }

    private void forwardToAdapter(View view,List<StihItem> listaStihova){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        stihAdapter = new StihAdapter(listaStihova);
        recyclerView.setAdapter(stihAdapter);
    }

    private void getStihoviFromPoglavlje(Poglavlje oznacenoPoglavlje,List<StihItem> listaStihova){
        listaStihova.clear();
        mDbHelper = new DatabaseHelper(getContext());

        mDatabase = mDbHelper.getReadableDatabase();

        int idPoglavlja = oznacenoPoglavlje.getId();

        String[] kolone = {"brojStiha","textStiha"};
        String selection = "s.IDPoglavlja = ?";
        String[] selectionArgs = { String.valueOf(idPoglavlja) };
        Cursor cursor = mDatabase.query("Stih s", kolone, selection, selectionArgs, null, null, null);


        while (cursor.moveToNext()) {
            String brojStiha = cursor.getString(cursor.getColumnIndexOrThrow(kolone[0]));
            String textStiha = cursor.getString(cursor.getColumnIndexOrThrow(kolone[1]));

            listaStihova.add(new StihItem(brojStiha,textStiha));
        }

        cursor.close();
        mDatabase.close();
    }


    private void getPoglavlja(List<Poglavlje> lista){
        mDbHelper = new DatabaseHelper(getContext());

        mDatabase = mDbHelper.getReadableDatabase();
        String[] kolone = {"ID","nazivPoglavlja"};

        Cursor cursor = mDatabase.query(true,"Poglavlje",kolone,null,null,null,null,null,null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(kolone[0]));
            String nazivPoglavlja = cursor.getString(cursor.getColumnIndexOrThrow(kolone[1]));

            lista.add(new Poglavlje(id,nazivPoglavlja));
        }

        cursor.close();
        mDatabase.close();
    }
}