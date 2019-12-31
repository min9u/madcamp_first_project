package com.example.tab_widget;

import android.graphics.Bitmap;

public class Item {

    private Bitmap image;
    private String imageTitle;

    public Item(Bitmap image, String imageTitle) {
        this.image = image;
        this.imageTitle = imageTitle;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getImageTitle() {
        return imageTitle;
    }
}