package com.xiphitech.bluetoothapp.bluetoothprinter_android;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.xiphitech.bluetoothapp.bluetoothprinter_android.printingutils.BTDeviceList;
import com.xiphitech.bluetoothapp.bluetoothprinter_android.printingutils.DeviceListActivity;

import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {


    private static final int REQUEST_ENABLE_BT = 2;
    private static BluetoothSocket btsocket;
    private static OutputStream btoutputstream;

    TextView message;

    byte FONT_TYPE;

    String messageToPrint;
    String titleToPrint;

    private BluetoothAdapter mBluetoothAdapter;

    public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleWidth);
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 10, 10, width,
                height, matrix, true);
        return resizedBitmap;
    }

    private static byte[] StartBmpToPrintCode(Bitmap bitmap, int t) {
        byte temp = 0;
        int j = 7;
        int start = 0;
        if (bitmap != null) {
            int mWidth = bitmap.getWidth();
            int mHeight = bitmap.getHeight();

            int[] mIntArray = new int[mWidth * mHeight];
            byte[] data = new byte[mWidth * mHeight];
            bitmap.getPixels(mIntArray, 0, mWidth, 0, 0, mWidth, mHeight);
            encodeYUV420SP(data, mIntArray, mWidth, mHeight, t);
            byte[] result = new byte[mWidth * mHeight / 8];
            for (int i = 0; i < mWidth * mHeight; i++) {
                temp = (byte) ((byte) (data[i] << j) + temp);
                j--;
                if (j < 0) {
                    j = 7;
                }
                if (i % 8 == 7) {
                    result[start++] = temp;
                    temp = 0;
                }
            }
            if (j != 7) {
                result[start++] = temp;
            }

            int aHeight = 24 - mHeight % 24;
            byte[] add = new byte[aHeight * 48];
            byte[] nresult = new byte[mWidth * mHeight / 8 + aHeight * 48];
            System.arraycopy(result, 0, nresult, 0, result.length);
            System.arraycopy(add, 0, nresult, result.length, add.length);

            byte[] byteContent = new byte[(mWidth / 8 + 4)
                    * (mHeight + aHeight)];// ´òÓ¡Êý×é
            byte[] bytehead = new byte[4];// Ã¿ÐÐ´òÓ¡Í·
            bytehead[0] = (byte) 0x1f;
            bytehead[1] = (byte) 0x10;
            bytehead[2] = (byte) (mWidth / 8);
            bytehead[3] = (byte) 0x00;
            for (int index = 0; index < mHeight + aHeight; index++) {
                System.arraycopy(bytehead, 0, byteContent, index * 52, 4);
                System.arraycopy(nresult, index * 48, byteContent,
                        index * 52 + 4, 48);

            }
            return byteContent;
        }
        return null;

    }

    public static void encodeYUV420SP(byte[] yuv420sp, int[] rgba, int width,
                                      int height, int t) {
        final int frameSize = width * height;
        int[] U, V;
        U = new int[frameSize];
        V = new int[frameSize];
        final int uvwidth = width / 2;
        int r, g, b, y, u, v;
        int bits = 8;
        int index = 0;
        int f = 0;
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                r = (rgba[index] & 0xff000000) >> 24;
                g = (rgba[index] & 0xff0000) >> 16;
                b = (rgba[index] & 0xff00) >> 8;
                // rgb to yuv
                y = ((66 * r + 129 * g + 25 * b + 128) >> 8) + 16;
                u = ((-38 * r - 74 * g + 112 * b + 128) >> 8) + 128;
                v = ((112 * r - 94 * g - 18 * b + 128) >> 8) + 128;
                // clip y
                // yuv420sp[index++] = (byte) ((y < 0) ? 0 : ((y > 255) ? 255 :
                // y));
                byte temp = (byte) ((y < 0) ? 0 : ((y > 255) ? 255 : y));
                if (t == 0) {
                    yuv420sp[index++] = temp > 0 ? (byte) 1 : (byte) 0;
                } else {
                    yuv420sp[index++] = temp > 0 ? (byte) 0 : (byte) 1;
                }

                // {
                // if (f == 0) {
                // yuv420sp[index++] = 0;
                // f = 1;
                // } else {
                // yuv420sp[index++] = 1;
                // f = 0;
                // }

                // }

            }

        }
        f = 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        titleToPrint = ((TextView) findViewById(R.id.hello)).getText().toString();
        messageToPrint = ((TextView) findViewById(R.id.lorem)).getText().toString();

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {

            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();

        } else {
            if (!mBluetoothAdapter.isEnabled()) {

                Intent enableBtIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent,
                        REQUEST_ENABLE_BT);

            } else {
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (btsocket != null) {
                btoutputstream.close();
                btsocket.close();
                btsocket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            btsocket = BTDeviceList.getSocket();
            if (btsocket != null) {
                print_bt();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_printpreview, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the HomeFragment/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_print) {

            if (mBluetoothAdapter == null) {

                Toast.makeText(this, "Bluetooth Not Supported !", Toast.LENGTH_LONG).show();

            } else {
                if (!mBluetoothAdapter.isEnabled()) {

                    Toast.makeText(this, "Please Turn On Bluetooth To Print Challan.", Toast.LENGTH_LONG).show();

                } else {

                    connect();

                }

            }

        }
        if (android.R.id.home == id) {

            finish();

        }

        return super.onOptionsItemSelected(item);

    }

    public void connect() {

        Intent i = new Intent(this, DeviceListActivity.class);
        startActivity(i);

           /* if(btsocket == null){
                Intent BTIntent = new Intent(getApplicationContext(), BTDeviceList.class);
                this.startActivityForResult(BTIntent, BTDeviceList.REQUEST_CONNECT_BT);
            }

            else{

                OutputStream opstream = null;
                try {
                    opstream = btsocket.getOutputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                btoutputstream = opstream;
                print_bt();

            }*/


    }

    private void print_bt() {
        try {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            byte[] arrayOfByte1 = {27, 33, 0};
            btoutputstream = btsocket.getOutputStream();

            //BOLD
            byte[] format = {27, 33, 0};
            format[2] = ((byte) (0x8 | arrayOfByte1[2]));
            btoutputstream.write(format);

            //            String Title="           M/s SKYLARK            \n";
            //                   Title+="     CONSTRUCTIONS PVT.LTD.      \n";

            String Title = titleToPrint;

            btoutputstream.write(Title.getBytes());

            //NORMAL
            btoutputstream.write(arrayOfByte1);
            btoutputstream.write(messageToPrint.getBytes());


            btoutputstream.write(0x0D);
            btoutputstream.write(0x0D);
            btoutputstream.write(0x0D);
            btoutputstream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
