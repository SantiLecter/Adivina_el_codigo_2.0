package example.adivina_el_codigo;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private Button newGameButton;

    /**
     * Maneja el evento cuando se hace clic en el botón "Nuevo Juego".
     *
     * @param event el evento de acción
     */
    @FXML
    private void startNewGame(ActionEvent event) {
        try {
            // Carga la pantalla del juego
            FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml"));
            Parent gameRoot = loader.load();

            // Obtiene el escenario actual
            Stage stage = (Stage) newGameButton.getScene().getWindow();

            // Establece la nueva escena
            Scene scene = new Scene(gameRoot, 600, 400);
            stage.setScene(scene);
            stage.setTitle("Adivina el código - Juego");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
