package com.tuproyecto.cagaroad.panels;

import com.tuproyecto.cagaroad.GameFrame;
import com.tuproyecto.cagaroad.GameState;
import com.tuproyecto.cagaroad.utils.GameConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Representa la pantalla de victoria del juego, mostrada al completar un nivel.
 * Muestra un mensaje de victoria, el puntaje final y una opción para volver al menú.
 */
public class VictoryPanel extends JPanel {

    private GameFrame gameFrame;
    private JLabel victoryLabel;
    private JLabel reachedBathroomLabel;
    private JLabel scoreLabel;
    private JLabel backToMenuLabel;

    public VictoryPanel(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        setPreferredSize(new Dimension(GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT));
        setBackground(new Color(50, 100, 50));
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 10, 0);

        victoryLabel = new JLabel("VICTORIA");
        victoryLabel.setFont(new Font("Arial", Font.BOLD, 100));
        victoryLabel.setForeground(Color.WHITE);
        gbc.insets = new Insets(0, 0, 30, 0);
        add(victoryLabel, gbc);

        reachedBathroomLabel = new JLabel("Has llegado al baño");
        reachedBathroomLabel.setFont(new Font("Arial", Font.PLAIN, 36));
        reachedBathroomLabel.setForeground(Color.WHITE);
        gbc.insets = new Insets(0, 0, 20, 0);
        add(reachedBathroomLabel, gbc);

        scoreLabel = new JLabel("Puntaje: ???");
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 36));
        scoreLabel.setForeground(Color.WHITE);
        gbc.insets = new Insets(0, 0, 50, 0);
        add(scoreLabel, gbc);

        backToMenuLabel = createOptionLabel("Volver al Menu");
        add(backToMenuLabel, gbc);

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

    public void setFinalScore(int score) {
        scoreLabel.setText("Puntaje: " + score);
    }

    private JLabel createOptionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 36));
        label.setForeground(Color.WHITE);
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return label;
    }

    /**
     * Dibuja el contenido del panel, incluyendo la representación del "baño" y el coche del jugador.
     * @param g El objeto Graphics usado para dibujar.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Dibujar el "baño"
        int bathroomWidth = 100;
        int bathroomHeight = 150;
        int bathroomX = GameConstants.GAME_WIDTH / 2 - bathroomWidth / 2;
        int bathroomY = GameConstants.GAME_HEIGHT - bathroomHeight - 50;

        g2d.setColor(new Color(139, 69, 19));
        g2d.fillRect(bathroomX, bathroomY, bathroomWidth, bathroomHeight);

        g2d.setColor(new Color(101, 67, 33));
        g2d.fillRect(bathroomX - 10, bathroomY - 20, bathroomWidth + 20, 30);

        g2d.setColor(Color.BLUE);
        g2d.fillRect(bathroomX + (bathroomWidth / 2) - 20, bathroomY + (bathroomHeight / 2), 40, bathroomHeight / 2);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.drawString("WC", bathroomX + (bathroomWidth / 2) - 15, bathroomY + (bathroomHeight / 2) + 30);

    }

    /**
     * Método auxiliar para dibujar un coche genérico (usando formas geométricas).
     * @param g2d El objeto Graphics2D usado para dibujar.
     * @param bodyColor El color principal del cuerpo del coche.
     * @param x La coordenada X de la esquina superior izquierda del coche.
     * @param y La coordenada Y de la esquina superior izquierda del coche.
     */
    private void drawGenericCar(Graphics2D g2d, Color bodyColor, int x, int y) {
        g2d.setColor(bodyColor);
        g2d.fillRect(x, y, GameConstants.CAR_WIDTH, GameConstants.CAR_HEIGHT);

        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(x, y, GameConstants.CAR_WIDTH, GameConstants.CAR_HEIGHT);

        g2d.setColor(new Color(135, 206, 235));
        g2d.fillRect(x + 5, y + 10, GameConstants.CAR_WIDTH - 10, GameConstants.CAR_HEIGHT / 4);
        g2d.fillRect(x + 5, y + GameConstants.CAR_HEIGHT - GameConstants.CAR_HEIGHT / 4 - 10, GameConstants.CAR_WIDTH - 10, GameConstants.CAR_HEIGHT / 4);

        g2d.setColor(Color.BLACK);
        g2d.fillRect(x - 5, y + 10, 10, 20);
        g2d.fillRect(x + GameConstants.CAR_WIDTH - 5, y + 10, 10, 20);
        g2d.fillRect(x - 5, y + GameConstants.CAR_HEIGHT - 30, 10, 20);
        g2d.fillRect(x + GameConstants.CAR_WIDTH - 5, y + GameConstants.CAR_HEIGHT - 30, 10, 20);
    }
}