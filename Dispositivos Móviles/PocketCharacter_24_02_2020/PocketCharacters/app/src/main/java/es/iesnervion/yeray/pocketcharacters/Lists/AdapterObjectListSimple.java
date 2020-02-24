package es.iesnervion.yeray.pocketcharacters.Lists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsObject;
import es.iesnervion.yeray.pocketcharacters.R;

public class AdapterObjectListSimple extends BaseAdapter {
    private Context _context;
    private int _layout;
    private ArrayList<ClsObject> _items;

    public AdapterObjectListSimple(Context context, int layout, ArrayList<ClsObject> items){
        _context = context;
        _layout = layout;
        _items = items;
    }

    @Override
    public int getCount(){
        return _items.size();
    }

    @Override
    public ClsObject getItem(int position){
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
        TextView objectType, objectName;
        ClsObject _item = getItem(position);

        if(v == null){
            LayoutInflater layoutInflater = LayoutInflater.from(this._context);//Inflamos la vista con nuestro propio layout
            v = layoutInflater.inflate(_layout, null);

            objectType = v.findViewById(R.id.TextViewListObjectSimpleType);
            objectName = v.findViewById(R.id.TextViewListObjectSimpleName);

            holder = new ViewHolder(objectType, objectName);//Almacenamos los datos en el holder
            v.setTag(holder);//Metemos el objeto en el tag de la vista
        }else{
            holder = (ViewHolder) v.getTag();
        }

        holder.get_objectType().setText(_item.get_type());
        holder.get_objectName().setText(_item.get_name());
        return v;
    }

    public class ViewHolder{
        TextView _objectType, _objectName;

        public ViewHolder(TextView objectType, TextView objectName) {
            this._objectType = objectType;
            this._objectName = objectName;
        }

        public TextView get_objectType() {
            return _objectType;
        }

        public TextView get_objectName() {
            return _objectName;
        }
    }
}
