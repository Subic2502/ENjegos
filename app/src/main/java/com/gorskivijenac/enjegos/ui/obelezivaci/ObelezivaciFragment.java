package com.gorskivijenac.enjegos.ui.obelezivaci;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.gorskivijenac.enjegos.R;
import com.gorskivijenac.enjegos.database.DatabaseHelper;
import com.gorskivijenac.enjegos.database.Omiljeno;
import com.gorskivijenac.enjegos.database.Poglavlje;
import com.gorskivijenac.enjegos.database.StihItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class ObelezivaciFragment extends Fragment {

    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

    Spinner spinner;
    Button buttonIzaberi;
    ImageView slika;
    Button buttonPreuzmi;

    Bitmap mutabilnaBitmapa;

    //-----------------------------------------

    private SQLiteDatabase mDatabase;
    private DatabaseHelper mDbHelper;


    public ObelezivaciFragment() {
        // Required empty public constructor
    }


    public static ObelezivaciFragment newInstance(String param1, String param2) {
        ObelezivaciFragment fragment = new ObelezivaciFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_obelezivaci, container, false);

        spinner = view.findViewById(R.id.fragment_obelezivaci_spinner);
        List<Omiljeno> listaOmiljenih = new ArrayList<>();
        getOmiljeni(listaOmiljenih);

        ArrayAdapter<Omiljeno> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, listaOmiljenih);
        spinner.setAdapter(adapter);

        buttonIzaberi = view.findViewById(R.id.fragment_obelezivaci_button);
        slika = view.findViewById(R.id.idIVImage);
        buttonPreuzmi = view.findViewById(R.id.fragment_obelezivaci_buttonPreuzmi);

        pickMedia = registerForActivityResult(
                new ActivityResultContracts.PickVisualMedia(), uri -> {
                    if (uri != null) {
                        /*Glide.with(this)
                                .load(uri)
                                .into(slika);*/

                        //Tekst iz spinera
                        Omiljeno selectedOmiljeno = (Omiljeno) spinner.getSelectedItem();
                        String selectedText = selectedOmiljeno.getStih().getBrojStiha() + ":" + selectedOmiljeno.getStih().getTextStiha() + " - Petar II Petrović Njegoš" ;


                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mutabilnaBitmapa = bitmap.copy(Bitmap.Config.ARGB_8888, true);

                        int bitmapWidth = mutabilnaBitmapa.getWidth();
                        int bitmapHeight = mutabilnaBitmapa.getHeight();


                        // Platno za bitmapu
                        Canvas canvas = new Canvas(mutabilnaBitmapa);

                        // podesavanja pisanja po platnu
                        TextPaint paint = new TextPaint();
                        paint.setColor(Color.WHITE);
                        paint.setTextSize(75);
                        paint.setShadowLayer(1f, 0f, 1f, Color.BLACK);

                        // Dobijanje granica za tekst
                        Rect granice = new Rect();
                        paint.getTextBounds(selectedText, 0, selectedText.length(), granice);

                        // Koordinate centra slike
                        float x = (bitmapWidth - granice.width()) / 2f;
                        float y = (bitmapHeight + granice.height()) / 2f;

                        // setovati duzinu teksta sa duzinom bitmape
                        int textDuzina = bitmapWidth;

                        // metoda da razbije tekst na vise redova ukoliko je potrebno
                        StaticLayout staticLayout = new StaticLayout(selectedText, paint, textDuzina,
                                Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);

                        // Draw the text on the canvas
                        canvas.save();
                        canvas.translate(0, y - staticLayout.getHeight() / 2);
                        staticLayout.draw(canvas);
                        canvas.restore();

                        // stavljanje slike u okvir za sliku
                        slika.setImageBitmap(mutabilnaBitmapa);
                    }
                    else {
                        System.out.println("Nista nije izabrano");
                    }
                });

        buttonIzaberi.setOnClickListener(view1 -> {
            launchPhotoPicker();
        });

        buttonPreuzmi.setOnClickListener(view1 -> {
            preuzmiSliku();
        });

        return view;
    }

    private void preuzmiSliku() {
        Random random = new Random();
        int broj = random.nextInt(100000);

        //nacin bez ContentProvidera
        /*File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), broj+".jpg");
        OutputStream os = null;
        try{
            os = new FileOutputStream(file);

            mutabilnaBitmapa.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        }catch (Exception e){
            throw new RuntimeException();
        }

        MediaScannerConnection.scanFile(getContext(),
                new String[] { file.getAbsolutePath() }, null,
                (path, uri) -> {
                    // dodata slika u galeriju
                });*/


        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, String.valueOf(broj));
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        Uri uri = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        try {
            OutputStream out = getContext().getContentResolver().openOutputStream(uri);
            mutabilnaBitmapa.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void getOmiljeni(List<Omiljeno> listaOmiljenih) {
        listaOmiljenih.clear();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        Gson gson = new Gson();

        // Get all keys from SharedPreferences
        Map<String, ?> allEntries = sharedPreferences.getAll();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String key = entry.getKey();

            // Check if the key starts with "stih-"
            if (key.startsWith("stih-")) {
                // Get the value from SharedPreferences
                String stihJson = entry.getValue().toString();
                StihItem stihItem = gson.fromJson(stihJson, StihItem.class);

                // Get the comment value from SharedPreferences
                String komentar = sharedPreferences.getString(key+"komentar", "");

                // Add to the list
                listaOmiljenih.add(new Omiljeno(stihItem, komentar));
            }
        }
    }


    private StihItem getStihFromId(int id) {
        mDbHelper = new DatabaseHelper(getContext());

        mDatabase = mDbHelper.getReadableDatabase();

        String[] kolone = {"brojStiha","textStiha"};
        String selection = "s.ID = ?";
        String[] selectionArgs = { String.valueOf(id) };
        Cursor cursor = mDatabase.query("Stih s", kolone, selection, selectionArgs, null, null, null);

        int brojStiha = 0;
        String textStiha = "";

        while (cursor.moveToNext()) {
            brojStiha = cursor.getInt(cursor.getColumnIndexOrThrow(kolone[0]));
            textStiha = cursor.getString(cursor.getColumnIndexOrThrow(kolone[1]));
        }

        cursor.close();
        mDatabase.close();
        return new StihItem(String.valueOf(brojStiha),textStiha);
    }

    private void launchPhotoPicker() {
        PickVisualMediaRequest request = new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build();

        pickMedia.launch(request);
    }


}