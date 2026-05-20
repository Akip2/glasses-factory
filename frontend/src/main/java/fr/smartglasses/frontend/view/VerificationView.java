package fr.smartglasses.frontend.view;

import fr.smartglasses.frontend.controller.SerialController;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
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

        Label title = new Label("Vérification de numéro de série");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 36px; -fx-font-weight: bold;");

        Label subtitle = new Label("Verifiez l'authenticité de vos lunettes connectées");
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

        Label searchSubtitle = new Label("Entrez le numéro de série complet pour verifier son authenticité");
        searchSubtitle.setStyle("-fx-text-fill: white; -fx-font-size: 15px;");

        searchHeader.getChildren().addAll(searchTitle, searchSubtitle);

        VBox form = new VBox(12);
        form.setPadding(new Insets(45, 25, 25, 25));

        Label fieldLabel = new Label("Numero de série");
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
            String serial = serialInput.getText().trim();
            if (serial.isBlank()) {
                result.setText("Veuillez entrer un numéro de série.");
                result.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #dc2626;");
                return;
            }
            serialController.isValid(serial)
                    .thenAccept(valid -> Platform.runLater(() -> {
                        verifyBtn.setDisable(false);
                        if (valid) {
                            result.setText("✓ Numéro valide");
                            result.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #16a34a;");
                        } else {
                            result.setText("✕ Numéro invalide");
                            result.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #dc2626;");
                        }
                    }))
                    .exceptionally(ex -> {
                        Platform.runLater(() -> {
                            verifyBtn.setDisable(false);
                            result.setText("Erreur : " + ex.getCause().getMessage());
                            result.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #dc2626;");
                        });
                        return null;
                    });
        });

        inputLine.getChildren().addAll(serialInput, verifyBtn);
        form.getChildren().addAll(fieldLabel, inputLine, result);
        searchCard.getChildren().addAll(searchHeader, form);

        content.getChildren().addAll(searchCard);
        page.getChildren().addAll(header, content);

        view.setCenter(page);
    }

    public BorderPane getView() {
        return view;
    }
}
