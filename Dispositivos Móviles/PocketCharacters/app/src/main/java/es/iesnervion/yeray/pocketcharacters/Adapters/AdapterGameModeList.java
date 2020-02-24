package es.iesnervion.yeray.pocketcharacters.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsGameMode;
import es.iesnervion.yeray.pocketcharacters.R;

public class AdapterGameModeList extends BaseAdapter {
    private Context _context;
    private int _layout;
    private ArrayList<ClsGameMode> _items;

    public AdapterGameModeList(Context context, int layout, ArrayList<ClsGameMode> items){
        _context = context;
        _layout = layout;
        _items = items;
    }

    @Override
    public int getCount(){
        return _items.size();
    }

    @Override
    public ClsGameMode getItem(int position){
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
        TextView gameModeName;
        ClsGameMode _item = getItem(position);

        if(v == null){
            LayoutInflater layoutInflater = LayoutInflater.from(this._context);//Inflamos la vista con nuestro propio layout
            v = layoutInflater.inflate(_layout, null);

            gameModeName = v.findViewById(R.id.TextViewGameModeName);

            holder = new ViewHolder(gameModeName);//Almacenamos los datos en el holder
            v.setTag(holder);//Metemos el objeto en el tag de la vista
        }else{
            holder = (ViewHolder) v.getTag();
        }

        holder.get_gameModeName().setText(_item.get_name());
        return v;
    }

    public class ViewHolder{
        TextView _gameModeName;

        public ViewHolder(TextView gameModeName) {
            this._gameModeName = gameModeName;
        }

        public TextView get_gameModeName() {
            return _gameModeName;
        }
    }
}
