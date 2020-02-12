package es.iesnervion.yeray.fragments_jugadores.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import es.iesnervion.yeray.fragments_jugadores.POJO.ClsPlayerWithPositions;
import es.iesnervion.yeray.fragments_jugadores.R;

public class AdapterPlayerList extends BaseAdapter {
    private Context _context;
    private int _layout;
    private ArrayList<ClsPlayerWithPositions> _items;

    public AdapterPlayerList(Context context, int layout, ArrayList<ClsPlayerWithPositions> items){
        _context = context;
        _layout = layout;
        _items = items;
    }

    @Override
    public int getCount(){
        return _items.size();
    }

    @Override
    public ClsPlayerWithPositions getItem(int position){
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
        TextView firstName, lastName, positions;
        ClsPlayerWithPositions _item = getItem(position);

        if(v == null){
            LayoutInflater layoutInflater = LayoutInflater.from(this._context);//Inflamos la vista con nuestro propio layout
            v = layoutInflater.inflate(_layout, null);

            firstName = v.findViewById(R.id.txtPlayerFirstNameItem);
            lastName = v.findViewById(R.id.txtPlayerLastNameItem);
            positions = v.findViewById(R.id.txtPlayerPosicionesItem);

            holder = new ViewHolder(firstName, lastName, positions);//Almacenamos los datos en el holder
            v.setTag(holder);//Metemos el objeto en el tag de la vista
        }else{
            holder = (ViewHolder) v.getTag();
        }

        holder.get_firstName().setText(_item.player.get_nombre());
        holder.get_lastName().setText(_item.player.get_apellidos());
        holder.get_positions().setText(_item.getStringPositions());
        return v;
    }

    public class ViewHolder{
        TextView _firstName, _lastName, _positions;

        public ViewHolder(TextView firstName, TextView lastName, TextView positions) {
            this._firstName = firstName;
            this._lastName = lastName;
            this._positions = positions;
        }

        public TextView get_firstName() {
            return _firstName;
        }

        public TextView get_lastName() {
            return _lastName;
        }

        public TextView get_positions() {
            return _positions;
        }
    }
}