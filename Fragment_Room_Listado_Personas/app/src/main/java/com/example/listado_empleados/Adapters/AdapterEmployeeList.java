package com.example.listado_empleados.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.listado_empleados.R;
import com.example.listado_empleados.Tuple.ClsPersonaConDepartamentoTuple;

import java.util.ArrayList;

public class AdapterEmployeeList extends BaseAdapter {
    private Context _context;
    private int _layout;
    private ArrayList<ClsPersonaConDepartamentoTuple> _items;

    public AdapterEmployeeList(Context context, int layout, ArrayList<ClsPersonaConDepartamentoTuple> items){
        _context = context;
        _layout = layout;
        _items = items;
    }

    @Override
    public int getCount(){
        return _items.size();
    }

    @Override
    public ClsPersonaConDepartamentoTuple getItem(int position){
        return _items.get(position);
    }

    @Override
    public long getItemId(int id){
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup){
        View v = convertView;
        ViewHolder holder;
        TextView personFirstName, personLastName, personPhone, personDepartament;
        ClsPersonaConDepartamentoTuple _item = getItem(position);

        if(v == null){
            LayoutInflater layoutInflater = LayoutInflater.from(this._context);//Inflamos la vista con nuestro propio layout
            v = layoutInflater.inflate(_layout, null);

            personFirstName = v.findViewById(R.id.ItemTextViewPersonFirstName);
            personLastName = v.findViewById(R.id.ItemTextViewPersonLastName);
            personPhone = v.findViewById(R.id.ItemTextViewPersonPhone);
            personDepartament = v.findViewById(R.id.ItemTextViewPersonDepartament);

            holder = new ViewHolder(personFirstName, personLastName, personPhone, personDepartament);//Almacenamos los datos en el holder
            v.setTag(holder);//Metemos el objeto en el tag de la vista
        }else{
            holder = (ViewHolder) v.getTag();
        }

        holder.get_personFirstName().setText(_item.get_nombre());
        holder.get_personLastName().setText(_item.get_apellidos());
        holder.get_personPhone().setText(_item.get_telefono());
        holder.get_personDepartament().setText(_item.get_departamento());
        return v;
    }

    public class ViewHolder{
        TextView _personFirstName, _personLastName, _personPhone, _personDepartament;

        private ViewHolder(TextView personFirstName, TextView personLastName, TextView personPhone, TextView personDepartament) {
            this._personFirstName = personFirstName;
            this._personLastName = personLastName;
            this._personPhone = personPhone;
            this._personDepartament = personDepartament;
        }

        private TextView get_personFirstName() {
            return _personFirstName;
        }

        private TextView get_personLastName() {
            return _personLastName;
        }

        private TextView get_personPhone() {
            return _personPhone;
        }

        private TextView get_personDepartament() {
            return _personDepartament;
        }
    }
}