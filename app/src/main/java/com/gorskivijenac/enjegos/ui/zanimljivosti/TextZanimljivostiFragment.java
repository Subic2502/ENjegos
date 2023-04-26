package com.gorskivijenac.enjegos.ui.zanimljivosti;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gorskivijenac.enjegos.R;


public class TextZanimljivostiFragment extends Fragment {

    TextView textView;

    public TextZanimljivostiFragment() {
        // Required empty public constructor
    }


    public static TextZanimljivostiFragment newInstance(String param1, String param2) {
        TextZanimljivostiFragment fragment = new TextZanimljivostiFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_text_zanimljivosti, container, false);

        textView = view.findViewById(R.id.fragment_zanimljivosti_textView);

        Bundle bundle = getArguments();
        String kljucnaRec = "";
        if (bundle != null) {
            kljucnaRec = bundle.getString("text");
        }

        String tekst;
        switch (kljucnaRec){
            case "sloboda":
                pronadjiTekst("sloboda");
                //textView.setText(tekst);
                break;
            case "pomorandze":
                pronadjiTekst("pomorandze");
                //textView.setText(tekst);
                break;
            case "zene":
                pronadjiTekst("zene");
                //textView.setText(tekst);
                break;
            case "lanci":
                pronadjiTekst("lanci");
                //textView.setText(tekst);
                break;
            case "cenzura":
                pronadjiTekst("cenzura");
                //textView.setText(tekst);
                break;

        }


        return view;
    }

    private void pronadjiTekst(String keyword) {
        String url = "https://stil.kurir.rs/lifestyle/zanimljivosti/33618/duhoviti-vladika-petar-petrovic-njegos-kakvog-do-sada-niste-znali";

        RequestQueue queue = Volley.newRequestQueue(getContext());

        // Request a string response from the provided URL
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    // Extract the desired paragraph based on the keyword
                    String startTag = "";
                    String endTag = "";

                    if (keyword.equals("sloboda")) {
                        startTag = "otvorim vrata";
                        endTag = "zastidjeti";
                    } else if (keyword.equals("pomorandze")) {
                        startTag = "Svagda bi";
                        endTag = "krišku od te!";
                    } else if (keyword.equals("zene")) {
                        startTag = "On (vladika)";
                        endTag = "i uživanja.";
                    } else if (keyword.equals("lanci")) {
                        startTag = "U crkvi Svetoga";
                        endTag = "ne ljube lance!";
                    } else if (keyword.equals("cenzura")) {
                        startTag = "Između ostalog";
                        endTag = "ti je gođ drago!";
                    }

                    int startIndex = response.indexOf(startTag);
                    int endIndex = response.indexOf(endTag, startIndex);
                    String paragraph = response.substring(startIndex, endIndex + endTag.length());

                    // Update the UI with the retrieved paragraph
                    textView.setText(paragraph);
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // odradi error
            }
        });

        // Add the request to the RequestQueue
        queue.add(stringRequest);
    }

}