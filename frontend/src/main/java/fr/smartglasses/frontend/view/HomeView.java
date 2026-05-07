package fr.smartglasses.frontend.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class HomeView {

    private final ScrollPane view;

    public HomeView(Layout layout) {

        VBox content = new VBox();

        HBox topSection = new HBox(60);
        topSection.setPadding(new Insets(60, 70, 60, 70));
        topSection.setAlignment(Pos.CENTER);
        topSection.setStyle("""
            -fx-background-color: linear-gradient(to right, #1e5bff, #173bb8);
        """);

        VBox textPart = new VBox(25);
        textPart.setPrefWidth(470);

        Label title = new Label("Lunettes\nConnectées\nNouvelle\nGénération");
        title.setStyle("""
            -fx-text-fill: white;
            -fx-font-size: 52px;
            -fx-font-weight: bold;
        """);

        Label desc = new Label(
                "Découvrez notre système de commande\n" +
                "innovant pour les lunettes intelligentes.\n" +
                "Simplicité, traçabilité et excellence\n" +
                "technologique."
        );
        desc.setStyle("""
            -fx-text-fill: white;
            -fx-font-size: 22px;
            -fx-line-spacing: 8px;
        """);

        HBox buttons = new HBox(20);

        Button catalogueBtn = new Button("Voir le\ncatalogue");
        catalogueBtn.setPrefSize(215, 100);
        catalogueBtn.setStyle("""
            -fx-background-color: white;
            -fx-text-fill: #1e5bff;
            -fx-font-size: 18px;
            -fx-font-weight: bold;
            -fx-background-radius: 12;
        """);
        catalogueBtn.setOnAction(e -> layout.setContent(new CatalogueView(layout, layout.getAppController().getOrderController()).getView()));

        Button verifyBtn = new Button("Vérifier un\nnuméro");
        verifyBtn.setPrefSize(235, 100);
        verifyBtn.setStyle("""
            -fx-background-color: transparent;
            -fx-border-color: rgba(255,255,255,0.35);
            -fx-text-fill: white;
            -fx-font-size: 18px;
            -fx-font-weight: bold;
            -fx-background-radius: 12;
            -fx-border-radius: 12;
        """);
        verifyBtn.setOnAction(e -> layout.setContent(new VerificationView(layout, layout.getAppController().getSerialController()).getView()));

        buttons.getChildren().addAll(catalogueBtn, verifyBtn);
        textPart.getChildren().addAll(title, desc, buttons);

        ImageView image = new ImageView(
                new Image(getClass().getResource("/images/ph1.png").toExternalForm())
        );
        image.setFitWidth(325);
        image.setFitHeight(480);
        image.setPreserveRatio(false);
        image.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.20), 20, 0, 0, 6);");

        topSection.getChildren().addAll(textPart, image);

        VBox whiteSection = new VBox(40);
        whiteSection.setPadding(new Insets(0, 40, 40, 40));
        whiteSection.setAlignment(Pos.TOP_CENTER);
        whiteSection.setStyle("-fx-background-color: white;");

        Label why = new Label("Pourquoi choisir SmartGlasses ?");
        why.setStyle("""
            -fx-font-size: 34px;
            -fx-font-weight: bold;
            -fx-text-fill: #111827;
        """);

        Label sub = new Label("Un système conçu pour l'excellence et la simplicité");
        sub.setStyle("""
            -fx-font-size: 22px;
            -fx-text-fill: #4b5563;
        """);

        HBox cards = new HBox(40);
        cards.setAlignment(Pos.CENTER);
        cards.getChildren().addAll(
                infoCard("✦", "Innovation", "Technologie de pointe\nintégrée"),
                infoCard("🛡", "Qualité", "Fabrication certifiée et\ncontrôlée"),
                infoCard("⚡", "Performance", "Système optimisé et\nréactif")
        );

        HBox actions = new HBox(40);
        actions.setAlignment(Pos.CENTER);
        actions.getChildren().addAll(
                actionCard("🛒", "Commander", "Parcourez notre catalogue\net passez commande",
                        () -> layout.setContent(new CatalogueView(layout, layout.getAppController().getOrderController()).getView())),
                actionCard("◷", "Fabrication", "Suivez la fabrication et\nrécupérez vos numéros",
                        () -> layout.setContent(new FabricationView(layout, layout.getAppController().getOrderController()).getView())),
                actionCard("⌕", "Vérifier", "Vérifiez l'authenticité d'un\nnuméro de série",
                        () -> layout.setContent(new VerificationView(layout, layout.getAppController().getSerialController()).getView()))
        );

        whiteSection.getChildren().addAll(why, sub, cards, actions);

        content.getChildren().addAll(topSection, whiteSection);

        view = new ScrollPane(content);
        view.setFitToWidth(true);
        view.setStyle("-fx-background: white;");
    }

    private VBox infoCard(String icon, String title, String text) {
        VBox box = new VBox(20);
        box.setPadding(new Insets(40));
        box.setPrefSize(305, 225);
        box.setStyle("""
            -fx-background-color: #f8fbff;
            -fx-border-color: #dbeafe;
            -fx-border-radius: 18;
            -fx-background-radius: 18;
        """);

        Label i = new Label(icon);
        i.setAlignment(Pos.CENTER);
        i.setPrefSize(70, 70);
        i.setStyle("""
            -fx-background-color: #2563ff;
            -fx-text-fill: white;
            -fx-font-size: 34px;
            -fx-font-weight: bold;
            -fx-background-radius: 18;
        """);

        Label t = new Label(title);
        t.setStyle("""
            -fx-font-size: 25px;
            -fx-font-weight: bold;
            -fx-text-fill: #111827;
        """);

        Label d = new Label(text);
        d.setStyle("""
            -fx-font-size: 20px;
            -fx-text-fill: #4b5563;
            -fx-line-spacing: 7px;
        """);

        box.getChildren().addAll(i, t, d);
        return box;
    }

    private VBox actionCard(String icon, String title, String text, Runnable action) {
        VBox box = new VBox(18);
        box.setPadding(new Insets(40));
        box.setPrefSize(312, 255);
        box.setStyle("""
            -fx-background-color: linear-gradient(to bottom right, #2f83ff, #1552f0);
            -fx-background-radius: 16;
            -fx-cursor: hand;
        """);

        Label i = new Label(icon);
        i.setStyle("-fx-text-fill: white; -fx-font-size: 42px;");

        Label t = new Label(title);
        t.setStyle("""
            -fx-text-fill: white;
            -fx-font-size: 26px;
            -fx-font-weight: bold;
        """);

        Label d = new Label(text);
        d.setStyle("""
            -fx-text-fill: white;
            -fx-font-size: 20px;
            -fx-line-spacing: 7px;
        """);

        box.setOnMouseClicked(e -> action.run());
        box.getChildren().addAll(i, t, d);
        return box;
    }

    public ScrollPane getView() {
        return view;
    }
}
