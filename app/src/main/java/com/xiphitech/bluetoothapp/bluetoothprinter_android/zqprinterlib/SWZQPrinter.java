package com.xiphitech.bluetoothapp.bluetoothprinter_android.zqprinterlib;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.serialport.SerialPort;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Apple on 02/09/16.
 */


public class SWZQPrinter extends Activity {

    public static final int AB_GetMode = -1;
    public static final int AB_3X0M = 0;
    public static final int AB_88H = 1;
    public static final int AB_SUCCESS = 0;
    public static final int AB_ERR_INITIALIZE = 101;
    public static final int AB_ERR_OVERFLOW = 102;
    public static final int AB_ERR_NOTSUPPORT = 103;
    public static final int AB_ERR_INVALID_MODE = 104;
    public static final int AB_ERR_BUFFER_FULL = 107;
    public static final int AB_ERR_ARGUMENT = 201;
    public static final int AB_ERR_INVALIDDATA = 202;
    public static final int AB_ERR_BARCODEDATA = 301;
    public static final int AB_ERR_OPEN = 401;
    public static final int AB_ERR_WRITE = 402;
    public static final int AB_ERR_READ = 403;
    public static final int AB_ERR_IMAGEOPEN = 501;
    public static final int ALIGNMENT_LEFT = 0;
    public static final int ALIGNMENT_CENTER = 1;
    public static final int ALIGNMENT_RIGHT = 2;
    public static final int FT_DEFAULT = 0;
    public static final int FT_FONTB = 1;
    public static final int FT_BOLD = 2;
    public static final int FT_UNDERLINE = 4;
    public static final int FT_REVERSE = 8;
    public static final int TS_0WIDTH = 0;
    public static final int TS_1WIDTH = 16;
    public static final int TS_2WIDTH = 32;
    public static final int TS_3WIDTH = 48;
    public static final int TS_4WIDTH = 64;
    public static final int TS_5WIDTH = 80;
    public static final int TS_6WIDTH = 96;
    public static final int TS_7WIDTH = 112;
    public static final int TS_0HEIGHT = 0;
    public static final int TS_1HEIGHT = 1;
    public static final int TS_2HEIGHT = 2;
    public static final int TS_3HEIGHT = 3;
    public static final int TS_4HEIGHT = 4;
    public static final int TS_5HEIGHT = 5;
    public static final int TS_6HEIGHT = 6;
    public static final int TS_7HEIGHT = 7;
    public static final int CS_PC437 = 0;
    public static final int CS_KATAKANA = 1;
    public static final int CS_PC850 = 2;
    public static final int CS_PC860 = 3;
    public static final int CS_PC863 = 4;
    public static final int CS_PC865 = 5;
    public static final int CS_WPC1253 = 6;
    public static final int CS_TRAN = 7;
    public static final int CS_WPC1256 = 8;
    public static final int CS_PC737 = 9;
    public static final int CS_WPC1250 = 10;
    public static final int CS_WPC1252 = 16;
    public static final int CS_PC866 = 17;
    public static final int CS_PC852 = 18;
    public static final int CS_PC858 = 19;
    public static final int CS_USER = 255;
    public static final int STS_COVEROPEN = 601;
    public static final int STS_EMPTYPAPER = 602;
    public static final int STS_POWEROVER = 603;
    public static final int STS_MSRMODE = 604;
    public static final int STS_ICMODE = 605;
    public static final int STS_ERROR = 606;
    public static final int BCS_EAN13 = 103;
    public static final int BCS_JAN13 = 104;
    public static final int BCS_EAN8 = 105;
    public static final int BCS_JAN8 = 106;
    public static final int BCS_Code39 = 107;
    public static final int BCS_Code128 = 111;
    public static final int BCS_Code128_ZQ = 112;
    public static final int BC_TEXT_NONE = 0;
    public static final int BC_TEXT_ABOVE = 1;
    public static final int BC_TEXT_BELOW = 2;
    public static final int PWR_HIGH = 700;
    public static final int PWR_MIDDLE = 701;
    public static final int PWR_LOW = 702;
    public static final int PWR_SMALL = 703;
    public static final int PWR_NOT = 704;
    public static final int QRCODE_NUM = 49;
    public static final int QRCODE_ALPH = 50;
    public static final int QRCODE_CHN = 51;
    static final String SPP_UUID = "C9DF9CF9-CBBE-E473-B58A-F8C4C3C8B272";
    static final String VERSION = "1.2.9";
    static final String TAG_PRINTER = "AB-3X0M";
    static final String TAG_TEST = "Test";
    private static final int CON_WIFI = 0;
    private static final int CON_BT = 1;
    private static final int CON_COM = 2;
    private static final int CON_UNKNOW = 3;
    private static final int REQUEST_ENABLE_BT = 2;
    public static BluetoothAdapter btAdapt;
    public static BluetoothSocket btSocket;
    public static Socket socket = null;
    public static OutputStream out;
    public static InputStream in;
    private static int g_nMode = 0;
    private static int g_nConnectType = 0;
    private static int g_nConnect = 0;
    private static boolean g_bInitAda = false;
    private static boolean g_bPrintQRReceipt = false;
    private static int g_nPrintCode = 0;
    private static boolean g_bNotCheck = false;
    Button butSwipe;
    private SerialPort mSerialPort = null;
    private View.OnClickListener calcBMI = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case 2131034113:
                    SWZQPrinter.this.MyConnect();
                    break;
                case 2131034114:
                    int nRet = SWZQPrinter.this.Disconnect();
                    Log.e("AB-3X0M", "Disconnect=" + String.valueOf(nRet));
                    break;
                case 2131034115:
                    SWZQPrinter.this.MyExit();
                case 2131034116:
                default:
                    break;
                case 2131034117:
                    SWZQPrinter.this.MyTest();
                    break;
                case 2131034118:
                    SWZQPrinter.this.MyLogo();
                    break;
                case 2131034119:
                    SWZQPrinter.this.MyBitmap();
                    break;
                case 2131034120:
                    SWZQPrinter.this.MyReceipt80();
                    break;
                case 2131034121:
                    SWZQPrinter.this.MyReceipt58();
                    break;
                case 2131034122:
                    SWZQPrinter.this.MyQRCode();
                    break;
                case 2131034123:
                    SWZQPrinter.this.MyManMode();
                    break;
                case 2131034124:
                    SWZQPrinter.this.MyAutoMode();
                    break;
                case 2131034125:
                    SWZQPrinter.this.MyLng();
            }

        }
    };

    public SWZQPrinter() {
    }

    private static char findHex(byte b) {
        int t = (new Byte(b)).intValue();
        t = t < 0 ? t + 16 : t;
        return t >= 0 && t <= 9 ? (char) (t + 48) : (char) (t - 10 + 65);
    }

    private static String byteToString(byte b) {
        byte maskHigh = -16;
        byte maskLow = 15;
        byte high = (byte) ((b & maskHigh) >> 4);
        byte low = (byte) (b & maskLow);
        StringBuffer buf = new StringBuffer();
        buf.append(findHex(high));
        buf.append(findHex(low));
        return buf.toString();
    }

    public static Bitmap toGrayscale(Bitmap bmpOriginal) {
        int width = bmpOriginal.getWidth();
        int height = bmpOriginal.getHeight();
        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0.0F);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0.0F, 0.0F, paint);
        return bmpGrayscale;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(2130903040);

        String strTitle;
        try {
            strTitle = String.format("SWZQPrinter V%s", this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException var10) {
            strTitle = "SWZQPrinter";
            var10.printStackTrace();
        }

        super.setTitle(strTitle);
        Button button = this.findViewById(2131034113);
        button.setOnClickListener(this.calcBMI);
        button = this.findViewById(2131034117);
        button.setOnClickListener(this.calcBMI);
        button = this.findViewById(2131034114);
        button.setOnClickListener(this.calcBMI);
        button = this.findViewById(2131034115);
        button.setOnClickListener(this.calcBMI);
        button = this.findViewById(2131034120);
        button.setOnClickListener(this.calcBMI);
        button = this.findViewById(2131034118);
        button.setOnClickListener(this.calcBMI);
        button = this.findViewById(2131034119);
        button.setOnClickListener(this.calcBMI);
        button = this.findViewById(2131034121);
        button.setOnClickListener(this.calcBMI);
        this.butSwipe = this.findViewById(2131034123);
        this.butSwipe.setOnClickListener(this.calcBMI);
        button = this.findViewById(2131034124);
        button.setOnClickListener(this.calcBMI);
        button = this.findViewById(2131034122);
        button.setOnClickListener(this.calcBMI);
        button = this.findViewById(2131034125);
        button.setOnClickListener(this.calcBMI);
        EditText etPrinter = this.findViewById(2131034116);
        etPrinter.setText("1234567\n");

        try {
            FileInputStream e = this.openFileInput("zonerich.txt");
            byte[] buffer = new byte[17];
            e.read(buffer);
            EditText etAddr = this.findViewById(2131034112);
            String s1 = new String(buffer);
            s1 = s1.trim();
            etAddr.setText(s1);
        } catch (IOException var9) {
            var9.printStackTrace();
        }

        System.setProperty("file.encoding", "gb2312");
    }

    private void MyConnect() {
        EditText etPrinterText = this.findViewById(2131034116);
        if (etPrinterText.getText().toString().toUpperCase().equals("MYGOD")) {
            g_bNotCheck = true;
        }

        EditText etAddr = this.findViewById(2131034112);
        int nRet = this.Connect(etAddr.getText().toString());
        Log.e("AB-3X0M", "Connect=" + String.valueOf(nRet));
        if (nRet != 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Info");
            builder.setMessage("Connect Failed:" + String.valueOf(nRet));
            builder.setPositiveButton("OK", null);
            builder.show();
        }

    }

    private void MyTest() {
        g_bPrintQRReceipt = !g_bPrintQRReceipt;
        EditText etPrinterText = this.findViewById(2131034116);
        int nRet;
        if (etPrinterText.getText().toString().equals("")) {
            nRet = this.PrintText("This is a sample!!!\r\n\r\n\r\n1234567890\r\nabcdefghijklmnopqrstuvwxyz\r\n\r\n\r\n", 0, 0, 0);
            byte[] dataBar3 = new byte[]{(byte) 49, (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54, (byte) 55};
            nRet = this.PrintBarcode(dataBar3, dataBar3.length, 107, 50, 3, 0, 2);
            Log.e("AB-3X0M", "PrintBarcode=" + String.valueOf(nRet));
        } else {
            nRet = this.PrintText(etPrinterText.getText().toString(), 0, 0, 0);
            Log.e("AB-3X0M", "PrintText=" + String.valueOf(nRet));
        }

    }

    private void MyReceipt80() {
        File sdcardPrint = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ZQPrin t.txt");
        if (sdcardPrint.exists()) {
            try {
                FileInputStream is = new FileInputStream(sdcardPrint.getAbsolutePath());
                int e = is.available();
                byte[] buffer = new byte[e];
                is.read(buffer);
                is.close();
                this.SendData(buffer);
            } catch (FileNotFoundException var6) {
                var6.printStackTrace();
            } catch (IOException var7) {
                var7.printStackTrace();
            }

        } else {
            int nRet = this.PrintText("Happy Restaurant\r\n", 1, 2, 17);
            nRet = this.PrintText("Tel:xxx-xxxxxxxx\r\n", 2, 0, 0);
            nRet = this.PrintText("Order No.:99                          Table:2\r\n", 0, 0, 0);
            nRet = this.PrintText("Customer:Mr.R               Card No:123456789\r\n", 0, 0, 0);
            nRet = this.PrintText("---------------------------------------------\r\n", 0, 0, 0);
            nRet = this.PrintText("1*Brandy                                 8.00\r\n", 0, 0, 0);
            nRet = this.PrintText("1*Whiskey                               12.00\r\n", 0, 0, 0);
            nRet = this.PrintText("1*Scotch                                 7.00\r\n", 0, 0, 0);
            nRet = this.PrintText("1*Vodka                                 20.00\r\n", 0, 0, 0);
            nRet = this.PrintText("1*Rum                                    8.00\r\n", 0, 0, 0);
            nRet = this.PrintText("---------------------------------------------\r\n", 0, 0, 0);
            nRet = this.PrintText("                                Total: $55.00\r\n", 0, 0, 0);
            nRet = this.PrintText("                                Cash: $100.00\r\n", 0, 0, 0);
            nRet = this.PrintText("                                Change:$45.00\r\n", 0, 0, 0);
            nRet = this.PrintText("---------------------------------------------\r\n", 0, 0, 0);
            nRet = this.PrintText("             Welcome Next Time               \r\n\r\n\r\n\r\n\r\n\r\n", 0, 0, 0);
            nRet = this.OpenCashdraw();
            Log.e("AB-3X0M", "OpenCashdraw=" + String.valueOf(nRet));
            nRet = this.CutPaper();
            Log.e("AB-3X0M", "CutPaper=" + String.valueOf(nRet));
        }
    }

    private void MyLogo() {
        String strBmpFile = this.getApplicationContext().getFilesDir().getAbsolutePath() + "/zq.bmp";
        File f = new File(strBmpFile);
        File sdcard = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/zq.bmp");
        if (sdcard.exists()) {
            strBmpFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/zq.bmp";
        } else if (!f.exists()) {
            Log.e("AB-3X0M", "Copy zq.bmp");

            try {
                InputStream e = this.getAssets().open("zq.bmp");
                int size = e.available();
                byte[] buffer = new byte[size];
                e.read(buffer);
                e.close();
                FileOutputStream fs = new FileOutputStream(strBmpFile);
                fs.write(buffer, 0, size);
            } catch (IOException var9) {
                var9.printStackTrace();
            }
        }

        int nRet;
        if (g_nConnectType == 0) {
            nRet = this.PrintImage1D76(strBmpFile, 0);
        } else {
            nRet = this.PrintImage1B2A(strBmpFile, 0);
        }

        Log.e("AB-3X0M", "PrintImage=" + String.valueOf(nRet));
    }

    private void MyBitmap() {
        String strpngFile = this.getApplicationContext().getFilesDir().getAbsolutePath() + "/testreceipt.png";
        Log.e("Test", strpngFile);
        File file = new File(strpngFile);
        File sdcard2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/zq.bmp");
        if (sdcard2.exists()) {
            strpngFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/testreceipt.png";
        } else if (!file.exists()) {
            Log.e("AB-3X0M", "Copy zq.png");

            try {
                InputStream bm = this.getAssets().open("testreceipt.png");
                int e1 = bm.available();
                byte[] buffer = new byte[e1];
                bm.read(buffer);
                bm.close();
                FileOutputStream fs = new FileOutputStream(strpngFile);
                fs.write(buffer, 0, e1);
            } catch (IOException var9) {
                var9.printStackTrace();
            }
        }

        Bitmap bm1 = BitmapFactory.decodeFile(strpngFile);

        try {
            this.PrintBitmap1D76(bm1, 0);
            this.LineFeed(4);
        } catch (IOException var8) {
            var8.printStackTrace();
        }

    }

    private void MyExit() {
        try {
            if (g_nConnect == 1) {
                this.Disconnect();
            }

            EditText e = this.findViewById(2131034112);
            FileOutputStream outStream = this.openFileOutput("zonerich.txt", 0);
            String strAddr = e.getText().toString();
            outStream.write(strAddr.getBytes());
            outStream.close();
            Log.e("AB-3X0M", "Save Address OK!");
        } catch (IOException var4) {
            var4.printStackTrace();
        }

        this.finish();
    }

    private void MyReceipt58() {
        int nRet = this.PrintText("Happy Restaurant\r\n", 1, 2, 17);
        nRet = this.PrintText("Tel:xxx-xxxxxxxx\r\n", 2, 0, 0);
        nRet = this.PrintText("Order No.:99           Table:2\r\n", 0, 0, 0);
        nRet = this.PrintText("Customer:Mr.R   Card No:123456\r\n", 0, 0, 0);
        nRet = this.PrintText("------------------------------\r\n", 0, 0, 0);
        nRet = this.PrintText("1*Brandy                  8.00\r\n", 0, 0, 0);
        nRet = this.PrintText("1*Whiskey                12.00\r\n", 0, 0, 0);
        nRet = this.PrintText("1*Scotch                  7.00\r\n", 0, 0, 0);
        nRet = this.PrintText("1*Vodka                  20.00\r\n", 0, 0, 0);
        nRet = this.PrintText("1*Rum                     8.00\r\n", 0, 0, 0);
        nRet = this.PrintText("------------------------------\r\n", 0, 0, 0);
        nRet = this.PrintText("                 Total: $55.00\r\n", 0, 0, 0);
        nRet = this.PrintText("                 Cash: $100.00\r\n", 0, 0, 0);
        nRet = this.PrintText("                 Change:$45.00\r\n", 0, 0, 0);
        nRet = this.PrintText("------------------------------\r\n", 0, 0, 0);
        nRet = this.PrintText("      Welcome Next Time       \r\n\r\n\r\n\r\n\r\n\r\n", 0, 0, 0);
        nRet = this.OpenCashdraw();
        Log.e("AB-3X0M", "OpenCashdraw=" + String.valueOf(nRet));
        nRet = this.CutPaper();
        Log.e("AB-3X0M", "CutPaper=" + String.valueOf(nRet));
    }

    private void MyManMode() {
        byte[] i5 = this.MsrTrack3();
        if (i5 != null) {
            new String();
            String tem_buffer = "";

            for (int i = 0; i < i5.length; ++i) {
                tem_buffer = tem_buffer + "0x" + byteToString(i5[i]) + ",";
            }

            (new AlertDialog.Builder(this)).setTitle("Card Track2,3 Data").setMessage(tem_buffer).setPositiveButton("OK", new android.content.DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            }).show();
            tem_buffer = null;
        }

    }

    private void MyAutoMode() {
        byte[] i5 = this.ReadData(242);
        if (i5 != null) {
            new String();
            String tem_buffer = "";

            for (int i = 0; i < i5.length; ++i) {
                tem_buffer = tem_buffer + "0x" + byteToString(i5[i]) + ",";
            }

            (new AlertDialog.Builder(this)).setTitle("Card Track1,2,3 Data").setMessage(tem_buffer).setPositiveButton("OK", new android.content.DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            }).show();
            tem_buffer = null;
        }

    }

    private void MyQRCode() {
        if (g_nConnect != 1) {
            AlertDialog.Builder nRet1 = new AlertDialog.Builder(this);
            nRet1.setTitle("Info");
            nRet1.setMessage("Path:" + this.getApplicationContext().getFilesDir().getAbsolutePath() + "\r\nPackage:" + this.getApplicationContext().getPackageResourcePath());
            nRet1.setPositiveButton("OK", null);
            nRet1.show();
        } else {
            if (g_bPrintQRReceipt) {
                int nRet = this.PrintText("杭州庆春店 专柜小票 <收银联>\r\n\r\n", 1, 0, 0);
                nRet = this.PrintText("NO.HZ08204600620130923160152\r\n", 0, 0, 0);
                nRet = this.PrintText("开票日期:2013-09-23 16:01:52         营业员:8975\r\n", 0, 0, 0);
                nRet = this.PrintText("=============================================\r\n", 0, 0, 0);
                nRet = this.PrintText("编码          单价     数量  折让       金额\r\n", 0, 0, 0);
                nRet = this.PrintText("---------------------------------------------\r\n", 0, 0, 0);
                nRet = this.PrintText("8168           218        1   109.00   109.00\r\n", 0, 0, 0);
                nRet = this.PrintText("81681          198        1    98.00    98.00\r\n", 0, 0, 0);
                nRet = this.PrintText("81682          258        1    12.90   245.10\r\n", 0, 0, 0);
                nRet = this.PrintText("8168            98        1     0.00    98.00\r\n", 0, 0, 0);
                nRet = this.PrintText("8168           178        1     0.00   178.00\r\n", 0, 0, 0);
                nRet = this.PrintText("8168           198        1     0.00   198.00\r\n", 0, 0, 0);
                nRet = this.PrintText("8168           178        1    89.00    89.00\r\n", 0, 0, 0);
                nRet = this.PrintText("81682          178        1     0.00   178.00\r\n", 0, 0, 0);
                nRet = this.PrintText("81681          178        1     0.00   178.00\r\n", 0, 0, 0);
                nRet = this.PrintText("81681          198        1    99.00    99.00\r\n", 0, 0, 0);
                nRet = this.PrintText("---------------------------------------------\r\n", 0, 0, 0);
                nRet = this.PrintText("[1] 手袋 / A1611367064F\r\n", 0, 0, 0);
                nRet = this.PrintText("[2] 帽子 / A1603514025S\r\n", 0, 0, 0);
                nRet = this.PrintText("[3] 连身衣 / 0121296306490\r\n", 0, 0, 0);
                nRet = this.PrintText("[4] 口水巾 / A1602054032F\r\n", 0, 0, 0);
                nRet = this.PrintText("[5] 帽 / A160207205152\r\n", 0, 0, 0);
                nRet = this.PrintText("[6] 挎包 / A1602319060F\r\n", 0, 0, 0);
                nRet = this.PrintText("[7] 帽子 / A160237302048\r\n", 0, 0, 0);
                nRet = this.PrintText("[8] 帽 / A16020723450\r\n", 0, 0, 0);
                nRet = this.PrintText("[9] 帽 / A160207203450\r\n", 0, 0, 0);
                nRet = this.PrintText("[10] 雨衣+袋 / A111150903195\r\n", 0, 0, 0);
                nRet = this.PrintText("---------------------------------------------\r\n", 0, 0, 0);
                nRet = this.PrintText("金额合计:        1472.10\r\n", 0, 0, 17);
                nRet = this.PrintText("欢迎再来\r\n\r\n\r\n\r\n", 1, 0, 17);
                nRet = this.OpenCashdraw();
                this.PrintQRCode(50, "87G1snuCBvUeuwmBqSzLq/Sosumb0/L+KL7xvcQ8acDPaOPu/FwcSdK+WJUN3Wxb5Mo/KOs2/PGTl9HZh2YUgZFLE3LfCGzCaEAhQjQbQQz4L6jO9gOerP2boRIuWvfrpJTe8CkI0QgH4ArbKLv93EneQ/xQp2ezOcvEONPRNHj1tOJA5F8PNZr2kfKUA+Dho943PtQKkoDLgLq7jfcXQGX06FhyID0tCTKEPonEEVtMJhoYjxlwSkbCLLSQ78otpvroAKB2sRXFuMszcFg4muWJ6Fs66tgmzQYaGI8ZcEpGw5Y4Ez9weqWLO9PpuaynsaKTnbxoZgssECKHFuTKfuoCPeWeBP4matr/tlNL1I26wPTHXz/FUtTj8vPlzjjCtoBh+BCU6HxxyJJbKd2qbeJRBr6AG+C3IMoYWoP8g9e5V9t9ugkSeoj5rglflvK2ZIOI8rPZFLArT6Lth5bSM4IAIXiEvrrVg/SFE8sBL5cSMCFCNBtBDPgrC9SBDa0KWsuhEi5a9+ukkEYjHznwjC4MuqfoYgaoTAwHA8y7eSE6I+P2rpparrd56YebYYSyKILHL8fR/JsyrPdM0u1OQgT0vYWr5685CT2ePqHucnJ/w=ad824d0f");
                nRet = this.LineFeed(8);
                nRet = this.CutPaper();
            } else {
                String strCode = "HZ08204600620130923160152";

                byte[] by;
                try {
                    by = strCode.getBytes(System.getProperty("file.encoding"));
                } catch (UnsupportedEncodingException var5) {
                    var5.printStackTrace();
                    return;
                }

                this.PrintBarcode(by, strCode.length(), 111, 50, 2, 0, 2);
                this.LineFeed(5);
                this.PrintQRCode(51, "测试二维码ABCD1234567890");
            }

        }
    }

    private void MyLng() {
        ++g_nPrintCode;
        if (g_nPrintCode > 1) {
            g_nPrintCode = 0;
        }

        Button but = this.findViewById(2131034125);
        switch (g_nPrintCode) {
            case 0:
                but.setText("CHN");
                System.setProperty("file.encoding", "gb2312");
                break;
            case 1:
                but.setText("CHT");
                System.setProperty("file.encoding", "big5");
        }

    }

    public int PrintText(String Data, int Alignment, int Attribute, int TextSize) {
        if (g_nConnect != 1) {
            return 101;
        } else {
            byte[] by = new byte[]{(byte) 27, (byte) 97, (byte) 48, (byte) 27, (byte) 33, (byte) 0, (byte) 29, (byte) 66, (byte) 0, (byte) 29, (byte) 33, (byte) 0};
            by[2] = (byte) (by[2] + Alignment);
            if (Attribute > 0) {
                if ((Attribute & 4) > 0) {
                    by[5] = (byte) (by[5] + 128);
                    Log.e("Test", "Mode:" + String.valueOf(by[5]));
                }

                if ((Attribute & 1) > 0) {
                    ++by[5];
                    Log.e("Test", "Mode:" + String.valueOf(by[5]));
                }

                if ((Attribute & 2) > 0) {
                    by[5] = (byte) (by[5] + 8);
                    Log.e("Test", "Mode:" + String.valueOf(by[5]));
                }
            }

            if ((Attribute & 8) > 0) {
                by[8] = 1;
                Log.e("Test", "Reverse:" + String.valueOf(by[8]));
            }

            by[11] = (byte) TextSize;

            try {
                byte[] e = Data.getBytes(System.getProperty("file.encoding"));
                out.write(by, 0, 12);
                this.SendData(e);
                return 0;
            } catch (IOException var7) {
                var7.printStackTrace();
                return 402;
            }
        }
    }

    public int PrintEscText(String Data) {
        if (g_nConnect != 1) {
            return 101;
        } else {
            String strTmp = "";
            String strReplace = "";
            Pattern p = Pattern.compile("ESC\\|[0-9]*P|\u001b\\|[0-9]*P");

            Matcher m;
            for (m = p.matcher(Data); m.find(); m = p.matcher(Data)) {
                int strFeedPaper = 0;
                if (!m.group().equals("\u001b|P") && !m.group().equals("ESC|P")) {
                    if (m.group().indexOf("ESC") != -1) {
                        strFeedPaper = Integer.parseInt(Data.substring(m.start() + 4, m.end() - 1));
                    } else {
                        strFeedPaper = Integer.parseInt(Data.substring(m.start() + 2, m.end() - 1));
                    }
                }

                if (strFeedPaper == 100) {
                    strReplace = String.format("%c%c%c", Integer.valueOf(29), Integer.valueOf(86), Integer.valueOf(0));
                } else {
                    strReplace = String.format("%c%c%c", Integer.valueOf(29), Integer.valueOf(86), Integer.valueOf(1));
                }

                Data = Data.replace(m.group(), strReplace);
            }

            String var9 = "";

            int nLen;
            for (nLen = 0; nLen < 6; ++nLen) {
                var9 = var9 + "\n";
            }

            p = Pattern.compile("ESC\\|[0-9]*fP|\u001b\\|[0-9]*fP");

            for (m = p.matcher(Data); m.find(); m = p.matcher(Data)) {
                nLen = 0;
                if (!m.group().equals("\u001b|fP") && !m.group().equals("ESC|fP")) {
                    if (m.group().indexOf("ESC") != -1) {
                        nLen = Integer.parseInt(Data.substring(m.start() + 4, m.end() - 2));
                    } else {
                        nLen = Integer.parseInt(Data.substring(m.start() + 2, m.end() - 2));
                    }
                }

                if (nLen == 100) {
                    strReplace = String.format("%s%c%c%c", var9, Integer.valueOf(29), Integer.valueOf(86), Integer.valueOf(0));
                } else {
                    strReplace = String.format("%s%c%c%c", var9, Integer.valueOf(29), Integer.valueOf(86), Integer.valueOf(1));
                }

                Data = Data.replace(m.group(), strReplace);
            }

            p = Pattern.compile("\u001b\\|[0-9]*B|ESC\\|[0-9]*B");

            for (m = p.matcher(Data); m.find(); m = p.matcher(Data)) {
                nLen = 0;
                if (!m.group().equals("\u001b|B") && !m.group().equals("ESC|B")) {
                    if (m.group().indexOf("ESC") != -1) {
                        nLen = Integer.parseInt(Data.substring(m.start() + 4, m.end() - 1));
                    } else {
                        nLen = Integer.parseInt(Data.substring(m.start() + 2, m.end() - 1));
                    }
                }

                strReplace = String.format("%c%c%c0", Integer.valueOf(28), Integer.valueOf(112), Integer.valueOf(nLen));
                Data = Data.replace(m.group(), strReplace);
            }

            p = Pattern.compile("ESC\\|[0-9]*lF|\u001b\\|[0-9]*lF");

            for (m = p.matcher(Data); m.find(); m = p.matcher(Data)) {
                nLen = 1;
                if (!m.group().equals("\u001b|lF") && !m.group().equals("ESC|lF")) {
                    if (m.group().indexOf("ESC") != -1) {
                        nLen = Integer.parseInt(Data.substring(m.start() + 4, m.end() - 2));
                    } else {
                        nLen = Integer.parseInt(Data.substring(m.start() + 2, m.end() - 2));
                    }
                }

                strReplace = "";

                for (int j = 0; j < nLen; ++j) {
                    strReplace = strReplace + "\n";
                }

                Data = Data.replace(m.group(), strReplace);
            }

            p = Pattern.compile("\u001b\\|[!]*bC|ESC\\|[!]*bC");

            for (m = p.matcher(Data); m.find(); m = p.matcher(Data)) {
                if (!m.group().equals("\u001b|bC") && !m.group().equals("ESC|bC")) {
                    strReplace = String.format("%c%c%c", Integer.valueOf(27), Integer.valueOf(69), Integer.valueOf(0));
                } else {
                    strReplace = String.format("%c%c%c", Integer.valueOf(27), Integer.valueOf(69), Integer.valueOf(1));
                }

                Data = Data.replace(m.group(), strReplace);
            }

            p = Pattern.compile("\u001b\\|[!]*[0-9]*uC|ESC\\|[!]*[0-9]*uC");

            for (m = p.matcher(Data); m.find(); m = p.matcher(Data)) {
                if (!m.group().equals("\u001b|!uC") && !m.group().equals("ESC|!uC")) {
                    if (!m.group().equals("\u001b|2uC") && !m.group().equals("ESC|2uC")) {
                        strReplace = String.format("%c%c%c", Integer.valueOf(27), Integer.valueOf(45), Integer.valueOf(49));
                    } else {
                        strReplace = String.format("%c%c%c", Integer.valueOf(27), Integer.valueOf(45), Integer.valueOf(50));
                    }
                } else {
                    strReplace = String.format("%c%c%c", Integer.valueOf(27), Integer.valueOf(45), Integer.valueOf(48));
                }

                Data = Data.replace(m.group(), strReplace);
            }

            p = Pattern.compile("\u001b\\|[1-4]C|ESC\\|[1-4]C");

            for (m = p.matcher(Data); m.find(); m = p.matcher(Data)) {
                if (!m.group().equals("\u001b|1C") && !m.group().equals("ESC|1C")) {
                    if (!m.group().equals("\u001b|2C") && !m.group().equals("ESC|2C")) {
                        if (!m.group().equals("\u001b|3C") && !m.group().equals("ESC|3C")) {
                            strReplace = String.format("%c%c%c", Integer.valueOf(29), Integer.valueOf(33), Integer.valueOf(17));
                        } else {
                            strReplace = String.format("%c%c%c", Integer.valueOf(29), Integer.valueOf(33), Integer.valueOf(1));
                        }
                    } else {
                        strReplace = String.format("%c%c%c", Integer.valueOf(29), Integer.valueOf(33), Integer.valueOf(16));
                    }
                } else {
                    strReplace = String.format("%c%c%c", Integer.valueOf(29), Integer.valueOf(33), Integer.valueOf(0));
                }

                Data = Data.replace(m.group(), strReplace);
            }

            p = Pattern.compile("\u001b\\|[0-9]*hC|ESC\\|[0-9]*hC");

            for (m = p.matcher(Data); m.find(); m = p.matcher(Data)) {
                nLen = 0;
                if (!m.group().equals("\u001b|hC") && !m.group().equals("ESC|hC")) {
                    if (m.group().indexOf("ESC") != -1) {
                        nLen = Integer.parseInt(Data.substring(m.start() + 4, m.end() - 2));
                    } else {
                        nLen = Integer.parseInt(Data.substring(m.start() + 2, m.end() - 2));
                    }
                }

                switch (nLen) {
                    case 0:
                    case 1:
                        strReplace = String.format("%c%c%c", Integer.valueOf(29), Integer.valueOf(33), Integer.valueOf(0));
                        break;
                    case 2:
                        strReplace = String.format("%c%c%c", Integer.valueOf(29), Integer.valueOf(33), Integer.valueOf(16));
                        break;
                    case 3:
                        strReplace = String.format("%c%c%c", Integer.valueOf(29), Integer.valueOf(33), Integer.valueOf(32));
                        break;
                    case 4:
                        strReplace = String.format("%c%c%c", Integer.valueOf(29), Integer.valueOf(33), Integer.valueOf(48));
                        break;
                    case 5:
                        strReplace = String.format("%c%c%c", Integer.valueOf(29), Integer.valueOf(33), Integer.valueOf(64));
                        break;
                    case 6:
                        strReplace = String.format("%c%c%c", Integer.valueOf(29), Integer.valueOf(33), Integer.valueOf(80));
                        break;
                    case 7:
                        strReplace = String.format("%c%c%c", Integer.valueOf(29), Integer.valueOf(33), Integer.valueOf(96));
                        break;
                    case 8:
                        strReplace = String.format("%c%c%c", Integer.valueOf(29), Integer.valueOf(33), Integer.valueOf(112));
                        break;
                    default:
                        strReplace = String.format("%c%c%c", Integer.valueOf(29), Integer.valueOf(33), Integer.valueOf(0));
                }

                Data = Data.replace(m.group(), strReplace);
            }

            p = Pattern.compile("\u001b\\|[0-9]*vC|ESC\\|[0-9]*vC");

            for (m = p.matcher(Data); m.find(); m = p.matcher(Data)) {
                nLen = 0;
                if (!m.group().equals("\u001b|vC") && !m.group().equals("ESC|vC")) {
                    if (m.group().indexOf("ESC") != -1) {
                        nLen = Integer.parseInt(Data.substring(m.start() + 4, m.end() - 2));
                    } else {
                        nLen = Integer.parseInt(Data.substring(m.start() + 2, m.end() - 2));
                    }
                }

                switch (nLen) {
                    case 0:
                    case 1:
                        strReplace = String.format("%c%c%c", Integer.valueOf(29), Integer.valueOf(33), Integer.valueOf(0));
                        break;
                    case 2:
                        strReplace = String.format("%c%c%c", Integer.valueOf(29), Integer.valueOf(33), Integer.valueOf(1));
                        break;
                    case 3:
                        strReplace = String.format("%c%c%c", Integer.valueOf(29), Integer.valueOf(33), Integer.valueOf(2));
                        break;
                    case 4:
                        strReplace = String.format("%c%c%c", Integer.valueOf(29), Integer.valueOf(33), Integer.valueOf(3));
                        break;
                    case 5:
                        strReplace = String.format("%c%c%c", Integer.valueOf(29), Integer.valueOf(33), Integer.valueOf(4));
                        break;
                    case 6:
                        strReplace = String.format("%c%c%c", Integer.valueOf(29), Integer.valueOf(33), Integer.valueOf(5));
                        break;
                    case 7:
                        strReplace = String.format("%c%c%c", Integer.valueOf(29), Integer.valueOf(33), Integer.valueOf(6));
                        break;
                    case 8:
                        strReplace = String.format("%c%c%c", Integer.valueOf(29), Integer.valueOf(33), Integer.valueOf(7));
                        break;
                    default:
                        strReplace = String.format("%c%c%c", Integer.valueOf(29), Integer.valueOf(33), Integer.valueOf(0));
                }

                Data = Data.replace(m.group(), strReplace);
            }

            p = Pattern.compile("\u001b\\|[0-9]*fC|ESC\\|[0-9]*fC");

            for (m = p.matcher(Data); m.find(); m = p.matcher(Data)) {
                strReplace = "";
                Data = Data.replace(m.group(), strReplace);
            }

            p = Pattern.compile("\u001b\\|cA|ESC\\|cA");

            for (m = p.matcher(Data); m.find(); m = p.matcher(Data)) {
                strReplace = String.format("%c%c%c", Integer.valueOf(27), Integer.valueOf(97), Integer.valueOf(49));
                Data = Data.replace(m.group(), strReplace);
            }

            p = Pattern.compile("\u001b\\|rA|ESC\\|rA");

            for (m = p.matcher(Data); m.find(); m = p.matcher(Data)) {
                strReplace = String.format("%c%c%c", Integer.valueOf(27), Integer.valueOf(97), Integer.valueOf(50));
                Data = Data.replace(m.group(), strReplace);
            }

            p = Pattern.compile("\u001b\\|lA|ESC\\|lA");

            for (m = p.matcher(Data); m.find(); m = p.matcher(Data)) {
                strReplace = String.format("%c%c%c", Integer.valueOf(27), Integer.valueOf(97), Integer.valueOf(48));
                Data = Data.replace(m.group(), strReplace);
            }

            p = Pattern.compile("\u001b\\|N|ESC\\|N");

            for (m = p.matcher(Data); m.find(); m = p.matcher(Data)) {
                strReplace = String.format("%c%c%c", Integer.valueOf(27), Integer.valueOf(97), Integer.valueOf(48));
                Data = Data.replace(m.group(), strReplace);
            }

            return this.SendData(Data);
        }
    }

    public int PrintBarcode(byte[] Data, int DataSize, int Symbology, int Height, int Width, int Alignment, int TextPosition) {
        if (g_nConnect != 1) {
            return 101;
        } else if (Symbology != 103 && Symbology != 104 && Symbology != 105 && Symbology != 106 && Symbology != 107 && Symbology != 111 && Symbology != 112) {
            return 201;
        } else {
            byte[] by;
            if (Symbology == 111) {
                by = new byte[]{(byte) 29, (byte) 104, (byte) -94, (byte) 29, (byte) 119, (byte) 3, (byte) 29, (byte) 107, (byte) 73, (byte) 0, (byte) 0, (byte) 0};
                if (Height >= 1 && Height <= 255) {
                    by[2] = (byte) Height;
                }

                if (Width >= 2 && Width <= 6) {
                    by[5] = (byte) Width;
                }

                by[9] = (byte) (DataSize + 2);
                by[10] = 123;
                by[11] = 66;

                try {
                    out.write(by);
                    out.write(Data);
                    return 0;
                } catch (IOException var10) {
                    var10.printStackTrace();
                    return 101;
                }
            } else {
                by = new byte[]{(byte) 29, (byte) 104, (byte) -94, (byte) 29, (byte) 119, (byte) 3, (byte) 29, (byte) 107, (byte) 1, (byte) 1};
                if (Height >= 1 && Height <= 255) {
                    by[2] = (byte) Height;
                }

                if (Width >= 2 && Width <= 6) {
                    by[5] = (byte) Width;
                }

                by[9] = (byte) DataSize;
                switch (Symbology) {
                    case 103:
                    case 104:
                        by[8] = 67;
                        break;
                    case 105:
                    case 106:
                        by[8] = 68;
                        break;
                    case 107:
                        by[8] = 69;
                    case 108:
                    case 109:
                    case 110:
                    case 111:
                    default:
                        break;
                    case 112:
                        by[8] = 73;
                }

                try {
                    out.write(by);
                    out.write(Data);
                    return 0;
                } catch (IOException var11) {
                    var11.printStackTrace();
                    return 101;
                }
            }
        }
    }

    public byte GetXORValue(byte by, int nOffset) {
        byte[] byCalc = new byte[]{(byte) -128, (byte) 64, (byte) 32, (byte) 16, (byte) 8, (byte) 4, (byte) 2, (byte) 1};
        int[] b = new int[8];

        for (int byRet = 0; byRet < 8; ++byRet) {
            if (byRet < nOffset) {
                if ((by & byCalc[byRet]) == 1) {
                    b[byRet] = 0;
                } else {
                    b[byRet] = 1;
                }
            } else {
                b[byRet] = 1;
            }
        }

        byte var7 = 0;

        for (int j = 0; j < 8; ++j) {
            var7 = (byte) (var7 + b[j] * byCalc[j]);
        }

        return var7;
    }

    public int PrintImage1B2A(String FileName, int Type) {
        if (g_nConnect != 1) {
            return 101;
        } else {
            File file = new File(FileName);
            FileInputStream inStream = null;

            try {
                inStream = new FileInputStream(file);
            } catch (FileNotFoundException var37) {
                var37.printStackTrace();
                return 501;
            }

            byte[] buffer = new byte[54];
            boolean nWidth = false;
            boolean nHeight = false;

            try {
                inStream.read(buffer, 0, 54);
                if (buffer[28] != 1) {
                    inStream.close();
                    return 202;
                } else {
                    boolean e = false;
                    boolean n2 = false;
                    boolean n3 = false;
                    boolean n4 = false;
                    int var41;
                    if (buffer[18] >= 0) {
                        var41 = buffer[18];
                    } else {
                        var41 = 256 + buffer[18];
                    }

                    int var42;
                    if (buffer[19] >= 0) {
                        var42 = buffer[19];
                    } else {
                        var42 = 256 + buffer[19];
                    }

                    int var44;
                    if (buffer[20] >= 0) {
                        var44 = buffer[20];
                    } else {
                        var44 = 256 + buffer[20];
                    }

                    int var43;
                    if (buffer[21] >= 0) {
                        var43 = buffer[21];
                    } else {
                        var43 = 256 + buffer[21];
                    }

                    int var39 = (var43 << 24) + (var44 << 16) + (var42 << 8) + var41;
                    if (buffer[22] >= 0) {
                        var41 = buffer[22];
                    } else {
                        var41 = 256 + buffer[22];
                    }

                    if (buffer[23] >= 0) {
                        var42 = buffer[23];
                    } else {
                        var42 = 256 + buffer[23];
                    }

                    if (buffer[24] >= 0) {
                        var44 = buffer[24];
                    } else {
                        var44 = 256 + buffer[24];
                    }

                    if (buffer[25] >= 0) {
                        var43 = buffer[25];
                    } else {
                        var43 = 256 + buffer[25];
                    }

                    int var40 = (var43 << 24) + (var44 << 16) + (var42 << 8) + var41;
                    Log.e("AB-3X0M", String.valueOf(var39) + "*" + var40);
                    int nWybWidth = (var39 + 31) / 8 / 4 * 4 * 8;
                    int nWybHeight = (var40 + 7) / 8 * 8;
                    int dwSize = nWybWidth * nWybHeight / 8;
                    byte[] lpDIB = new byte[dwSize];
                    inStream.read(buffer, 0, 8);
                    byte[] lpWby = new byte[nWybWidth / 8];

                    int Xh;
                    int Xl;
                    for (Xh = 0; Xh < nWybHeight; ++Xh) {
                        if (Xh >= var40) {
                            for (Xl = 0; Xl < nWybWidth / 8; ++Xl) {
                                lpWby[Xl] = -1;
                            }
                        } else {
                            inStream.read(lpWby, 0, nWybWidth / 8);
                            if (var39 % 8 == 0) {
                                for (Xl = 0; Xl < nWybWidth / 8 - var39 / 8; ++Xl) {
                                    lpWby[var39 / 8 + Xl] = -1;
                                }
                            } else {
                                for (Xl = 0; Xl < nWybWidth / 8 - var39 / 8 - 1; ++Xl) {
                                    lpWby[var39 / 8 + 1 + Xl] = -1;
                                }

                                lpWby[var39 / 8] = this.GetXORValue(lpWby[var39 / 8], var39 % 8);
                            }
                        }

                        for (Xl = 0; Xl < nWybWidth / 8; ++Xl) {
                            lpDIB[(nWybHeight - Xh - 1) * (nWybWidth / 8) + Xl] = lpWby[Xl];
                        }
                    }

                    inStream.close();
                    Xh = nWybWidth / 256;
                    Xl = nWybWidth % 256;
                    int nNVWidth = Xh * 256 + Xl;
                    int dwSendSize = nNVWidth + 5;
                    byte[] lpSend = new byte[dwSendSize];
                    lpSend[0] = 27;
                    lpSend[1] = 42;
                    lpSend[2] = (byte) Type;
                    lpSend[3] = (byte) Xl;
                    lpSend[4] = (byte) Xh;

                    for (int byCalc = 0; byCalc < dwSendSize - 5; ++byCalc) {
                        lpSend[5 + byCalc] = -1;
                    }

                    byte[] var45 = new byte[]{(byte) -128, (byte) 64, (byte) 32, (byte) 16, (byte) 8, (byte) 4, (byte) 2, (byte) 1};
                    byte b0 = 0;
                    byte b1 = 0;
                    byte b2 = 0;
                    byte b3 = 0;
                    byte b4 = 0;
                    byte b5 = 0;
                    byte b6 = 0;
                    byte b7 = 0;
                    int nPos = 0;
                    byte[] pbyCol = new byte[8];

                    for (int i = 0; i < nWybHeight / 8; ++i) {
                        for (int p = 0; p < nNVWidth / 8; ++p) {
                            int z;
                            for (z = 0; z < 8; ++z) {
                                pbyCol[z] = lpDIB[(i * 8 + z) * nWybWidth / 8 + p];
                            }

                            for (z = 0; z < 8; ++z) {
                                for (int q = 0; q < 8; ++q) {
                                    if ((pbyCol[0] & var45[z]) == 0) {
                                        b0 = 1;
                                    } else {
                                        b0 = 0;
                                    }

                                    if ((pbyCol[1] & var45[z]) == 0) {
                                        b1 = 1;
                                    } else {
                                        b1 = 0;
                                    }

                                    if ((pbyCol[2] & var45[z]) == 0) {
                                        b2 = 1;
                                    } else {
                                        b2 = 0;
                                    }

                                    if ((pbyCol[3] & var45[z]) == 0) {
                                        b3 = 1;
                                    } else {
                                        b3 = 0;
                                    }

                                    if ((pbyCol[4] & var45[z]) == 0) {
                                        b4 = 1;
                                    } else {
                                        b4 = 0;
                                    }

                                    if ((pbyCol[5] & var45[z]) == 0) {
                                        b5 = 1;
                                    } else {
                                        b5 = 0;
                                    }

                                    if ((pbyCol[6] & var45[z]) == 0) {
                                        b6 = 1;
                                    } else {
                                        b6 = 0;
                                    }

                                    if ((pbyCol[7] & var45[z]) == 0) {
                                        b7 = 1;
                                    } else {
                                        b7 = 0;
                                    }
                                }

                                lpSend[5 + nPos] = (byte) (b0 * 128 + b1 * 64 + b2 * 32 + b3 * 16 + b4 * 8 + b5 * 4 + b6 * 2 + b7);
                                ++nPos;
                            }
                        }

                        this.SendData(lpSend);
                        nPos = 0;
                    }

                    return 0;
                }
            } catch (IOException var38) {
                var38.printStackTrace();
                return 501;
            }
        }
    }

    public int PrintImage1D76(String FileName, int Type) {
        if (g_nConnect != 1) {
            return 101;
        } else {
            File file = new File(FileName);
            FileInputStream inStream = null;

            try {
                inStream = new FileInputStream(file);
            } catch (FileNotFoundException var19) {
                var19.printStackTrace();
                return 501;
            }

            byte[] buffer = new byte[54];
            boolean nWidth = false;
            boolean nHeight = false;

            try {
                inStream.read(buffer, 0, 54);
                if (buffer[28] != 1) {
                    inStream.close();
                    return 202;
                } else {
                    boolean e = false;
                    boolean n2 = false;
                    boolean n3 = false;
                    boolean n4 = false;
                    int var23;
                    if (buffer[18] >= 0) {
                        var23 = buffer[18];
                    } else {
                        var23 = 256 + buffer[18];
                    }

                    int var24;
                    if (buffer[19] >= 0) {
                        var24 = buffer[19];
                    } else {
                        var24 = 256 + buffer[19];
                    }

                    int var26;
                    if (buffer[20] >= 0) {
                        var26 = buffer[20];
                    } else {
                        var26 = 256 + buffer[20];
                    }

                    int var25;
                    if (buffer[21] >= 0) {
                        var25 = buffer[21];
                    } else {
                        var25 = 256 + buffer[21];
                    }

                    int var21 = (var25 << 24) + (var26 << 16) + (var24 << 8) + var23;
                    if (buffer[22] >= 0) {
                        var23 = buffer[22];
                    } else {
                        var23 = 256 + buffer[22];
                    }

                    if (buffer[23] >= 0) {
                        var24 = buffer[23];
                    } else {
                        var24 = 256 + buffer[23];
                    }

                    if (buffer[24] >= 0) {
                        var26 = buffer[24];
                    } else {
                        var26 = 256 + buffer[24];
                    }

                    if (buffer[25] >= 0) {
                        var25 = buffer[25];
                    } else {
                        var25 = 256 + buffer[25];
                    }

                    int var22 = (var25 << 24) + (var26 << 16) + (var24 << 8) + var23;
                    Log.e("AB-3X0M", String.valueOf(var21) + "*" + var22);
                    int nWybWidth = (var21 + 31) / 8 / 4 * 4 * 8;
                    int nWybHeight = var22;
                    int dwSize = nWybWidth * var22 / 8 + 8;
                    byte[] lpDIB = new byte[dwSize];
                    lpDIB[0] = 29;
                    lpDIB[1] = 118;
                    lpDIB[2] = 48;
                    lpDIB[3] = (byte) Type;
                    lpDIB[4] = (byte) (nWybWidth / 8 % 256);
                    lpDIB[5] = (byte) (nWybWidth / 8 / 256);
                    lpDIB[6] = (byte) (var22 % 256);
                    lpDIB[7] = (byte) (var22 / 256);
                    inStream.read(buffer, 0, 8);
                    boolean nOffPos = false;

                    int i;
                    for (i = 0; i < nWybHeight; ++i) {
                        int var27 = 8 + nWybWidth / 8 * (nWybHeight - i - 1);
                        inStream.read(lpDIB, 8 + nWybWidth / 8 * (nWybHeight - i - 1), nWybWidth / 8);
                        if (nWybWidth > var21) {
                            int j;
                            if (var21 % 8 == 0) {
                                for (j = var27 + var21 / 8; j < var27 + nWybWidth / 8; ++j) {
                                    lpDIB[j] = -1;
                                }
                            } else {
                                for (j = var27 + var21 / 8 + 1; j < var27 + nWybWidth / 8; ++j) {
                                    lpDIB[j] = -1;
                                }

                                lpDIB[var27 + var21 / 8] = this.GetXORValue(lpDIB[var27 + var21 / 8], var21 % 8);
                            }
                        }
                    }

                    inStream.close();

                    for (i = 8; i < dwSize; ++i) {
                        lpDIB[i] = (byte) (~lpDIB[i]);
                    }

                    this.SendData(lpDIB);
                    return 0;
                }
            } catch (IOException var20) {
                var20.printStackTrace();
                return 501;
            }
        }
    }

    public int LineFeed(int Value) {
        if (g_nConnect != 1) {
            return 101;
        } else if (Value == 0) {
            return 0;
        } else {
            String message = "";

            for (int send = 0; send < Value; ++send) {
                message = message + "\r\n";
            }

            byte[] var6 = message.getBytes();

            try {
                out.write(var6);
                return 0;
            } catch (IOException var5) {
                var5.printStackTrace();
                return 402;
            }
        }
    }

    public int MarkFeed(int Value) {
        return g_nConnect != 1 ? 101 : 103;
    }

    int SetImage(int Number, String FileName, int Width, int Alignment, int Level) {
        return g_nConnect != 1 ? 101 : 103;
    }

    public int SetCharacterSet(int Value) {
        if (g_nConnect != 1) {
            return 101;
        } else if ((Value <= 0 || Value >= 10) && (Value <= 16 || Value >= 19) && Value == 255) {
            return 104;
        } else {
            byte[] send = new byte[]{(byte) 27, (byte) 116, (byte) 1};
            send[2] = (byte) Value;

            try {
                out.write(send);
                return 0;
            } catch (IOException var4) {
                var4.printStackTrace();
                return 402;
            }
        }
    }

    public int SetInterCharacterSet(int Value) {
        return g_nConnect != 1 ? 101 : 103;
    }

    public int Connect(String BtAddr) {
        BtAddr = BtAddr.toUpperCase();
        BtAddr = BtAddr.trim();
        Log.e("AB-3X0M", "Connect:" + BtAddr);
        Pattern p = Pattern.compile("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}:[0-9]{1,6}");
        Matcher m = p.matcher(BtAddr);
        if (m.matches()) {
            g_nConnectType = 0;
            Log.e("AB-3X0M", "connect WIFI");
        } else {
            p = Pattern.compile("[0-9,A-F,a-f]{2}:[0-9,A-F,a-f]{2}:[0-9,A-F,a-f]{2}:[0-9,A-F,a-f]{2}:[0-9,A-F,a-f]{2}:[0-9,A-F,a-f]{2}");
            m = p.matcher(BtAddr);
            if (m.matches()) {
                g_nConnectType = 1;
                Log.e("AB-3X0M", "connect Bluetooth");
            } else {
                p = Pattern.compile("((TTYS)|(TTYUSB))[0-9]:((2400)|(4800)|(9600)|(19200)|(38400)|(57600)|(115200))");
                m = p.matcher(BtAddr);
                if (!m.matches()) {
                    Log.e("AB-3X0M", "Param Error");
                    return 202;
                }

                g_nConnectType = 2;
                Log.e("AB-3X0M", "connect Serial Port");
            }
        }

        if (g_nConnect == 1) {
            return 0;
        } else {
            if (!g_bInitAda) {
                g_bInitAda = true;
                if (g_nConnectType == 1) {
                    btAdapt = BluetoothAdapter.getDefaultAdapter();
                    Log.e("AB-3X0M", "getDefaultAdapter");
                    if (!btAdapt.isEnabled()) {
                        new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE");
                    }
                }
            }

            int e;
            String n;
            int byread;
            if (g_nConnectType == 0) {
                e = BtAddr.indexOf(58, 0);
                if (e <= 0) {
                    return 201;
                }

                try {
                    n = BtAddr.substring(0, e);
                    byread = Integer.parseInt(BtAddr.substring(e + 1, BtAddr.length()));
                    InetAddress bRet = InetAddress.getByName(n);
                    socket = new Socket(bRet, byread);
                    if (socket == null) {
                        Log.e("AB-3X0M", "new Socket: Error");
                        return 201;
                    }
                } catch (Exception var13) {
                    Log.e("AB-3X0M", "new Socket: Error", var13);
                }
            } else if (g_nConnectType == 1) {
                UUID e1 = UUID.fromString("C9DF9CF9-CBBE-E473-B58A-F8C4C3C8B272");
                if (!BluetoothAdapter.checkBluetoothAddress(BtAddr)) {
                    return 201;
                }

                try {
                    BluetoothDevice n1 = btAdapt.getRemoteDevice(BtAddr);

                    try {
                        Method byread1 = n1.getClass().getMethod("createRfcommSocket", Integer.TYPE);
                        btSocket = (BluetoothSocket) byread1.invoke(n1, Integer.valueOf(1));
                    } catch (Exception var11) {
                        var11.printStackTrace();
                        btSocket = n1.createRfcommSocketToServiceRecord(e1);
                    }

                    Log.e("AB-3X0M", "createRfcommSocketToServiceRecord");
                    boolean bRet1 = btAdapt.cancelDiscovery();
                    btSocket.connect();
                    Log.e("AB-3X0M", "Connected");
                } catch (IOException var12) {
                    var12.printStackTrace();
                    return 101;
                }
            } else {
                e = BtAddr.indexOf(58, 0);
                if (e <= 0) {
                    return 201;
                }

                try {
                    n = BtAddr.substring(0, e);
                    n = n.replace("TTYS", "ttyS");
                    n = n.replace("TTYUSB", "ttyUSB");
                    byread = Integer.parseInt(BtAddr.substring(e + 1, BtAddr.length()));
                    this.mSerialPort = new SerialPort(new File("/dev/" + n), byread);
                } catch (Exception var10) {
                    Log.e("AB-3X0M", "new SerialPort: Error", var10);
                    return 103;
                }
            }

            try {
                if (g_nConnectType == 0) {
                    try {
                        out = socket.getOutputStream();
                        in = socket.getInputStream();
                    } catch (Exception var9) {
                        return 101;
                    }

                    if (out == null || in == null) {
                        return 101;
                    }

                    Log.e("AB-3X0M", "Connected ok");
                } else {
                    if (g_nConnectType != 1) {
                        out = this.mSerialPort.getOutputStream();
                        in = this.mSerialPort.getInputStream();
                        g_nConnect = 1;
                        return 0;
                    }

                    out = btSocket.getOutputStream();
                    in = btSocket.getInputStream();

                    //"00:1F:B7:05:98:9F") || BtAddr.equals("00:1F:B7:05:87:48")
                    if (Arrays.asList(BtAddr.split(":")).size() == 6) {
                        Log.e("AB-3X0M", BtAddr);
                        g_nConnect = 1;
                        return 0;
                    }
                }

                if (g_bNotCheck) {
                    g_nConnect = 1;
                    return 0;
                } else {
                    byte[] e2 = new byte[]{(byte) 16, (byte) 5, (byte) 8, (byte) 1};
                    int n2 = (int) (Math.random() * 254.0D);
                    e2[3] = (byte) n2;

                    try {
                        out.write(e2);
                    } catch (NullPointerException var8) {
                        return 101;
                    }

                    byte[] byread2 = new byte[1];
                    if (in.read(byread2) == 1) {
                        if (n2 >= 128) {
                            n2 = ~n2 & 255;
                        } else {
                            n2 ^= 32;
                        }

                        if (byread2[0] == n2) {
                            g_nConnect = 1;
                            return 0;
                        } else {
                            this.Disconnect();
                            return 202;
                        }
                    } else {
                        this.Disconnect();
                        return 202;
                    }
                }
            } catch (IOException var14) {
                var14.printStackTrace();
                return 101;
            }
        }
    }

    public int Disconnect() {
        Log.e("AB-3X0M", "Disconnect");
        if (in != null) {
            try {
                in.close();
            } catch (IOException var5) {
                var5.printStackTrace();
            }
        }

        if (out != null) {
            try {
                out.close();
            } catch (IOException var4) {
                var4.printStackTrace();
            }
        }

        if (btSocket != null) {
            try {
                btSocket.close();
            } catch (IOException var3) {
                var3.printStackTrace();
                return 101;
            }
        }

        if (socket != null) {
            try {
                socket.close();
            } catch (IOException var2) {
                var2.printStackTrace();
                return 101;
            }
        }

        if (this.mSerialPort != null) {
            this.mSerialPort.close();
            this.mSerialPort = null;
        }

        g_nConnect = 0;
        return 0;
    }

    public int GetStatus() {
        if (g_nConnect != 1) {
            return 101;
        } else {
            byte[] send = new byte[]{(byte) 16, (byte) 4, (byte) 4};

            try {
                out.write(send);
                byte[] e = new byte[2];
                in.read(e);
                Log.e("AB-3X0M", "Data=" + String.valueOf(e[0]));
                return e[0] == 114 ? 602 : 0;
            } catch (IOException var3) {
                var3.printStackTrace();
                return 403;
            }
        }
    }

    public int GetPowerStatus() {
        if (g_nConnect != 1) {
            return 101;
        } else {
            byte[] send = new byte[]{(byte) 29, (byte) 73, (byte) 98};

            try {
                out.write(send);
                byte[] e = new byte[5];

                for (int nRead = 0; nRead < 4; nRead += in.read(e, nRead, 4 - nRead)) {
                }

                Log.e("AB-3X0M", "Data=" + String.valueOf(e[0]) + "," + e[1] + "," + e[2] + "," + e[3]);
                return e[2] == 48 ? 700 : (e[2] == 49 ? 701 : (e[2] == 50 ? 702 : (e[2] == 51 ? 703 : (e[2] == 52 ? 704 : 0))));
            } catch (IOException var4) {
                var4.printStackTrace();
                return 403;
            }
        }
    }

    public int Directio(byte[] pSend, int uiLength, byte[] pReceive, int[] uiReceiveLength) {
        if (g_nConnect != 1) {
            return 101;
        } else {
            try {
                out.write(pSend, 0, uiLength);
                if (pReceive != null) {
                    Log.e("AB-3X0M", "ReceiveLength=" + String.valueOf(uiReceiveLength[0]));
                    int e = 0;
                    int nMaxLen = uiReceiveLength[0];

                    while (e < nMaxLen) {
                        e += in.read(pReceive, e, nMaxLen - e);
                        Log.e("AB-3X0M", "ok=" + String.valueOf(e));
                    }

                    uiReceiveLength[0] = e;
                }

                return 0;
            } catch (IOException var7) {
                var7.printStackTrace();
                return 403;
            }
        }
    }

    public int IcOn() {
        return g_nConnect != 1 ? 101 : 103;
    }

    public int IcOff() {
        return g_nConnect != 1 ? 101 : 103;
    }

    public int IcGetStatus() {
        return g_nConnect != 1 ? 101 : 103;
    }

    public int MsrReady() {
        return g_nConnect != 1 ? 101 : 0;
    }

    public int MsrGetData() {
        return g_nConnect != 1 ? 101 : 0;
    }

    public int MsrCancel() {
        return g_nConnect != 1 ? 101 : 0;
    }

    public byte[] MsrTrack1() {
        byte[] by = new byte[84];
        if (g_nConnect != 1) {
            return by;
        } else {
            byte[] send = new byte[]{(byte) 27, (byte) 77, (byte) 70, (byte) 27, (byte) 77, (byte) 73};

            try {
                out.write(send);
                int e = 0;
                byte nMaxLen = 84;
                long nStart = System.currentTimeMillis();
                long nEnd = System.currentTimeMillis();
                boolean nReadReady = false;

                while (e < nMaxLen && nEnd - nStart <= 3000L) {
                    try {
                        Thread.currentThread();
                        Thread.sleep(20L);
                    } catch (InterruptedException var11) {
                        var11.printStackTrace();
                    }

                    int nReadReady1 = in.available();
                    if (nReadReady1 > 0) {
                        e += in.read(by, e, nReadReady1);
                    }

                    nEnd = System.currentTimeMillis();
                    Log.e("AB-3X0M", "ReadData=" + String.valueOf(e));
                }
            } catch (IOException var12) {
                var12.printStackTrace();
            }

            return by;
        }
    }

    public byte[] MsrTrack2() {
        byte[] by = new byte[45];
        if (g_nConnect != 1) {
            return by;
        } else {
            byte[] send = new byte[]{(byte) 27, (byte) 77, (byte) 71, (byte) 27, (byte) 77, (byte) 73};

            try {
                out.write(send);
                int e = 0;
                byte nMaxLen = 45;
                long nStart = System.currentTimeMillis();
                long nEnd = System.currentTimeMillis();
                boolean nReadReady = false;

                while (e < nMaxLen && nEnd - nStart <= 3000L) {
                    try {
                        Thread.currentThread();
                        Thread.sleep(20L);
                    } catch (InterruptedException var11) {
                        var11.printStackTrace();
                    }

                    int nReadReady1 = in.available();
                    if (nReadReady1 > 0) {
                        e += in.read(by, e, nReadReady1);
                    }

                    nEnd = System.currentTimeMillis();
                    Log.e("AB-3X0M", "ReadData=" + String.valueOf(e));
                }
            } catch (IOException var12) {
                var12.printStackTrace();
            }

            return by;
        }
    }

    public byte[] MsrTrack3() {
        byte[] by = new byte[112];
        if (g_nConnect != 1) {
            return by;
        } else {
            byte[] send = new byte[]{(byte) 27, (byte) 77, (byte) 68, (byte) 27, (byte) 77, (byte) 73};

            try {
                out.write(send);
                int e = 0;
                byte nMaxLen = 112;
                long nStart = System.currentTimeMillis();
                long nEnd = System.currentTimeMillis();

                for (boolean nReadReady = false; e < nMaxLen && nEnd - nStart <= 3000L; nEnd = System.currentTimeMillis()) {
                    try {
                        Thread.currentThread();
                        Thread.sleep(20L);
                    } catch (InterruptedException var11) {
                        var11.printStackTrace();
                    }

                    int nReadReady1 = in.available();
                    if (nReadReady1 > 0) {
                        e += in.read(by, e, nReadReady1);
                    }
                }

                Log.e("AB-3X0M", "ReadData=" + String.valueOf(e));
            } catch (IOException var12) {
                var12.printStackTrace();
            }

            return by;
        }
    }

    public byte[] MsrTrack12() {
        byte[] by = new byte[122];
        if (g_nConnect != 1) {
            return by;
        } else {
            byte[] send = new byte[]{(byte) 27, (byte) 77, (byte) 72, (byte) 27, (byte) 77, (byte) 73};

            try {
                out.write(send);
                int e = 0;
                byte nMaxLen = 122;
                long nStart = System.currentTimeMillis();
                long nEnd = System.currentTimeMillis();
                boolean nReadReady = false;

                while (e < nMaxLen && nEnd - nStart <= 3000L) {
                    try {
                        Thread.currentThread();
                        Thread.sleep(20L);
                    } catch (InterruptedException var11) {
                        var11.printStackTrace();
                    }

                    int nReadReady1 = in.available();
                    if (nReadReady1 > 0) {
                        e += in.read(by, e, nReadReady1);
                    }

                    nEnd = System.currentTimeMillis();
                    Log.e("AB-3X0M", "ReadData=" + String.valueOf(e));
                }
            } catch (IOException var12) {
                var12.printStackTrace();
            }

            return by;
        }
    }

    public byte[] MsrTrack23() {
        byte[] by = new byte[150];
        if (g_nConnect != 1) {
            return by;
        } else {
            byte[] send = new byte[]{(byte) 27, (byte) 77, (byte) 69, (byte) 27, (byte) 77, (byte) 73};

            try {
                out.write(send);
                int e = 0;
                short nMaxLen = 150;
                long nStart = System.currentTimeMillis();
                long nEnd = System.currentTimeMillis();
                boolean nReadReady = false;

                while (e < nMaxLen && nEnd - nStart <= 3000L) {
                    try {
                        Thread.currentThread();
                        Thread.sleep(20L);
                    } catch (InterruptedException var11) {
                        var11.printStackTrace();
                    }

                    int nReadReady1 = in.available();
                    if (nReadReady1 > 0) {
                        e += in.read(by, e, nReadReady1);
                    }

                    nEnd = System.currentTimeMillis();
                    Log.e("AB-3X0M", "ReadData=" + String.valueOf(e));
                }
            } catch (IOException var12) {
                var12.printStackTrace();
            }

            return by;
        }
    }

    public String GetVersion() {
        return "1.2.9";
    }

    public int OpenCashdraw() {
        if (g_nConnect != 1) {
            return 101;
        } else {
            byte[] send = new byte[]{(byte) 27, (byte) 112, (byte) 0, (byte) 16, (byte) 16};

            try {
                out.write(send);
                return 0;
            } catch (IOException var3) {
                var3.printStackTrace();
                return 402;
            }
        }
    }

    public int CutPaper() {
        if (g_nConnect != 1) {
            return 101;
        } else {
            byte[] send = new byte[]{(byte) 27, (byte) 109};

            try {
                out.write(send);
                return 0;
            } catch (IOException var3) {
                var3.printStackTrace();
                return 402;
            }
        }
    }

    public int PrintBitmap1D76(Bitmap image, int Type) throws IOException {
        if (g_nConnect != 1) {
            return 101;
        } else {
            Log.e("Test", image.getConfig().toString());
            int nWidth = image.getWidth();
            int nHeight = image.getHeight();
            Bitmap bm;
            if (nWidth > 832) {
                bm = Bitmap.createScaledBitmap(image, 832, image.getHeight() * 832 / image.getWidth(), true);
                nWidth = 832;
                nHeight = bm.getHeight();
            } else {
                bm = image;
            }

            Log.e("AB-3X0M", String.valueOf(nWidth) + "*" + nHeight);
            int nWybWidth = (nWidth + 31) / 8 / 4 * 4 * 8;
            int nWybHeight = nHeight;
            int dwSize = nWybWidth * nHeight / 8 + 8;
            byte[] lpDIB = new byte[dwSize];
            lpDIB[0] = 29;
            lpDIB[1] = 118;
            lpDIB[2] = 48;
            lpDIB[3] = (byte) Type;
            lpDIB[4] = (byte) (nWybWidth / 8 % 256);
            lpDIB[5] = (byte) (nWybWidth / 8 / 256);
            lpDIB[6] = (byte) (nHeight % 256);
            lpDIB[7] = (byte) (nHeight / 256);
            boolean nOffPos = false;
            int[] n = new int[8];

            for (int i = 0; i < nWybHeight; ++i) {
                for (int j = 0; j < nWybWidth / 8; ++j) {
                    for (int k = 0; k < 8; ++k) {
                        int color;
                        if (8 * j + k < nWidth) {
                            color = bm.getPixel(8 * j + k, i);
                        } else {
                            color = -1;
                        }

                        int a = Color.alpha(color);
                        int r = Color.red(color);
                        int g = Color.green(color);
                        int b = Color.blue(color);
                        if (color != -16777216 && (a != 255 || r >= 128 || g >= 128 || b >= 128)) {
                            n[k] = 0;
                        } else {
                            n[k] = 1;
                        }
                    }

                    int var20 = 8 + nWybWidth / 8 * i;
                    lpDIB[var20 + j] = (byte) (n[0] * 128 + n[1] * 64 + n[2] * 32 + n[3] * 16 + n[4] * 8 + n[5] * 4 + n[6] * 2 + n[7]);
                }
            }

            this.SendData(lpDIB);
            return 0;
        }
    }

    public byte[] ReadData(int nReadLen) {
        byte[] by = new byte[nReadLen];
        if (g_nConnect != 1) {
            return by;
        } else {
            try {
                int e = 0;
                int nMaxLen = nReadLen;
                long nStart = System.currentTimeMillis();
                long nEnd = System.currentTimeMillis();
                boolean nReadReady = false;

                while (e < nMaxLen && nEnd - nStart <= 3000L) {
                    try {
                        Thread.currentThread();
                        Thread.sleep(20L);
                    } catch (InterruptedException var11) {
                        var11.printStackTrace();
                    }

                    int nReadReady1 = in.available();
                    if (nReadReady1 > 0) {
                        e += in.read(by, e, nReadReady1);
                    }

                    nEnd = System.currentTimeMillis();
                    Log.e("AB-3X0M", "ReadData=" + String.valueOf(e));
                }
            } catch (IOException var12) {
                var12.printStackTrace();
            }

            return by;
        }
    }

    public int SetMode(int nMode) {
        if (nMode >= 0 && nMode <= 1) {
            g_nMode = nMode;
        }

        return g_nMode;
    }

    private int SendData(byte[] send) {
        if (g_nConnect != 1) {
            return 101;
        } else {
            try {
                short e = 512;
                int nPos = 0;
                boolean nLen = false;
                int nNum = (send.length + e - 1) / e;

                for (int i = 0; i < nNum; ++i) {
                    int var8;
                    if (i == nNum - 1) {
                        var8 = send.length - i * e;
                    } else {
                        var8 = e;
                    }

                    out.write(send, nPos, var8);
                    nPos += var8;
                }

                return 0;
            } catch (IOException var7) {
                var7.printStackTrace();
                return 402;
            }
        }
    }

    private int SendData(String Data) {
        if (g_nConnect != 1) {
            return 101;
        } else {
            try {
                byte[] e = Data.getBytes(System.getProperty("file.encoding"));
                short nPackSize = 512;
                int nPos = 0;
                boolean nLen = false;
                int nNum = (e.length + nPackSize - 1) / nPackSize;

                for (int i = 0; i < nNum; ++i) {
                    int var9;
                    if (i == nNum - 1) {
                        var9 = e.length - i * nPackSize;
                    } else {
                        var9 = nPackSize;
                    }

                    out.write(e, nPos, var9);
                    nPos += var9;
                }

                return 0;
            } catch (IOException var8) {
                var8.printStackTrace();
                return 402;
            }
        }
    }

    public int PrintQRCode(int nMode, String strCode) {
        if (nMode != 49 && nMode != 50 && nMode != 51) {
            return 202;
        } else if (strCode.equals("")) {
            return 202;
        } else {
            byte[] send;
            try {
                send = strCode.getBytes(System.getProperty("file.encoding"));
            } catch (UnsupportedEncodingException var7) {
                var7.printStackTrace();
                return 402;
            }

            int nSize = send.length;
            byte[] lpData = new byte[nSize + 24];
            lpData[0] = 29;
            lpData[1] = 40;
            lpData[2] = 107;
            lpData[3] = 3;
            lpData[4] = 0;
            lpData[5] = 49;
            lpData[6] = 65;
            lpData[7] = (byte) nMode;
            lpData[8] = 29;
            lpData[9] = 40;
            lpData[10] = 107;
            lpData[11] = (byte) ((nSize + 3) % 256);
            lpData[12] = (byte) ((nSize + 3) / 256);
            lpData[13] = 49;
            lpData[14] = 80;
            lpData[15] = 48;

            for (int i = 0; i < nSize; ++i) {
                lpData[16 + i] = send[i];
            }

            lpData[16 + nSize] = 29;
            lpData[16 + nSize + 1] = 40;
            lpData[16 + nSize + 2] = 107;
            lpData[16 + nSize + 3] = 3;
            lpData[16 + nSize + 4] = 0;
            lpData[16 + nSize + 5] = 49;
            lpData[16 + nSize + 6] = 81;
            lpData[16 + nSize + 7] = 48;
            return this.SendData(lpData);
        }
    }
}
