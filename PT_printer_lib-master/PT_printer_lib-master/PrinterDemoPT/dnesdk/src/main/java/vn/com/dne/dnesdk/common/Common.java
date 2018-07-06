package vn.com.dne.dnesdk.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;

import java.io.File;

public class Common {

    public static final String SETTINGS_PORT = "port";
    public static final String SETTINGS_PAPERSIZE = "papersize";
    public static final String BLUETOOTH = "BLUETOOTH";
    public static final String NET = "NET";
    public static final String USB = "USB";
    public static final int SEARCH_TIMES = 10;
    // MsgHandle
    public static final int MSG_SDK_EVENT = 10001;
    public static final int MSG_PRINT_START = 10002;
    public static final int MSG_PRINT_END = 10003;
    public static final int MSG_PRINT_CANCEL = 10004;
    public static final int MSG_TRANSFER_START = 10005;
    public static final int MSG_WRONG_OS = 10006;
    public static final int MSG_NO_USB = 10007;
    public static final int MSG_DATA_SEND_START = 10030;
    public static final int MSG_DATA_SEND_END = 10031;
    public static final int MSG_GET_FIRM = 10099;

    public static int mUsbRequest;
    public static final String CUSTOM_PAPER_FOLDER = Environment
            .getExternalStorageDirectory().toString() + "/customPaperFileSet/";

    public static final int ERR_SETUP_PRINT = 100;
    public static final int ERR_PRINT = 101;
    public static final int PRINT_SUCCESS = 102;
}