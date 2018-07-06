package com.example.duyda.test;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Picture;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {


    ImageView iv;
    WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        wv = (WebView) findViewById(R.id.webView);
//        iv = (ImageView) findViewById(R.id.imageView);

        wv=(WebView)findViewById(R.id.webView);
        wv.loadUrl("file:///android_asset/label_template_1.html");

      //  ImageView iv = (ImageView) findViewById(R.id.imageView);

        Bitmap test=getBitmapOfWebView(wv);

        iv.setImageBitmap(test);


    }



    private Bitmap getBitmapOfWebView(final WebView webView) {
        Picture picture = webView.capturePicture();
        Bitmap bitmap = Bitmap.createBitmap(picture.getWidth(), picture.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        picture.draw(canvas);
        return bitmap;
    }

}