package fr.smartglasses.frontend;

import fr.smartglasses.frontend.view.Layout;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        Layout layout = new Layout();

        Scene scene = new Scene(layout.getRoot(), 1200, 800);

        stage.setTitle("SmartGlasses");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}