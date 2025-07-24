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
import java.util.Collections; // Para Collections.shuffle
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

    // ¡NUEVO! Para controlar la generación de oleadas de obstáculos
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
        setFocusTraversalKeysEnabled(false); // Deshabilita las teclas de navegación de foco

        random = new Random();
        road = new Road();
        obstacleCars = new ArrayList<>();

        gameTimer = new Timer(GameConstants.DELAY, this);

        resetGame(1); // Inicializa con el Nivel 1
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
        lastObstacleSpawnTime = System.currentTimeMillis(); // Reinicia el tiempo de generación de obstáculos

        // ¡MODIFICADO! Asignación de longitud del nivel usando las constantes
        if (level == 0) { // Modo infinito
            currentLevelLength = GameConstants.LEVEL_LENGTHS[0]; // Integer.MAX_VALUE
        } else if (level >= 1 && level < GameConstants.LEVEL_LENGTHS.length) {
            currentLevelLength = GameConstants.LEVEL_LENGTHS[level];
        } else {
            // Nivel inválido, por seguridad se usa la longitud del Nivel 1
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

    /**
     * Este método se llama en cada "tick" del Timer del juego.
     * Contiene la lógica principal de actualización y redibujado.
     * @param e El evento de acción del Timer.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameStarted) {
            repaint();
            return;
        }

        updateGame();
        repaint();
    }

    /**
     * Contiene toda la lógica de actualización del estado del juego:
     * movimiento, generación de obstáculos, colisiones, puntaje y progreso.
     */
    private void updateGame() {
        // Mueve el camino y los obstáculos
        road.move(GameConstants.GAME_SPEED_BASE);
        for (ObstacleCar car : obstacleCars) {
            car.move(GameConstants.GAME_SPEED_BASE);
        }

        // ¡MODIFICADO! Llama a la nueva lógica de generación de oleadas
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastObstacleSpawnTime >= GameConstants.OBSTACLE_SPAWN_INTERVAL_MS) {
            generateObstaclesWave(); // Genera una nueva oleada de autos
            lastObstacleSpawnTime = currentTime;
        }

        // Elimina los obstáculos que han salido de la pantalla
        Iterator<ObstacleCar> iterator = obstacleCars.iterator();
        while (iterator.hasNext()) {
            ObstacleCar car = iterator.next();
            if (car.getY() > GameConstants.GAME_HEIGHT) {
                iterator.remove();
            }
        }

        // Detección de colisiones
        for (ObstacleCar car : obstacleCars) {
            if (playerCar.checkCollision(car)) {
                gameTimer.stop();
                gameFrame.setGameState(GameState.DEFEAT);
                return;
            }
        }

        // Actualiza el puntaje
        if (gameStartTime == 0) {
            gameStartTime = currentTime;
            lastScoreTime = currentTime;
        }
        if (currentTime - lastScoreTime >= GameConstants.SCORE_INTERVAL_MS) {
            score += GameConstants.SCORE_PER_INTERVAL;
            lastScoreTime = currentTime;
        }

        // Actualiza el progreso y verifica la victoria
        currentProgress += GameConstants.GAME_SPEED_BASE;
        if (currentLevelLength != Integer.MAX_VALUE && currentProgress >= currentLevelLength) {
            gameTimer.stop();
            gameFrame.setGameState(GameState.VICTORY);
        }
    }

    /**
     * ¡NUEVO! Genera una "oleada" de obstáculos en el camino.
     * Asegura que siempre haya al menos un carril libre para pasar.
     */
    private void generateObstaclesWave() {
        // Determina cuántos carriles se bloquearán en esta oleada.
        // Se bloquearán entre 1 y (NUM_LANES - MIN_LANES_FREE_IN_WAVE) carriles.
        // Ej: Si NUM_LANES=3 y MIN_LANES_FREE_IN_WAVE=1, bloqueará entre 1 y 2 carriles.
        int maxLanesToBlock = GameConstants.NUM_LANES - GameConstants.MIN_LANES_FREE_IN_WAVE;
        if (maxLanesToBlock <= 0) { // Si solo hay 1 carril, o la lógica es mala, no bloquear nada
            return;
        }
        int lanesToBlock = random.nextInt(maxLanesToBlock) + 1; // Genera entre 1 y `maxLanesToBlock` autos

        // Crea una lista de todos los carriles disponibles y la mezcla
        List<Integer> availableLanes = new ArrayList<>();
        for (int i = 0; i < GameConstants.NUM_LANES; i++) {
            availableLanes.add(i);
        }
        Collections.shuffle(availableLanes, random); // Mezcla aleatoriamente los carriles

        // Bloquea los primeros 'lanesToBlock' carriles de la lista mezclada
        for (int i = 0; i < lanesToBlock; i++) {
            int lane = availableLanes.get(i); // Obtiene un carril aleatorio y único
            // Calcula la posición X para el auto en el carril elegido
            int startX = (GameConstants.GAME_WIDTH / 6) + (lane * GameConstants.LANE_WIDTH) + (GameConstants.LANE_WIDTH / 2) - (GameConstants.CAR_WIDTH / 2);
            // Añade el nuevo auto obstáculo justo por encima de la pantalla
            obstacleCars.add(new ObstacleCar(startX, -GameConstants.CAR_HEIGHT, getRandomObstacleColor()));
        }
    }

    /**
     * Devuelve un color aleatorio para los autos enemigos.
     * @return Un objeto Color.
     */
    private Color getRandomObstacleColor() {
        Color[] colors = {Color.GREEN, Color.BLUE, Color.MAGENTA, Color.ORANGE, Color.CYAN, Color.WHITE};
        return colors[random.nextInt(colors.length)];
    }

    /**
     * Este método se encarga de dibujar todos los elementos gráficos del juego.
     * @param g El objeto Graphics usado para dibujar.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        if (!gameStarted) {
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 72));
            drawCenteredString(g2d, "JUGAR", GameConstants.GAME_HEIGHT / 2 - 50);
            g2d.setFont(new Font("Arial", Font.PLAIN, 24));
            // Texto actualizado para la tecla 'E'
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

    /**
     * Método de utilidad para dibujar una cadena de texto centrada horizontalmente.
     * @param g2d El objeto Graphics2D para dibujar.
     * @param text El texto a dibujar.
     * @param y La coordenada Y para el texto.
     */
    private void drawCenteredString(Graphics2D g2d, String text, int y) {
        FontMetrics metrics = g2d.getFontMetrics();
        int x = (GameConstants.GAME_WIDTH - metrics.stringWidth(text)) / 2;
        g2d.drawString(text, x, y);
    }

    // --- Métodos de la interfaz KeyListener ---

    /**
     * Este método se llama cuando se presiona una tecla.
     * Contiene la lógica para el movimiento del jugador y el inicio del juego.
     * @param e El evento de teclado.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("GamePanel: Tecla presionada: " + KeyEvent.getKeyText(e.getKeyCode()) + " (KeyCode: " + e.getKeyCode() + ")"); // Debugging
        if (!gameStarted) {
            // Verifica KeyEvent.VK_E para iniciar el juego
            if (e.getKeyCode() == KeyEvent.VK_E) {
                System.out.println("GamePanel: Tecla 'E' detectada. Iniciando juego.");
                gameStarted = true;
                gameStartTime = System.currentTimeMillis();
                lastScoreTime = gameStartTime;
                repaint(); // Forzar un repaint para que la pantalla de juego real aparezca inmediatamente
            }
        } else {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                playerCar.moveLeft();
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                playerCar.moveRight();
            }
        }
    }

    /**
     * Este método se llama cuando se suelta una tecla.
     * No es esencial para este tipo de juego, así que se deja vacío.
     * @param e El evento de teclado.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        // No se necesita lógica aquí.
    }

    /**
     * Este método se llama cuando se escribe (pulsa y suelta) una tecla.
     * Generalmente no se usa para control de juegos en tiempo real, se deja vacío.
     * @param e El evento de teclado.
     */
    @Override
    public void keyTyped(KeyEvent e) {
        // No se necesita lógica aquí.
    }

    // --- Getters para acceder al estado del juego desde otras clases ---

    /**
     * Obtiene el puntaje actual del juego.
     * @return El puntaje actual.
     */
    public int getScore() {
        return score;
    }

    /**
     * Obtiene el número del nivel que se está jugando actualmente.
     * Útil para que la pantalla de derrota sepa qué nivel reintentar.
     * @return El número del nivel.
     */
    public int getCurrentLevelNumber() {
        return currentLevelNumber;
    }
}