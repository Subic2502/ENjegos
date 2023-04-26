package com.gorskivijenac.enjegos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class PocetniFragment extends Fragment {

    ImageView slika;
    TextView dobrodoslica;
    TextView uputstvo;
    TextView materijal;

    public PocetniFragment() {
        // Required empty public constructor
    }


    public static PocetniFragment newInstance(String param1, String param2) {
        PocetniFragment fragment = new PocetniFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pocetni, container, false);

        slika = new ImageView(getContext());
        slika.findViewById(R.id.fragment_pocetni_slika);

        dobrodoslica = view.findViewById(R.id.fragment_pocetni);
        uputstvo = view.findViewById(R.id.fragment_pocetni_textOpisa);
        materijal = view.findViewById(R.id.pocetni_fragment_materijal);


        return view;
    }
}