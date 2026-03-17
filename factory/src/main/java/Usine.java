import bernard_flou.Fabricateur;
import bernard_flou.Fabricateur.Lunette;
import bernard_flou.Fabricateur.TypeLunette;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Usine {
    private final Fabricateur fabricateur;

    public Usine() {
        this.fabricateur = new Fabricateur();
    }

    /**
     * Lance la production de lunettes. Chaque entrée de la `Map`
     * associe au type de lunette la quantité qu'il faut en produire.
     */
    List<Lunette> produire(final Map<TypeLunette, Integer> typesLunettes) {
        List<Lunette> resultat = new ArrayList<>();

        typesLunettes.forEach((type, qte) -> {
            int restant = qte;
            while(restant > 0) {
                int lotNb = Math.min(this.fabricateur.getCapacity(), restant);

                TypeLunette[] types = new TypeLunette[lotNb];
                Arrays.fill(types, type);
                this.fabricateur.configurer(types);

                for (int i = 0; i < lotNb; i++) {
                    resultat.add(this.fabricateur.fabriquer(type));
                    restant--;
                }
            }
        });

        return resultat;
    }
}