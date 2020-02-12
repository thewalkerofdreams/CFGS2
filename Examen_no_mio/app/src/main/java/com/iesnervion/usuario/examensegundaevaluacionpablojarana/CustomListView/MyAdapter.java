package com.iesnervion.usuario.examensegundaevaluacionpablojarana.CustomListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;


import com.iesnervion.usuario.examensegundaevaluacionpablojarana.Models.FutbolistaConPosiciones;
import com.iesnervion.usuario.examensegundaevaluacionpablojarana.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Usuario on 31/10/2017.
 */

public class MyAdapter<T> extends ArrayAdapter
{
    private int idFila;
    public <T> MyAdapter(Context c, int resID, ArrayList<T> objects)
    {
        super(c,resID,objects);
        this.idFila=resID;
    }
    @Override
    public View getView(int pos, View convertView, ViewGroup parent)
    {
        DefaultViewHolder vh=null;
        FutbolistaConPosiciones futbolista=(FutbolistaConPosiciones) getItem(pos);
        if(convertView==null)
        {
            LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.row_style, parent, false);
            vh=new DefaultViewHolder(convertView);
            convertView.setTag(vh);
        }
        else
        {
            vh =(DefaultViewHolder) convertView.getTag();
        }

        if(futbolista!=null)
        {
            vh.getNombreJugador().setText(futbolista.getNombre());
            vh.getApellidosJugador().setText(futbolista.getApellidos());
            for(int i=0;i<futbolista.getPosiciones().size();i++)
            {
                vh.getPosicionesJugador().setText(vh.getPosicionesJugador().getText().toString()+", "+futbolista.getPosiciones().get(i).getDemarcarcion());
            }
        }
        return convertView;
    }
}
