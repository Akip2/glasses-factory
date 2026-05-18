package fr.smartglasses.frontend.controller;

import fr.smartglasses.frontend.service.ClientMqtt;

public class SerialController {

    private final OrderController orderController;

    public SerialController(OrderController orderController) {
        this.orderController = orderController;
    }

    public boolean isValid(String serialNumber) {

        ClientMqtt client = new ClientMqtt("tcp://localhost:1883");

        return false;
    }
}
