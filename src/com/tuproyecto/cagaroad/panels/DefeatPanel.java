// com.tuproyecto.cagaroad.panels.DefeatPanel.java
package com.tuproyecto.cagaroad.panels;

import com.tuproyecto.cagaroad.GameFrame;
import com.tuproyecto.cagaroad.GameState;
import com.tuproyecto.cagaroad.gameobjects.ObstacleCar; // Para dibujar la colisión
import com.tuproyecto.cagaroad.gameobjects.PlayerCar; // Para dibujar el auto del jugador
import com.tuproyecto.cagaroad.utils.GameConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DefeatPanel extends JPanel {

    private GameFrame gameFrame;
    private JLabel defeatLabel;
    private JLabel retryLabel;
    private JLabel backToMenuLabel;

    public DefeatPanel(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        setPreferredSize(new Dimension(GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT));
        setBackground(new Color(150, 50, 50)); // Un rojo que sugiere derrota
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 10, 0);

        // Mensaje de derrota
        defeatLabel = new JLabel("Perdiste, te has CAGADO"); // Tu texto original
        defeatLabel.setFont(new Font("Arial", Font.BOLD, 48));
        defeatLabel.setForeground(Color.WHITE);
        gbc.insets = new Insets(0, 0, 50, 0);
        add(defeatLabel, gbc);

        // Opción: Reintentar
        retryLabel = createOptionLabel("Reintentar");
        add(retryLabel, gbc);

        // Opción: Volver al menú
        backToMenuLabel = createOptionLabel("Volver al Menu");
        add(backToMenuLabel, gbc);

        // Listener para reintentar
        retryLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Para reintentar, necesitamos saber el último nivel jugado.
                // Podríamos pasar este valor a GamePanel a través del GameFrame,
                // o GamePanel podría tener un getter para el último nivel intentado.
                // Aquí asumimos que GamePanel tiene un getter para currentLevelNumber.
                gameFrame.restartGame(gameFrame.getGamePanel().getCurrentLevelNumber());
            }
            @Override
            public void mouseEntered(MouseEvent e) { retryLabel.setForeground(Color.CYAN); }
            @Override
            public void mouseExited(MouseEvent e) { retryLabel.setForeground(Color.WHITE); }
        });

        // Listener para volver al menú
        backToMenuLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                gameFrame.setGameState(GameState.MENU);
            }
            @Override
            public void mouseEntered(MouseEvent e) { backToMenuLabel.setForeground(Color.CYAN); }
            @Override
            public void mouseExited(MouseEvent e) { backToMenuLabel.setForeground(Color.WHITE); }
        });
    }

    // Método auxiliar para crear etiquetas de opción
    private JLabel createOptionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 36));
        label.setForeground(Color.WHITE);
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return label;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Opcional: Dibujar los coches colisionados como en tu imagen
        // Esto es un poco más complejo porque necesitarías saber las posiciones
        // de colisión exactas. Por simplicidad, puedes dibujar dos coches
        // uno encima del otro para simular la colisión.
        // Dibuja el auto del jugador (rojo)
        // Puedes crear una instancia de PlayerCar o usar un método auxiliar
        int playerX = GameConstants.GAME_WIDTH / 2 - GameConstants.CAR_WIDTH / 2;
        int playerY = GameConstants.GAME_HEIGHT / 2 + 50;

        // Dibujar coche del jugador (rojo, como si fuera el que chocó)
        // Usar la lógica de dibujo de PlayerCar
        Color playerColor = gameFrame.getPlayerCarColor(); // Obtén el color actual
        drawGenericCar(g2d, playerColor, playerX, playerY);

        // Dibujar un coche obstáculo (verde)
        drawGenericCar(g2d, Color.GREEN, playerX, playerY - GameConstants.CAR_HEIGHT + 20); // Un poco superpuesto
    }

    // Método auxiliar para dibujar un coche genérico (puede estar en una clase de utilidades o en la clase Car)
    private void drawGenericCar(Graphics2D g2d, Color bodyColor, int x, int y) {
        g2d.setColor(bodyColor);
        g2d.fillRect(x, y, GameConstants.CAR_WIDTH, GameConstants.CAR_HEIGHT);

        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(x, y, GameConstants.CAR_WIDTH, GameConstants.CAR_HEIGHT);

        g2d.setColor(new Color(135, 206, 235)); // Ventanas
        g2d.fillRect(x + 5, y + 10, GameConstants.CAR_WIDTH - 10, GameConstants.CAR_HEIGHT / 4);
        g2d.fillRect(x + 5, y + GameConstants.CAR_HEIGHT - GameConstants.CAR_HEIGHT / 4 - 10, GameConstants.CAR_WIDTH - 10, GameConstants.CAR_HEIGHT / 4);

        g2d.setColor(Color.BLACK); // Ruedas
        g2d.fillRect(x - 5, y + 10, 10, 20);
        g2d.fillRect(x + GameConstants.CAR_WIDTH - 5, y + 10, 10, 20);
        g2d.fillRect(x - 5, y + GameConstants.CAR_HEIGHT - 30, 10, 20);
        g2d.fillRect(x + GameConstants.CAR_WIDTH - 5, y + GameConstants.CAR_HEIGHT - 30, 10, 20);
    }
}