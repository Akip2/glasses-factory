import bernard_flou.Fabricateur;

import java.util.concurrent.CompletableFuture;

/**
 * Représente une demande de fabrication d'une lunette.
 * Contient le type à produire et un futur qui sera complété une fois la lunette fabriquée.
 */
public class Demande {
    final Fabricateur.TypeLunette type;
    final CompletableFuture<Fabricateur.Lunette> futur;

    Demande(Fabricateur.TypeLunette type) {
        this.type = type;
        this.futur = new CompletableFuture<>();
    }
}
