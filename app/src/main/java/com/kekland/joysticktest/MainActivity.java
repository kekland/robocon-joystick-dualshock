package com.kekland.joysticktest;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.github.controlwear.virtual.joystick.android.JoystickView;
import me.aflak.bluetooth.Bluetooth;

/**
 * Created by kekland322 on 11/9/17.
 */

public class MainActivity extends AppCompatActivity {

    ControllerProcess process = new ControllerProcess();

    View bluetoothSettings;

    Bluetooth adapter;
    BluetoothDevice selectedDevice;

    Boolean locked = true;
    //HomeKeyLocker locker = new HomeKeyLocker();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //locker.Lock(this);

        /*findViewById(R.id.lockButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locked = !locked;
                if(locked) {
                    locker.Lock(MainActivity.this);
                }
                else {
                    locker.Lock(MainActivity.this);
                }
            }
        });*/

        adapter = new Bluetooth(MainActivity.this);

        bluetoothSettings = findViewById(R.id.bluetoothSettings);

        bluetoothSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBluetoothSettingsClick();
            }
        });

        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String dat = process.data.toString();
                            ((TextView) findViewById(R.id.debugData)).setText(dat);

                            if(connected) {
                                adapter.send(dat);
                            }
                        } catch (Exception e) {
                            Log.e("Error Updating", e.toString());
                        }
                    }
                });
            }
        }, 100, 100, TimeUnit.MILLISECONDS);
    }

    void onBluetoothSettingsClick() {
        List<BluetoothDevice> devices = adapter.getPairedDevices();
        String[] list = new String[devices.size()];

        final BluetoothDevice[] devicesA = new BluetoothDevice[devices.size()];

        if (devices.size()>0)
        {
            int i = 0;
            for(BluetoothDevice bt : devices)
            {
                list[i] = bt.getName() + "\n" + bt.getAddress(); //Get the device's name and the address
                devicesA[i] = bt;
                i++;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Select device")
                    .setItems(list, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            selectedDevice = devicesA[which];
                            new ConnectBT().execute();
                        }
                    });

            builder.create().show();
        }
        else
        {
            Toast.makeText(MainActivity.this, "No Paired Bluetooth Devices Found.",
                    Toast.LENGTH_LONG).show();
        }
    }

    boolean connected = false;
    //static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private ProgressDialog progress;
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            connected = false;
            progress = ProgressDialog.show(MainActivity.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (!connected)
                {
                    adapter.connectToAddress(selectedDevice.getAddress());
                }
            }
            catch (Exception e)
            {
                Log.e("Error", e.toString());
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                Toast.makeText(MainActivity.this, "Connection failed.", Toast.LENGTH_SHORT).show();
                connected = false;
                ((TextView)findViewById(R.id.bluetoothName)).setText(
                        "Failed connecting"
                );
            }
            else
            {
                ((TextView)findViewById(R.id.bluetoothName)).setText(
                        selectedDevice.getName()
                );
                connected = true;
                Toast.makeText(MainActivity.this, "Connected.", Toast.LENGTH_SHORT).show();
            }
            progress.dismiss();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(process.HandleButtonsDown(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(process.HandleButtonsUp(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {

        if(process.HandleStickMovement(event)) {
            return true;
        }
        return super.onGenericMotionEvent(event);
    }

    /*@Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            //Base movements
            case KeyEvent.KEYCODE_W:
                data.setStickLY(0);
                data.setStickRY(0);
                return true;
            case KeyEvent.KEYCODE_A:
                data.setStickLX(0);
                data.setStickRX(0);
                return true;
            case KeyEvent.KEYCODE_S:
                data.setStickLY(0);
                data.setStickRY(0);
                return true;
            case KeyEvent.KEYCODE_D:
                data.setStickLY(0);
                data.setStickRY(0);
                return true;

            //Lift1
            case KeyEvent.KEYCODE_Q:
                data.setButtonL1(false);
                return true;
            case KeyEvent.KEYCODE_E:
                data.setButtonL2(false);
                return true;

            //Claw1
            case KeyEvent.KEYCODE_R:
                data.setButtonR1(false);
                return true;
            case KeyEvent.KEYCODE_F:
                data.setButtonR2(false);
            default:
                return super.onKeyUp(keyCode, event);
        }
    }*/

   /* @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_W:
                data.setStickLY(100);
                data.setStickRY(100);
                //data.setStickLX(0);
                //data.setStickRX(0);
                return true;
            case KeyEvent.KEYCODE_A:
                data.setStickLX(-100);
                data.setStickRX(100);
                //data.setStickLY(0);
                //data.setStickRY(0);
                return true;
            case KeyEvent.KEYCODE_S:
                data.setStickLY(-100);
                data.setStickRY(-100);
                //data.setStickLX(0);
                //data.setStickRX(0);
                return true;
            case KeyEvent.KEYCODE_D:
                data.setStickLX(100);
                data.setStickRX(-100);
                //data.setStickLY(0);
                //data.setStickRY(0);
                return true;

            //Lift1
            case KeyEvent.KEYCODE_Q:
                data.setButtonL1(true);
                return true;
            case KeyEvent.KEYCODE_E:
                data.setButtonL2(true);
                return true;

            //Claw1
            case KeyEvent.KEYCODE_R:
                data.setButtonR1(true);
                return true;
            case KeyEvent.KEYCODE_F:
                data.setButtonR2(true);
            default:
                return super.onKeyUp(keyCode, event);
        }
    }*/
}
