// com.tuproyecto.cagaroad.panels.MainMenuPanel.java
package com.tuproyecto.cagaroad.panels;

import com.tuproyecto.cagaroad.GameFrame;
import com.tuproyecto.cagaroad.GameState;
import com.tuproyecto.cagaroad.utils.GameConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainMenuPanel extends JPanel {

    private GameFrame gameFrame;
    private JLabel titleLabel;
    private JLabel levelsLabel, customizeLabel, infiniteModeLabel, exitLabel;

    public MainMenuPanel(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        setPreferredSize(new Dimension(GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT));
        setBackground(new Color(23, 75, 96)); // Un azul verdoso oscuro como en tu imagen
        setLayout(new GridBagLayout()); // Usar GridBagLayout para centrar y espaciar

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER; // Cada componente en una nueva fila
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 10, 0); // Espacio entre componentes

        // Título del juego
        titleLabel = new JLabel("CAGAROAD");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 72));
        titleLabel.setForeground(Color.WHITE);
        gbc.insets = new Insets(0, 0, 50, 0); // Más espacio después del título
        add(titleLabel, gbc);

        // Opciones del menú
        Font optionFont = new Font("Arial", Font.PLAIN, 36);
        Color optionColor = Color.WHITE;
        gbc.insets = new Insets(10, 0, 10, 0); // Restaurar espaciado

        levelsLabel = createMenuLabel("Niveles", optionFont, optionColor);
        customizeLabel = createMenuLabel("Customizar", optionFont, optionColor);
        infiniteModeLabel = createMenuLabel("Modo Infinito", optionFont, optionColor);
        exitLabel = createMenuLabel("Salir", optionFont, optionColor);

        add(levelsLabel, gbc);
        add(customizeLabel, gbc);
        add(infiniteModeLabel, gbc);
        add(exitLabel, gbc);

        // Añadir listeners para cada opción
        levelsLabel.addMouseListener(new MenuOptionListener(GameState.LEVEL_SELECT));
        customizeLabel.addMouseListener(new MenuOptionListener(GameState.CUSTOMIZE));
        infiniteModeLabel.addMouseListener(new MenuOptionListener(GameState.PLAYING, true)); // Modo infinito
        exitLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0); // Cierra la aplicación
            }
        });
    }

    // Método auxiliar para crear etiquetas de menú con un estilo común
    private JLabel createMenuLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        label.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Cambia el cursor para indicar que es clickeable
        return label;
    }

    // Clase interna para manejar clics y efectos de hover
    private class MenuOptionListener extends MouseAdapter {
        private GameState targetState;
        private boolean isInfiniteMode = false; // Bandera para modo infinito

        public MenuOptionListener(GameState targetState) {
            this.targetState = targetState;
        }

        public MenuOptionListener(GameState targetState, boolean isInfiniteMode) {
            this.targetState = targetState;
            this.isInfiniteMode = isInfiniteMode;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            JLabel label = (JLabel) e.getSource();
            label.setForeground(Color.CYAN); // Cambia el color al pasar el mouse
        }

        @Override
        public void mouseExited(MouseEvent e) {
            JLabel label = (JLabel) e.getSource();
            label.setForeground(Color.WHITE); // Vuelve al color original
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (isInfiniteMode) {
                gameFrame.restartGame(0); // 0 podría significar modo infinito o un nivel especial
            } else {
                gameFrame.setGameState(targetState);
            }
        }
    }
}