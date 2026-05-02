package fr.smartglasses.frontend.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;

public class FabricationView {

    private final BorderPane view = new BorderPane();
    private final ProgressBar progressBar = new ProgressBar(0);
    private final Label percentLabel = new Label("0%");

    public FabricationView(Layout layout) {
        showInProgress(layout);
    }

    private void showInProgress(Layout layout) {
        view.setStyle("-fx-background-color: #f1f6ff;");

        VBox card = new VBox(25);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(55));
        card.setMaxWidth(760);
        card.setStyle("""
            -fx-background-color: white;
            -fx-background-radius: 16;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 28, 0, 0, 10);
        """);

        Label icon = new Label("▣");
        icon.setAlignment(Pos.CENTER);
        icon.setPrefSize(120, 120);
        icon.setStyle("""
            -fx-background-color: white;
            -fx-border-color: #93c5fd;
            -fx-border-width: 18;
            -fx-border-radius: 100;
            -fx-background-radius: 100;
            -fx-text-fill: #1e5bff;
            -fx-font-size: 52px;
            -fx-font-weight: bold;
        """);

        Label title = new Label("Fabrication en cours");
        title.setStyle("-fx-font-size: 34px; -fx-font-weight: bold; -fx-text-fill: #020617;");

        Label subtitle = new Label("Vos lunettes sont en cours de fabrication, veuillez patienter...");
        subtitle.setStyle("-fx-font-size: 20px; -fx-text-fill: #334155;");

        HBox progressHeader = new HBox();
        progressHeader.setAlignment(Pos.CENTER_LEFT);
        progressHeader.setPrefWidth(650);

        Label progressText = new Label("Progression");
        progressText.setStyle("-fx-font-size: 16px; -fx-text-fill: #334155;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        percentLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1e5bff;");
        progressHeader.getChildren().addAll(progressText, spacer, percentLabel);

        progressBar.setPrefWidth(650);
        progressBar.setPrefHeight(14);
        progressBar.setStyle("-fx-accent: #020617;");

        HBox steps = new HBox(20);
        steps.setAlignment(Pos.CENTER);

        steps.getChildren().addAll(
                stepBox("◷", "Préparation", true),
                stepBox("▣", "Assemblage", false),
                stepBox("✓", "Finalisation", false)
        );

        card.getChildren().addAll(icon, title, subtitle, progressHeader, progressBar, steps);

        StackPane center = new StackPane(card);
        center.setPadding(new Insets(90, 60, 60, 60));

        view.setCenter(center);

        startProgress(layout);
    }

    private VBox stepBox(String icon, String text, boolean active) {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setPrefSize(205, 85);

        box.setStyle(active
                ? "-fx-background-color: #eff6ff; -fx-background-radius: 10;"
                : "-fx-background-color: #f8fafc; -fx-background-radius: 10;"
        );

        Label i = new Label(icon);
        i.setStyle((active ? "-fx-text-fill: #1e5bff;" : "-fx-text-fill: #64748b;") + "-fx-font-size: 30px;");

        Label label = new Label(text);
        label.setStyle((active ? "-fx-text-fill: #1e5bff;" : "-fx-text-fill: #64748b;") + "-fx-font-size: 15px;");

        box.getChildren().addAll(i, label);
        return box;
    }

    private void startProgress(Layout layout) {
        Timeline timeline = new Timeline();
        timeline.setCycleCount(100);

        KeyFrame frame = new KeyFrame(Duration.millis(50), e -> {
            double value = progressBar.getProgress() + 0.01;
            progressBar.setProgress(value);
            percentLabel.setText((int) (value * 100) + "%");

            if (value >= 1.0) {
                showFinished(layout);
            }
        });

        timeline.getKeyFrames().add(frame);
        timeline.play();
    }

    private void showFinished(Layout layout) {
        view.getChildren().clear();
        view.setStyle("-fx-background-color: #f1f6ff;");

        VBox page = new VBox();
        page.setStyle("-fx-background-color: #f1f6ff;");

        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(35, 0, 35, 310));
        header.setStyle("-fx-background-color: #1e5bff;");

        Label check = new Label("✓");
        check.setAlignment(Pos.CENTER);
        check.setPrefSize(45, 45);
        check.setStyle("""
            -fx-background-color: white;
            -fx-text-fill: #1e5bff;
            -fx-font-size: 28px;
            -fx-font-weight: bold;
            -fx-background-radius: 50;
        """);

        VBox headerText = new VBox(2);
        Label title = new Label("Fabrication terminée !");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 32px; -fx-font-weight: bold;");
        Label subtitle = new Label("Vos numéros de série sont prêts");
        subtitle.setStyle("-fx-text-fill: white; -fx-font-size: 17px;");
        headerText.getChildren().addAll(title, subtitle);
        header.getChildren().addAll(check, headerText);

        VBox tableCard = new VBox();
        tableCard.setMaxWidth(910);
        tableCard.setStyle("""
            -fx-background-color: white;
            -fx-background-radius: 10;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 18, 0, 0, 7);
        """);

        HBox tableTop = new HBox();
        tableTop.setAlignment(Pos.CENTER_LEFT);
        tableTop.setPadding(new Insets(25));
        tableTop.setStyle("-fx-background-color: #2f7cff; -fx-background-radius: 10 10 0 0;");

        VBox left = new VBox(8);
        Label tableTitle = new Label("Numéros de série générés");
        tableTitle.setStyle("-fx-text-fill: white; -fx-font-size: 23px; -fx-font-weight: bold;");
        Label count = new Label("6 lunettes fabriquées");
        count.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        left.getChildren().addAll(tableTitle, count);

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        VBox cmd = new VBox(5);
        cmd.setPadding(new Insets(12, 22, 12, 22));
        cmd.setStyle("-fx-background-color: rgba(255,255,255,0.18); -fx-background-radius: 8;");
        Label cmdSmall = new Label("Commande");
        cmdSmall.setStyle("-fx-text-fill: white; -fx-font-size: 13px;");
        Label cmdId = new Label("CMD-42961499");
        cmdId.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");
        cmd.getChildren().addAll(cmdSmall, cmdId);

        tableTop.getChildren().addAll(left, sp, cmd);

        GridPane table = new GridPane();
        table.setPadding(new Insets(35, 25, 35, 25));
        table.setVgap(12);
        table.setHgap(60);

        addRow(table, 0, "#", "Numéro de série", "Modèle", "Statut", true);
        addRow(table, 1, "1", "✣  SN-SPO-721818", "SmartGlass Sport", "✓  Généré", false);
        addRow(table, 2, "2", "✣  SN-SPO-592808", "SmartGlass Sport", "✓  Généré", false);
        addRow(table, 3, "3", "✣  SN-LIT-615699", "SmartGlass Lite", "✓  Généré", false);
        addRow(table, 4, "4", "✣  SN-LIT-898745", "SmartGlass Lite", "✓  Généré", false);
        addRow(table, 5, "5", "✣  SN-SPO-358418", "SmartGlass Sport", "✓  Généré", false);
        addRow(table, 6, "6", "✣  SN-PRO-864669", "SmartGlass Pro", "✓  Généré", false);

        HBox bottom = new HBox(15);
        bottom.setAlignment(Pos.CENTER_LEFT);
        bottom.setPadding(new Insets(20, 25, 20, 25));
        bottom.setStyle("-fx-background-color: #f8fafc; -fx-background-radius: 0 0 10 10;");

        Label note = new Label("Vous pouvez vérifier chaque numéro de série dans l'onglet \"Vérifier\"");
        note.setStyle("-fx-text-fill: #334155; -fx-font-size: 13px;");

        Region bottomSpacer = new Region();
        HBox.setHgrow(bottomSpacer, Priority.ALWAYS);

        Button newOrder = new Button("Nouvelle commande");
        Button verify = new Button("Vérifier un numéro");

        newOrder.setOnAction(e -> layout.setContent(new CatalogueView(layout).getView()));
        verify.setOnAction(e -> layout.setContent(new VerificationView(layout).getView()));

        bottom.getChildren().addAll(note, bottomSpacer, newOrder, verify);
        tableCard.getChildren().addAll(tableTop, table, bottom);

        VBox info = new VBox(8);
        info.setMaxWidth(910);
        info.setPadding(new Insets(20));
        info.setStyle("-fx-background-color: #1e5bff; -fx-background-radius: 10;");
        Label infoTitle = new Label("✦  À propos des numéros de série");
        infoTitle.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");
        Label infoText = new Label("Chaque paire de lunettes possède un numéro de série unique qui garantit son authenticité et permet de tracer son origine.");
        infoText.setStyle("-fx-text-fill: white; -fx-font-size: 13px;");
        info.getChildren().addAll(infoTitle, infoText);

        VBox content = new VBox(20, tableCard, info);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(40));

        page.getChildren().addAll(header, content);
        view.setCenter(page);
    }

    private void addRow(GridPane table, int row, String c1, String c2, String c3, String c4, boolean header) {
        Label l1 = cell(c1, header);
        Label l2 = cell(c2, header);
        Label l3 = cell(c3, header);
        Label l4 = cell(c4, header);

        if (!header && c2.contains("SN-")) l2.setStyle("-fx-text-fill: #1e5bff; -fx-font-weight: bold;");
        if (!header && c4.contains("Généré")) {
            l4.setStyle("-fx-background-color: #16a34a; -fx-text-fill: white; -fx-padding: 4 10; -fx-background-radius: 6;");
        }

        table.add(l1, 0, row);
        table.add(l2, 1, row);
        table.add(l3, 2, row);
        table.add(l4, 3, row);
    }

    private Label cell(String text, boolean header) {
        Label label = new Label(text);
        label.setMinWidth(110);
        label.setStyle(header
                ? "-fx-font-size: 13px; -fx-font-weight: bold;"
                : "-fx-font-size: 13px;");
        return label;
    }

    public BorderPane getView() {
        return view;
    }
}