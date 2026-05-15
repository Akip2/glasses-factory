import org.eclipse.paho.client.mqttv3.*;

public class Serveur {
    private final Usine usine;

    public Serveur(Usine usine) {
        this.usine = usine;
    }

    public void start(String url, String clientId) throws MqttException {
        MqttClient client = new MqttClient(url, clientId);

        client.connect();

        client.setCallback(new MessageManager(this.usine, client));

        client.subscribe("orders/+");
    }
}