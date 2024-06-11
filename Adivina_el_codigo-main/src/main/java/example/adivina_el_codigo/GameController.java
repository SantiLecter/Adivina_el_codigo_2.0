package example.adivina_el_codigo;

import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class GameController {

    @FXML
    private Label gameStatusLabel;

    @FXML
    private Circle selectedColor1;

    @FXML
    private Circle selectedColor2;

    @FXML
    private Circle selectedColor3;

    @FXML
    private Circle selectedColor4;

    @FXML
    private Label feedbackLabel;

    @FXML
    private Circle colorRed;

    @FXML
    private Circle colorGreen;

    @FXML
    private Circle colorBlue;

    @FXML
    private Circle colorYellow;

    @FXML
    private GridPane attemptsGrid;

    @FXML
    protected GridPane feedbackGrid;

    @FXML
    private Label attemptsLeftLabel;

    private GameLogic gameLogic;
    private List<String> currentGuess;
    private int currentSelectionIndex;

    /**
     * Inicializa el controlador del juego.
     */
    public void initialize() {
        gameLogic = new GameLogic();
        gameStatusLabel.setText("Bienvenido! Empieza a adivinar el codigo.");
        currentGuess = new ArrayList<>();
        currentSelectionIndex = 0;

        // Inicializa la cuadrícula de intentos con círculos vacíos más grandes
        initializeAttemptsGrid();

        // Inicializa la cuadrícula de retroalimentación con rectángulos vacíos
        initializeFeedbackGrid();

        // Establece los intentos restantes iniciales
        attemptsLeftLabel.setText("Intentos restantes: 10");
    }

    /**
     * Inicializa la cuadrícula de intentos con círculos vacíos.
     */
    private void initializeAttemptsGrid() {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 4; col++) {
                Circle circle = new Circle(15);
                circle.setFill(Color.LIGHTGREY);
                circle.setStroke(Color.BLACK);
                circle.setStrokeWidth(1.5);
                circle.setEffect(new DropShadow(3.75, 2.25, 2.25, Color.rgb(0, 0, 0, 0.5)));
                attemptsGrid.add(circle, col, row);
            }
        }
    }

    /**
     * Inicializa la cuadrícula de retroalimentación con rectángulos vacíos.
     */
    private void initializeFeedbackGrid() {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 2; col++) {
                Rectangle rectangle = new Rectangle(27.9, 27.9); // Tamaño de los cuadrados de retroalimentación, 7% más
                                                                 // pequeños que los círculos
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(1.5);
                rectangle.setEffect(new DropShadow(3.75, 2.25, 2.25, Color.rgb(0, 0, 0, 0.5)));
                feedbackGrid.add(rectangle, col, row);
            }
        }
    }

    /**
     * Maneja el evento cuando se selecciona un color.
     *
     * @param event el evento del mouse
     */
    @FXML
    private void selectColor(MouseEvent event) {
        Circle selectedCircle = (Circle) event.getSource();
        String color = null;

        if (selectedCircle == colorRed) {
            color = "Red";
        } else if (selectedCircle == colorGreen) {
            color = "Green";
        } else if (selectedCircle == colorBlue) {
            color = "Blue";
        } else if (selectedCircle == colorYellow) {
            color = "Yellow";
        }

        if (color != null && currentSelectionIndex < 4) {
            currentGuess.add(color);
            updateSelectedColors();
            currentSelectionIndex++;
        }
    }

    /**
     * Actualiza los círculos de colores seleccionados en función de la suposición
     * actual.
     */
    private void updateSelectedColors() {
        if (currentGuess.size() > 0) {
            selectedColor1.setFill(getColor(currentGuess.get(0)));
        }
        if (currentGuess.size() > 1) {
            selectedColor2.setFill(getColor(currentGuess.get(1)));
        }
        if (currentGuess.size() > 2) {
            selectedColor3.setFill(getColor(currentGuess.get(2)));
        }
        if (currentGuess.size() > 3) {
            selectedColor4.setFill(getColor(currentGuess.get(3)));
        }
    }

    /**
     * Devuelve el objeto Color correspondiente para un nombre de color dado.
     *
     * @param color el nombre del color
     * @return el objeto Color
     */
    private Color getColor(String color) {
        switch (color) {
            case "Red":
                return Color.RED;
            case "Green":
                return Color.GREEN;
            case "Blue":
                return Color.BLUE;
            case "Yellow":
                return Color.YELLOW;
            default:
                return Color.LIGHTGREY;
        }
    }

    /**
     * Maneja el evento cuando se hace clic en el botón "Enviar suposición".
     */
    @FXML
    private void submitGuess() {
        if (currentGuess.size() < 4) {
            feedbackLabel.setText("Por favor seleccione 4 colores.");
            return;
        }

        GameLogic.Feedback feedback = gameLogic.checkGuess(currentGuess);

        // Añade la suposición a la cuadrícula de intentos
        addGuessToGrid(currentGuess, gameLogic.getAttempts() - 1);

        // Añade la retroalimentación a la cuadrícula de retroalimentación
        addFeedbackToGrid(feedback, gameLogic.getAttempts() - 1);

        // Actualiza los intentos restantes
        int attemptsLeft = 10 - gameLogic.getAttempts();
        attemptsLeftLabel.setText("Intentos restantes: " + attemptsLeft);

        if (feedback.getCorrectPosition() == 4) {
            gameStatusLabel.setText("Felicitaciones! Adivinaste el codigo!");
        } else if (gameLogic.getAttempts() >= 10) {
            gameStatusLabel.setText("Perdiste! No te quedan mas intentos.");
        }

        // Reinicia para la siguiente suposición
        clearSelection();
    }

    /**
     * Añade la suposición del usuario a la cuadrícula de intentos.
     *
     * @param guess    la suposición del usuario
     * @param rowIndex el índice de la fila en la cuadrícula
     */
    private void addGuessToGrid(List<String> guess, int rowIndex) {
        for (int i = 0; i < guess.size(); i++) {
            Circle circle = (Circle) getNodeByRowColumnIndex(rowIndex, i, attemptsGrid);
            if (circle != null) {
                circle.setFill(getColor(guess.get(i)));
            }
        }
    }

    /**
     * Añade la retroalimentación a la cuadrícula de retroalimentación.
     *
     * @param feedback la retroalimentación de la lógica del juego
     * @param rowIndex el índice de la fila en la cuadrícula
     */
    private void addFeedbackToGrid(GameLogic.Feedback feedback, int rowIndex) {
        int correctPositions = feedback.getCorrectPosition();
        int correctColors = feedback.getCorrectColor();

        // Primera columna para posiciones correctas (verde claro)
        Rectangle correctPositionRect = (Rectangle) getNodeByRowColumnIndex(rowIndex, 0, feedbackGrid);
        if (correctPositionRect != null) {
            correctPositionRect.setFill(Color.LIGHTGREEN);
            Label correctPositionLabel = new Label(String.valueOf(correctPositions));
            correctPositionLabel.setFont(new Font(correctPositionLabel.getFont().getSize() * 1.15)); // Aumenta el
                                                                                                     // tamaño de la
                                                                                                     // fuente en un 15%
            correctPositionLabel.setTranslateX(5); // Mueve la etiqueta ligeramente a la derecha
            feedbackGrid.add(correctPositionLabel, 0, rowIndex);
        }

        // Segunda columna para colores correctos (naranja)
        Rectangle correctColorRect = (Rectangle) getNodeByRowColumnIndex(rowIndex, 1, feedbackGrid);
        if (correctColorRect != null) {
            correctColorRect.setFill(Color.ORANGE);
            Label correctColorLabel = new Label(String.valueOf(correctColors));
            correctColorLabel.setFont(new Font(correctColorLabel.getFont().getSize() * 1.15)); // Aumenta el tamaño de
                                                                                               // la fuente en un 15%
            correctColorLabel.setTranslateX(5); // Mueve la etiqueta ligeramente a la derecha
            feedbackGrid.add(correctColorLabel, 1, rowIndex);
        }
    }

    /**
     * Obtiene el nodo por el índice de fila y columna de la cuadrícula.
     *
     * @param row      el índice de la fila
     * @param column   el índice de la columna
     * @param gridPane la cuadrícula
     * @return el nodo en el índice de fila y columna especificado, o null si no se
     *         encuentra ningún nodo en esa posición
     */
    private javafx.scene.Node getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane) {
        for (javafx.scene.Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                return node;
            }
        }
        return null;
    }

    /**
     * Limpia la selección actual.
     */
    @FXML
    private void clearSelection() {
        currentGuess.clear();
        currentSelectionIndex = 0;
        selectedColor1.setFill(Color.LIGHTGREY);
        selectedColor2.setFill(Color.LIGHTGREY);
        selectedColor3.setFill(Color.LIGHTGREY);
        selectedColor4.setFill(Color.LIGHTGREY);
        feedbackLabel.setText("");
    }

    // Setter method for feedbackGrid
    public void setFeedbackGrid(GridPane feedbackGrid) {
        this.feedbackGrid = feedbackGrid;
    }

    public void setAttemptsGrid(GridPane attemptsGrid) {
        this.attemptsGrid = attemptsGrid;
    }

    public void setAttemptsLeftLabel(Label attemptsLeftLabel) {
        this.attemptsLeftLabel = attemptsLeftLabel;
    }

    public void setSelectedColor1(Circle selectedColor1) {
        this.selectedColor1 = selectedColor1;
    }

    public void setSelectedColor2(Circle selectedColor2) {
        this.selectedColor2 = selectedColor2;
    }

    public void setSelectedColor3(Circle selectedColor3) {
        this.selectedColor3 = selectedColor3;
    }

    public void setSelectedColor4(Circle selectedColor4) {
        this.selectedColor4 = selectedColor4;
    }

    public void setColorRed(Circle colorRed) {
        this.colorRed = colorRed;
    }

    public void setColorGreen(Circle colorGreen) {
        this.colorGreen = colorGreen;
    }

    public void setColorBlue(Circle colorBlue) {
        this.colorBlue = colorBlue;
    }

    public void setColorYellow(Circle colorYellow) {
        this.colorYellow = colorYellow;
    }

    public void setFeedbackLabel(Label feedbackLabel) {
        this.feedbackLabel = feedbackLabel;
    }

    public void setGameStatusLabel(Label gameStatusLabel) {
        this.gameStatusLabel = gameStatusLabel;
    }

}
