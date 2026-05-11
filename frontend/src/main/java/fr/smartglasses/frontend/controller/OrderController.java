package fr.smartglasses.frontend.controller;

import fr.smartglasses.frontend.model.GlassesModel;
import fr.smartglasses.frontend.model.Order;
import fr.smartglasses.frontend.model.SerialNumber;

import java.util.List;
import java.util.Random;

public class OrderController {

    private final Random random = new Random();
    private Order currentOrder;

    private final List<GlassesModel> catalogue = List.of(
            new GlassesModel(
                    "PRO",
                    "SmartGlass Pro",
                    "Le modele haut de gamme avec toutes les fonctionnalites",
                    "899 EUR",
                    "/images/pro.png",
                    new String[]{"GPS integre", "Camera 4K", "Bluetooth 5.0", "Batterie longue duree"}
            ),
            new GlassesModel(
                    "SPO",
                    "SmartGlass Sport",
                    "Concu pour les sportifs et les aventuriers",
                    "699 EUR",
                    "/images/sport.png",
                    new String[]{"Resistant a l'eau", "Capteurs biometriques", "Mode sport", "Design leger"}
            )
    );

    public List<GlassesModel> getCatalogue() {
        return catalogue;
    }

    public Order createOrder(GlassesModel model, int quantity) {
        String id = "CMD-" + (10000000 + random.nextInt(90000000));
        currentOrder = new Order(id, model, quantity);
        currentOrder.startFabrication();
        return currentOrder;
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public Order completeCurrentOrder() {
        if (currentOrder == null) {
            return null;
        }

        if (!currentOrder.isCompleted()) {
            for (int index = 0; index < currentOrder.getQuantity(); index++) {
                currentOrder.addSerialNumber(new SerialNumber(generateSerialNumber(currentOrder.getModel())));
            }
            currentOrder.complete();
        }

        return currentOrder;
    }

    private String generateSerialNumber(GlassesModel model) {
        return "SN-" + model.code() + "-" + (100000 + random.nextInt(900000));
    }
}
