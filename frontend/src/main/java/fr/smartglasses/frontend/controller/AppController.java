package fr.smartglasses.frontend.controller;

public class AppController {

    private final OrderController orderController = new OrderController();
    private final SerialController serialController = new SerialController(orderController);

    public OrderController getOrderController() {
        return orderController;
    }

    public SerialController getSerialController() {
        return serialController;
    }
}
