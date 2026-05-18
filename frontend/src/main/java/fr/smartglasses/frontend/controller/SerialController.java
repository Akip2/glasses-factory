package fr.smartglasses.frontend.controller;

import fr.smartglasses.frontend.service.ClientMqtt;

public class SerialController {

    private final OrderController orderController;

    public SerialController(OrderController orderController) {
        this.orderController = orderController;
    }

    public boolean isValid(String serialNumber) {
        try {
            ClientMqtt client = new ClientMqtt("tcp://localhost:1883");
            String response = client.verifierSerie(serialNumber);
            client.disconnect();
            return !response.equals("invalid");
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la vérification : " + e.getMessage());
        }
    }
}
