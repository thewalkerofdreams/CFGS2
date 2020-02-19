package es.iesnervion.yeray.pocketcharacters.Lists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsObject;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsObjectType;
import es.iesnervion.yeray.pocketcharacters.R;

public class AdapterObjectTypeList extends BaseAdapter {
    private Context _context;
    private int _layout;
    private ArrayList<ClsObjectType> _items;

    public AdapterObjectTypeList(Context context, int layout, ArrayList<ClsObjectType> items){
        _context = context;
        _layout = layout;
        _items = items;
    }

    @Override
    public int getCount(){
        return _items.size();
    }

    @Override
    public ClsObjectType getItem(int position){
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
        TextView objectType;
        ClsObjectType _item = getItem(position);

        if(v == null){
            LayoutInflater layoutInflater = LayoutInflater.from(this._context);//Inflamos la vista con nuestro propio layout
            v = layoutInflater.inflate(_layout, null);

            objectType = v.findViewById(R.id.TextListObjectTypeName);

            holder = new ViewHolder(objectType);//Almacenamos los datos en el holder
            v.setTag(holder);//Metemos el objeto en el tag de la vista
        }else{
            holder = (ViewHolder) v.getTag();
        }

        holder.get_objectType().setText(_item.get_name());
        return v;
    }

    public class ViewHolder{
        TextView _objectType;

        public ViewHolder(TextView objectType) {
            this._objectType = objectType;
        }

        public TextView get_objectType() {
            return _objectType;
        }
    }
}
