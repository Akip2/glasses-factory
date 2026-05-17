import org.eclipse.paho.client.mqttv3.*;

/**
 * Gère la connexion au broker MQTT et les abonnements aux topics.
 */
public class Serveur {
    private final Usine usine;

    public Serveur(Usine usine) {
        this.usine = usine;
    }

    /**
     * Connecte le client au broker et s'abonne aux topics orders et serials.
     *
     * @param url l'url du broker MQTT
     * @param clientId l'identifiant du client
     * @throws MqttException si la connexion échoue
     */
    public void start(String url, String clientId) throws MqttException {
        MqttClient client = new MqttClient(url, clientId);

        client.connect();

        client.setCallback(new MessageManager(this.usine, client));

        client.subscribe("orders/+");
        client.subscribe("serials/+/check");
    }
}