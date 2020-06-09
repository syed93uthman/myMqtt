package com.example.mymqtt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttConnect;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttWireMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    MqttAndroidClient client;
    MqttMessage msg;

    private int run = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView mqttBtn = (TextView) findViewById(R.id.mqttBtn);
        final String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(MainActivity.this,"tcp://192.168.1.20:1883",clientId);

        mqttBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (true) {
                    msg = new MqttMessage("From Android".getBytes());
                    try {
                        IMqttToken token = client.connect();
                        token.setActionCallback(new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                // We are connected
                                Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_LONG).show();
//                    Log.d(TAG, "onSuccess");
                                try {
                                    client.publish("/test/data",msg);
                                } catch (MqttException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                // Something went wrong e.g. connection timeout or firewall problems
                                Toast.makeText(MainActivity.this, "Connection Fail", Toast.LENGTH_LONG).show();
//                    Log.d(TAG, "onFailure");

                            }
                        });
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if(client.isConnected()){
                    mqttBtn.setText("Connected");
                }
                else{
                    mqttBtn.setText("Not Connected");
                }
            }
        });


    }
}