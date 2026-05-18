package fr.smartglasses.frontend.service;

import org.eclipse.paho.client.mqttv3.*;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ClientMqtt {

    private final String brokerUrl;
    private MqttClient client;
    private final BlockingQueue<String> responseQueue = new LinkedBlockingQueue<>();

    public ClientMqtt(String brokerUrl) {
        this.brokerUrl = brokerUrl;
    }

    /**
     * Établit la connexion au broker MQTT
     */
    public void connect(String clientId) throws MqttException {
        client = new MqttClient(brokerUrl, clientId);
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                System.out.println("Connexion MQTT perdue : " + cause.getMessage());
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                String payload = new String(message.getPayload());
                System.out.println("Message reçu sur " + topic + " : " + payload);
                responseQueue.offer(payload);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
            }
        });
        client.connect();
    }

    // TODO
}

