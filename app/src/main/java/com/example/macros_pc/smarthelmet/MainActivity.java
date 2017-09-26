package com.example.macros_pc.smarthelmet;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.android.gms.maps.SupportMapFragment;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "shelmet";

    Handler h;

    private static final int REQUEST_ENABLE_BT = 1;
    final int RECIEVE_MESSAGE = 1;		// Статус для Handler
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder sb = new StringBuilder();
    TextView time;
    long date;

    private ConnectedThread mConnectedThread;

    // SPP UUID сервиса
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // MAC-адрес Bluetooth модуля
    private static String address = "20:16:11:16:77:70";



    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        time = (TextView) findViewById(R.id.time);
        date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm");
        String dateString = sdf.format(date);
        time.setText(dateString);

        //txtArduino = (TextView) findViewById(R.id.txtArduino);		// для вывода текста, полученного от Arduino
        TextView maps = (TextView) findViewById(R.id.map1);
        maps.setBackgroundColor(Color.BLACK);
        final ImageSwitcher leftsw = (ImageSwitcher)findViewById(R.id.left);
        leftsw.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imgl = new ImageView(getApplicationContext());
                imgl.setScaleType(ImageView.ScaleType.FIT_CENTER);
                return imgl;
            }
        });
        leftsw.setImageResource(R.drawable.left_arrow);
        final ImageSwitcher stopsw = (ImageSwitcher)findViewById(R.id.stop);
        stopsw.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imgl = new ImageView(getApplicationContext());
                imgl.setScaleType(ImageView.ScaleType.FIT_CENTER);
                return imgl;
            }
        });
        stopsw.setImageResource(R.drawable.stopp);
        final ImageSwitcher rightsw = (ImageSwitcher)findViewById(R.id.right);
        rightsw.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imgl = new ImageView(getApplicationContext());
                imgl.setScaleType(ImageView.ScaleType.FIT_CENTER);
                return imgl;
            }
        });
        rightsw.setImageResource(R.drawable.right_arrow);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        //Log.i("map", Boolean.toString(mapFragment == null));
        Navigation n = new Navigation();
        //Log.i("nav", Boolean.toString(n == null));

        mapFragment.getMapAsync(n);

        h = new Handler() {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case RECIEVE_MESSAGE:													// если приняли сообщение в Handler
                        byte[] readBuf = (byte[]) msg.obj;
                        String strIncom = new String(readBuf, 0, msg.arg1);
                        sb.append(strIncom);												// формируем строку
                        int endOfLineIndex = sb.indexOf("\r\n");							// определяем символы конца строки
                        if (endOfLineIndex > 0) { 											// если встречаем конец строки,
                            String sbprint = sb.substring(0, endOfLineIndex);				// то извлекаем строку
                            sb.delete(0, sb.length());                                      // и очищаем sb
                            if(sbprint.equals("Left")){
                                leftsw.setImageResource(R.drawable.gleft_arrow);
                            }else{
                                if(sbprint.equals("Right")){
                                    rightsw.setImageResource(R.drawable.gright_arrow);
                                }else{
                                    if(sbprint.equals("Stop")){
                                        stopsw.setImageResource(R.drawable.rstop);
                                    }else{
                                        if(sbprint.equals("CanselL")){
                                            leftsw.setImageResource(R.drawable.left_arrow);
                                        }else{
                                            if(sbprint.equals("CanselR")){
                                                rightsw.setImageResource(R.drawable.right_arrow);
                                            }else{
                                                if(sbprint.equals("CanselS")){
                                                    stopsw.setImageResource(R.drawable.stopp);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            //txtArduino.setText(sbprint); 	        // обновляем TextView
                        }
                        break;
                }
            };
        };

        btAdapter = BluetoothAdapter.getDefaultAdapter();		// получаем локальный Bluetooth адаптер
        checkBTState();
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "...onResume - попытка соединения...");

        // Set up a pointer to the remote node using it's address.
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        // Two things are needed to make a connection:
        //   A MAC address, which we got above.
        //   A Service ID or UUID.  In this case we are using the
        //     UUID for SPP.
        try {
            btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            errorExit("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
        }

        // Discovery is resource intensive.  Make sure it isn't going on
        // when you attempt to connect and pass your message.
        btAdapter.cancelDiscovery();

        // Establish the connection.  This will block until it connects.
        Log.d(TAG, "...Соединяемся...");
        try {
            btSocket.connect();
            Log.d(TAG, "...Соединение установлено и готово к передачи данных...");
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
            }
        }

        // Create a data stream so we can talk to server.
        Log.d(TAG, "...Создание Socket...");

        mConnectedThread = new ConnectedThread(btSocket);
        mConnectedThread.start();
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d(TAG, "...In onPause()...");

        try     {
            btSocket.close();
        } catch (IOException e2) {
            errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
        }
    }

    private void checkBTState() {
        // Check for Bluetooth support and then check to make sure it is turned on
        // Emulator doesn't support Bluetooth and will return null
        if(btAdapter==null) {
            errorExit("Fatal Error", "Bluetooth не поддерживается");
        } else {
            if (btAdapter.isEnabled()) {
                Log.d(TAG, "...Bluetooth включен...");
            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(btAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    private void errorExit(String title, String message){
        Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
        finish();
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[256];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);		// Получаем кол-во байт и само собщение в байтовый массив "buffer"
                    h.obtainMessage(RECIEVE_MESSAGE, bytes, -1, buffer).sendToTarget();		// Отправляем в очередь сообщений Handler
                } catch (IOException e) {
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(String message) {
            Log.d(TAG, "...Данные для отправки: " + message + "...");
            byte[] msgBuffer = message.getBytes();
            try {
                mmOutStream.write(msgBuffer);
            } catch (IOException e) {
                Log.d(TAG, "...Ошибка отправки данных: " + e.getMessage() + "...");
            }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }
}
