package vn.com.dne.printerdemopt;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SizeF;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import vn.com.dne.dnesdk.PrinterManager;
import vn.com.dne.dnesdk.model.Device;

public class MainActivity extends AppCompatActivity {

    Button button;
    PrinterManager printerManager;
    CheckBox autoCut, cutAtEnd, halfCut, specialTape;
    EditText size;
    Spinner spinSize, spinPrinter, spinHTML;
    WebView wv;
//    ImageView iv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        printerManager = PrinterManager.getInstance(this);
        button = (Button) findViewById(R.id.button);
        autoCut = (CheckBox) findViewById(R.id.chbxAutoCut);
        cutAtEnd = (CheckBox) findViewById(R.id.chbxCutAtEnd);
        halfCut = (CheckBox) findViewById(R.id.chbxHalfCut);
        specialTape = (CheckBox) findViewById(R.id.chbxSpecialTape);

        //Select size
        spinSize = (Spinner) findViewById(R.id.spinnerSize);

        ArrayAdapter<String> sizePrint = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.names));
        sizePrint.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinSize.setAdapter(sizePrint);

        //Select printer
        spinPrinter = (Spinner) findViewById(R.id.spinnerPrinter);

        ArrayAdapter<String> printer = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.names1));
        printer.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinPrinter.setAdapter(printer);

        spinHTML = (Spinner) findViewById(R.id.html);

        final ArrayAdapter<String> html = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.names2));
        html.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinHTML.setAdapter(html);


        //Load local HTML
        wv = (WebView) findViewById(R.id.webView);
        //wv.getSettings().setJavaScriptEnabled(true);
        //wv.loadUrl("file:///android_asset/label_template_5.html");

        //wv.loadUrl("file:///android_asset/"+spinHTML.getSelectedItem().toString());

        spinHTML.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                wv.loadUrl("file:///android_asset/" + spinHTML.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
        //Set print button = false
        button.setEnabled(false);


        specialTape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (specialTape.isChecked()) {
                    autoCut.setChecked(false);
                    cutAtEnd.setChecked(false);
                    halfCut.setChecked(false);
                }
                if (autoCut.isChecked() || cutAtEnd.isChecked() || halfCut.isChecked() || specialTape.isChecked())
                    button.setEnabled(true);
                else
                    button.setEnabled(false);
            }
        });

        autoCut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (autoCut.isChecked()) {
                    specialTape.setChecked(false);
                }
                if (autoCut.isChecked() || cutAtEnd.isChecked() || halfCut.isChecked() || specialTape.isChecked())
                    button.setEnabled(true);
                else
                    button.setEnabled(false);
            }
        });

        cutAtEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cutAtEnd.isChecked()) {
                    specialTape.setChecked(false);
                }
                if (autoCut.isChecked() || cutAtEnd.isChecked() || halfCut.isChecked() || specialTape.isChecked())
                    button.setEnabled(true);
                else
                    button.setEnabled(false);
            }
        });

        halfCut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (halfCut.isChecked()) {
                    specialTape.setChecked(false);
                }
                if (autoCut.isChecked() || cutAtEnd.isChecked() || halfCut.isChecked() || specialTape.isChecked())
                    button.setEnabled(true);
                else
                    button.setEnabled(false);
            }
        });


//        final Bitmap test = getBitmapOfWebView(wv);




        //Print
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Bitmap test;
                switch (spinHTML.getSelectedItem().toString()) {
                    case "label_template_1.html":
                        test = BitmapFactory.decodeResource(getResources(), R.drawable.a);
                        break;
                    case "label_template_2.html":
                        test = BitmapFactory.decodeResource(getResources(), R.drawable.printest);
                        break;
                    case "label_template_3.html":
                        test = BitmapFactory.decodeResource(getResources(), R.drawable.c);
                        break;
                    case "label_template_4.html":
                        test = BitmapFactory.decodeResource(getResources(), R.drawable.d);
                        break;
                    default:
                        test = BitmapFactory.decodeResource(getResources(), R.drawable.e);
                        break;
                }
                //setup printer
                printerManager.setModelName(spinPrinter.getSelectedItem().toString());
                List<Device> list = printerManager.searchPrinter();
                if (list.size() > 0) {
                    printerManager.setUpPrinter(list.get(0));
//                    printerManager.printBarcode(icon, true, true, true, false, 24);
                    printerManager.printBarcode(test, autoCut.isChecked(), cutAtEnd.isChecked(), halfCut.isChecked(), specialTape.isChecked(), 24);
                }
            }
        });
    }


//    public Bitmap perform(Bitmap bitmap) {
//        Bitmap bmOut = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
//                bitmap.getConfig());
//        int A, R, G, B;
//        int w = bitmap.getWidth();
//        int h = bitmap.getHeight();
//        int[] colors = new int[w * h];
//        bitmap.getPixels(colors, 0, w, 0, 0, w, h);
//        int i, j;
//        int pos, val;
//        for (i = 0; i < h; i++) {
//            for (j = 0; j < w; j++) {
//                pos = i * w + j;
//                A = (colors[pos] >> 24) & 0xFF;
//                R = (colors[pos] >> 16) & 0xFF;
//                G = (colors[pos] >> 8) & 0xFF;
//                B = colors[pos] & 0xFF;
//                //Thuật toán xử lý cho pixel tại vị trí (i,j)
//                colors[pos] = Color.argb(A, R, G, B);
//            }
//        }
//        bmOut.setPixels(colors, 0, w, 0, 0, w, h);
//        return bmOut;
//    }

    private Bitmap getBitmapOfWebView(final WebView webView) {
        Picture picture = webView.capturePicture();
        Bitmap bitmap = Bitmap.createBitmap(picture.getWidth(), picture.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        picture.draw(canvas);
        return bitmap;
    }


}

