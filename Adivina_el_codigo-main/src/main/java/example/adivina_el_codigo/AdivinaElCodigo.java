package example.adivina_el_codigo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AdivinaElCodigo extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/example/adivina_el_codigo/login.fxml"));
        primaryStage.setTitle("Adivina el codigo");
        Scene scene = new Scene(root, 400, 300);
        scene.getStylesheets().add(getClass().getResource("/example/adivina_el_codigo/styles/style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
