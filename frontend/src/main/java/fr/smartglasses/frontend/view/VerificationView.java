package fr.smartglasses.frontend.view;

import fr.smartglasses.frontend.controller.SerialController;
import fr.smartglasses.frontend.model.Order;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class VerificationView {

    private final BorderPane view = new BorderPane();

    public VerificationView(Layout layout, SerialController serialController) {
        view.setStyle("-fx-background-color: #f1f6ff;");

        VBox page = new VBox();
        page.setStyle("-fx-background-color: #f1f6ff;");

        VBox header = new VBox(15);
        header.setPadding(new Insets(40, 0, 45, 300));
        header.setStyle("-fx-background-color: #1e5bff;");

        Label title = new Label("Verification de numero de serie");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 36px; -fx-font-weight: bold;");

        Label subtitle = new Label("Verifiez l'authenticite de vos lunettes connectees");
        subtitle.setStyle("-fx-text-fill: white; -fx-font-size: 20px;");

        header.getChildren().addAll(title, subtitle);

        VBox content = new VBox(30);
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(50, 40, 40, 40));

        VBox searchCard = new VBox();
        searchCard.setMaxWidth(830);
        searchCard.setStyle("""
            -fx-background-color: white;
            -fx-background-radius: 12;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 18, 0, 0, 6);
        """);

        VBox searchHeader = new VBox(12);
        searchHeader.setPadding(new Insets(25));
        searchHeader.setStyle("""
            -fx-background-color: #1e5bff;
            -fx-background-radius: 12 12 0 0;
        """);

        Label searchTitle = new Label("Recherche");
        searchTitle.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");

        Label searchSubtitle = new Label("Entrez le numero de serie complet pour verifier son authenticite");
        searchSubtitle.setStyle("-fx-text-fill: white; -fx-font-size: 15px;");

        searchHeader.getChildren().addAll(searchTitle, searchSubtitle);

        VBox form = new VBox(12);
        form.setPadding(new Insets(45, 25, 25, 25));

        Label fieldLabel = new Label("Numero de serie");
        fieldLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #020617;");

        HBox inputLine = new HBox(12);
        inputLine.setAlignment(Pos.CENTER_LEFT);

        TextField serialInput = new TextField();
        serialInput.setPromptText("Ex : SN-PRO-001234");
        serialInput.setPrefHeight(58);
        serialInput.setStyle("""
            -fx-background-color: #f3f4f6;
            -fx-background-radius: 7;
            -fx-border-color: transparent;
            -fx-font-size: 14px;
            -fx-padding: 0 15;
        """);
        HBox.setHgrow(serialInput, Priority.ALWAYS);

        Button verifyBtn = new Button("Verifier");
        verifyBtn.setPrefSize(110, 42);
        verifyBtn.setStyle("""
            -fx-background-color: #8bb6ff;
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-background-radius: 7;
            -fx-cursor: hand;
        """);

        Label result = new Label();
        result.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");

        verifyBtn.setOnAction(e -> {
            String value = serialInput.getText().trim();

            if (serialController.isValid(value)) {
                result.setText("Numero valide : lunettes authentiques");
                result.setStyle("-fx-text-fill: #16a34a; -fx-font-size: 15px; -fx-font-weight: bold;");
            } else {
                result.setText("Numero invalide ou introuvable");
                result.setStyle("-fx-text-fill: #dc2626; -fx-font-size: 15px; -fx-font-weight: bold;");
            }
        });

        inputLine.getChildren().addAll(serialInput, verifyBtn);
        form.getChildren().addAll(fieldLabel, inputLine, result);
        searchCard.getChildren().addAll(searchHeader, form);

        VBox examplesCard = new VBox();
        examplesCard.setMaxWidth(830);
        examplesCard.setStyle("""
            -fx-background-color: white;
            -fx-background-radius: 12;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 14, 0, 0, 5);
        """);

        VBox examplesHeader = new VBox(8);
        examplesHeader.setPadding(new Insets(25));
        examplesHeader.setStyle("-fx-background-color: #f8fafc;");

        Label examplesTitle = new Label("Numero disponible");
        examplesTitle.setStyle("-fx-font-size: 20px; -fx-text-fill: #020617;");

        Label examplesSubtitle = new Label("Le numero apparait ici apres la fabrication d'une commande");
        examplesSubtitle.setStyle("-fx-font-size: 15px; -fx-text-fill: #64748b;");

        examplesHeader.getChildren().addAll(examplesTitle, examplesSubtitle);

        GridPane examplesGrid = new GridPane();
        examplesGrid.setPadding(new Insets(25));
        examplesGrid.setHgap(12);
        examplesGrid.setVgap(12);

        Order order = layout.getAppController().getOrderController().getCurrentOrder();
        if (order != null && order.isCompleted()) {
            examplesGrid.add(exampleBox(order.getSerialNumbers().getFirst().value()), 0, 0);
        } else {
            examplesGrid.add(exampleBox("Aucun numero genere pour le moment"), 0, 0);
        }

        examplesCard.getChildren().addAll(examplesHeader, examplesGrid);

        content.getChildren().addAll(searchCard, examplesCard);
        page.getChildren().addAll(header, content);

        view.setCenter(page);
    }

    private HBox exampleBox(String text) {
        HBox box = new HBox();
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(14, 18, 14, 18));
        box.setPrefSize(385, 48);
        box.setStyle("""
            -fx-background-color: white;
            -fx-border-color: #e5e7eb;
            -fx-border-radius: 8;
            -fx-background-radius: 8;
        """);

        Label label = new Label(text);
        label.setStyle("""
            -fx-font-size: 14px;
            -fx-text-fill: #020617;
            -fx-font-weight: bold;
        """);

        box.getChildren().add(label);
        return box;
    }

    public BorderPane getView() {
        return view;
    }
}
