package fr.smartglasses.frontend.controller;

import fr.smartglasses.frontend.model.*;
import fr.smartglasses.frontend.service.ClientMqtt;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;

public class OrderController {

    private Order currentOrder;

    private final StringProperty infosCommandeProperty = new SimpleStringProperty("");

    private final List<GlassesModel> catalogue = List.of(
            new GlassesModel(
                    "BANANA",
                    "Bananaaaa",
                    "Design iconique des annees 50, parfait pour un look vintage et decontracte.",
                    "89.99 EUR",
                    "/images/banana.png",
                    "Nouveau"
            ),
            new GlassesModel(
                    "CHATGPT",
                    "BlaBlaBla",
                    "Lunettes style aviateur avec verres polarises et monture en metal dore.",
                    "74.99 EUR",
                    "/images/chatgpt.png",
                    ""
            ),
            new GlassesModel(
                    "LECHAT",
                    "Miaousse",
                    "Lunettes de vue sophistiquees avec monture fine et design contemporain.",
                    "129.99 EUR",
                    "/images/le_chat.png",
                    "-10%"
            ),
            new GlassesModel(
                    "CLAUDE",
                    "Claude",
                    "Monture ultra-legere et resistante, ideale pour les activites sportives.",
                    "99.99 EUR",
                    "/images/claude.png",
                    "Bestseller"
            )
    );

    public List<GlassesModel> getCatalogue() {
        return catalogue;
    }

    /*
    * Création d'une nouvelle commande avec un modèle de lunettes et une quantité donnée
    *
    * @param model modèle de lunettes
    * @param quantity quantité du modèle
    * @return la nouvelle commande
    * */
    public Order createOrder(GlassesModel model, int quantity) {

        // génération aléatoire du n° de commande
        Random random = new Random();
        String id = "CMD-" + (10000000 + random.nextInt(90000000)); // TODO : à voir si on laisse la génération aléatoire comme ça

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
    * */
    public void addGlasses(GlassesModel model, int quantity) {
        if (currentOrder == null) {
            this.currentOrder = createOrder(model, quantity);
        }

        currentOrder.addGlasses(model, quantity);
        refreshInfos();
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public String getInfosCommande() {
        return currentOrder != null ? currentOrder.toString() : "Aucune commande en cours";
    }

    /*
     * Permet de lancer la fabrication des lunettes de la commande en cours
     * On attend la réponse du serveur et on indique la réponse à l'utilisateur
     * */
    public boolean startFabrication() {

        currentOrder.setStatus(OrderStatus.IN_PROGRESS);

        ClientMqtt client = new ClientMqtt("tcp://localhost:1883");

        try {
            // TODO
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    public void refreshInfos() {
        infosCommandeProperty.set(getInfosCommande());
    }

    public StringProperty infosCommandeProperty() {
        return infosCommandeProperty;
    }

    /**
     * Crée une nouvelle commande et réinitialise la commande en cours
     */
    public void createNewOrder() {
        if (currentOrder != null && !currentOrder.isCompleted()) {
            currentOrder.resetOrder();
        }

        // Générer une nouvelle commande
        Random random = new Random();
        String id = "CMD-" + (10000000 + random.nextInt(90000000));
        currentOrder = new Order(id);
        refreshInfos();
    }
}
