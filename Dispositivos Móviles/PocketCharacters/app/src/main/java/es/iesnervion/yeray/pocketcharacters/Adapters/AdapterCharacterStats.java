package es.iesnervion.yeray.pocketcharacters.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import es.iesnervion.yeray.pocketcharacters.EntitiesModels.ClsStatModel;
import es.iesnervion.yeray.pocketcharacters.R;

public class AdapterCharacterStats extends BaseAdapter {
    private Context _context;
    private int _layout;
    private ArrayList<ClsStatModel> _items;

    public AdapterCharacterStats(Context context, int layout, ArrayList<ClsStatModel> items){
        _context = context;
        _layout = layout;
        _items = items;
    }

    @Override
    public int getCount(){
        return _items.size();
    }

    @Override
    public ClsStatModel getItem(int position){
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
        TextView statName, value;
        ClsStatModel _item = getItem(position);

        if(v == null){
            LayoutInflater layoutInflater = LayoutInflater.from(this._context);//Inflamos la vista con nuestro propio layout
            v = layoutInflater.inflate(_layout, null);

            statName = v.findViewById(R.id.TextViewStatName);
            value = v.findViewById(R.id.TextViewStatValue);

            holder = new ViewHolder(statName, value);//Almacenamos los datos en el holder
            v.setTag(holder);//Metemos el objeto en el tag de la vista
        }else{
            holder = (ViewHolder) v.getTag();
        }

        holder.get_statName().setText(_item.get_name());
        holder.get_value().setText(_item.get_value());
        return v;
    }

    public class ViewHolder{
        TextView _statName, _value;

        public ViewHolder(TextView statName, TextView value) {
            this._statName = statName;
            this._value = value;
        }

        public TextView get_statName() {
            return _statName;
        }

        public TextView get_value() {
            return _value;
        }
    }
}
