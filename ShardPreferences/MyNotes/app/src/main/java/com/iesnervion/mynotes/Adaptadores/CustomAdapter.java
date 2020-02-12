package com.iesnervion.mynotes.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.iesnervion.mynotes.Entidades.ClsNota;
import com.iesnervion.mynotes.R;
import com.iesnervion.mynotes.ViewHolders.filaNotaVH;
import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<ClsNota> listaNotas;

    public CustomAdapter(Context context, ArrayList<ClsNota> listaNotas) {
        this.context = context;
        this.listaNotas = listaNotas;
    }

    @Override
    public int getCount() {
        int tam;
        //Asi si no hay notas no se cierra la App.
        if(listaNotas == null){
            tam = 0;
        }else{
            tam = listaNotas.size();
        }

        return tam;
    }

    @Override
    public Object getItem(int position) {
        return listaNotas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        TextView title;
        TextView date;
        filaNotaVH holder;
        View row = convertView;

        ClsNota nota = listaNotas.get(position);

        if(row == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            row = inflater.inflate(R.layout.row, parent, false);

            //Creamos las referencias
            title = row.findViewById(R.id.tvTitulo);
            date = row.findViewById(R.id.tvFechaMod);
            holder = new filaNotaVH(title, date);

            row.setTag(holder);
        }else{
            holder = (filaNotaVH) row.getTag();
        }

        //Establecemos el contenido de las vistas
        holder.getTituloVH().setText(nota.getTitulo());
        holder.getFechaVH().setText(nota.getFecha());

        return row;
    }
}
