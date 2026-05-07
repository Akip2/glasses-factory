package fr.smartglasses.frontend.view;

import fr.smartglasses.frontend.controller.AppController;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class Layout {

    private final BorderPane root = new BorderPane();
    private final AppController appController;

    public Layout(AppController appController) {
        this.appController = appController;
        root.setLeft(createSidebar());
        setContent(new HomeView(this).getView());
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(25);
        sidebar.setPrefWidth(230);
        sidebar.setPadding(new Insets(25, 15, 25, 15));
        sidebar.setStyle("-fx-background-color: #1e5bff;");

        Label title = new Label("SmartGlasses");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");

        Label subtitle = new Label("Système de commande");
        subtitle.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        Button accueil = menuButton("⌂  Accueil");
        Button catalogue = menuButton("🛒  Catalogue");
        Button fabrication = menuButton("◷  Fabrication");
        Button verifier = menuButton("⌕  Vérifier");

        accueil.setOnAction(e -> setContent(new HomeView(this).getView()));
        catalogue.setOnAction(e -> setContent(new CatalogueView(this, appController.getOrderController()).getView()));
        fabrication.setOnAction(e -> setContent(new FabricationView(this, appController.getOrderController()).getView()));
        verifier.setOnAction(e -> setContent(new VerificationView(this, appController.getSerialController()).getView()));

        sidebar.getChildren().addAll(title, subtitle, accueil, catalogue, fabrication, verifier);
        return sidebar;
    }

    private Button menuButton(String text) {
        Button btn = new Button(text);
        btn.setPrefWidth(200);
        btn.setPrefHeight(55);
        btn.setStyle("""
            -fx-background-color: transparent;
            -fx-text-fill: white;
            -fx-font-size: 17px;
            -fx-alignment: center-left;
            -fx-padding: 0 0 0 20;
        """);

        btn.setOnMouseEntered(e -> btn.setStyle("""
            -fx-background-color: white;
            -fx-text-fill: #1e5bff;
            -fx-font-size: 17px;
            -fx-background-radius: 12;
            -fx-alignment: center-left;
            -fx-padding: 0 0 0 20;
        """));

        btn.setOnMouseExited(e -> btn.setStyle("""
            -fx-background-color: transparent;
            -fx-text-fill: white;
            -fx-font-size: 17px;
            -fx-alignment: center-left;
            -fx-padding: 0 0 0 20;
        """));

        return btn;
    }

    public void setContent(Node node) {
        root.setCenter(node);
    }

    public BorderPane getRoot() {
        return root;
    }

    public AppController getAppController() {
        return appController;
    }
}
