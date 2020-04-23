package com.example.adventuremaps.Models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.example.adventuremaps.Management.ApplicationConstants;
import com.example.adventuremaps.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class MyClusterRenderer extends DefaultClusterRenderer<MyClusterItem> {

    private Context context;

    public MyClusterRenderer(Context context, GoogleMap map, ClusterManager<MyClusterItem> clusterManager) {
        super(context, map, clusterManager);
        this.context = context;
    }

    @Override
    protected void onBeforeClusterItemRendered(MyClusterItem item, MarkerOptions markerOptions) {

        BitmapDrawable bitmapdraw = null;
        Bitmap smallIcon = null;

        if(item.getItemSelected()){//Si el item se encuentra seleccionado
            bitmapdraw = (BitmapDrawable) context.getResources().getDrawable(Integer.valueOf(R.drawable.blue_marker));
        }else{
            switch (item.getTag()){
                case "Fav":
                    bitmapdraw = (BitmapDrawable) context.getResources().getDrawable(Integer.valueOf(R.drawable.marker_fav));
                    break;
                case "Owner":
                    bitmapdraw = (BitmapDrawable) context.getResources().getDrawable(Integer.valueOf(R.drawable.own_location));
                    break;
                case "NoOwner":
                    bitmapdraw = (BitmapDrawable) context.getResources().getDrawable(Integer.valueOf(R.drawable.simple_marker));
                    break;
            }
        }
        smallIcon = Bitmap.createScaledBitmap(bitmapdraw.getBitmap(), ApplicationConstants.MARKER_WITH_SIZE, ApplicationConstants.MARKER_HEIGHT_SIZE, false);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallIcon));
    }

    @Override
    protected void onClusterItemRendered(MyClusterItem clusterItem, Marker marker) {
        super.onClusterItemRendered(clusterItem, marker);
        marker.setTag(clusterItem.getTag());//Insertamos el tag al marcador
    }
}
