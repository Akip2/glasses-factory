package fr.smartglasses.frontend.controller;

import fr.smartglasses.frontend.service.ClientMqtt;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.concurrent.CompletableFuture;

/**
 * Gère la vérification des numéros de série grace au serveur MQTT
 */
public class SerialController {

    /**
     * Vérifie si un numéro de série est valide auprès du serveur
     *
     * @param serialNumber le numéro de série à vérifier
     * @return un CompletableFuture qui contient vrai ou faux en fonction de la réposne du serveur
     * @throws RuntimeException si la connexion au serveur échoue ou si le délai expire
     */
    public boolean isValid(String serialNumber) {
        try {
            ClientMqtt client = new ClientMqtt("tcp://localhost:1883");
            String response = client.verifierSerie(serialNumber);
            client.disconnect();
            return !response.equals("invalid");
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la vérification : " + e.getMessage());
        }
    }
}