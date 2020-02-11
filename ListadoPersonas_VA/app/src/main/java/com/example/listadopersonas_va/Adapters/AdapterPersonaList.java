package com.example.listadopersonas_va.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.listadopersonas_va.DDBB_Entities.ClsPersona;
import com.example.listadopersonas_va.R;

import java.util.ArrayList;

public class AdapterPersonaList extends BaseAdapter {
    private Context _context;
    private int _layout;
    private ArrayList<ClsPersona> _items;

    public AdapterPersonaList(Context context, int layout, ArrayList<ClsPersona> items){
        _context = context;
        _layout = layout;
        _items = items;
    }

    @Override
    public int getCount(){
        return _items.size();
    }

    @Override
    public ClsPersona getItem(int position){
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
        TextView firstName, lastName, phone, dateOfBirth;
        ClsPersona _item = getItem(position);

        if(v == null){
            LayoutInflater layoutInflater = LayoutInflater.from(this._context);//Inflamos la vista con nuestro propio layout
            v = layoutInflater.inflate(_layout, null);

            firstName = v.findViewById(R.id.txtPersonFirstNameItemList);
            lastName = v.findViewById(R.id.txtPeronLastNameItemList);
            phone = v.findViewById(R.id.txtPeronPhoneItemList);
            dateOfBirth = v.findViewById(R.id.txtPeronBirthdayItemList);

            holder = new ViewHolder(firstName, lastName, phone, dateOfBirth);//Almacenamos los datos en el holder
            v.setTag(holder);//Metemos el objeto en el tag de la vista
        }else{
            holder = (ViewHolder) v.getTag();
        }

        holder.get_firstName().setText(_item.get_nombre());
        holder.get_lastName().setText(_item.get_apellidos());
        holder.get_phone().setText(_item.get_telefono());
        holder.get_dateOfBirth().setText(_item.obtenerFechaNacimientoCorta());
        return v;
    }

    public class ViewHolder{
        TextView _firstName, _lastName, _phone, _dateOfBirth;

        public ViewHolder(TextView firstName, TextView lastName, TextView phone, TextView dateOfBirth) {
            this._firstName = firstName;
            this._lastName = lastName;
            this._phone = phone;
            this._dateOfBirth = dateOfBirth;
        }

        public TextView get_firstName() {
            return _firstName;
        }

        public TextView get_lastName() {
            return _lastName;
        }

        public TextView get_phone() {
            return _phone;
        }

        public TextView get_dateOfBirth() {
            return _dateOfBirth;
        }
    }
}