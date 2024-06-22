package example.adivina_el_codigo;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LoginController {

    @FXML
    private Button singlePlayerButton;

    @FXML
    private Button twoPlayerButton;
    
    @FXML
    private Button closeButton;
    
    @FXML
    private ImageView singlePlayerImage;

    @FXML
    private ImageView twoPlayerImage;
    

    
    @FXML
    private void initialize() {
        // Cargar imágenes
    	try {
    	    singlePlayerImage.setImage(new Image(getClass().getResourceAsStream("/example/adivina_el_codigo/images/user1.png")));
    	} catch (NullPointerException e) {
    	    System.out.println("Error loading singlePlayerImage.png: " + e.getMessage());
    	}
    	try {
    	    twoPlayerImage.setImage(new Image(getClass().getResourceAsStream("/example/adivina_el_codigo/images/user2.png")));
    	} catch (NullPointerException e) {
    	    System.out.println("Error loading twoPlayerImage.png: " + e.getMessage());
    	}
    }


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
        	FXMLLoader loader = new FXMLLoader(getClass().getResource(
        	        isTwoPlayer ? "/example/adivina_el_codigo/TwoPlayerGame.fxml" : "/example/adivina_el_codigo/game.fxml"));
        	Parent gameRoot = loader.load();

            // Obtiene el escenario actual
            Stage stage = (Stage) singlePlayerButton.getScene().getWindow();

            // Establece la nueva escena
            Scene scene = new Scene(gameRoot, 800, 1000);
            scene.getStylesheets().add(getClass().getResource("/example/adivina_el_codigo/styles/style.css").toExternalForm());
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
    
    /**
     * Maneja el evento de cerrar el juego.
     *
     * 
     */
    @FXML
    private void closeApp() {
        Platform.exit();
    }
}
