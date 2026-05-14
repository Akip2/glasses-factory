import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

public class Serveur {
    private final Usine usine;

    public Serveur(Usine usine) {
        this.usine = usine;
    }

    public void start(String url, String clientId) throws MqttException {
        MqttClient client = new MqttClient(url, clientId);

        client.connect();

        client.subscribe("orders/+");
    }
}