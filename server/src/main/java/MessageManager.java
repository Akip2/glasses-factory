import bernard_flou.Fabricateur;
import org.eclipse.paho.client.mqttv3.*;

import java.util.List;
import java.util.Map;

public class MessageManager implements MqttCallback {
    private final Usine usine;
    private final MqttClient client;
    private final MessageParser parser;

    public MessageManager(Usine usine, MqttClient client) {
        this.usine = usine;
        this.client = client;
        this.parser = new MessageParser();
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        System.out.println(topic);
        String orderId = topic.split("/")[1];
        String payload = new String(message.getPayload());

        try {
            Map<Fabricateur.TypeLunette, Integer> typeLunettes = parser.parsePayload(payload);

            // Si le parsing du payload n'a pas levé d'exception on valide la commande et on la traite
            publier(topic + "/validated", "");

            try {
                String res = traiterCommande(orderId, typeLunettes);
                publier(topic + "/delivery", res);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                publier(topic + "/error", e.getMessage());
            }
        } catch (IllegalArgumentException e) {
            publier(topic + "/cancelled", e.getMessage());
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("Connexion perdue : " + cause.getMessage());
    }

    private void publier(String topic, String payload) {
        try {
            client.publish(topic, new MqttMessage(payload.getBytes()));
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    private String traiterCommande(String orderId, Map<Fabricateur.TypeLunette, Integer> typesLunettes) {
        List<Fabricateur.Lunette> lunettes = this.usine.produire(typesLunettes);

        return parser.serializeLunettes(lunettes);
    }
}
