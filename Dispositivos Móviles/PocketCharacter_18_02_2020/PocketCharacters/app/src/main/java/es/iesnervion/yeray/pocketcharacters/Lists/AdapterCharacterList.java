package es.iesnervion.yeray.pocketcharacters.Lists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsCharacter;
import es.iesnervion.yeray.pocketcharacters.R;

public class AdapterCharacterList extends BaseAdapter {
    private Context _context;
    private int _layout;
    private ArrayList<ClsCharacter> _items;

    public AdapterCharacterList(Context context, int layout, ArrayList<ClsCharacter> items){
        _context = context;
        _layout = layout;
        _items = items;
    }

    @Override
    public int getCount(){
        return _items.size();
    }

    @Override
    public ClsCharacter getItem(int position){
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
        TextView characterName, gameModeName, chapterName, creationDate;
        ClsCharacter _item = getItem(position);

        if(v == null){
            LayoutInflater layoutInflater = LayoutInflater.from(this._context);//Inflamos la vista con nuestro propio layout
            v = layoutInflater.inflate(_layout, null);

            characterName = v.findViewById(R.id.TextViewCharacterName);
            gameModeName = v.findViewById(R.id.TextViewGameModeName);
            chapterName = v.findViewById(R.id.TextViewChapterName);
            //creationDate = v.findViewById(R.id.TextViewCreationDate);

            holder = new ViewHolder(characterName, gameModeName, chapterName);//Almacenamos los datos en el holder
            v.setTag(holder);//Metemos el objeto en el tag de la vista
        }else{
            holder = (ViewHolder) v.getTag();
        }

        holder.get_characterName().setText(_item.get_characterName());
        holder.get_gameModeName().setText(_item.get_gameMode());
        holder.get_chapterName().setText(_item.get_chapterName());
        //holder.get_creationDate().setText(_item.get_creationDate().toString());
        return v;
    }

    public class ViewHolder{
        TextView _characterName, _gameModeName, _chapterName, _creationDate;

        public ViewHolder(TextView characterName, TextView gameModeName, TextView chapterName) {
            this._characterName = characterName;
            this._gameModeName = gameModeName;
            this._chapterName = chapterName;
            //this._creationDate = creationDate;
        }

        public TextView get_characterName() {
            return _characterName;
        }

        public TextView get_gameModeName() {
            return _gameModeName;
        }

        public TextView get_chapterName() {
            return _chapterName;
        }

        /*public TextView get_creationDate() {
            return _creationDate;
        }*/
    }
}
