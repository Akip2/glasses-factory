package fr.smartglasses.frontend.controller;

import fr.smartglasses.frontend.model.*;
import fr.smartglasses.frontend.service.ClientMqtt;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class OrderController {

    // Commande en cours
    private Order currentOrder;
    // Informations sur les lunettes de la commande en cours
    private StringProperty infosCommandeProperty = new SimpleStringProperty("");

    /*
     * Création d'une nouvelle commande avec un modèle de lunettes et une quantité donnée
     *
     * @param model modèle de lunettes
     * @param quantity quantité du modèle
     * @return la nouvelle commande
     */
    public Order createOrder(GlassesModel model, int quantity) {
        String id = "CMD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        currentOrder = new Order(id);
        currentOrder.addGlasses(model, quantity);

        return currentOrder;
    }

    /*
     * Permet d'ajouter une nouvelle paire de lunettes à la commande en cours,
     * ou de créer une nouvelle commande et d'y ajouter la nouvelle paire si aucune n'est en cours
     *
     * @param model le modèle de lunettes à ajouter
     * @param quantity la quantité à ajouter
     */
    public void addGlasses(GlassesModel model, int quantity) {
        if (currentOrder == null) {
            this.currentOrder = createOrder(model, quantity);
        }

        currentOrder.addGlasses(model, quantity);
        refreshInfos();
    }

    /*
     * Permet de lancer la fabrication des lunettes de la commande en cours
     * On attend la réponse du serveur et on indique la réponse à l'utilisateur
     */
    public void startFabrication() {

        try {
            ClientMqtt client = new ClientMqtt("tcp://localhost:1883");

            // Construire le payload : "TYPE:QTE;TYPE:QTE"
            StringBuilder payload = new StringBuilder();
            for (Map.Entry<GlassesModel, Integer> entry : currentOrder.getOrder().entrySet()) {
                payload.append(entry.getKey().code())
                        .append(":")
                        .append(entry.getValue())
                        .append(";");
            }

            String deliveryPayload = client.passerCommande(currentOrder.getId(), payload.toString());
            client.disconnect();

            // Parser la réponse : "TYPE:SERIAL;TYPE:SERIAL;..."
            List<SerialPair> serials = new ArrayList<>();
            for (String part : deliveryPayload.split(";")) {
                if (part.isBlank()) continue;
                String[] kv = part.split(":");
                if (kv.length == 2) {
                    serials.add(new SerialPair(kv[0], kv[1]));
                }
            }

            currentOrder.setSerialNumbers(serials);

            // actualisation de la vue
            refreshInfos();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void resetOrder(){
        currentOrder = null;
        infosCommandeProperty = new SimpleStringProperty("");
        refreshInfos();
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public String getInfosCommande() {
        return currentOrder != null ? currentOrder.toString() : "Aucune commande en cours";
    }

    public void refreshInfos() {
        infosCommandeProperty.set(getInfosCommande());
    }

    public StringProperty infosCommandeProperty() {
        return infosCommandeProperty;
    }

    public List<GlassesModel> getCatalogue() {
        return GlassesModel.CATALOGUE;
    }
}
