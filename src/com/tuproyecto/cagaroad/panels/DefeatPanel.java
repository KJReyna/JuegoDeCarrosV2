package com.tuproyecto.cagaroad.panels;

import com.tuproyecto.cagaroad.GameFrame;
import com.tuproyecto.cagaroad.GameState;
import com.tuproyecto.cagaroad.utils.GameConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Representa la pantalla de derrota del juego, mostrada al colisionar.
 * Muestra un mensaje de derrota y opciones para reintentar o volver al menú.
 */
public class DefeatPanel extends JPanel {

    private GameFrame gameFrame;
    private JLabel defeatLabel;
    private JLabel finalScoreLabel; // ¡NUEVO! Etiqueta para el puntaje final
    private JLabel retryLabel;
    private JLabel backToMenuLabel;

    private int finalScore; // Almacena el puntaje final

    public DefeatPanel(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        setPreferredSize(new Dimension(GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT));
        setBackground(new Color(150, 50, 50));
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 10, 0);

        defeatLabel = new JLabel("Perdiste, te has CAGADO");
        defeatLabel.setFont(new Font("Arial", Font.BOLD, 48));
        defeatLabel.setForeground(Color.WHITE);
        gbc.insets = new Insets(0, 0, 30, 0); // Ajustado el espacio
        add(defeatLabel, gbc);

        // ¡NUEVO! Etiqueta para mostrar el puntaje final
        finalScoreLabel = new JLabel("Puntaje Final: 0");
        finalScoreLabel.setFont(new Font("Arial", Font.PLAIN, 36));
        finalScoreLabel.setForeground(Color.WHITE);
        gbc.insets = new Insets(0, 0, 40, 0); // Espacio después del puntaje
        add(finalScoreLabel, gbc);

        retryLabel = createOptionLabel("Reintentar");
        add(retryLabel, gbc);

        backToMenuLabel = createOptionLabel("Volver al Menu");
        add(backToMenuLabel, gbc);

        retryLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                gameFrame.restartGame(gameFrame.getGamePanel().getCurrentLevelNumber());
            }
            @Override
            public void mouseEntered(MouseEvent e) { retryLabel.setForeground(Color.CYAN); }
            @Override
            public void mouseExited(MouseEvent e) { retryLabel.setForeground(Color.WHITE); }
        });

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

    /**
     * ¡NUEVO! Método para establecer el puntaje final que se mostrará.
     * @param score El puntaje final del juego.
     */
    public void setFinalScore(int score) {
        this.finalScore = score;
        finalScoreLabel.setText("Puntaje Final: " + finalScore);
    }

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

        Color playerColor = gameFrame.getPlayerCarColor();
        int playerX = GameConstants.GAME_WIDTH / 2 - GameConstants.CAR_WIDTH / 2;
        int playerY = GameConstants.GAME_HEIGHT / 2 + 50;

        drawGenericCar(g2d, playerColor, playerX, playerY);
        drawGenericCar(g2d, Color.GREEN, playerX, playerY - GameConstants.CAR_HEIGHT + 20);
    }

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