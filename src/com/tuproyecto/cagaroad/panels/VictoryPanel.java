// com.tuproyecto.cagaroad.panels.VictoryPanel.java
package com.tuproyecto.cagaroad.panels;

import com.tuproyecto.cagaroad.GameFrame;
import com.tuproyecto.cagaroad.GameState;
import com.tuproyecto.cagaroad.gameobjects.PlayerCar; // Si quieres dibujar el coche del jugador
import com.tuproyecto.cagaroad.utils.GameConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class VictoryPanel extends JPanel {

    private GameFrame gameFrame;
    private JLabel victoryLabel;
    private JLabel reachedBathroomLabel; // Para el mensaje "Has llegado al baño"
    private JLabel scoreLabel; // Para mostrar el puntaje
    private JLabel backToMenuLabel;

    public VictoryPanel(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        setPreferredSize(new Dimension(GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT));
        setBackground(new Color(50, 100, 50)); // Un verde que sugiere victoria
        setLayout(new GridBagLayout()); // Para centrar los componentes

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER; // Cada componente en una nueva fila
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 10, 0); // Espacio entre componentes

        // Etiqueta de victoria
        victoryLabel = new JLabel("VICTORIA");
        victoryLabel.setFont(new Font("Arial", Font.BOLD, 100));
        victoryLabel.setForeground(Color.WHITE);
        gbc.insets = new Insets(0, 0, 30, 0); // Espacio después de la victoria
        add(victoryLabel, gbc);

        // Mensaje "Has llegado al baño"
        reachedBathroomLabel = new JLabel("Has llegado al baño");
        reachedBathroomLabel.setFont(new Font("Arial", Font.PLAIN, 36));
        reachedBathroomLabel.setForeground(Color.WHITE);
        gbc.insets = new Insets(0, 0, 20, 0);
        add(reachedBathroomLabel, gbc);

        // Puntaje final (el puntaje actual se obtendrá cuando se muestre el panel)
        scoreLabel = new JLabel("Puntaje: ???"); // Se actualizará al mostrar el panel
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 36));
        scoreLabel.setForeground(Color.WHITE);
        gbc.insets = new Insets(0, 0, 50, 0);
        add(scoreLabel, gbc);

        // Opción: Volver al menú
        backToMenuLabel = createOptionLabel("Volver al Menu");
        add(backToMenuLabel, gbc);

        // Listener para el botón de volver al menú
        backToMenuLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                gameFrame.setGameState(GameState.MENU);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                backToMenuLabel.setForeground(Color.CYAN);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                backToMenuLabel.setForeground(Color.WHITE);
            }
        });
    }

    // Método que se puede llamar para actualizar el puntaje al mostrar el panel
    public void setFinalScore(int score) {
        scoreLabel.setText("Puntaje: " + score);
    }

    // Método auxiliar para crear etiquetas de opción con un estilo común
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

        // Dibujar el "baño" como en tu imagen de concepto
        int bathroomX = GameConstants.GAME_WIDTH / 2 - 50; // Centro del baño
        int bathroomY = GameConstants.GAME_HEIGHT - 200; // Posición vertical del baño

        // Edificio principal (marrón)
        g2d.setColor(new Color(139, 69, 19)); // Marrón
        g2d.fillRect(bathroomX, bathroomY, 100, 150);

        // Techo (marrón más oscuro)
        g2d.setColor(new Color(101, 67, 33));
        g2d.fillRect(bathroomX - 10, bathroomY - 20, 120, 30);

        // Puerta (azul claro)
        g2d.setColor(Color.BLUE);
        g2d.fillRect(bathroomX + 30, bathroomY + 70, 40, 80);

        // Ventana (amarilla)
        g2d.setColor(Color.YELLOW);
        g2d.fillRect(bathroomX + 20, bathroomY + 20, 20, 20);

        // Letrero "WC"
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.drawString("WC", bathroomX + 35, bathroomY + 115);

        // Opcional: dibujar el coche del jugador llegando al baño
        // Esto requeriría una instancia de PlayerCar o un método drawCar aquí
        // Por simplicidad, puedes omitir dibujar el auto aquí o crear un método auxiliar.
    }
}