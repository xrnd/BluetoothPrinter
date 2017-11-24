package com.xiphitech.bluetoothapp.bluetoothprinter_android.printingutils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xiphitech.bluetoothapp.bluetoothprinter_android.R;
import com.xiphitech.bluetoothapp.bluetoothprinter_android.zqprinterlib.SWZQPrinter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Apple on 18/11/15.
 */

public class DeviceListActivity extends AppCompatActivity implements Runnable {


    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final String TAG = "Device List";

    static Boolean isConnected;

    BluetoothDevice mBluetoothDevice;
    Set<BluetoothDevice> mPairedDevices;

    String titleToPrint;
    String messageToPrint;

    String barcodeString = "ITIS/BLUETOOTH/PRINT?PREVIEW";

    Thread mBlutoothConnectThread;
    SWZQPrinter PrinterService = null;
    private BluetoothAdapter mBluetoothAdapter;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;

    private UUID applicationUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private ProgressDialog mBluetoothConnectProgressDialog;
    private BluetoothSocket mBluetoothSocket;

    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> mAdapterView, View mView, int mPosition, long mLong) {


            try {
                //mBluetoothAdapter.cancelDiscovery();
                String mDeviceInfo = ((TextView) mView).getText().toString();
                String mDeviceAddress = mDeviceInfo.substring(mDeviceInfo.length() - 17);
                Log.v(TAG, "Device_Address " + mDeviceAddress);


                Log.v(TAG, "Coming incoming address " + mDeviceAddress);
                mBluetoothDevice = mBluetoothAdapter
                        .getRemoteDevice(mDeviceAddress);

                mBluetoothConnectProgressDialog = ProgressDialog.show(DeviceListActivity.this,
                        "Connecting...", mBluetoothDevice.getName() + " : "
                                + mBluetoothDevice.getAddress(), true, false);
                mBlutoothConnectThread = new Thread(DeviceListActivity.this);
                mBlutoothConnectThread.start();
                // pairToDevice(mBluetoothDevice); This method is replaced by
                // progress dialog with thread
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {


            mBluetoothConnectProgressDialog.dismiss();
            Toast.makeText(DeviceListActivity.this, "Device connected", Toast.LENGTH_LONG).show();

            PrintWithZQPrinter();

        }

    };

    public static byte intToByteArray(int value) {
        byte[] b = ByteBuffer.allocate(4).putInt(value).array();

        for (int k = 0; k < b.length; k++) {
            System.out.println("Selva  [" + k + "] = " + "0x"
                    + UnicodeFormat.byteToHex(b[k]));
        }

        return b[3];
    }

    @Override
    protected void onCreate(Bundle mSavedInstanceState) {
        super.onCreate(mSavedInstanceState);

        setContentView(R.layout.bluetooth_device_list);


        setResult(Activity.RESULT_CANCELED);
        mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.bluetooth_device_name);

        ListView mPairedListView = findViewById(R.id.paired_devices);
        mPairedListView.setAdapter(mPairedDevicesArrayAdapter);
        mPairedListView.setOnItemClickListener(mDeviceClickListener);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(DeviceListActivity.this, "Error", Toast.LENGTH_LONG).show();
        } else {
            if (!mBluetoothAdapter.isEnabled()) {

                Intent enableBtIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent,
                        REQUEST_ENABLE_BT);
            } else {
                ListPairedDevices();


                if (mPairedDevices.size() > 0) {
                    findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
                    for (BluetoothDevice mDevice : mPairedDevices) {
                        mPairedDevicesArrayAdapter.add(mDevice.getName() + "\n" + mDevice.getAddress());
                    }
                } else {
                    String mNoDevices = "None Paired";//getResources().getText(R.string.none_paired).toString();
                    mPairedDevicesArrayAdapter.add(mNoDevices);
                }

            }
        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Printer List");

        PrinterService = new SWZQPrinter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_printer_list, menu);


        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (android.R.id.home == id) {
            finish();
        }

        if (R.id.action_disconnect == id) {
            PrinterService.Disconnect();

        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        try {
            if (mBluetoothSocket != null)
                mBluetoothSocket.close();

            if (mBluetoothAdapter != null) {
                mBluetoothAdapter.cancelDiscovery();
            }

        } catch (Exception e) {
            Log.e("Tag", "Exe ", e);
        }
    }

    @Override
    public void onBackPressed() {
        try {
            if (mBluetoothSocket != null)
                mBluetoothSocket.close();
        } catch (Exception e) {
            Log.e("Tag", "Exe ", e);
        }
        setResult(RESULT_CANCELED);
        finish();
    }

    public void onActivityResult(int mRequestCode, int mResultCode,
                                 Intent mDataIntent) {
        super.onActivityResult(mRequestCode, mResultCode, mDataIntent);

        switch (mRequestCode) {
            case REQUEST_CONNECT_DEVICE:
                if (mResultCode == Activity.RESULT_OK) {
                    Bundle mExtra = mDataIntent.getExtras();
                    String mDeviceAddress = mExtra.getString("DeviceAddress");
                    Log.v(TAG, "Coming incoming address " + mDeviceAddress);
                    mBluetoothDevice = mBluetoothAdapter
                            .getRemoteDevice(mDeviceAddress);
                    mBluetoothConnectProgressDialog = ProgressDialog.show(this,
                            "Connecting...", mBluetoothDevice.getName() + " : "
                                    + mBluetoothDevice.getAddress(), true, false);
                    Thread mBlutoothConnectThread = new Thread(this);
                    mBlutoothConnectThread.start();
                    // pairToDevice(mBluetoothDevice); This method is replaced by
                    // progress dialog with thread
                }
                break;

            case REQUEST_ENABLE_BT:
                if (mResultCode == Activity.RESULT_OK) {
                    ListPairedDevices();
                    Intent connectIntent = new Intent(DeviceListActivity.this,
                            DeviceListActivity.class);
                    startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
                } else {
                    Toast.makeText(DeviceListActivity.this, "Message", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void ListPairedDevices() {
        mPairedDevices = mBluetoothAdapter
                .getBondedDevices();
        if (mPairedDevices.size() > 0) {
            for (BluetoothDevice mDevice : mPairedDevices) {
                Log.v(TAG, "PairedDevices: " + mDevice.getName() + "  "
                        + mDevice.getAddress());
            }
        }
    }

    public void run() {
//        try {
//
        //mBluetoothSocket = mBluetoothDevice
        //     .createRfcommSocketToServiceRecord(applicationUUID);
        // mBluetoothAdapter.cancelDiscovery();
        //mBluetoothSocket.connect();
        // mHandler.sendEmptyMessage(0);

//        } catch (IOException eConnectException) {
//            Log.d(TAG, "CouldNotConnectToSocket", eConnectException);
//            closeSocket(mBluetoothSocket);
//            return;
//        }


        try {


            int nRet = PrinterService.Connect(mBluetoothDevice.getAddress());


            if (nRet == 0) {

                isConnected = true;
                mHandler.sendEmptyMessage(0);

            } else {

                mBluetoothConnectProgressDialog.dismiss();

                isConnected = false;
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    private void closeSocket(BluetoothSocket nOpenSocket) {
        try {
            nOpenSocket.close();

            mBluetoothConnectProgressDialog.dismiss();

            Log.d(TAG, "SocketClosed");

        } catch (IOException ex) {
            Log.d(TAG, "CouldNotCloseSocket");
        }
    }

    private void PrintWithZQPrinter() {


        if (isConnected) {

            int returnvalue = SWZQPrinter.AB_SUCCESS;

            Bitmap bm = null;
            try {

                bm = BitmapFactory.decodeResource(getResources(), R.drawable.print_image);

                returnvalue = PrinterService.PrintBitmap1D76(bm, 0);
                PrinterService.LineFeed(1);

            } catch (IOException e) {
                e.printStackTrace();
            }

            //PRINT COMPANY NAME
            returnvalue = PrinterService.PrintText(titleToPrint
                    , SWZQPrinter.ALIGNMENT_CENTER,
                    SWZQPrinter.FT_BOLD,
                    SWZQPrinter.TS_0WIDTH | SWZQPrinter.TS_0HEIGHT);
            PrinterService.LineFeed(1);

            //PRINT MESSAGE
            returnvalue = PrinterService.PrintText(messageToPrint
                    , SWZQPrinter.ALIGNMENT_LEFT, SWZQPrinter.FT_DEFAULT,
                    SWZQPrinter.TS_0WIDTH | SWZQPrinter.TS_0HEIGHT);
            PrinterService.LineFeed(1);


            //PRINT BARCODE
            byte[] dataBarcode = new byte[0];
            try {

                dataBarcode = barcodeString.getBytes(System.getProperty("file.encoding"));
                int returnvalueFinal = SWZQPrinter.AB_SUCCESS;

                returnvalueFinal = PrinterService.PrintBarcode(dataBarcode, dataBarcode.length,
                        SWZQPrinter.BCS_Code39, 50, 3,
                        SWZQPrinter.ALIGNMENT_CENTER,
                        SWZQPrinter.BC_TEXT_BELOW);

                PrinterService.LineFeed(3);

                if (returnvalueFinal == SWZQPrinter.AB_SUCCESS) {

                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            Toast.makeText(DeviceListActivity.this, "check printer & bluetooth", Toast.LENGTH_SHORT).show();

        }

    }


    private String printSeperator() {

        return "\n--------------------------------\n";

    }

}
