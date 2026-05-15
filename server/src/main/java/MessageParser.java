import bernard_flou.Fabricateur;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageParser {

    public Fabricateur.TypeLunette parseType(String type) throws IllegalArgumentException {
        try {
            return Fabricateur.TypeLunette.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Type de lunette inconnu : " + type);
        }
    }

    public boolean qteLegal(int qte) {
        return (qte >= 0 && qte < 10);
    }

    public Map<Fabricateur.TypeLunette, Integer> parsePayload(String payload) throws IllegalArgumentException {
        Map<Fabricateur.TypeLunette, Integer> res = new HashMap<>();
        String[] commandes = payload.split(";");

        int totalQte = 0;
        for (String commande : commandes) {
            String[] split = commande.split(":");

            if (split.length != 2) {
                throw new IllegalArgumentException("Format de commande invalide");
            }

            Fabricateur.TypeLunette type = parseType(split[0]);
            int qte = Integer.parseInt(split[1]);

            if (!qteLegal(qte)) {
                throw new IllegalArgumentException("Quantité invalide, doit être comprise entre 0 (inclu) et 10 (exclu)");
            }

            totalQte += qte;
            res.put(type, qte);
        }

        if (totalQte == 0) {
            throw new IllegalArgumentException("Quantité totale invalide, doit être strictement supérieure à 0");
        }

        return res;
    }

    public String serializeLunettes(List<Fabricateur.Lunette> lunettes) {
        StringBuilder res = new StringBuilder();
        for (Fabricateur.Lunette lunette : lunettes) {
            res.append(lunette.type).append(":").append(lunette.serial).append(";");
        }
        return res.toString();
    }
}