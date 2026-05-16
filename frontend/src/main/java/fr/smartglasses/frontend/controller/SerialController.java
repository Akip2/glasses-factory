package fr.smartglasses.frontend.controller;

import fr.smartglasses.frontend.model.Order;

public class SerialController {

    private final OrderController orderController;

    public SerialController(OrderController orderController) {
        this.orderController = orderController;
    }

//    public boolean isValid(String serialNumber) {
//        Order order = orderController.getCurrentOrder();
//        return order != null && order.getSerialNumbers().stream()
//                .anyMatch(number -> number.value().equals(serialNumber));
//    }
}
