import bernard_flou.Fabricateur;
import bernard_flou.Fabricateur.Lunette;
import bernard_flou.Fabricateur.TypeLunette;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Usine {
    private final Fabricateur fabricateur;
    private final ExecutorService executorService;

    public Usine(int capacity) {
        this.fabricateur = new Fabricateur(capacity);
        this.executorService = Executors.newFixedThreadPool(capacity);
    }

    /**
     * Lance la production de lunettes. Chaque entrée de la `Map`
     * associe au type de lunette la quantité qu'il faut en produire.
     */
    public List<Lunette> produire(final Map<TypeLunette, Integer> typesLunettes) {
        List<TypeLunette> aProduire = listerLunettes(typesLunettes);
        List<Lunette> resultat = new ArrayList<>();

        int capacite = fabricateur.getCapacity();

        for (int debut = 0; debut < aProduire.size(); debut += capacite) {
            int fin = Math.min(debut + capacite, aProduire.size());
            TypeLunette[] lot = aProduire.subList(debut, fin).toArray(new TypeLunette[0]);

            synchronized (this) {
                resultat.addAll(fabriquerLot(lot));
            }
        }

        return resultat;
    }

    private List<Lunette> fabriquerLot(TypeLunette[] lot) {
        this.fabricateur.configurer(lot);

        List<Future<Lunette>> futures = new ArrayList<>();
        for (TypeLunette type : lot) {
            futures.add(executorService.submit(() -> this.fabricateur.fabriquer(type)));
        }

        List<Lunette> resultat = new ArrayList<>();
        for (Future<Lunette> future : futures) {
            try {
                resultat.add(future.get());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Fabrication interrompue", e);
            } catch (ExecutionException e) {
                throw new RuntimeException("Erreur pendant la fabrication", e.getCause());
            }
        }

        return resultat;
    }

    private List<TypeLunette> listerLunettes(Map<TypeLunette, Integer> typesLunettes) {
        List<TypeLunette> listeLunettes = new ArrayList<>();

        for (Map.Entry<TypeLunette, Integer> entry : typesLunettes.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                listeLunettes.add(entry.getKey());
            }
        }

        return listeLunettes;
    }
}