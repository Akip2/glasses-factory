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

        HBox mainSection = new HBox(60);
        mainSection.setPadding(new Insets(60, 70, 60, 70));
        mainSection.setAlignment(Pos.CENTER);
        mainSection.setStyle("""
            -fx-background-color: linear-gradient(to right, #1e5bff, #173bb8);
        """);
        VBox.setVgrow(mainSection, Priority.ALWAYS);

        VBox textPart = new VBox(25);
        textPart.setPrefWidth(470);

        Label title = new Label("Lunettes Connectées\nNouvelle Génération");
        title.setStyle("""
            -fx-text-fill: white;
            -fx-font-size: 42px;
            -fx-font-weight: bold;
        """);

        Label desc = new Label("Commandez vous lunettes dès maintenant !");
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

        mainSection.getChildren().addAll(textPart, image);

        content.getChildren().addAll(mainSection);
        content.setFillWidth(true);

        view = new ScrollPane(content);
        view.setFitToWidth(true);
        view.setStyle("-fx-background: white;");

        mainSection.prefHeightProperty().bind(view.heightProperty());
    }

    public ScrollPane getView() {
        return view;
    }
}
