package fr.smartglasses.frontend.view;

import fr.smartglasses.frontend.controller.OrderController;
import fr.smartglasses.frontend.model.Order;
import fr.smartglasses.frontend.model.SerialPair;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class FabricationView {

    private final BorderPane view = new BorderPane();
    private final OrderController orderController;

    public FabricationView(Layout layout, OrderController orderController) {
        this.orderController = orderController;

        Order order = orderController.getCurrentOrder();
        if (order == null) {
            showEmpty(); // pas de commande en cours
        } else if (order.getSerialNumbers() != null && !order.getSerialNumbers().isEmpty()) {
            showSuccess(layout); // commande déjà fabriquée
        } else {
            showWaiting(layout); // la fabrication de la commande est en cours
        }
    }

    // Affiche un message quand aucune commande n'est en cours
    private void showEmpty() {
        view.setStyle("-fx-background-color: #f1f6ff;");

        VBox card = new VBox(20);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(55));
        card.setMaxWidth(600);
        card.setStyle("""
            -fx-background-color: white;
            -fx-background-radius: 16;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 28, 0, 0, 10);
        """);

        Label icon = new Label("◷");
        icon.setAlignment(Pos.CENTER);
        icon.setPrefSize(100, 100);
        icon.setStyle("""
            -fx-background-color: #eff6ff;
            -fx-border-color: #93c5fd;
            -fx-border-width: 3;
            -fx-border-radius: 100;
            -fx-background-radius: 100;
            -fx-text-fill: #1e5bff;
            -fx-font-size: 42px;
        """);

        Label title = new Label("Aucune commande en cours");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #020617;");

        Label subtitle = new Label("Passez une commande depuis le catalogue pour lancer la fabrication.");
        subtitle.setStyle("-fx-font-size: 16px; -fx-text-fill: #475569; -fx-wrap-text: true;");
        subtitle.setMaxWidth(480);
        subtitle.setAlignment(Pos.CENTER);

        card.getChildren().addAll(icon, title, subtitle);

        StackPane center = new StackPane(card);
        center.setPadding(new Insets(90, 60, 60, 60));
        view.setCenter(center);
    }

    // Affiche "Veuillez patienter" et lance la fabrication en arrière-plan
    private void showWaiting(Layout layout) {
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

        Label icon = new Label("⏳");
        icon.setAlignment(Pos.CENTER);
        icon.setPrefSize(120, 120);
        icon.setStyle("""
            -fx-background-color: white;
            -fx-border-color: #93c5fd;
            -fx-border-width: 18;
            -fx-border-radius: 100;
            -fx-background-radius: 100;
            -fx-text-fill: #1e5bff;
            -fx-font-size: 50px;
        """);

        Label title = new Label("Fabrication en cours");
        title.setStyle("-fx-font-size: 34px; -fx-font-weight: bold; -fx-text-fill: #020617;");

        Label subtitle = new Label("Vos lunettes sont en cours de fabrication, veuillez patienter...");
        subtitle.setStyle("-fx-font-size: 20px; -fx-text-fill: #334155;");

        card.getChildren().addAll(icon, title, subtitle);

        StackPane center = new StackPane(card);
        center.setPadding(new Insets(90, 60, 60, 60));
        view.setCenter(center);

        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.submit(() -> {
            try {
                orderController.startFabrication();
                Platform.runLater(() -> showSuccess(layout));
            } catch (Exception e) {
                Platform.runLater(() -> showError(layout, e.getMessage()));
            } finally {
                executor.shutdown(); // libère les ressources une fois la tâche finie
            }
        });
    }

    // Affiche le message de succès avec les numéros de série
    private void showSuccess(Layout layout) {
        view.getChildren().clear();
        view.setStyle("-fx-background-color: #f1f6ff;");

        Order order = orderController.getCurrentOrder();
        String orderId = order == null ? "CMD-00000000" : order.getId();
        List<SerialPair> serials = order == null ? List.of() : order.getSerialNumbers();
        int quantity = serials.size();

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
            -fx-text-fill: #16a34a;
            -fx-font-size: 24px;
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
        Label count = new Label(quantity + " lunette(s) fabriquée(s)");
        count.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        left.getChildren().addAll(tableTitle, count);

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        VBox cmd = new VBox(5);
        cmd.setPadding(new Insets(12, 22, 12, 22));
        cmd.setStyle("-fx-background-color: rgba(255,255,255,0.18); -fx-background-radius: 8;");
        Label cmdSmall = new Label("Commande");
        cmdSmall.setStyle("-fx-text-fill: white; -fx-font-size: 13px;");
        Label cmdId = new Label(orderId);
        cmdId.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");
        cmd.getChildren().addAll(cmdSmall, cmdId);

        tableTop.getChildren().addAll(left, sp, cmd);

        GridPane table = new GridPane();
        table.setPadding(new Insets(35, 25, 35, 25));
        table.setVgap(12);
        table.setHgap(60);

        addRow(table, 0, "#", "Numéro de série", "Modèle", "Statut", true);
        for (int i = 0; i < serials.size(); i++) {
            SerialPair pair = serials.get(i);
            addRow(table, i + 1, String.valueOf(i + 1), pair.numeroSerie(), pair.modele(), "Généré", false);
        }

        HBox bottom = new HBox(15);
        bottom.setAlignment(Pos.CENTER_LEFT);
        bottom.setPadding(new Insets(20, 25, 20, 25));
        bottom.setStyle("-fx-background-color: #f8fafc; -fx-background-radius: 0 0 10 10;");

        Label note = new Label("Vous pouvez vérifier chaque numéro de série dans l'onglet Vérifier");
        note.setStyle("-fx-text-fill: #334155; -fx-font-size: 13px;");

        Region bottomSpacer = new Region();
        HBox.setHgrow(bottomSpacer, Priority.ALWAYS);

        Button newOrder = new Button("Nouvelle commande");
        Button verify = new Button("Vérifier un numéro");

        newOrder.setOnAction(e -> {
            orderController.resetOrder();
            layout.setContent(new CatalogueView(layout, orderController).getView());
        });
        verify.setOnAction(e -> layout.setContent(new VerificationView(layout, layout.getAppController().getSerialController()).getView()));

        bottom.getChildren().addAll(note, bottomSpacer, newOrder, verify);
        tableCard.getChildren().addAll(tableTop, table, bottom);


        VBox content = new VBox(20, tableCard);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(40));

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background: #f1f6ff; -fx-background-color: #f1f6ff;");

        page.getChildren().addAll(header, scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        view.setCenter(page);
    }

    // Affiche un message d'erreur
    private void showError(Layout layout, String errorMsg) {
        view.getChildren().clear();
        view.setStyle("-fx-background-color: #f1f6ff;");

        VBox page = new VBox();
        page.setStyle("-fx-background-color: #f1f6ff;");

        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(35, 0, 35, 310));
        header.setStyle("-fx-background-color: #dc2626;");

        Label errorIcon = new Label("✕");
        errorIcon.setAlignment(Pos.CENTER);
        errorIcon.setPrefSize(45, 45);
        errorIcon.setStyle("""
            -fx-background-color: white;
            -fx-text-fill: #dc2626;
            -fx-font-size: 28px;
            -fx-font-weight: bold;
            -fx-background-radius: 50;
        """);

        VBox headerText = new VBox(2);
        Label title = new Label("Erreur de fabrication");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 32px; -fx-font-weight: bold;");
        Label subtitle = new Label("Une erreur s'est produite lors de la fabrication");
        subtitle.setStyle("-fx-text-fill: white; -fx-font-size: 17px;");
        headerText.getChildren().addAll(title, subtitle);
        header.getChildren().addAll(errorIcon, headerText);

        VBox errorCard = new VBox(20);
        errorCard.setMaxWidth(910);
        errorCard.setStyle("""
            -fx-background-color: white;
            -fx-background-radius: 10;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 18, 0, 0, 7);
        """);
        errorCard.setPadding(new Insets(40));

        Label errorTitle = new Label("Message d'erreur");
        errorTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #020617;");

        Label errorMessage = new Label(errorMsg);
        errorMessage.setStyle("-fx-font-size: 16px; -fx-text-fill: #334155; -fx-wrap-text: true;");
        errorMessage.setMaxWidth(800);

        errorCard.getChildren().addAll(errorTitle, errorMessage);

        HBox actions = new HBox(15);
        actions.setAlignment(Pos.CENTER);
        actions.setPadding(new Insets(20));

        Button retryBtn = new Button("Réessayer");
        retryBtn.setStyle("""
            -fx-background-color: #dc2626;
            -fx-text-fill: white;
            -fx-font-size: 16px;
            -fx-padding: 10 30;
            -fx-background-radius: 8;
            -fx-cursor: hand;
        """);
        retryBtn.setOnAction(e -> layout.setContent(new FabricationView(layout, orderController).getView()));

        Button backBtn = new Button("Retour au catalogue");
        backBtn.setStyle("""
            -fx-background-color: #6b7280;
            -fx-text-fill: white;
            -fx-font-size: 16px;
            -fx-padding: 10 30;
            -fx-background-radius: 8;
            -fx-cursor: hand;
        """);
        backBtn.setOnAction(e -> layout.setContent(new CatalogueView(layout, orderController).getView()));

        actions.getChildren().addAll(retryBtn, backBtn);

        VBox content = new VBox(20, errorCard, actions);
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

        if (!header) {
            l2.setStyle("-fx-text-fill: #1e5bff; -fx-font-weight: bold;");
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
