package fr.smartglasses.frontend.model;

/**
 * Représente une paire de lunettes fabriquée avec son modèle et son numéro de série
 */
public record SerialPair(String modele, String numeroSerie) {

    @Override
    public String toString() {
        return modele + " : " + numeroSerie;
    }
}
