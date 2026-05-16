package fr.smartglasses.frontend.model;

import java.util.*;

public class Order {

    private final String id;
    private final Map<GlassesModel, Integer> glassesQuantity = new HashMap<>();
    private OrderStatus status = OrderStatus.CREATED;

    /*
    * Constructeur de commande
    *
    * @param id identifiant unique de la commande
    * */
    public Order(String id) {
        this.id = id;
    }

    /*
    * Ajouter un nouveau modèle de lunettes à la commande avec une quantité donnée
    * Ou remplace la quantité initialement enregistrée si le modèle existe déjà
    *
    * @param model moèle de la paire de lunettes
    * @param quantity quantité de ce modèle
    * */
    public void addGlasses(GlassesModel model, int quantity) {
        // gère automatiquement l'insertion et la modification
        glassesQuantity.put(model, quantity);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Commande ").append(id).append(" :\n");

        for (Map.Entry<GlassesModel, Integer> entry : glassesQuantity.entrySet()) {
            sb.append("- ")
                    .append(entry.getKey().code())
                    .append(" | ")
                    .append(entry.getKey().name())
                    .append(" x")
                    .append(entry.getValue())
                    .append("\n");
        }

        return sb.toString();
    }

    public void resetOrder(){
        glassesQuantity.clear();
        status = OrderStatus.CREATED;
    }

    // Getters / Setters

    public Map<GlassesModel, Integer> getOrder() {
        return glassesQuantity;
    }

    public String getId() {
        return id;
    }

    public List<GlassesModel> getModel() {
        return new ArrayList<>(glassesQuantity.keySet());
    }

    public int getTotalQuantity() {
        return glassesQuantity.values().stream().mapToInt(Integer::intValue).sum();
    }

    // Statut de la commande

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void complete() {
        status = OrderStatus.COMPLETED;
    }

    public boolean isCompleted() {
        return status == OrderStatus.COMPLETED;
    }
}
