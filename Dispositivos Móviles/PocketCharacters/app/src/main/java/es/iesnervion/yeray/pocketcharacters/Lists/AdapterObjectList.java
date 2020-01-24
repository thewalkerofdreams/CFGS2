package es.iesnervion.yeray.pocketcharacters.Lists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import es.iesnervion.yeray.pocketcharacters.EntitiesModels.ClsObjectAndQuantity;
import es.iesnervion.yeray.pocketcharacters.R;

public class AdapterObjectList extends BaseAdapter {
    private Context _context;
    private int _layout;
    private ArrayList<ClsObjectAndQuantity> _items;

    public AdapterObjectList(Context context, int layout, ArrayList<ClsObjectAndQuantity> items){
        _context = context;
        _layout = layout;
        _items = items;
    }

    @Override
    public int getCount(){
        return _items.size();
    }

    @Override
    public ClsObjectAndQuantity getItem(int position){
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
        TextView objectType, objectName, quantity;
        ClsObjectAndQuantity _item = getItem(position);

        if(v == null){
            LayoutInflater layoutInflater = LayoutInflater.from(this._context);//Inflamos la vista con nuestro propio layout
            v = layoutInflater.inflate(_layout, null);

            objectType = v.findViewById(R.id.TextViewListObjectType);
            objectName = v.findViewById(R.id.TextViewListObjectName);
            quantity = v.findViewById(R.id.TextViewListObjectQuantity);

            holder = new ViewHolder(objectType, objectName, quantity);//Almacenamos los datos en el holder
            v.setTag(holder);//Metemos el objeto en el tag de la vista
        }else{
            holder = (ViewHolder) v.getTag();
        }

        holder.get_objectType().setText(_item.get_object().get_type());
        holder.get_objectName().setText(_item.get_object().get_name());
        holder.get_quantity().setText(_item.get_quantity());
        return v;
    }

    public class ViewHolder{
        TextView _objectType, _objectName, _quantity;

        public ViewHolder(TextView objectType, TextView objectName, TextView quantity) {
            this._objectType = objectType;
            this._objectName = objectName;
            this._quantity = quantity;
        }

        public TextView get_objectType() {
            return _objectType;
        }

        public TextView get_objectName() {
            return _objectName;
        }

        public TextView get_quantity() {
            return _quantity;
        }
    }
}
