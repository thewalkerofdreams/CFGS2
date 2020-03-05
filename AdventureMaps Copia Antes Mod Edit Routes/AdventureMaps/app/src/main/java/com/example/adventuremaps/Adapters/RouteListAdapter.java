package com.example.adventuremaps.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.adventuremaps.FireBaseEntities.ClsRoute;
import com.example.adventuremaps.R;

import java.util.ArrayList;

public class RouteListAdapter extends BaseAdapter {
    private Context _context;
    private int _layout;
    private ArrayList<ClsRoute> _items;

    public RouteListAdapter(Context context, int layout, ArrayList<ClsRoute> items){
        _context = context;
        _layout = layout;
        _items = items;
    }

    @Override
    public int getCount(){
        return _items.size();
    }

    @Override
    public ClsRoute getItem(int position){
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
        TextView routeName, routeDateOfCreation;
        ImageButton routeFav;
        ClsRoute _item = getItem(position);

        if(v == null){
            LayoutInflater layoutInflater = LayoutInflater.from(this._context);//Inflamos la vista con nuestro propio layout
            v = layoutInflater.inflate(_layout, null);

            routeName = v.findViewById(R.id.RouteName);
            routeDateOfCreation = v.findViewById(R.id.RouteDateOfCreation);
            routeFav = v.findViewById(R.id.ImageButtonRouteFav);

            holder = new ViewHolder(routeName, routeDateOfCreation, routeFav);//Almacenamos los datos en el holder
            v.setTag(holder);//Metemos el objeto en el tag de la vista
        }else{
            holder = (ViewHolder) v.getTag();
        }

        holder.get_routeName().setText(_item.getName());
        holder.get_routeDateOfCreation().setText(String.valueOf(_item.getDateOfCreation()));
        if(_item.getFavourite()){//Si la ruta esta marcada como favorita
            holder.get_routeFav().setImageResource(R.drawable.fill_star);
        }else{
            holder.get_routeFav().setImageResource(R.drawable.empty_star);
        }

        return v;
    }

    public class ViewHolder{
        TextView _routeName, _routeDateOfCreation;
        ImageButton _routeFav;

        public ViewHolder(TextView routeName, TextView routeDateOfCreation, ImageButton routeFav) {
            this._routeName = routeName;
            this._routeDateOfCreation = routeDateOfCreation;
            this._routeFav = routeFav;
        }

        public TextView get_routeName() {
            return _routeName;
        }

        public TextView get_routeDateOfCreation() {
            return _routeDateOfCreation;
        }

        public ImageButton get_routeFav() {
            return _routeFav;
        }
    }
}
