import bernard_flou.Fabricateur;
import org.eclipse.paho.client.mqttv3.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageManager implements MqttCallback {
    private final Usine usine;
    private final MqttClient client;

    public MessageManager(Usine usine, MqttClient client) {
        this.usine = usine;
        this.client = client;
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        System.out.println(topic);
        String orderId = topic.split("/")[1];
        String payload = new String(message.getPayload());

        try {
            Map<Fabricateur.TypeLunette, Integer> typeLunettes = parsePayload(payload);

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

    private Fabricateur.TypeLunette parseType(String type) throws IllegalArgumentException {
        try {
            return Fabricateur.TypeLunette.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Type de lunette inconnu");
        }
    }

    private boolean qteLegal(int qte) {
        return (qte >= 0 && qte < 10);
    }

    private Map<Fabricateur.TypeLunette, Integer> parsePayload(String payload) throws IllegalArgumentException {
        Map<Fabricateur.TypeLunette, Integer> res = new HashMap<>();
        String[] commandes = payload.split(";");

        int totalQte = 0;
        for(String commande : commandes) {
            String[] split = commande.split(":");

            if(split.length != 2) {
                throw new IllegalArgumentException("Format de commande invalide");
            }

            Fabricateur.TypeLunette type = this.parseType(split[0]);
            int qte = Integer.parseInt(split[1]);

            if(!qteLegal(qte)) {
                throw new IllegalArgumentException("Quantité invalide, doit être comprise entre 0 (inclu) et 10 (exclu)");
            }

            totalQte += qte;
            res.put(type, qte);
        }

        if(totalQte == 0) {
            throw new IllegalArgumentException("Quantité totale invalide, doit être strictement supérieure à 0");
        }

        return res;
    }

    private String traiterCommande(String orderId, Map<Fabricateur.TypeLunette, Integer> typesLunettes) {
        List<Fabricateur.Lunette> lunettes = this.usine.produire(typesLunettes);

        return serializeLunettes(lunettes);
    }

    private String serializeLunettes(List<Fabricateur.Lunette> lunettes) {
        StringBuilder res = new StringBuilder();
        for(Fabricateur.Lunette lunette : lunettes) {
            res.append(lunette.type).append(":").append(lunette.serial).append(";");
        }

        return res.toString();
    }
}
