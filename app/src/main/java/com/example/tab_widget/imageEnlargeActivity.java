package com.example.tab_widget;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class imageEnlargeActivity extends AppCompatActivity {
    private TextView enlargeTitle;
    private ImageView enlargeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subactivity_enlarge);

        enlargeImage = (ImageView) findViewById(R.id.enlargeImage);
        enlargeTitle = (TextView) findViewById(R.id.enlargeTitle);


        //receive Data
        Intent intent = getIntent();
        String title = intent.getExtras().getString("title");
        String image = intent.getExtras().getString("image");
        if (image == null) {
            Log.d("nullpoint error", "Yes");
        } else
        Log.d("nullpoint error", "No");
        enlargeTitle.setText(title);
        enlargeImage.setImageURI(Uri.parse(image));
    }
}
