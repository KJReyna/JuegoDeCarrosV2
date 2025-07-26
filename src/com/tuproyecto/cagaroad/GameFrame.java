package com.tuproyecto.cagaroad;

import com.tuproyecto.cagaroad.panels.*;
import com.tuproyecto.cagaroad.utils.GameConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Set;

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
    private DefeatPanel defeatPanel; // Referencia a DefeatPanel

    private GameState currentGameState;

    /**
     * Constructor del GameFrame. Configura la ventana y los paneles iniciales.
     */
    public GameFrame() {
        setTitle("CAGAROAD - Director´s cut");
        setSize(GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null); //(esto solo hace que aparezca en el centro de la pantalla el juego

        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        add(mainPanel);

        splashPanel = new SplashPanel(this);
        mainMenuPanel = new MainMenuPanel(this);
        levelSelectPanel = new LevelSelectPanel(this);
        customizePanel = new CustomizePanel(this);
        gamePanel = new GamePanel(this);
        victoryPanel = new VictoryPanel(this);
        defeatPanel = new DefeatPanel(this); // Inicializa DefeatPanel

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

        removeKeyListener(gamePanel);

        cardLayout.show(mainPanel, newState.name());

        if (newState == GameState.PLAYING) {
            addKeyListener(gamePanel);
            gamePanel.startGame();
            requestFocus();

            SwingUtilities.invokeLater(() -> {
                gamePanel.requestFocusInWindow();
                System.out.println("GameFrame: requestFocusInWindow() invocado para GamePanel. GamePanel is focus owner: " + gamePanel.isFocusOwner());
                System.out.println("GameFrame: Current focus owner is: " + KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner());
            });
        } else {
            gamePanel.stopGame();
        }

        if (newState == GameState.LEVEL_SELECT) {
            levelSelectPanel.resetSelection();
        } else if (newState == GameState.VICTORY) {
            victoryPanel.setFinalScore(gamePanel.getScore());
        } else if (newState == GameState.DEFEAT) { // ¡NUEVO! Pasa el puntaje a DefeatPanel
            defeatPanel.setFinalScore(gamePanel.getScore());
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