package fr.smartglasses.frontend.model;

import java.util.*;
import java.time.LocalDateTime;

public class Order {

    private final String id;
    private final Map<GlassesModel, Integer> glassesQuantity = new HashMap<>();
    private OrderStatus status = OrderStatus.CREATED;
    private List<SerialPair> serialNumbers = new ArrayList<>(); // Numéros générés
    private LocalDateTime completionDate;

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

        if (!serialNumbers.isEmpty()) {
            sb.append("\nNuméros de série générés (").append(serialNumbers.size()).append(") :\n");
            int count = 1;
            for (SerialPair pair : serialNumbers) {
                sb.append("  ").append(count).append(". ").append(pair).append("\n");
                count++;
            }
        }

        if (status == OrderStatus.COMPLETED && completionDate != null) {
            sb.append("\nDate de fabrication : ").append(completionDate).append("\n");
        }

        return sb.toString();
    }

    public void resetOrder() {
        glassesQuantity.clear();
        serialNumbers.clear();
        status = OrderStatus.CREATED;
        completionDate = null;
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
        this.completionDate = LocalDateTime.now();
    }

    public boolean isCompleted() {
        return status == OrderStatus.COMPLETED;
    }

    /**
     * Ajoute les numéros de série générés par la fabrication
     *
     * @param serialNumbers liste des paires de numéros de série et modèles correspondants
     */
    public void setSerialNumbers(List<SerialPair> serialNumbers) {
        this.serialNumbers = serialNumbers != null ? serialNumbers : new ArrayList<>();
    }

    /**
     * Retourne tous les numéros de série générés
     *
     * @return liste des paires numéro de série / modèle
     */
    public List<SerialPair> getSerialNumbers() {
        return serialNumbers;
    }

}
