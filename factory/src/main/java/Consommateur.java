import bernard_flou.Fabricateur;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Consomme les demandes de fabrication depuis la queue partagée et pilote le Fabricateur.
 * Regroupe les demandes en lots pour maximiser l'utilisation de la capacité du Fabricateur.
 * Les lunettes d'un même lot sont fabriquées en parallèle via l'ExecutorService.
 */
public class Consommateur implements Runnable {
    private final Fabricateur fabricateur;
    private final ExecutorService executorService;
    private final BlockingQueue<Demande> queue;

    public Consommateur(Fabricateur fabricateur, ExecutorService executorService, BlockingQueue<Demande> queue) {
        this.fabricateur = fabricateur;
        this.executorService = executorService;
        this.queue = queue;
    }

    /**
     * Attend des demandes dans la queue, les regroupe en lots et lance la fabrication.
     * S'exécute jusqu'à ce que le thread soit interrompu.
     */
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                List<Demande> lot = new ArrayList<>();
                lot.add(queue.take()); // attend au moins une demande
                queue.drainTo(lot, fabricateur.getCapacity() - 1); // complète le lot

                Fabricateur.TypeLunette[] types = lot.stream()
                        .map(d -> d.type)
                        .toArray(Fabricateur.TypeLunette[]::new);

                fabricateur.configurer(types);

                List<Future> futures = new ArrayList<>();
                for (Demande demande : lot) {
                    futures.add(executorService.submit(() -> {
                        try {
                            demande.futur.complete(fabricateur.fabriquer(demande.type));
                        } catch (Exception e) {
                            demande.futur.completeExceptionally(e);
                        }
                    }));
                }

                for (Future<?> future : futures) {
                    try {
                        future.get();
                    } catch (ExecutionException | InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}