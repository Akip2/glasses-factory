public enum ErreurMessage {
    FORMAT_INVALIDE("Format de commande invalide"),
    TYPE_INCONNU("Type de lunette inconnu"),
    QUANTITE_INVALIDE("Quantité invalide, doit être comprise entre 0 (inclu) et 10 (exclu)"),
    QUANTITE_TOTALE_INVALIDE("Quantité totale invalide, doit être strictement supérieure à 0");

    private final String message;

    ErreurMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}