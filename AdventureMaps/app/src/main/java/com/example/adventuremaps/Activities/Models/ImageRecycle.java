package com.example.adventuremaps.Activities.Models;

import android.graphics.Bitmap;
import android.net.Uri;

public class ImageRecycle {
    private Uri image;
    private boolean imageLoaded;

    public ImageRecycle(Uri image){
        this.image = image;
        imageLoaded = false;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public boolean isImageLoaded() {
        return imageLoaded;
    }

    public void setImageLoaded(boolean imageLoaded) {
        this.imageLoaded = imageLoaded;
    }
}