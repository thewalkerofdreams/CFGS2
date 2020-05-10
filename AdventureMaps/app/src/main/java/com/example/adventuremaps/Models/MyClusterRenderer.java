package com.example.adventuremaps.Models;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import androidx.core.content.res.ResourcesCompat;

import com.example.adventuremaps.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class MyClusterRenderer extends DefaultClusterRenderer<MyClusterItem> {

    private Activity activity;

    public MyClusterRenderer(Activity activity, GoogleMap map, ClusterManager<MyClusterItem> clusterManager) {
        super(activity, map, clusterManager);
        this.activity = activity;
    }

    @Override
    protected void onBeforeClusterItemRendered(final MyClusterItem item, final MarkerOptions markerOptions) {

        activity.runOnUiThread(new Runnable() {//Insertamos el icono en un hilo secundario
            public void run() {
                BitmapDrawable bitmapdraw = null;

                if(item.getItemSelected()){//Si el item se encuentra seleccionado
                    bitmapdraw = (BitmapDrawable) ResourcesCompat.getDrawable(activity.getResources(), R.mipmap.blue_marker, null);
                }else{
                    switch (item.getTag()){
                        case "Fav":
                            bitmapdraw = (BitmapDrawable) ResourcesCompat.getDrawable(activity.getResources(), R.mipmap.marker_fav, null);
                            break;
                        case "Owner":
                            bitmapdraw = (BitmapDrawable) ResourcesCompat.getDrawable(activity.getResources(), R.mipmap.own_location, null);
                            break;
                        case "NoOwner":
                            bitmapdraw = (BitmapDrawable) ResourcesCompat.getDrawable(activity.getResources(), R.mipmap.simple_marker, null);
                            break;
                    }
                }

                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(bitmapdraw.getBitmap())));
            }
        });
    }

    @Override
    protected void onClusterItemRendered(MyClusterItem clusterItem, Marker marker) {
        super.onClusterItemRendered(clusterItem, marker);
        marker.setTag(clusterItem.getTag());//Insertamos el tag al marcador
    }
}
