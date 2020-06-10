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
    MqttAndroidClient client ;
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

    Boolean conStatus = false;

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
                brokerIp = brokerIpTextView.getText().toString();
                port = portTextView.getText().toString();
                topic = topicTextView.getText().toString();
                message = messageTextView.getText().toString();

                if(checkTextView()){

                    try {
                        IMqttToken token = client.connect();
                        token.setActionCallback(new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                connectStatusTextView.setText("Connected");
                                conStatus = true;
                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                connectStatusTextView.setText("Fail to Connect");
                                conStatus = false;
                            }
                        });
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }

                }
                else{
                    connectStatusTextView.setText("Not Connected");
                    conStatus = false;
                }
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(conStatus){
                    topic = topicTextView.getText().toString();
                    message = messageTextView.getText().toString();
                    msg = new MqttMessage(message.getBytes());
                    try {
                        client.publish(topic,msg);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if(client != null) {
                    if (client.isConnected()) {
                        connectButton.setText("Connected");
                    } else {
                        connectButton.setText("Not Connected");
                    }
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
            String address = "tcp://" + brokerIp +":" + port;
            client = new MqttAndroidClient(MainActivity.this,address,clientId);
            return true;
        }
    }

}