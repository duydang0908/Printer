package vn.com.dne.dnesdk;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import com.brother.ptouch.sdk.NetPrinter;
import com.brother.ptouch.sdk.Printer;
import com.brother.ptouch.sdk.PrinterInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import vn.com.dne.dnesdk.common.BasePrint;
import vn.com.dne.dnesdk.common.Common;
import vn.com.dne.dnesdk.common.ImagePrint;
import vn.com.dne.dnesdk.common.MsgDialog;
import vn.com.dne.dnesdk.common.MsgHandle;
import vn.com.dne.dnesdk.model.Device;

public class PrinterManager {
    public Context context;
    private static PrinterManager INSTANCE = null;
    private static BasePrint myPrint = null;

    private String modelName = "PT-P750W";
    private NetPrinter[] mNetPrinter;

    private PrinterManager(Context context) {
        this.context = context;
    }

    public static synchronized PrinterManager getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new PrinterManager(context);
            myPrint = new ImagePrint(context);
        }
        return (INSTANCE);
    }

    public List<Device> searchPrinter() {
        SearchThread asyncRate = new SearchThread();
        List<Device> list = new ArrayList<>();
        try {
            list = asyncRate.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return list;
    }

    private class SearchThread extends AsyncTask<Void, Integer, List<Device>> {

        @Override
        protected List<Device> doInBackground(Void... params) {

            List<Device> deviceList = new ArrayList<Device>();
            boolean searchEnd = false;
            for (int i = 0; i < Common.SEARCH_TIMES; i++) {
                try {
                    // clear the item list
                    if (deviceList != null) {
                        deviceList.clear();
                    }
                    Printer myPrinter = new Printer();
                    PrinterInfo info = myPrinter.getPrinterInfo();
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                    info.enabledTethering = Boolean.parseBoolean(sharedPreferences
                            .getString("enabledTethering", "false"));
                    myPrinter.setPrinterInfo(info);

                    mNetPrinter = myPrinter.getNetPrinters(modelName);
                    final int netPrinterCount = mNetPrinter.length;

                    // when find printers,set the printers' information to the list.
                    if (netPrinterCount > 0) {
                        searchEnd = true;
                        String dispBuff[] = new String[netPrinterCount];
                        for (int ii = 0; ii < netPrinterCount; ii++) {
                            dispBuff[ii] = mNetPrinter[ii].modelName + "\n\n"
                                    + mNetPrinter[ii].ipAddress + "\n"
                                    + mNetPrinter[ii].macAddress + "\n"
                                    + mNetPrinter[ii].serNo + "\n"
                                    + mNetPrinter[ii].nodeName;
                            deviceList.add(new Device(mNetPrinter[ii].modelName, mNetPrinter[ii].ipAddress));
                        }
                    } else if (netPrinterCount == 0
                            && i == (Common.SEARCH_TIMES - 1)) { // when no printer
                        searchEnd = true;
                    }
                    if (searchEnd) {
                        break;
                    }
                } catch (Exception e) {
                }
            }
            return deviceList;
        }

        @Override
        protected void onPostExecute(List<Device> deviceList) {
        }
    }

    public void setUpPrinter(Device device) {
        SharedPreferences.Editor sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context).edit();
        sharedPreferences.putString("store_device_name", device.deviceName);
        sharedPreferences.putString("store_device_address", device.deviceAddress);
        sharedPreferences.apply();
    }

    public int printBarcode(Bitmap bitmap, boolean isAutoCut, boolean isCutAtEnd, boolean isHalfCut, boolean isSpecialTape, int size) {

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String store_device_address = sharedPreferences.getString("store_device_address", "IP");

        if (isSpecialTape) {
            sharedPreferences.edit().putBoolean("store_isAutoCut", false).apply();
            sharedPreferences.edit().putBoolean("store_isCutAtEnd", false).apply();
            sharedPreferences.edit().putBoolean("store_isHalfCut", false).apply();
            sharedPreferences.edit().putBoolean("store_isSpecialTape", true).apply();
        } else {
            sharedPreferences.edit().putBoolean("store_isAutoCut", isAutoCut).apply();
            sharedPreferences.edit().putBoolean("store_isCutAtEnd", isCutAtEnd).apply();
            sharedPreferences.edit().putBoolean("store_isHalfCut", isHalfCut).apply();
            sharedPreferences.edit().putBoolean("store_isSpecialTape", isSpecialTape).apply();
        }
        sharedPreferences.edit().putInt("store_size_page", size).apply();

        if (store_device_address.equalsIgnoreCase("IP")) {
            return Common.ERR_SETUP_PRINT;
        } else {
            String filename = "temp.jpg";
            File dest = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    File sd = Environment.getExternalStorageDirectory();
                    dest = new File(sd, filename);
                }
            } else {
                File sd = Environment.getExternalStorageDirectory();
                dest = new File(sd, filename);
            }
            try {
                FileOutputStream out = new FileOutputStream(dest);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
                ((ImagePrint) myPrint).setURL(dest.getAbsolutePath());
                myPrint.print();
                return Common.PRINT_SUCCESS;
            } catch (Exception e) {
                e.printStackTrace();
                return Common.ERR_PRINT;
            }
        }
    }
}