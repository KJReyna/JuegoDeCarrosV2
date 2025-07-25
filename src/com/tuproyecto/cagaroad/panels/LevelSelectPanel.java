// com.tuproyecto.cagaroad.panels.LevelSelectPanel.java
package com.tuproyecto.cagaroad.panels;

import com.tuproyecto.cagaroad.GameFrame;
import com.tuproyecto.cagaroad.GameState;
import com.tuproyecto.cagaroad.utils.GameConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LevelSelectPanel extends JPanel {

    private GameFrame gameFrame;
    private JLabel level1Label, level2Label, level3Label;
    private JLabel backButton; // Para volver al menú principal

    // Fuentes para los niveles
    private final Font NORMAL_FONT = new Font("Arial", Font.BOLD, 48);
    private final Font HOVER_FONT = new Font("Arial", Font.BOLD, 72);

    public LevelSelectPanel(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        setPreferredSize(new Dimension(GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT));
        setBackground(new Color(23, 75, 96)); // Fondo similar al menú
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20); // Espacio entre los niveles

        // Configuración de las etiquetas de nivel
        level1Label = createLevelLabel("Nivel 1", 1);
        level2Label = createLevelLabel("Nivel 2", 2);
        level3Label = createLevelLabel("Nivel 3", 3);

        gbc.gridx = 0; add(level1Label, gbc);
        gbc.gridx = 1; add(level2Label, gbc);
        gbc.gridx = 2; add(level3Label, gbc);

        // Botón de Volver al Menú
        backButton = new JLabel("Volver al Menú");
        backButton.setFont(new Font("Arial", Font.PLAIN, 24));
        backButton.setForeground(Color.WHITE);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                gameFrame.setGameState(GameState.MENU);
            }
            @Override
            public void mouseEntered(MouseEvent e) { backButton.setForeground(Color.RED); }
            @Override
            public void mouseExited(MouseEvent e) { backButton.setForeground(Color.WHITE); }
        });
        gbc.gridx = 1; // Centrar el botón de volver
        gbc.gridy = 1; // Debajo de los niveles
        gbc.insets = new Insets(50, 0, 0, 0); // Espacio superior
        add(backButton, gbc);

        resetSelection(); // Para asegurar que las etiquetas estén en su estado inicial
    }

    private JLabel createLevelLabel(String text, int levelNumber) {
        JLabel label = new JLabel(text);
        label.setFont(NORMAL_FONT);
        label.setForeground(Color.WHITE);
        label.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2)); // Borde blanco
        label.setOpaque(true);
        label.setBackground(Color.DARK_GRAY);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(200, 100)); // Tamaño fijo inicial
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));

        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                label.setFont(HOVER_FONT); // Agrandar
                // Esto es un truco, preferiblemente recalcular el tamaño con un Layout Manager o revalidar el padre
                label.setPreferredSize(new Dimension(250, 120)); // Tamaño más grande
                revalidate(); // Revalida el layout para que el nuevo tamaño se aplique
                repaint(); // Redibuja
            }

            @Override
            public void mouseExited(MouseEvent e) {
                label.setFont(NORMAL_FONT); // Achicar
                label.setPreferredSize(new Dimension(200, 100)); // Tamaño original
                revalidate();
                repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                gameFrame.restartGame(levelNumber); // Iniciar el juego con el nivel seleccionado
            }
        });
        return label;
    }

    // Método para reiniciar el estado visual de las etiquetas de nivel
    public void resetSelection() {
        level1Label.setFont(NORMAL_FONT);
        level2Label.setFont(NORMAL_FONT);
        level3Label.setFont(NORMAL_FONT);
        level1Label.setPreferredSize(new Dimension(200, 100));
        level2Label.setPreferredSize(new Dimension(200, 100));
        level3Label.setPreferredSize(new Dimension(200, 100));
        revalidate();
        repaint();
    }
}