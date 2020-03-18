package com.example.adventuremaps.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.adventuremaps.Activities.Models.ClsImageWithId;
import com.example.adventuremaps.R;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ClsImageWithId> items;

    public ImageAdapter(Context context, ArrayList<ClsImageWithId> items){
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        ImageView imageView;
        View v = convertView;

        if (v == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);//Inflamos la vista con nuestro propio layout
            v = layoutInflater.inflate(R.layout.item_image_gallery, null);

            imageView = v.findViewById(R.id.ImageViewItem);

            holder = new ViewHolder(imageView);
            v.setTag(holder);//Metemos el objeto en el tag de la vista
        } else {
            holder = (ViewHolder) v.getTag();
        }
        holder.get_imageView().setImageBitmap(items.get(position).get_bitmap());

        return v;
    }

    public class ViewHolder{
        ImageView _imageView;

        public ViewHolder(ImageView imageView) {
            this._imageView = imageView;
        }

        public ImageView get_imageView() {
            return _imageView;
        }
    }
}
