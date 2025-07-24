package com.tuproyecto.cagaroad;

import com.tuproyecto.cagaroad.panels.*;
import com.tuproyecto.cagaroad.utils.GameConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent; // Asegúrate de que esto esté importado
import java.util.Set; // Asegúrate de que esto esté importado

/**
 * GameFrame es la ventana principal (JFrame) de la aplicación CAGAROAD.
 * Utiliza CardLayout para cambiar entre las diferentes pantallas (paneles) del juego.
 * Gestiona el estado general del juego y la interacción entre los paneles.
 */
public class GameFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    private SplashPanel splashPanel;
    private MainMenuPanel mainMenuPanel;
    private LevelSelectPanel levelSelectPanel;
    private CustomizePanel customizePanel;
    private GamePanel gamePanel;
    private VictoryPanel victoryPanel;
    private DefeatPanel defeatPanel;

    private GameState currentGameState;

    /**
     * Constructor del GameFrame. Configura la ventana y los paneles iniciales.
     */
    public GameFrame() {
        setTitle("CAGAROAD - El Juego");
        setSize(GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        // --- CORRECCIONES CLAVE PARA EL FOCO EN EL JFrame ---
        setFocusable(true); // Hace que el JFrame sea capaz de recibir el foco
        // Deshabilita las teclas de navegación de foco predeterminadas de Swing (ej. TAB, SHIFT, ESPACIO).
        // Esto es CRUCIAL para que los juegos capturen eventos de teclado directamente.
        setFocusTraversalKeysEnabled(false);
        // -----------------------------------------------------

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        add(mainPanel);

        splashPanel = new SplashPanel(this);
        mainMenuPanel = new MainMenuPanel(this);
        levelSelectPanel = new LevelSelectPanel(this);
        customizePanel = new CustomizePanel(this);
        gamePanel = new GamePanel(this); // GamePanel ya implementa KeyListener
        victoryPanel = new VictoryPanel(this);
        defeatPanel = new DefeatPanel(this);

        mainPanel.add(splashPanel, GameState.SPLASH.name());
        mainPanel.add(mainMenuPanel, GameState.MENU.name());
        mainPanel.add(levelSelectPanel, GameState.LEVEL_SELECT.name());
        mainPanel.add(customizePanel, GameState.CUSTOMIZE.name());
        mainPanel.add(gamePanel, GameState.PLAYING.name());
        mainPanel.add(victoryPanel, GameState.VICTORY.name());
        mainPanel.add(defeatPanel, GameState.DEFEAT.name());

        setGameState(GameState.SPLASH);
    }

    /**
     * Cambia el estado actual del juego y muestra el panel correspondiente.
     * Gestiona la adición/eliminación del KeyListener y la solicitud de foco para el GamePanel.
     * @param newState El nuevo estado del juego al que cambiar.
     */
    public void setGameState(GameState newState) {
        System.out.println("GameFrame: Cambiando estado a: " + newState.name());
        this.currentGameState = newState;

        // Siempre remueve el KeyListener de GamePanel antes de cualquier cambio.
        // Esto previene duplicados o listeners activos en paneles incorrectos.
        // Si el GamePanel no es un KeyListener para este JFrame (porque no ha sido añadido o ya se removió),
        // este método simplemente no hace nada, lo cual es seguro.
        removeKeyListener(gamePanel); // Siempre remueve el listener

        // Muestra el panel asociado al nuevo estado utilizando CardLayout
        cardLayout.show(mainPanel, newState.name());

        // Manejo especial para el estado PLAYING (el juego en sí)
        if (newState == GameState.PLAYING) {
            // 1. Añade el GamePanel como KeyListener a este JFrame.
            addKeyListener(gamePanel);

            // 2. Inicia el bucle de juego del GamePanel.
            gamePanel.startGame();

            // 3. ¡CORRECCIONES CLAVE PARA ASEGURAR EL FOCO!
            // Primero, solicita el foco para el JFrame completo.
            // Esto asegura que la ventana principal esté activa y lista para recibir eventos.
            requestFocus();

            // Luego, usa invokeLater para solicitar el foco en el GamePanel.
            // Esto garantiza que la solicitud se procese después de que Swing haya
            // terminado de organizar y dibujar el GamePanel, lo que lo hace más fiable.
            SwingUtilities.invokeLater(() -> {
                gamePanel.requestFocusInWindow();
                // Mensajes de depuración para verificar el foco
                System.out.println("GameFrame: requestFocusInWindow() invocado para GamePanel. GamePanel is focus owner: " + gamePanel.isFocusOwner());
                System.out.println("GameFrame: Current focus owner is: " + KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner());
            });
        } else {
            // Si salimos del estado PLAYING, detiene el bucle de juego del GamePanel.
            gamePanel.stopGame();
            // El KeyListener ya se removió al inicio de este método.
        }

        // Otros manejos específicos para cada estado:
        if (newState == GameState.LEVEL_SELECT) {
            levelSelectPanel.resetSelection();
        } else if (newState == GameState.VICTORY) {
            victoryPanel.setFinalScore(gamePanel.getScore());
        }
    }

    public GameState getCurrentGameState() {
        return currentGameState;
    }

    public Color getPlayerCarColor() {
        return customizePanel.getSelectedColor();
    }

    public void restartGame(int level) {
        gamePanel.resetGame(level);
        setGameState(GameState.PLAYING);
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }
}