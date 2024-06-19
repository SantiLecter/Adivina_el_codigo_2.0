package example.adivina_el_codigo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameLogic {

    private final String[] colors = {"Red", "Green", "Blue", "Yellow"};
    private List<String> code;
    private int attempts;

    /**
     * Constructor de la lógica del juego.
     * Genera un nuevo código y reinicia el número de intentos.
     */
    public GameLogic() {
        this.code = generateCode();
        this.attempts = 0;
    }

    /**
     * Genera un código aleatorio de 4 colores.
     *
     * @return una lista con el código generado.
     */
    private List<String> generateCode() {
        List<String> generatedCode = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            generatedCode.add(colors[random.nextInt(colors.length)]);
        }
        return generatedCode;
    }

    /**
     * Establece el código a adivinar.
     *
     * @param code la lista con el código a adivinar.
     */
    public void setCodeToGuess(List<String> code) {
        this.code = new ArrayList<>(code);
    }

    /**
     * Obtiene el código generado.
     *
     * @return la lista con el código.
     */
    public List<String> getCode() {
        return code;
    }

    /**
     * Obtiene el número de intentos realizados.
     *
     * @return el número de intentos.
     */
    public int getAttempts() {
        return attempts;
    }

    /**
     * Verifica la suposición del usuario comparándola con el código generado.
     * Incrementa el número de intentos y devuelve un objeto Feedback con la información de la verificación.
     *
     * @param guess la suposición del usuario.
     * @return un objeto Feedback con la cantidad de posiciones correctas y colores correctos en posiciones incorrectas.
     */
    public Feedback checkGuess(List<String> guess) {
        attempts++;

        int correctPosition = 0;
        int correctColor = 0;
        List<String> codeCopy = new ArrayList<>(code);
        List<String> guessCopy = new ArrayList<>(guess);

        // Primera iteracion para verificar posiciones correctas
        for (int i = 0; i < guess.size(); i++) {
            if (guess.get(i).equals(code.get(i))) {
                correctPosition++;
                codeCopy.set(i, null);
                guessCopy.set(i, null);
            }
        }

        // Segunda iteracion para verificar colores correctos en posiciones incorrectas
        for (int i = 0; i < guessCopy.size(); i++) {
            if (guessCopy.get(i) != null && codeCopy.contains(guessCopy.get(i))) {
                correctColor++;
                codeCopy.set(codeCopy.indexOf(guessCopy.get(i)), null);
            }
        }

        return new Feedback(correctPosition, correctColor);
    }

    /**
     * Clase interna para representar la retroalimentación de la verificación de la suposición.
     */
    public static class Feedback {
        private final int correctPosition;
        private final int correctColor;

        /**
         * Constructor del objeto Feedback.
         *
         * @param correctPosition la cantidad de posiciones correctas.
         * @param correctColor la cantidad de colores correctos en posiciones incorrectas.
         */
        public Feedback(int correctPosition, int correctColor) {
            this.correctPosition = correctPosition;
            this.correctColor = correctColor;
        }

        /**
         * Obtiene la cantidad de posiciones correctas.
         *
         * @return la cantidad de posiciones correctas.
         */
        public int getCorrectPosition() {
            return correctPosition;
        }

        /**
         * Obtiene la cantidad de colores correctos en posiciones incorrectas.
         *
         * @return la cantidad de colores correctos en posiciones incorrectas.
         */
        public int getCorrectColor() {
            return correctColor;
        }
    }
}
