package fr.smartglasses.frontend.controller;

import fr.smartglasses.frontend.model.GlassesModel;
import fr.smartglasses.frontend.model.Order;
import fr.smartglasses.frontend.model.OrderStatus;

import java.util.List;
import java.util.Random;

public class OrderController {

    private final Random random = new Random();
    private Order currentOrder;

    // TODO : à utiliser comme ça ? A voir...
    // private final ExecutorService executor = Executors.newFixedThreadPool(4);

    private final List<GlassesModel> catalogue = List.of(
            new GlassesModel(
                    "BANANA",
                    "Bananaaaa",
                    "Design iconique des annees 50, parfait pour un look vintage et decontracte.",
                    "89.99 EUR",
                    "/images/banana.png",
                    "Nouveau"
            ),
            new GlassesModel(
                    "CHATGPT",
                    "BlaBlaBla",
                    "Lunettes style aviateur avec verres polarises et monture en metal dore.",
                    "74.99 EUR",
                    "/images/chatgpt.png",
                    ""
            ),
            new GlassesModel(
                    "LECHAT",
                    "Miaousse",
                    "Lunettes de vue sophistiquees avec monture fine et design contemporain.",
                    "129.99 EUR",
                    "/images/le_chat.png",
                    "-10%"
            ),
            new GlassesModel(
                    "CLAUDE",
                    "Claude",
                    "Monture ultra-legere et resistante, ideale pour les activites sportives.",
                    "99.99 EUR",
                    "/images/claude.png",
                    "Bestseller"
            )
    );

    public List<GlassesModel> getCatalogue() {
        return catalogue;
    }

    /*
    * Création d'une nouvelle commande avec un modèle de lunettes et une quantité donnée
    *
    * @param model modèle de lunettes
    * @param quantity quantité du modèle
    * @return la nouvelle commande
    * */
    public Order createOrder(GlassesModel model, int quantity) {
        // génération du n° de commande aléatoire
        String id = "CMD-" + (10000000 + random.nextInt(90000000)); // TODO : à voir si on laisse la génération aléatoire comme ça

        currentOrder = new Order(id);
        currentOrder.addGlasses(model, quantity);

        return currentOrder;
    }

    /*
    * Permet d'ajouter une nouvelle paire de lunettes à la commande en cours,
    * ou de créer une nouvelle commande et d'y ajouter la nouvelle paire si aucune n'est en cours
    *
    * @param model le modèle de lunettes à ajouter
    * @param quantity la quantité à ajouter
    * */
    public void addGlasses(GlassesModel model, int quantity) {
        if (currentOrder == null) {
            this.currentOrder = createOrder(model, quantity);
        }

        currentOrder.addGlasses(model, quantity);
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public String getInfosCommande() {
        return currentOrder != null ? currentOrder.toString() : "Aucune commande en cours";
    }

    // TODO : à supprimer plus tard, uniquement pour les tests
    public Order completeCurrentOrder() {
        if (currentOrder == null) {
            return null;
        }

        currentOrder.complete();

        return currentOrder;
    }

    /*
    * Permet de lancer la fabrication des lunettes de la commande en cours
    * On attend la réponse du serveur et on indique la réponse à l'utilisateur
    * */
    public void startFabrication() {
//        currentOrder.setStatus(OrderStatus.IN_PROGRESS);
//
//        // TODO : à faire quand Antoine aura fait les threads
//        Future<String> future = executor.submit(() -> callServer());
//
//        // on peut faire autre chose en attendant
//
//        // bloque ici jusqu'à la réponse
//        // on attend max 60s, après c'est considéré comme un échec
//        String response = future.get(60, TimeUnit.SECONDS);
//        currentOrder.complete();
    }
}
