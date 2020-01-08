package es.iesnervion.yeray.clienteapirest.Lists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import es.iesnervion.yeray.clienteapirest.Entities.Book;
import es.iesnervion.yeray.clienteapirest.R;

public class AdapterBookList extends BaseAdapter {
    private Context _context;
    private ArrayList<Book> _items;

    public AdapterBookList(Context context, ArrayList<Book> items){
        _context = context;
        _items = items;
    }

    @Override
    public int getCount(){
        return _items.size();
    }

    @Override
    public Book getItem(int position){
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
        TextView code, name, numPages;
        Book _item = getItem(position);

        if(v == null){
            //Inflamos la vista con nuestro propio layout
            LayoutInflater layoutInflater = LayoutInflater.from(this._context);
            v = layoutInflater.inflate(R.layout.item_list, null);

            code = v.findViewById(R.id.CodeBook);
            name = v.findViewById(R.id.NameBook);
            numPages = v.findViewById(R.id.NumPagesBook);

            //Almacenamos los datos en el holder
            holder = new ViewHolder(code, name, numPages);
            //Metemos el objeto en el tag de la vista
            v.setTag(holder);
        }else{
            holder = (ViewHolder) v.getTag();
        }

        holder.get_code().setText(_item.getCode());
        holder.get_name().setText(_item.getName());
        holder.get_numPages().setText(_item.getNumPages());
        return v;
    }

    public class ViewHolder{
        TextView _code, _name, _numPages;

        public ViewHolder(){
            _code = null;
            _name = null;
            _numPages = null;
        }

        public ViewHolder(TextView code, TextView name, TextView numPages) {
            this._code = code;
            this._name = name;
            this._numPages = numPages;
        }

        private TextView get_code() {
            return _code;
        }

        private TextView get_name() {
            return _name;
        }

        private TextView get_numPages() {
            return _numPages;
        }
    }

}
