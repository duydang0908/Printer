package com.example.duyda.asd;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String html = "<html><body>Hello, World!</body></html>";
        String mime = "text/html";
        String encoding = "utf-8";

        final WebView myWebView = (WebView)this.findViewById(R.id.myWebView);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.loadDataWithBaseURL(null, html, mime, encoding, null);

        Button printButton = (Button)findViewById(R.id.print_button);
        printButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createWebPrintJob(myWebView);
            }
        });
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void createWebPrintJob(WebView webView) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            PrintManager printManager = (PrintManager) this
                    .getSystemService(Context.PRINT_SERVICE);

            PrintDocumentAdapter printAdapter =
                    null;
            printAdapter = webView.createPrintDocumentAdapter("MyDocument");
            String jobName = getString(R.string.app_name) + " Print Test";

            printManager.print(jobName, printAdapter,
                    new PrintAttributes().Builder().build());
        }
        else{
            Toast.makeText(MainActivity.this, "Print job has been canceled! ", Toast.LENGTH_SHORT).show();
        }

    }
}
