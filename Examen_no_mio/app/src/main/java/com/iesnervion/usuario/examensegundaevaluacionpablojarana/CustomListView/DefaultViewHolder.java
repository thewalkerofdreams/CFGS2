package com.iesnervion.usuario.examensegundaevaluacionpablojarana.CustomListView;

import android.view.View;
import android.widget.TextView;

import com.iesnervion.usuario.examensegundaevaluacionpablojarana.R;


/**
 * Created by Usuario on 31/10/2017.
 */

public class DefaultViewHolder //A su cosntructor le pasaremos el converView y los R.id.loquesea dependiendo lo q tengamos en el layout de la fila
{
    private TextView nombreJugador;
    private TextView apellidosJugador;
    private TextView posicionesJugador;

    public DefaultViewHolder(View row)
    {
        this.nombreJugador =(TextView)row.findViewById(R.id.nombreJugador);
        this.apellidosJugador=(TextView)row.findViewById(R.id.apellidosJugador);
        this.posicionesJugador=(TextView)row.findViewById(R.id.posicionesJugador);
    }

    public TextView getNombreJugador() {
        return nombreJugador;
    }

    public void setNombreJugador(TextView nombreJugador) {
        this.nombreJugador = nombreJugador;
    }

    public TextView getApellidosJugador() {
        return apellidosJugador;
    }

    public void setApellidosJugador(TextView apellidosJugador) {
        this.apellidosJugador = apellidosJugador;
    }

    public TextView getPosicionesJugador() {
        return posicionesJugador;
    }

    public void setPosicionesJugador(TextView posicionesJugador) {
        this.posicionesJugador = posicionesJugador;
    }
}