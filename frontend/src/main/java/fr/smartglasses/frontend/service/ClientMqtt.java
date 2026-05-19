package fr.smartglasses.frontend.service;

import org.eclipse.paho.client.mqttv3.*;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class ClientMqtt implements MqttCallback {

    private final MqttClient client;

    // Files d'attente pour synchroniser les réponses MQTT avec le thread appelant
    private final BlockingQueue<String> orderResponseQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<String> serialResponseQueue = new LinkedBlockingQueue<>();

    /*
     * Constructeur de ClientMqtt
     *
     * @param brokerUrl l'url du broker MQTT
     */
    public ClientMqtt(String brokerUrl) throws MqttException {
        this.client = new MqttClient(brokerUrl, MqttClient.generateClientId());
        this.client.setCallback(this);
        this.client.connect();

        this.client.subscribe("orders/+/validated");
        this.client.subscribe("orders/+/delivery");
        this.client.subscribe("orders/+/error");
        this.client.subscribe("orders/+/cancelled");
        this.client.subscribe("serials/+");
    }

    /**
     * Publie une commande et attend la réponse
     *
     * @param orderId identifiant de la commande
     * @param payload le contenu de la commande
     * @return le payload de livraison (numéros de série)
     * @throws Exception si la commande est annulée, en erreur, ou si le timeout expire
     */
    public String passerCommande(String orderId, String payload) throws Exception {
        orderResponseQueue.clear();
        client.publish("orders/" + orderId, new MqttMessage(payload.getBytes()));

        String response = orderResponseQueue.poll(60, TimeUnit.SECONDS);

        if (response == null) {
            throw new Exception("Timeout : pas de réponse du serveur");
        }
        if (response.startsWith("ERROR:") || response.startsWith("CANCELLED:")) {
            throw new Exception(response.substring(response.indexOf(':') + 1));
        }
        return response; // payload delivery : "TYPE:SERIAL;TYPE:SERIAL;..."
    }

    /**
     * Publie une demande de vérification de numéro de série et attend la réponse
     *
     * @param numeroSerie le numéro de série à vérifier
     * @return le type de lunette associé, ou "invalid"
     */
    public String verifierSerie(String numeroSerie) throws Exception {
        serialResponseQueue.clear();
        client.publish("serials/" + numeroSerie + "/check", new MqttMessage(new byte[0]));

        String response = serialResponseQueue.poll(10, TimeUnit.SECONDS);

        if (response == null) {
            throw new Exception("Timeout : pas de réponse du serveur");
        }
        return response;
    }

    /**
     * Traite les messages MQTT reçus
     *
     * @param topic le topic du message
     * @param message le message reçu
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) {
        String payload = new String(message.getPayload());
        String[] parts = topic.split("/");

        if (parts[0].equals("orders") && parts.length == 3) {
            String event = parts[2];
            switch (event) {
                case "validated" -> {}
                case "delivery"  -> orderResponseQueue.offer(payload);
                case "error"     -> orderResponseQueue.offer("ERROR:" + payload);
                case "cancelled" -> orderResponseQueue.offer("CANCELLED:" + payload);
            }
        } else if (parts[0].equals("serials") && parts.length == 2) {
            serialResponseQueue.offer(payload);
        }
    }

    public void disconnect() throws MqttException {
        if (client.isConnected()) client.disconnect();
    }

    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("Connexion MQTT perdue : " + cause.getMessage());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {}
}