import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import static org.junit.jupiter.api.Assertions.*;
import example.adivina_el_codigo.GameLogic;
import example.adivina_el_codigo.GameController;

import java.util.List;
import java.util.Arrays;

public class GameControllerTest extends ApplicationTest {

    private GameController gameController;

    @BeforeEach
    public void setUp() throws Exception {
        gameController = new GameController();
        gameController.feedbackGrid = new GridPane();
        gameController.attemptsGrid = new GridPane();
        gameController.attemptsLeftLabel = new Label();
        gameController.selectedColor1 = new Circle();
        gameController.selectedColor2 = new Circle();
        gameController.selectedColor3 = new Circle();
        gameController.selectedColor4 = new Circle();
        gameController.colorRed = new Circle();
        gameController.colorGreen = new Circle();
        gameController.colorBlue = new Circle();
        gameController.colorYellow = new Circle();
        gameController.feedbackLabel = new Label();
        gameController.gameStatusLabel = new Label();
        gameController.initialize();
    }

    @Test
    public void testGenerateCode() {
        GameLogic gameLogic = new GameLogic();
        List<String> code = gameLogic.getCode();

        // Se evalúa que el código no sea nulo y tenga la longitud correcta (4 en este caso)
        assertEquals(4, code.size());

        // Evaluamos que cada elemento en el código tenga un color válido en la lista de colores
        for (String color : code) {
            assertTrue(Arrays.asList(gameLogic.colors).contains(color));
        }
    }


    @Test
    public void testInitializeFeedbackGrid() {
        // Verifica que la cuadrícula tenga 10 filas y 2 columnas por fila
        int rowCount = 10;
        int colCount = 2;

        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < colCount; col++) {
                Rectangle rectangle = (Rectangle) getNodeByRowColumnIndex(row, col, gameController.feedbackGrid);
                assertNotNull(rectangle, "El rectángulo no debe ser nulo");
                assertEquals(Color.TRANSPARENT, rectangle.getFill(), "El rectángulo debe estar vacío");
            }
        }
    }

    @Test
    public void testInitializeAttemptsLeftLabel() {
        // Verifica que la etiqueta de intentos restantes se inicialice correctamente
        assertEquals("Intentos restantes: 10", gameController.attemptsLeftLabel.getText());
    }

    @Test
    public void testSelectColorUpdatesSelectedCircle() {
        // Simula la selección de un color
        gameController.selectColor(createMouseEvent(gameController.colorRed));
        assertEquals(Color.RED, gameController.selectedColor1.getFill());

        gameController.selectColor(createMouseEvent(gameController.colorGreen));
        assertEquals(Color.GREEN, gameController.selectedColor2.getFill());

        gameController.selectColor(createMouseEvent(gameController.colorBlue));
        assertEquals(Color.BLUE, gameController.selectedColor3.getFill());

        gameController.selectColor(createMouseEvent(gameController.colorYellow));
        assertEquals(Color.YELLOW, gameController.selectedColor4.getFill());
    }

    @Test
    public void testSelectFourthColorDisablesFurtherSelection() {
        // Simula la selección de cuatro colores
        gameController.selectColor(createMouseEvent(gameController.colorRed));
        gameController.selectColor(createMouseEvent(gameController.colorGreen));
        gameController.selectColor(createMouseEvent(gameController.colorBlue));
        gameController.selectColor(createMouseEvent(gameController.colorYellow));

        // Verifica que los cuatro colores se hayan seleccionado
        assertEquals(Color.RED, gameController.selectedColor1.getFill());
        assertEquals(Color.GREEN, gameController.selectedColor2.getFill());
        assertEquals(Color.BLUE, gameController.selectedColor3.getFill());
        assertEquals(Color.YELLOW, gameController.selectedColor4.getFill());

        // Intenta seleccionar un quinto color
        gameController.selectColor(createMouseEvent(gameController.colorRed));

        // Verifica que no se haya añadido un quinto color
        assertEquals(4, gameController.currentGuess.size());
    }

    @Test
    public void testSubmitGuessShowsErrorMessageIfLessThanFourColors() {
        // Selecciona menos de 4 colores
        gameController.selectColor(createMouseEvent(gameController.colorRed));
        gameController.selectColor(createMouseEvent(gameController.colorGreen));

        // Envía la suposición
        gameController.submitGuess();

        // Verifica que se muestre el mensaje de error
        assertEquals("Por favor seleccione 4 colores.", gameController.feedbackLabel.getText());
    }

    @Test
    public void testSubmitGuessAddsGuessToGrid() {
        // Selecciona 4 colores
        List<String> colors = Arrays.asList("Red", "Green", "Blue", "Yellow");
        for (String color : colors) {
            gameController.selectColor(createMouseEvent(getCircleByColor(color)));
        }

        // Envía la suposición
        gameController.submitGuess();

        // Verifica que la suposición se haya añadido a la cuadrícula de intentos
        for (int i = 0; i < colors.size(); i++) {
            Circle circle = (Circle) getNodeByRowColumnIndex(0, i, gameController.attemptsGrid);
            assertEquals(gameController.getColor(colors.get(i)), circle.getFill());
        }
    }

    @Test
    public void testSubmitGuessAddsFeedbackToGrid() {
        // Configura una suposición correcta
        gameController.gameLogic = new GameLogic() {
            @Override
            public Feedback checkGuess(List<String> guess) {
                return new Feedback(4, 0);
            }
        };

        // Selecciona 4 colores
        List<String> colors = Arrays.asList("Red", "Green", "Blue", "Yellow");
        for (String color : colors) {
            gameController.selectColor(createMouseEvent(getCircleByColor(color)));
        }

        // Envía la suposición
        gameController.submitGuess();

        // Verifica que la retroalimentación se haya añadido a la cuadrícula de retroalimentación
        Rectangle rect = (Rectangle) getNodeByRowColumnIndex(0, 0, gameController.feedbackGrid);
        assertEquals(Color.LIGHTGREEN, rect.getFill());
    }

    @Test
    public void testSubmitGuessWhenOutOfAttemptsShowsFailureMessage() {
        // Configura la lógica del juego para simular que se han agotado los intentos
        gameController.gameLogic = new GameLogic() {
            private int attempts = 10;

            @Override
            public int getAttempts() {
                return attempts;
            }

            @Override
            public Feedback checkGuess(List<String> guess) {
                return new Feedback(0, 0);
            }
        };

        // Selecciona 4 colores
        List<String> colors = Arrays.asList("Red", "Green", "Blue", "Yellow");
        for (String color : colors) {
            gameController.selectColor(createMouseEvent(getCircleByColor(color)));
        }

        // Envía la suposición
        gameController.submitGuess();

        // Verifica que se muestre el mensaje de derrota
        assertEquals("Perdiste! No te quedan mas intentos.", gameController.gameStatusLabel.getText());
    }

    @Test
    public void testClearSelectionAfterSubmitGuess() {
        // Selecciona 4 colores
        List<String> colors = Arrays.asList("Red", "Green", "Blue", "Yellow");
        for (String color : colors) {
            gameController.selectColor(createMouseEvent(getCircleByColor(color)));
        }

        // Envía la suposición
        gameController.submitGuess();

        // Verifica que la selección actual se haya borrado
        assertEquals(Color.LIGHTGREY, gameController.selectedColor1.getFill());
        assertEquals(Color.LIGHTGREY, gameController.selectedColor2.getFill());
        assertEquals(Color.LIGHTGREY, gameController.selectedColor3.getFill());
        assertEquals(Color.LIGHTGREY, gameController.selectedColor4.getFill());

        // Verifica que el mensaje de retroalimentación se haya limpiado
        assertEquals("", gameController.feedbackLabel.getText());
    }

    @Test
    public void testCorrectPositionIncrements() {
        // Configura la lógica del juego para una suposición específica
        gameController.gameLogic = new GameLogic() {
            @Override
            public Feedback checkGuess(List<String> guess) {
                return new Feedback(1, 0);  // Simula 1 posición correcta
            }
        };

        // Selecciona los colores
        List<String> colors = Arrays.asList("Red", "Green", "Blue", "Yellow");
        for (String color : colors) {
            gameController.selectColor(createMouseEvent(getCircleByColor(color)));
        }

        // Envía la suposición
        gameController.submitGuess();

        // Verifica la retroalimentación
        Rectangle rect = (Rectangle) getNodeByRowColumnIndex(0, 1, gameController.feedbackGrid);
        assertEquals(Color.ORANGE, rect.getFill());
    }

    @Test
    public void testCorrectColorIncrements() {
        // Configura la lógica del juego para una suposición específica
        gameController.gameLogic = new GameLogic() {
            @Override
            public Feedback checkGuess(List<String> guess) {
                return new Feedback(0, 1);  // Simula 1 color correcto
            }
        };

        @Override
        public void start(Stage stage) throws Exception {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("example.adivina_el_codigo"));
            Parent root = loader.load();
            controller = loader.getController();
            stage.setScene(new Scene(root));
            stage.show();
        }

        @BeforeEach
        public void setup() {
            // La configuración de JavaFX se maneja en el método start
        }

        @Test
        public void testShowErrorMessageIfLessThanFourColorsSelected() {
            // Hacer clic en el botón "Enviar intento" sin seleccionar 4 colores
            FxRobot robot = new FxRobot();
            robot.clickOn("#submitGuessButton");

            // Verificar que el mensaje de error se muestra
            Label feedbackLabel = lookup("#feedbackLabel").queryAs(Label.class);
            assertEquals("Por favor seleccione 4 colores.", feedbackLabel.getText());
        }

        // Selecciona los colores
        List<String> colors = Arrays.asList("Yellow", "Green", "Blue", "Red");
        for (String color : colors) {
            gameController.selectColor(createMouseEvent(getCircleByColor(color)));
        }

        // Envía la suposición
        gameController.submitGuess();

        // Verifica la retroalimentación
        Rectangle rect = (Rectangle) getNodeByRowColumnIndex(0, 1, gameController.feedbackGrid);
        assertEquals(Color.ORANGE, rect.getFill());
    }
}