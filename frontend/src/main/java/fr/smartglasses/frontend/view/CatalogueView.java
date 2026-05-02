package fr.smartglasses.frontend.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class CatalogueView {

    private final ScrollPane view;

    public CatalogueView(Layout layout) {
        VBox page = new VBox();
        page.setStyle("-fx-background-color: #f1f6ff;");

        VBox header = new VBox(15);
        header.setPadding(new Insets(0, 0, 55, 45));
        header.setPrefHeight(150);
        header.setStyle("-fx-background-color: #1e5bff;");

        Label title = new Label("Catalogue de lunettes");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 42px; -fx-font-weight: bold;");

        Label subtitle = new Label("Choisissez parmi nos modèles de lunettes connectées");
        subtitle.setStyle("-fx-text-fill: #dbeafe; -fx-font-size: 24px;");

        header.getChildren().addAll(title, subtitle);

        HBox cards = new HBox(40);
        cards.setAlignment(Pos.CENTER);
        cards.setPadding(new Insets(60, 50, 60, 50));

        cards.getChildren().addAll(
                productCard(
                        "SmartGlass Pro",
                        "Le modèle haut de gamme avec toutes les fonctionnalités",
                        "899€",
                        "/images/pro.png",
                        new String[]{"GPS intégré", "Caméra 4K", "Bluetooth 5.0", "Batterie longue durée"},
                        layout
                ),
                productCard(
                        "SmartGlass Sport",
                        "Conçu pour les sportifs et les aventuriers",
                        "699€",
                        "/images/sport.png",
                        new String[]{"Résistant à l’eau", "Capteurs biométriques", "Mode sport", "Design léger"},
                        layout
                )
        );

        page.getChildren().addAll(header, cards);

        view = new ScrollPane(page);
        view.setFitToWidth(true);
        view.setStyle("-fx-background: #f1f6ff;");
    }

    private VBox productCard(
            String name,
            String description,
            String price,
            String imagePath,
            String[] features,
            Layout layout
    ) {
        VBox card = new VBox();
        card.setPrefWidth(520);
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

        ImageView image = new ImageView(new Image(getClass().getResource(imagePath).toExternalForm()));
        image.setFitWidth(470);
        image.setFitHeight(180);
        image.setPreserveRatio(false);

        imageArea.getChildren().add(image);

        VBox body = new VBox(15);
        body.setPadding(new Insets(45, 30, 30, 30));

        HBox titleRow = new HBox();
        titleRow.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label(name);
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #020617;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label priceLabel = new Label(price);
        priceLabel.setStyle("""
            -fx-background-color: #1e5bff;
            -fx-text-fill: white;
            -fx-font-size: 22px;
            -fx-padding: 10 18;
            -fx-background-radius: 8;
        """);

        titleRow.getChildren().addAll(title, spacer, priceLabel);

        Label desc = new Label(description);
        desc.setStyle("-fx-font-size: 17px; -fx-text-fill: #475569;");

        VBox featureList = new VBox(10);
        for (String f : features) {
            Label item = new Label("✓  " + f);
            item.setStyle("-fx-font-size: 16px; -fx-text-fill: #1f2937;");
            featureList.getChildren().add(item);
        }

        Region line = new Region();
        line.setPrefHeight(1);
        line.setStyle("-fx-background-color: #e5e7eb;");

        Button addBtn = new Button("🛒  Ajouter au panier");
        addBtn.setPrefHeight(45);
        addBtn.setStyle("""
            -fx-background-color: #1e5bff;
            -fx-text-fill: white;
            -fx-font-size: 16px;
            -fx-padding: 0 22;
            -fx-background-radius: 8;
            -fx-cursor: hand;
        """);
        addBtn.setOnAction(e -> layout.setContent(new FabricationView(layout).getView()));

        body.getChildren().addAll(titleRow, desc, featureList, line, addBtn);

        card.getChildren().addAll(imageArea, body);
        return card;
    }

    public ScrollPane getView() {
        return view;
    }
}