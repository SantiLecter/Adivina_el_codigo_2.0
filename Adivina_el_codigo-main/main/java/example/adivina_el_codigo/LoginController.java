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
    private Button singlePlayerButton;

    @FXML
    private Button twoPlayerButton;

    /**
     * Maneja el evento cuando se hace clic en el botón "Un Jugador".
     *
     * @param event el evento de acción
     */
    @FXML
    private void startSinglePlayerGame(ActionEvent event) {
        startGame(false);
    }

    /**
     * Maneja el evento cuando se hace clic en el botón "Dos Jugadores".
     *
     * @param event el evento de acción
     */
    @FXML
    private void startTwoPlayerGame(ActionEvent event) {
        startGame(true);
    }

    /**
     * Inicia el juego con el modo especificado.
     *
     * @param isTwoPlayer true si es modo de dos jugadores, false si es modo de un jugador
     */
    private void startGame(boolean isTwoPlayer) {
        try {
            FXMLLoader loader;
            if (isTwoPlayer) {
                loader = new FXMLLoader(getClass().getResource("/example/adivina_el_codigo/TwoPlayerGame.fxml"));
            } else {
                loader = new FXMLLoader(getClass().getResource("/example/adivina_el_codigo/game.fxml"));
            }
            Parent gameRoot = loader.load();

            // Obtiene el escenario actual
            Stage stage = (Stage) singlePlayerButton.getScene().getWindow();

            // Establece la nueva escena
            Scene scene = new Scene(gameRoot, 600, 400);
            stage.setScene(scene);
            stage.setTitle("Adivina el código - Juego");

            // Pasa el modo de juego al controlador del juego
            if (isTwoPlayer) {
                TwoPlayerGameController gameController = loader.getController();
                gameController.initialize();
            } else {
                GameController gameController = loader.getController();
                gameController.setTwoPlayerMode(isTwoPlayer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
