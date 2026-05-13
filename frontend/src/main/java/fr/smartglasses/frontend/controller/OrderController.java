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
