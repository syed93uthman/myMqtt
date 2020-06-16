package com.example.mymqtt;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttItem implements Parcelable {
    MqttAndroidClient client ;
    MqttMessage msg;

    String brokerIp;
    String port;
    String topic;
    String message;
    String clientId;

    boolean conStatus = false;

    public void set(String brokerIp, String port, String topic) {
        this.brokerIp = brokerIp;
        this.port = port;
        this.topic = topic;
        this.clientId = MqttClient.generateClientId();
    }

    public void connect(Context context){
        if(createClient(context)){
            try {
                IMqttToken token = client.connect();
                token.setActionCallback(new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        conStatus = true;
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        conStatus = false;
                    }
                });
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    public void disconnect(){
        try {
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publishMessage(String message){
        if(conStatus){
            msg = new MqttMessage(message.getBytes());
            try {
                client.publish(topic,msg);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean createClient(Context context){
        if(brokerIp.isEmpty()){
            Toast.makeText(context,"ip is empty",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(topic.isEmpty()){
            Toast.makeText(context,"Topic is empty",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(message.isEmpty()){
            Toast.makeText(context,"Message is empty",Toast.LENGTH_LONG).show();
            return false;
        }
        else{
            if(port.isEmpty()){

                port = "1883";
            }
            String address = "tcp://" + brokerIp +":" + port;
            client = new MqttAndroidClient(context,address,clientId);
            return true;
        }
    }

    protected MqttItem(Parcel in) {
        brokerIp = in.readString();
        port = in.readString();
        topic = in.readString();
        message = in.readString();
        clientId = in.readString();
    }

    public static final Creator<MqttItem> CREATOR = new Creator<MqttItem>() {
        @Override
        public MqttItem createFromParcel(Parcel in) {
            return new MqttItem(in);
        }

        @Override
        public MqttItem[] newArray(int size) {
            return new MqttItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(brokerIp);
        parcel.writeString(port);
        parcel.writeString(topic);
        parcel.writeString(message);
        parcel.writeString(clientId);
    }
}
