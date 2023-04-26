package com.gorskivijenac.enjegos.ui.zanimljivosti;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.gorskivijenac.enjegos.R;
import com.gorskivijenac.enjegos.database.DatabaseHelper;
import com.gorskivijenac.enjegos.database.Poglavlje;
import com.gorskivijenac.enjegos.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;


public class ZanimljivostiFragment extends Fragment {

    Spinner spinner;
    Button dugmeLovcen;
    TextView textView;


    //------------------------------
    private SQLiteDatabase mDatabase;
    private DatabaseHelper mDbHelper;

    public ZanimljivostiFragment() {
        // Required empty public constructor
    }

    public static ZanimljivostiFragment newInstance(String param1, String param2) {
        ZanimljivostiFragment fragment = new ZanimljivostiFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zanimljivosti, container, false);

        spinner = view.findViewById(R.id.fragment_zanimljivosti_spinner);

        List<String> listaNaslova = getListaNaslova(view);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, listaNaslova);
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String naslov = spinner.getSelectedItem().toString();
                if(naslov.equals("Njegoš i sloboda")){
                    TextZanimljivostiFragment fragment = new TextZanimljivostiFragment();

                    Bundle bundle = new Bundle();
                    bundle.putString("text", "sloboda");

                    fragment.setArguments(bundle);

                    replaceFragment(fragment);
                    //String tekst = pronadjiTekst("1");
                    //textView.setText(tekst);
                }else if(naslov.equals("Njegoš i pomorandže")){
                    TextZanimljivostiFragment fragment = new TextZanimljivostiFragment();

                    Bundle bundle = new Bundle();
                    bundle.putString("text", "pomorandze");

                    fragment.setArguments(bundle);

                    replaceFragment(fragment);

                    /*String tekst = pronadjiTekst("2");
                    textView.setText(tekst);*/
                }
                else if(naslov.equals("Njegoš i žene")){
                    TextZanimljivostiFragment fragment = new TextZanimljivostiFragment();

                    Bundle bundle = new Bundle();
                    bundle.putString("text", "zene");

                    fragment.setArguments(bundle);

                    replaceFragment(fragment);

                    /*String tekst = pronadjiTekst("3");
                    textView.setText(tekst);*/
                }
                else if(naslov.equals("Njegoš i cenzura")){
                    TextZanimljivostiFragment fragment = new TextZanimljivostiFragment();

                    Bundle bundle = new Bundle();
                    bundle.putString("text", "cenzura");

                    fragment.setArguments(bundle);

                    replaceFragment(fragment);

                    /*String tekst = pronadjiTekst("4");
                    textView.setText(tekst);*/
                }else if(naslov.equals("Njegoš i lanci")){
                    TextZanimljivostiFragment fragment = new TextZanimljivostiFragment();

                    Bundle bundle = new Bundle();
                    bundle.putString("text", "lanci");

                    fragment.setArguments(bundle);

                    replaceFragment(fragment);
                    /*String tekst = pronadjiTekst("5");
                    textView.setText(tekst);*/
                }else{
                    MapaFragment fragment = new MapaFragment();
                    replaceFragment(fragment);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //nista
            }
        });


        return view;
    }

    private String pronadjiTekst(String id) {
        mDbHelper = new DatabaseHelper(getContext());

        mDatabase = mDbHelper.getReadableDatabase();
        String[] kolone = {"textZanimljivosti"};
        String selection = "z.ID = ?";
        String[] selectionArgs = { id };
        Cursor cursor = mDatabase.query("Zanimljivosti z",kolone,selection,selectionArgs,null,null,null,null);

        String textZanimljivosti = "";

        while (cursor.moveToNext()) {
            textZanimljivosti = cursor.getString(cursor.getColumnIndexOrThrow(kolone[0]));

        }

        cursor.close();
        mDatabase.close();
        return textZanimljivosti;
    }

    private List<String> getListaNaslova(View view) {

        List<String> lista = new ArrayList<>();

        lista.add(getResources().getString(R.string.njegos_i_sloboda));
        lista.add(getResources().getString(R.string.njegos_i_cenzura));
        lista.add(getResources().getString(R.string.njegos_i_pomorandze));
        lista.add(getResources().getString(R.string.njegos_i_zenenje));
        lista.add(getResources().getString(R.string.njegos_i_lanci));
        lista.add(getResources().getString(R.string.idi_na_lov_en));

        return lista;
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_zanimljivosti_framelayout,fragment);
        fragmentTransaction.commit();
    }
}