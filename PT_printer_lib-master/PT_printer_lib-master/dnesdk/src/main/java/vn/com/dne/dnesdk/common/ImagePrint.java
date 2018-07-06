/**
 * ImagePrint for printing
 *
 * @author Brother Industries, Ltd.
 * @version 2.2
 */
package vn.com.dne.dnesdk.common;

import android.content.Context;
import android.graphics.Bitmap;

import com.brother.ptouch.sdk.PrinterInfo.ErrorCode;

import java.io.File;
import java.util.ArrayList;

public class ImagePrint extends BasePrint {
    private String url;

    public ImagePrint(Context context) {
        super(context);
    }
    public void setURL(String url) {
        this.url = url;
    }
    @Override
    protected void doPrint() {
        mPrintResult = mPrinter.printFile(url);
        try {
            File file = new File(url);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}