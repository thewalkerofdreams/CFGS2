package es.iesnervion.yeray.pocketcharacters.Lists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsStat;
import es.iesnervion.yeray.pocketcharacters.R;

public class AdapterStatList extends BaseAdapter {

    private Context _context;
    private int _layout;
    private ArrayList<ClsStat> _items;

    public AdapterStatList(Context context, int layout, ArrayList<ClsStat> items){
        _context = context;
        _layout = layout;
        _items = items;
    }

    @Override
    public int getCount(){
        return _items.size();
    }

    @Override
    public ClsStat getItem(int position){
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
        TextView statName;
        ClsStat _item = getItem(position);

        if(v == null){
            LayoutInflater layoutInflater = LayoutInflater.from(this._context);//Inflamos la vista con nuestro propio layout
            v = layoutInflater.inflate(_layout, null);

            statName = v.findViewById(R.id.TextViewGameModeName);

            holder = new ViewHolder(statName);//Almacenamos los datos en el holder
            v.setTag(holder);//Metemos el objeto en el tag de la vista
        }else{
            holder = (ViewHolder) v.getTag();
        }

        holder.get_statName().setText(_item.get_name());
        return v;
    }

    public class ViewHolder{
        TextView _statName;

        public ViewHolder(TextView statName) {
            this._statName = statName;
        }

        public TextView get_statName() {
            return _statName;
        }
    }
}
