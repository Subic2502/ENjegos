package com.gorskivijenac.enjegos.ui.citaj;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gorskivijenac.enjegos.OznaciKaoOmiljeno;
import com.gorskivijenac.enjegos.R;
import com.gorskivijenac.enjegos.StihoviPoKriterijumu;
import com.gorskivijenac.enjegos.Tumacenje;
import com.gorskivijenac.enjegos.database.DatabaseHelper;
import com.gorskivijenac.enjegos.database.StihItem;

import java.util.List;

public class StihAdapter extends RecyclerView.Adapter<StihAdapter.ViewHolder> {
    List<StihItem> stihItems;

    public StihAdapter(List<StihItem> stihItems) {
        this.stihItems = stihItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_stih, parent, false);
        return new ViewHolder(view,stihItems);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StihItem stihItem = stihItems.get(position);
        if(stihItem.getTextStiha().contains("Poglavlje")){
            holder.brojStihaTextView.setText(stihItem.getBrojStiha());
            holder.textStihaTextView.setText(stihItem.getTextStiha());
            holder.brojStihaTextView.setTypeface(null, Typeface.BOLD);
            holder.textStihaTextView.setTypeface(null, Typeface.BOLD);
        }else{
            holder.brojStihaTextView.setText(stihItem.getBrojStiha());
            holder.textStihaTextView.setText(stihItem.getTextStiha());

            holder.textStihaTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(view.getContext(), holder.textStihaTextView);

                    popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(menuItem -> {
                        if(menuItem.getTitle().equals("Označi kao omiljen")){

                            Intent intent = new Intent(view.getContext(), OznaciKaoOmiljeno.class);
                            intent.putExtra("brojStiha", stihItem.getBrojStiha());
                            view.getContext().startActivity(intent);

                        }else if(menuItem.getTitle().equals("Podeli citat")){
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("text/plain");
                            intent.putExtra(Intent.EXTRA_TEXT, "Vidi stih koji zelim da podelim sa tobom: \n"+stihItem.getTextStiha());

                            view.getContext().startActivity(Intent.createChooser(intent, "Share via"));

                        }else{
                            Intent intent = new Intent(view.getContext(), Tumacenje.class);
                            intent.putExtra("brojStiha",stihItem.getBrojStiha() );

                            view.getContext().startActivity(intent);
                        }
                        return true;
                    });
                    popupMenu.show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return stihItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView brojStihaTextView;
        public TextView textStihaTextView;

        public ViewHolder(@NonNull View itemView,List<StihItem> stihItems) {
            super(itemView);
            brojStihaTextView = itemView.findViewById(R.id.textView_brojStiha);
            textStihaTextView = itemView.findViewById(R.id.textView_textStiha);
        }
    }
}
