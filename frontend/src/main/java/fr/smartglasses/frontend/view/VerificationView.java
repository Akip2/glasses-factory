package fr.smartglasses.frontend.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class VerificationView {

    private final BorderPane view = new BorderPane();

    public VerificationView(Layout layout) {
        view.setStyle("-fx-background-color: #f1f6ff;");

        VBox page = new VBox();
        page.setStyle("-fx-background-color: #f1f6ff;");

        VBox header = new VBox(15);
        header.setPadding(new Insets(40, 0, 45, 300));
        header.setStyle("-fx-background-color: #1e5bff;");

        Label title = new Label("Vérification de numéro de série");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 36px; -fx-font-weight: bold;");

        Label subtitle = new Label("Vérifiez l'authenticité de vos lunettes connectées");
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

        Label searchSubtitle = new Label("Entrez le numéro de série complet pour vérifier son authenticité");
        searchSubtitle.setStyle("-fx-text-fill: white; -fx-font-size: 15px;");

        searchHeader.getChildren().addAll(searchTitle, searchSubtitle);

        VBox form = new VBox(12);
        form.setPadding(new Insets(45, 25, 25, 25));

        Label fieldLabel = new Label("Numéro de série");
        fieldLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #020617;");

        HBox inputLine = new HBox(12);
        inputLine.setAlignment(Pos.CENTER_LEFT);

        TextField serialInput = new TextField();
        serialInput.setPromptText("Ex :  SN-PRO-001234");
        serialInput.setPrefHeight(58);
        serialInput.setStyle("""
            -fx-background-color: #f3f4f6;
            -fx-background-radius: 7;
            -fx-border-color: transparent;
            -fx-font-size: 14px;
            -fx-padding: 0 15;
        """);
        HBox.setHgrow(serialInput, Priority.ALWAYS);

        Button verifyBtn = new Button("⌕  Vérifier");
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

            if (value.startsWith("SN-")) {
                result.setText("✓ Numéro valide : lunettes authentiques");
                result.setStyle("-fx-text-fill: #16a34a; -fx-font-size: 15px; -fx-font-weight: bold;");
            } else {
                result.setText("✕ Numéro invalide ou introuvable");
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

        Label examplesTitle = new Label("Exemples de numéros valides");
        examplesTitle.setStyle("-fx-font-size: 20px; -fx-text-fill: #020617;");

        Label examplesSubtitle = new Label("Utilisez l'un de ces numéros pour tester la vérification");
        examplesSubtitle.setStyle("-fx-font-size: 15px; -fx-text-fill: #64748b;");

        examplesHeader.getChildren().addAll(examplesTitle, examplesSubtitle);

        GridPane examplesGrid = new GridPane();
        examplesGrid.setPadding(new Insets(25));
        examplesGrid.setHgap(12);
        examplesGrid.setVgap(12);

        examplesGrid.add(exampleBox("✣  SN-PRO-001234"), 0, 0);
        examplesGrid.add(exampleBox("✣  SN-PRO-001235"), 1, 0);
        examplesGrid.add(exampleBox("✣  SN-LIT-002341"), 0, 1);
        examplesGrid.add(exampleBox("✣  SN-SPO-003456"), 1, 1);

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