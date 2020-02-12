package com.iesnervion.mynotes.ViewHolders;

import android.widget.TextView;

public class filaNotaVH {
    //Clase ViewHolder para el listView con las notas.
     private TextView tituloVH;
     private TextView fechaVH;

    //Constructores
    //Con parametros
    public filaNotaVH(TextView titulo, TextView fecha) {
        this.tituloVH = titulo;
        this.fechaVH = fecha;
    }

    //Getters
    public TextView getTituloVH() {
        return tituloVH;
    }

    public TextView getFechaVH() {
        return fechaVH;
    }
}
