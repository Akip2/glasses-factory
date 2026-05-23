import bernard_flou.Fabricateur;
import bernard_flou.Fabricateur.Lunette;
import bernard_flou.Fabricateur.TypeLunette;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Gère la production de lunettes en s'appuyant sur le Fabricateur.
 * Thread-safe : produire() peut être appelée par plusieurs threads simultanément.
 */
public class Usine {
    private final BlockingQueue<Demande> queue;

    /**
     * @param capacity capacité du fabricateur (nombre de lunettes par lot)
     */
    public Usine(int capacity) {
        Fabricateur fabricateur = new Fabricateur(capacity);
        ExecutorService executorService = Executors.newFixedThreadPool(capacity);
        this.queue = new LinkedBlockingQueue<>();

        Consommateur consommateur = new Consommateur(fabricateur, executorService, queue);
        this.demarrerConsommateur(consommateur);
    }

    private void demarrerConsommateur(Consommateur consommateur) {
        Thread thread = new Thread(consommateur);
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Lance la production de lunettes.
     *
     * @param typesLunettes map associant chaque type à la quantité à produire
     * @return la liste des lunettes produites
     */
    public List<Lunette> produire(final Map<TypeLunette, Integer> typesLunettes) {
        List<Demande> demandes = new ArrayList<>();
        for (TypeLunette type : listerLunettes(typesLunettes)) {
            demandes.add(new Demande(type));
        }

        queue.addAll(demandes);

        List<Lunette> resultat = new ArrayList<>();
        for (Demande demande : demandes) {
            try {
                resultat.add(demande.futur.get());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Fabrication interrompue", e);
            } catch (ExecutionException e) {
                throw new RuntimeException("Erreur pendant la fabrication", e.getCause());
            }
        }

        return resultat;
    }

    /**
     * Convertit la map en liste plate.
     */
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