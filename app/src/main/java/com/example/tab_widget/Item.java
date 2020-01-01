package com.example.tab_widget;

import android.graphics.Bitmap;
import android.net.Uri;

public class Item {

    private Uri uri;
    private String imageTitle;

    public Item(Uri uri, String imageTitle) {
        this.uri = uri;
        this.imageTitle = imageTitle;
    }

    public Uri getUri(){return uri;}

    public String getImageTitle() {
        return imageTitle;
    }
}