package fr.smartglasses.frontend.model;

import java.util.*;

public class Order {

    // id unique de la commande
    private final String id;
    // map contenant les modèles choisis et leur quantité
    private final Map<GlassesModel, Integer> glassesQuantity = new HashMap<>();
    // Liste contentant les modèles et leur numéro de série associé une fois la commande fabriquée
    private List<SerialPair> serialNumbers = new ArrayList<>(); // Numéros générés

    /*
     * Constructeur de commande
     *
     * @param id identifiant unique de la commande
     */
    public Order(String id) {
        this.id = id;
    }

    /*
     * Ajouter un nouveau modèle de lunettes à la commande avec une quantité donnée
     * Ou remplace la quantité initialement enregistrée si le modèle existe déjà
     *
     * @param model moèle de la paire de lunettes
     * @param quantity quantité de ce modèle
     */
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

        return sb.toString();
    }

    // Getters - Setters

    public Map<GlassesModel, Integer> getOrder() {
        return glassesQuantity;
    }

    public String getId() {
        return id;
    }

    public List<SerialPair> getSerialNumbers() {
        return serialNumbers;
    }

    public void setSerialNumbers(List<SerialPair> serialNumbers) {
        this.serialNumbers = serialNumbers != null ? serialNumbers : new ArrayList<>();
    }

}
