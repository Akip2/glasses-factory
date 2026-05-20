package fr.smartglasses.frontend.view;

import fr.smartglasses.frontend.controller.OrderController;
import fr.smartglasses.frontend.model.GlassesModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.Objects;

public class CatalogueView {

    private final ScrollPane view;
    private final Layout layout;

    public CatalogueView(Layout layout, OrderController orderController) {
        this.layout = layout;
        VBox page = new VBox();
        page.setStyle("-fx-background-color: #f1f6ff;");

        TilePane cards = new TilePane();
        cards.setAlignment(Pos.CENTER);
        cards.setHgap(40);
        cards.setVgap(40);
        cards.setPrefColumns(2);
        cards.setPadding(new Insets(60, 50, 60, 50));

        for (GlassesModel model : orderController.getCatalogue()) {
            cards.getChildren().add(productCard(model, orderController));
        }

        // détail de la commande en cours, lié avec le contrôleur pour un affichage dynamique
        HBox panier = panier(orderController);

        // ajout de tous les éléments de la page au layout
        page.getChildren().addAll(header(), cards, panier);

        view = new ScrollPane(page);
        view.setFitToWidth(true);
        view.setStyle("-fx-background: #f1f6ff;");
    }

    /*
     * Header de la page catalogue, avec un titre et un sous-titre
     *
     * @return le header stylisé et correctement organisé, prêt à être affiché en haut de la page
     */
    private VBox header() {
        VBox header = new VBox(15);
        header.setPadding(new Insets(0, 0, 55, 45));
        header.setPrefHeight(150);
        header.setStyle("-fx-background-color: #1e5bff;");

        Label title = new Label("Catalogue de lunettes");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 42px; -fx-font-weight: bold;");

        Label subtitle = new Label("Choisissez parmi nos modèles de lunettes connectées");
        subtitle.setStyle("-fx-text-fill: #dbeafe; -fx-font-size: 24px;");

        header.getChildren().addAll(title, subtitle);

        return header;
    }

    /*
     * Carte d'affichage d'un modèle de lunettes dans le catalogue
     *
     * @param model modèle de lunette à afficher sur la carte
     * @param orderController contrôleur de la commande, pour gérer l'ajout au panier
     * @return la carte stylisée prête à être affichée
     */
    private VBox productCard(GlassesModel model, OrderController orderController) {
        VBox card = new VBox();
        card.setPrefWidth(430);
        card.setStyle("""
            -fx-background-color: white;
            -fx-background-radius: 15;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 18, 0, 0, 8);
        """);

        VBox imageArea = new VBox();
        imageArea.setPadding(new Insets(25));
        imageArea.setStyle("""
            -fx-background-color: #1e5bff;
            -fx-background-radius: 15 15 0 0;
        """);

        ImageView image = new ImageView(new Image(Objects.requireNonNull(getClass().getResource(model.imagePath())).toExternalForm()));
        image.setFitWidth(380);
        image.setFitHeight(180);
        image.setPreserveRatio(false);

        imageArea.getChildren().add(image);

        VBox body = new VBox(15);
        body.setPadding(new Insets(45, 30, 30, 30));

        HBox titleRow = new HBox();
        titleRow.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label(model.name());
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #020617;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label priceLabel = new Label(model.price());
        priceLabel.setStyle("""
            -fx-background-color: #1e5bff;
            -fx-text-fill: white;
            -fx-font-size: 22px;
            -fx-padding: 10 18;
            -fx-background-radius: 8;
        """);

        titleRow.getChildren().addAll(title, spacer);
        if (!model.badge().isBlank()) {
            Label badge = new Label(model.badge());
            badge.setStyle("""
                -fx-background-color: #eff6ff;
                -fx-text-fill: #1e5bff;
                -fx-font-size: 14px;
                -fx-padding: 8 12;
                -fx-background-radius: 8;
            """);
            titleRow.getChildren().add(badge);
        }
        titleRow.getChildren().add(priceLabel);

        Label desc = new Label(model.description());
        desc.setStyle("-fx-font-size: 17px; -fx-text-fill: #475569;");

        Region line = new Region();
        line.setPrefHeight(1);
        line.setStyle("-fx-background-color: #e5e7eb;");

        Label quantityLabel = new Label("Quantite");
        quantityLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #020617;");

        Spinner<Integer> quantitySpinner = new Spinner<>(1, 9, 1);
        quantitySpinner.setEditable(true);
        quantitySpinner.setPrefWidth(90);

        HBox orderLine = new HBox(12);
        orderLine.setAlignment(Pos.CENTER_LEFT);

        Button addBtn = new Button("Ajouter au panier");
        addBtn.setPrefHeight(45);
        addBtn.setStyle("""
            -fx-background-color: #1e5bff;
            -fx-text-fill: white;
            -fx-font-size: 16px;
            -fx-padding: 0 22;
            -fx-background-radius: 8;
            -fx-cursor: hand;
        """);
        addBtn.setOnAction(e -> {
            orderController.addGlasses(model, quantitySpinner.getValue());
        });

        orderLine.getChildren().addAll(quantityLabel, quantitySpinner, addBtn);
        body.getChildren().addAll(titleRow, desc, line, orderLine);

        card.getChildren().addAll(imageArea, body);
        return card;
    }

    /*
     * Zone de panier de la commande, qui affiche les informations de la commande
     *
     * @return la zone de panier de la commande
     */
    HBox panier(OrderController orderController) {

        VBox cart = new VBox(14);
        cart.setMaxWidth(860);
        cart.setPadding(new Insets(22, 28, 22, 28));
        cart.setStyle("""
            -fx-background-color: white;
            -fx-background-radius: 12;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 16, 0, 0, 5);
        """);

        Label cartTitle = new Label("🛒  Panier");
        cartTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1e5bff;");

        Label cartInfos = new Label();
        cartInfos.setStyle("-fx-font-size: 14px; -fx-text-fill: #475569;");
        cartInfos.textProperty().bind(orderController.infosCommandeProperty());

        Button btnPasserCommande = new Button("Commander");
        btnPasserCommande.setPrefHeight(45);
        btnPasserCommande.setStyle("""
            -fx-background-color: #1e5bff;
            -fx-text-fill: white;
            -fx-font-size: 16px;
            -fx-padding: 0 22;
            -fx-background-radius: 8;
            -fx-cursor: hand;
        """);
        btnPasserCommande.setOnAction(e -> {
            // Redirection vers la page de fabrication
            FabricationView fabricationView = new FabricationView(layout, orderController);
            layout.setContent(fabricationView.getView());
        });

        cart.getChildren().addAll(cartTitle, cartInfos, btnPasserCommande);

        // centrer le panier horizontalement
        HBox cartWrapper = new HBox(cart);
        cartWrapper.setAlignment(Pos.CENTER);
        cartWrapper.setPadding(new Insets(0, 50, 60, 50));

        return cartWrapper;
    }

    public ScrollPane getView() {
        return view;
    }
}
