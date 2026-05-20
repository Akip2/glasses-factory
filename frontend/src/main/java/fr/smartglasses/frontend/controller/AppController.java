package fr.smartglasses.frontend.controller;

/**
 * Contrôleur principal de l'application
 * Regroupe les contrôleurs utilisés dans l'application
 */
public class AppController {

    // Gère les commandes
    private final OrderController orderController = new OrderController();
    // Gère la vérification des numéros de série
    private final SerialController serialController = new SerialController();

    /**
     * Retourne le contrôleur de commandes
     *
     * @return l'instance de {@link OrderController}
     */
    public OrderController getOrderController() {
        return orderController;
    }

    /**
     * Retourne le contrôleur de numéro de série
     *
     * @return l'instance de {@link SerialController}
     */
    public SerialController getSerialController() {
        return serialController;
    }
}