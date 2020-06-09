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
import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    MqttAndroidClient client;
    MqttMessage msg;

    TextView connectButton;
    TextView sendButton;
    TextView connectStatusTextView;
    TextView brokerIpTextView;
    TextView portTextView;
    TextView topicTextView;
    TextView messageTextView;

    String brokerIp;
    String port;
    String topic;
    String message;
    String clientId;

    private int run = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectButton = (TextView) findViewById(R.id.connectButton);
        sendButton = (TextView) findViewById(R.id.sendButton);
        connectStatusTextView = (TextView) findViewById(R.id.connectStatusTextView);
        brokerIpTextView = (TextView) findViewById(R.id.brokerIpTextView);
        portTextView = (TextView) findViewById(R.id.portTextView);
        topicTextView = (TextView) findViewById(R.id.topicTextView);
        messageTextView = (TextView) findViewById(R.id.messageTextView);

        clientId = MqttClient.generateClientId();



        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                brokerIp = brokerIpTextView.toString();
                port = portTextView.toString();
                topic = topicTextView.toString();
                message = messageTextView.toString();

                if(checkTextView()){
                    connectStatusTextView.setText("Connected");
                }
                else{
                    connectStatusTextView.setText("Not Connected");
                }
            }
        });

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if(client.isConnected()){
                    connectButton.setText("Connected");
                }
                else{
                    connectButton.setText("Not Connected");
                }
            }
        });




    }

    public boolean checkTextView(){
        if(brokerIp.isEmpty()){
            Toast.makeText(MainActivity.this,"Broker Ip is Empty",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(topic.isEmpty()){
            Toast.makeText(MainActivity.this,"Topic is Empty",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(message.isEmpty()){
            Toast.makeText(MainActivity.this,"Message is Empty",Toast.LENGTH_LONG).show();
            return false;
        }
        else{
            if(port.isEmpty()){
                port = "1883";
            }
            String address = "tcp//" + brokerIp +":" + port;
            client = new MqttAndroidClient(MainActivity.this,address,clientId);
            return true;
        }
    }

}