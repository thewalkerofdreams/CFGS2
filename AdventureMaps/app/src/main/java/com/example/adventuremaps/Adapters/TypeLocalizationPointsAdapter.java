package com.example.adventuremaps.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.adventuremaps.R;

import java.util.ArrayList;

public class TypeLocalizationPointsAdapter extends BaseAdapter {
    private Context _context;
    private int _layout;
    private ArrayList<String> _items;

    public TypeLocalizationPointsAdapter(Context context, int layout, ArrayList<String> items){
        _context = context;
        _layout = layout;
        _items = items;
    }

    @Override
    public int getCount(){
        return _items.size();
    }

    @Override
    public String getItem(int position){
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
        TextView typeName;
        String _item = getItem(position);

        if(v == null){
            LayoutInflater layoutInflater = LayoutInflater.from(this._context);//Inflamos la vista con nuestro propio layout
            v = layoutInflater.inflate(_layout, null);

            typeName = v.findViewById(R.id.TextViewTypeLocalizationNameItemList);

            holder = new ViewHolder(typeName);//Almacenamos los datos en el holder
            v.setTag(holder);//Metemos el objeto en el tag de la vista
        }else{
            holder = (ViewHolder) v.getTag();
        }

        holder.get_typeName().setText(_item);
        return v;
    }

    public class ViewHolder{
        TextView _typeName;

        public ViewHolder(TextView typeName) {
            this._typeName = typeName;
        }

        public TextView get_typeName() {
            return _typeName;
        }
    }
}
