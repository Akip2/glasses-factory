package fr.smartglasses.frontend.model;

/**
 * Représente une paire de lunettes fabriquée avec son numéro de série et son modèle
 */
public record SerialPair(String numeroSerie, String modele) {

    @Override
    public String toString() {
        return modele + " : " + numeroSerie;
    }
}
