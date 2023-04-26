package com.gorskivijenac.enjegos.ui.raspolozenja;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gorskivijenac.enjegos.R;
import com.gorskivijenac.enjegos.StihoviPoKriterijumu;
import com.gorskivijenac.enjegos.database.DatabaseHelper;
import com.gorskivijenac.enjegos.database.StihItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class RaspolozenjaFragment extends Fragment {

    TextView kakoSeOsecate;
    //----------------------------

    private SQLiteDatabase mDatabase;
    private DatabaseHelper mDbHelper;

    //----------------------------

    Button buttonLjubav;
    Button buttonNervoza;
    Button buttonGnev;
    Button buttonNada;
    Button buttonDepresija;
    Button buttonMir;
    Button buttonStrah;
    Button buttonStrpljenje;
    Button buttonIskusenje;
    Button buttonGordost;
    Button buttonRadost;
    Button buttonLjubomora;
    Button buttonGubitak;
    Button buttonIscelenje;


    public RaspolozenjaFragment() {
        // Required empty public constructor
    }

    public static RaspolozenjaFragment newInstance(String param1, String param2) {
        RaspolozenjaFragment fragment = new RaspolozenjaFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_raspolozenja, container, false);

        kakoSeOsecate = view.findViewById(R.id.fragment_raspolozenja_textView);

        buttonLjubav = view.findViewById(R.id.fragment_raspolozenja_ljubavbutton);
        buttonDepresija = view.findViewById(R.id.fragment_raspolozenja_depresijabutton);
        buttonGnev = view.findViewById(R.id.fragment_raspolozenja_gnevbutton);
        buttonGordost = view.findViewById(R.id.fragment_raspolozenja_gordostbutton);
        buttonGubitak = view.findViewById(R.id.fragment_raspolozenja_gubitakbutton);
        buttonIscelenje = view.findViewById(R.id.iscelenjebutton);
        buttonIskusenje = view.findViewById(R.id.fragment_raspolozenja_iskusenjebutton);
        buttonLjubomora = view.findViewById(R.id.fragment_raspolozenja_ljubomorabutton);
        buttonMir = view.findViewById(R.id.fragment_raspolozenja_mirbutton);
        buttonNada = view.findViewById(R.id.nfragment_raspolozenja_nadabutton);
        buttonNervoza = view.findViewById(R.id.fragment_raspolozenja_nervozabutton);
        buttonRadost = view.findViewById(R.id.fragment_raspolozenja_radostbutton);
        buttonStrah = view.findViewById(R.id.fragment_raspolozenja_strahbutton);
        buttonStrpljenje = view.findViewById(R.id.fragment_raspolozenja_strpljenje);

        List<StihItem> listaStihova = new ArrayList<>();


        buttonLjubav.setOnClickListener(v -> {

            getStihoviFromRaspolozenje(listaStihova,"Ljubav");

            Intent intent = new Intent(getContext(), StihoviPoKriterijumu.class);
            intent.putExtra("raspolozenje", "Ljubav");
            startActivity(intent);
        });

        buttonNada.setOnClickListener(v -> {

            getStihoviFromRaspolozenje(listaStihova,"Nada");

            Intent intent = new Intent(getContext(), StihoviPoKriterijumu.class);
            intent.putExtra("raspolozenje", "Nada");
            startActivity(intent);
        });

        return view;
    }

    private void getStihoviFromRaspolozenje(List<StihItem> listaStihova,String raspolozenje) {
        listaStihova.clear();
        mDbHelper = new DatabaseHelper(getContext());

        mDatabase = mDbHelper.getReadableDatabase();

        String[] kolone = {"brojStiha","textStiha"};
        String selection = "s.raspolozenje = ?";
        String[] selectionArgs = { String.valueOf(raspolozenje) };
        Cursor cursor = mDatabase.query("Stih s", kolone, selection, selectionArgs, null, null, null);


        while (cursor.moveToNext()) {
            String brojStiha = cursor.getString(cursor.getColumnIndexOrThrow(kolone[0]));
            String textStiha = cursor.getString(cursor.getColumnIndexOrThrow(kolone[1]));

            listaStihova.add(new StihItem(brojStiha,textStiha));
        }

        cursor.close();
        mDatabase.close();

    }
}