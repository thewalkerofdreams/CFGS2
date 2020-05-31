package com.example.adventuremaps.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.adventuremaps.Models.ClsLocalizationPointWithFav;
import com.example.adventuremaps.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LocalizationListAdapter extends BaseAdapter {
    private Context _context;
    private int _layout;
    private ArrayList<ClsLocalizationPointWithFav> _items;

    public LocalizationListAdapter(Context context, int layout, ArrayList<ClsLocalizationPointWithFav> items){
        _context = context;
        _layout = layout;
        _items = items;
    }

    @Override
    public int getCount(){
        return _items.size();
    }

    @Override
    public ClsLocalizationPointWithFav getItem(int position){
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
        TextView localizationName, localizationDateOfCreation;
        ImageButton localizationFav, localizationNavigate;
        ClsLocalizationPointWithFav _item = getItem(position);

        if(v == null){
            LayoutInflater layoutInflater = LayoutInflater.from(this._context);//Inflamos la vista con nuestro propio layout
            v = layoutInflater.inflate(_layout, null);

            localizationName = v.findViewById(R.id.LocalizationName);
            localizationDateOfCreation = v.findViewById(R.id.LocalizationDateOfCreation);
            localizationFav = v.findViewById(R.id.ImageButtonLocalizationFav);
            localizationNavigate = v.findViewById(R.id.ImageButtonNavigateLocalization);

            holder = new ViewHolder(localizationName, localizationDateOfCreation, localizationFav, localizationNavigate);//Almacenamos los datos en el holder
            v.setTag(holder);//Metemos el objeto en el tag de la vista
        }else{
            holder = (ViewHolder) v.getTag();
        }

        holder.get_localizationName().setText(_item.get_localizationPoint().getName());

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");//Convertimos la fecha del tipo Long a Date
        Date resultdate = new Date(_item.get_localizationPoint().getDateOfCreation());
        holder.get_localizationDateOfCreation().setText(sdf.format(resultdate));

        holder.get_localizationFav().setTag(_item.get_localizationPoint().getLocalizationPointId());//Almacenamos el id de la ruta que representa el item en un tag del botón, para realizar los posteriores cambios de Fav.
        if(_item.is_favourite()){//Si la ruta esta marcada como favorita
            holder.get_localizationFav().setBackgroundResource(R.drawable.fill_star);
        }else{
            holder.get_localizationFav().setBackgroundResource(R.drawable.empty_star);
        }

        holder.get_localizationNavigate().setTag(_item.get_localizationPoint().getLocalizationPointId());//Almacenamos el id en un tag para una posterior navegación

        return v;
    }

    public class ViewHolder{
        TextView _localizationName, _localizationDateOfCreation;
        ImageButton _localizationFav, _localizationNavigate;

        public ViewHolder(TextView localizationName, TextView localizationDateOfCreation, ImageButton localizationFav, ImageButton localizationNavigate) {
            this._localizationName = localizationName;
            this._localizationDateOfCreation = localizationDateOfCreation;
            this._localizationFav = localizationFav;
            this._localizationNavigate = localizationNavigate;
        }

        public TextView get_localizationName() {
            return _localizationName;
        }

        public TextView get_localizationDateOfCreation() {
            return _localizationDateOfCreation;
        }

        public ImageButton get_localizationFav() {
            return _localizationFav;
        }

        public ImageButton get_localizationNavigate() {
            return _localizationNavigate;
        }
    }
}
