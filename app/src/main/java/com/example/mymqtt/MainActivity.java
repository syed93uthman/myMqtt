package com.example.mymqtt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    JsonHandler myJson;
    MqttHandler myMqtt;

    Button connectButton;
    Button sendButton;
    TextView connectStatusTextView;
    TextView brokerIpTextView;
    TextView portTextView;
    TextView topicTextView;

    TextView idTextView;
    TextView latTextView;
    TextView longTextView;
    TextView altTextView;
    TextView elevationTextView;
    TextView headingTextView;

    TextView idJsonTextView;
    TextView latJsonTextView;
    TextView longJsonTextView;
    TextView altJsonTextView;
    TextView elevationJsonTextView;
    TextView headingJsonTextView;

    Button messageActivityButton;

    String brokerIp;
    String port;
    String topic;
    String message;
    String clientId;

    Boolean conStatus = false;

    JSONObject droneJson;

    private int run = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myJson = new JsonHandler();
        myMqtt  = new MqttHandler();

        connectButton =  findViewById(R.id.connectButton);
        sendButton = findViewById(R.id.sendButton);
        connectStatusTextView = (TextView) findViewById(R.id.connectStatusTextView);
        brokerIpTextView = (TextView) findViewById(R.id.brokerIpTextView);
        portTextView = (TextView) findViewById(R.id.portTextView);
        topicTextView = (TextView) findViewById(R.id.topicTextView);

        idTextView = findViewById(R.id.idTextView);
        latTextView = findViewById(R.id.latTextView);
        longTextView = findViewById(R.id.longTextView);
        altTextView = findViewById(R.id.altTextView);
        elevationTextView = findViewById(R.id.elevationTextView);
        headingTextView = findViewById(R.id.headingTextView);

        idJsonTextView = findViewById(R.id.idJsonTextView);
        latJsonTextView = findViewById(R.id.latJsonTextView);
        longJsonTextView = findViewById(R.id.longJsonTextView);
        altJsonTextView = findViewById(R.id.altJsonTextView);
        elevationJsonTextView = findViewById(R.id.elevationJsonTextView);
        headingJsonTextView = findViewById(R.id.headingJsonTextView);

        messageActivityButton = findViewById(R.id.messageActivityButton);

        clientId = MqttClient.generateClientId();

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                brokerIp = brokerIpTextView.getText().toString();
                port = portTextView.getText().toString();
                topic = topicTextView.getText().toString();
                establish();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(conStatus){
                    topic = topicTextView.getText().toString();
                    myMqtt.publishMessage(droneJson.toString());
                }
            }
        });



        try {
            droneJson = myJson.createJson(this.getAssets().open("droneData.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        messageActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    droneJson.put("id",idTextView.getText().toString());
                    droneJson.put("lat",latTextView.getText().toString());
                    droneJson.put("long",longTextView.getText().toString());
                    droneJson.put("altitude",altTextView.getText().toString());
                    droneJson.put("elevationCam",elevationTextView.getText().toString());
                    droneJson.put("heading",headingTextView.getText().toString());

                    idJsonTextView.setText(droneJson.getString("id"));
                    latJsonTextView.setText(droneJson.getString("lat"));
                    longJsonTextView.setText(droneJson.getString("long"));
                    altJsonTextView.setText(droneJson.getString("altitude"));
                    elevationJsonTextView.setText(droneJson.getString("elevationCam"));
                    headingJsonTextView.setText(droneJson.getString("heading"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void establish(){
        if(myMqtt.connect(brokerIp,port,topic,this)) {
            connectStatusTextView.setText("Connected");
            conStatus = true;
        }
        else {
            connectStatusTextView.setText("Not Connected");
            conStatus = false;
        }
    }


}