import bernard_flou.Fabricateur;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Parse et sérialise les messages MQTT du protocole de commande.
 * Format des commandes : "TYPE:QTE;TYPE:QTE"
 * Format des livraisons : "TYPE:SERIAL;TYPE:SERIAL"
 */
public class MessageParser {
    /**
     * @param type le nom du type de lunette
     * @return le TypeLunette correspondant
     * @throws IllegalArgumentException si le type est inconnu
     */
    public Fabricateur.TypeLunette parseType(String type) throws IllegalArgumentException {
        try {
            return Fabricateur.TypeLunette.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(ErreurMessage.TYPE_INCONNU.getMessage());
        }
    }

    /**
     * @param qte la quantité à vérifier
     * @return true si la quantité est comprise entre 0 (inclu) et 10 (exclu)
     */
    public boolean qteLegal(int qte) {
        return (qte >= 0 && qte < 10);
    }

    /**
     * Parse un payload de commande en map TypeLunette -> quantité.
     *
     * @param payload le payload à parser
     * @return la map des types et quantités
     * @throws IllegalArgumentException si le format est invalide ou les quantités incorrectes
     */
    public Map<Fabricateur.TypeLunette, Integer> parsePayload(String payload) throws IllegalArgumentException {
        Map<Fabricateur.TypeLunette, Integer> res = new HashMap<>();
        String[] commandes = payload.split(";");

        int totalQte = 0;
        for (String commande : commandes) {
            String[] split = commande.split(":");

            if (split.length != 2) {
                throw new IllegalArgumentException(ErreurMessage.FORMAT_INVALIDE.getMessage());
            }

            Fabricateur.TypeLunette type = parseType(split[0]);
            int qte = Integer.parseInt(split[1]);

            if (!qteLegal(qte)) {
                throw new IllegalArgumentException(ErreurMessage.QUANTITE_INVALIDE.getMessage());
            }

            totalQte += qte;
            res.put(type, qte);
        }

        if (totalQte == 0) {
            throw new IllegalArgumentException(ErreurMessage.QUANTITE_TOTALE_INVALIDE.getMessage());
        }

        return res;
    }

    /**
     * Sérialise une liste de lunettes produites.
     *
     * @param lunettes la liste des lunettes à sérialiser
     * @return le payload de livraison
     */
    public String serializeLunettes(List<Fabricateur.Lunette> lunettes) {
        StringBuilder res = new StringBuilder();
        for (Fabricateur.Lunette lunette : lunettes) {
            res.append(lunette.type).append(":").append(lunette.serial).append(";");
        }
        return res.toString();
    }
}