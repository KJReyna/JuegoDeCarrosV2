package com.tuproyecto.cagaroad.panels;

import com.tuproyecto.cagaroad.GameFrame;
import com.tuproyecto.cagaroad.GameState;
import com.tuproyecto.cagaroad.gameobjects.ObstacleCar;
import com.tuproyecto.cagaroad.gameobjects.PlayerCar;
import com.tuproyecto.cagaroad.gameobjects.Road;
import com.tuproyecto.cagaroad.utils.GameConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * GamePanel es el JPanel principal donde se dibuja y se ejecuta la lógica del juego.
 * Implementa ActionListener para el Timer del juego y KeyListener para la entrada del teclado.
 */
public class GamePanel extends JPanel implements ActionListener, KeyListener {

    private GameFrame gameFrame;
    private Timer gameTimer;
    private PlayerCar playerCar;
    private Road road;
    private List<ObstacleCar> obstacleCars;
    private Random random;

    private int score;
    private long gameStartTime;
    private long lastScoreTime;
    private int currentLevelLength;
    private int currentProgress;

    private boolean gameStarted = false;
    private int currentLevelNumber;

    private long lastObstacleSpawnTime;

    /**
     * Constructor del GamePanel.
     * @param gameFrame La referencia al GameFrame principal para cambiar de estado.
     */
    public GamePanel(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        setPreferredSize(new Dimension(GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT));
        setBackground(GameConstants.GRASS_COLOR);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        random = new Random();
        road = new Road();
        obstacleCars = new ArrayList<>();

        gameTimer = new Timer(GameConstants.DELAY, this);

        resetGame(1);
    }

    /**
     * Prepara o reinicia el estado del juego para un nivel específico.
     * @param level El número del nivel a iniciar (0 para modo infinito).
     */
    public void resetGame(int level) {
        this.currentLevelNumber = level;
        playerCar = new PlayerCar(gameFrame.getPlayerCarColor());
        obstacleCars.clear();
        score = 0;
        currentProgress = 0;
        gameStarted = false;
        lastObstacleSpawnTime = System.currentTimeMillis();

        if (level == 0) {
            currentLevelLength = GameConstants.LEVEL_LENGTHS[0];
        } else if (level >= 1 && level < GameConstants.LEVEL_LENGTHS.length) {
            currentLevelLength = GameConstants.LEVEL_LENGTHS[level];
        } else {
            System.err.println("Nivel inválido: " + level + ". Usando longitud de Nivel 1.");
            currentLevelLength = GameConstants.LEVEL_LENGTHS[1];
        }
    }

    /**
     * Inicia el bucle de juego si no está ya corriendo. Llamado por GameFrame.
     */
    public void startGame() {
        System.out.println("GamePanel: startGame() llamado.");
        if (!gameTimer.isRunning()) {
            gameTimer.start();
        }
        requestFocusInWindow();
        System.out.println("GamePanel: requestFocusInWindow() llamado en startGame(). GamePanel is focus owner: " + isFocusOwner());
    }

    /**
     * Detiene el bucle de juego. Llamado por GameFrame.
     */
    public void stopGame() {
        gameTimer.stop();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameStarted) {
            repaint();
            return;
        }

        updateGame();
        repaint();
    }

    private void updateGame() {
        road.move(GameConstants.GAME_SPEED_BASE);
        for (ObstacleCar car : obstacleCars) {
            car.move(GameConstants.GAME_SPEED_BASE);
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastObstacleSpawnTime >= GameConstants.OBSTACLE_SPAWN_INTERVAL_MS) {
            generateObstaclesWave();
            lastObstacleSpawnTime = currentTime;
        }

        Iterator<ObstacleCar> iterator = obstacleCars.iterator();
        while (iterator.hasNext()) {
            ObstacleCar car = iterator.next();
            if (car.getY() > GameConstants.GAME_HEIGHT) {
                iterator.remove();
            }
        }

        for (ObstacleCar car : obstacleCars) {
            if (playerCar.checkCollision(car)) {
                gameTimer.stop();
                gameFrame.setGameState(GameState.DEFEAT);
                return;
            }
        }

        if (gameStartTime == 0) {
            gameStartTime = currentTime;
            lastScoreTime = currentTime;
        }
        if (currentTime - lastScoreTime >= GameConstants.SCORE_INTERVAL_MS) {
            score += GameConstants.SCORE_PER_INTERVAL;
            lastScoreTime = currentTime;
        }

        currentProgress += GameConstants.GAME_SPEED_BASE;
        if (currentLevelLength != Integer.MAX_VALUE && currentProgress >= currentLevelLength) {
            gameTimer.stop();
            gameFrame.setGameState(GameState.VICTORY);
        }
    }

    private void generateObstaclesWave() {
        int maxLanesToBlock = GameConstants.NUM_LANES - GameConstants.MIN_LANES_FREE_IN_WAVE;
        if (maxLanesToBlock <= 0) {
            return;
        }
        int lanesToBlock = random.nextInt(maxLanesToBlock) + 1;

        List<Integer> availableLanes = new ArrayList<>();
        for (int i = 0; i < GameConstants.NUM_LANES; i++) {
            availableLanes.add(i);
        }
        Collections.shuffle(availableLanes, random);

        for (int i = 0; i < lanesToBlock; i++) {
            int lane = availableLanes.get(i);
            int startX = (GameConstants.GAME_WIDTH / 6) + (lane * GameConstants.LANE_WIDTH) + (GameConstants.LANE_WIDTH / 2) - (GameConstants.CAR_WIDTH / 2);
            obstacleCars.add(new ObstacleCar(startX, -GameConstants.CAR_HEIGHT, getRandomObstacleColor()));
        }
    }

    /**
     * Devuelve un color aleatorio para los autos enemigos, eligiendo entre colores básicos.
     * @return Un objeto Color.
     */
    private Color getRandomObstacleColor() {
        // Colores que se usarán para los obstáculos geométricos.
        Color[] colors = {
                Color.GREEN,
                Color.BLUE,
                Color.MAGENTA,
                Color.YELLOW,
                Color.CYAN,
                Color.BLACK,
                Color.RED
        };
        return colors[random.nextInt(colors.length)];
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        if (!gameStarted) {
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 72));
            drawCenteredString(g2d, "JUGAR", GameConstants.GAME_HEIGHT / 2 - 50);
            g2d.setFont(new Font("Arial", Font.PLAIN, 24));
            drawCenteredString(g2d, "Presione E para empezar...", GameConstants.GAME_HEIGHT / 2 + 20);
            return;
        }

        road.draw(g2d);
        playerCar.draw(g2d);

        for (ObstacleCar car : obstacleCars) {
            car.draw(g2d);
        }

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        g2d.drawString("Puntaje: " + score, GameConstants.GAME_WIDTH - 200, 30);

        int percentage = 0;
        if (currentLevelLength != Integer.MAX_VALUE) {
            percentage = (int) ((double) currentProgress / currentLevelLength * 100);
            if (percentage > 100) percentage = 100;
            g2d.drawString(percentage + "%", GameConstants.GAME_WIDTH - 200, 60);
        } else {
            g2d.drawString("Modo Infinito", GameConstants.GAME_WIDTH - 200, 60);
        }
    }

    private void drawCenteredString(Graphics2D g2d, String text, int y) {
        FontMetrics metrics = g2d.getFontMetrics();
        int x = (GameConstants.GAME_WIDTH - metrics.stringWidth(text)) / 2;
        g2d.drawString(text, x, y);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("GamePanel: Tecla presionada: " + KeyEvent.getKeyText(e.getKeyCode()) + " (KeyCode: " + e.getKeyCode() + ")");
        if (!gameStarted) {
            if (e.getKeyCode() == KeyEvent.VK_E) {
                System.out.println("GamePanel: Tecla 'E' detectada. Iniciando juego.");
                gameStarted = true;
                gameStartTime = System.currentTimeMillis();
                lastScoreTime = gameStartTime;
                repaint();
            }
        } else {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                playerCar.moveLeft();
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                playerCar.moveRight();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public int getScore() {
        return score;
    }

    public int getCurrentLevelNumber() {
        return currentLevelNumber;
    }
}