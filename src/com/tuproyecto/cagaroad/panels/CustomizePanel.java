// com.tuproyecto.cagaroad.panels.CustomizePanel.java
package com.tuproyecto.cagaroad.panels;

import com.tuproyecto.cagaroad.GameFrame;
import com.tuproyecto.cagaroad.GameState;
import com.tuproyecto.cagaroad.utils.GameConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CustomizePanel extends JPanel {

    private GameFrame gameFrame;
    private Color selectedCarColor = Color.RED; // Color inicial por defecto
    private JLabel backButton;

    // Colores disponibles
    private final Color[] AVAILABLE_COLORS = {Color.BLACK, Color.RED, Color.YELLOW, Color.CYAN, Color.MAGENTA};

    public CustomizePanel(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        setPreferredSize(new Dimension(GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT));
        setBackground(Color.DARK_GRAY); // Fondo oscuro
        setLayout(null); // Usar layout nulo para posicionar manualmente

        // Título
        JLabel title = new JLabel("Selecciona un color");
        title.setFont(new Font("Arial", Font.BOLD, 40));
        title.setForeground(Color.WHITE);
        title.setBounds((GameConstants.GAME_WIDTH - title.getPreferredSize().width) / 2, 50, title.getPreferredSize().width, 50);
        add(title);

        // Añadir botones de color
        int startX = GameConstants.GAME_WIDTH / 4;
        int startY = GameConstants.GAME_HEIGHT / 2 - 100;
        int colorBoxSize = 50;
        int spacing = 20;

        for (int i = 0; i < AVAILABLE_COLORS.length; i++) {
            Color color = AVAILABLE_COLORS[i];
            JPanel colorBox = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.setColor(color);
                    g.fillRect(0, 0, getWidth(), getHeight());
                    if (color.equals(selectedCarColor)) {
                        g.setColor(Color.WHITE); // Borde blanco para el seleccionado
                        ((Graphics2D)g).setStroke(new BasicStroke(3));
                        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                    }
                }
            };
            colorBox.setBounds(startX, startY + i * (colorBoxSize + spacing), colorBoxSize, colorBoxSize);
            colorBox.setCursor(new Cursor(Cursor.HAND_CURSOR));
            colorBox.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    selectedCarColor = color;
                    repaint(); // Redibuja para mostrar la selección
                }
            });
            add(colorBox);
        }

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
        backButton.setBounds(GameConstants.GAME_WIDTH / 2 - backButton.getPreferredSize().width / 2,
                GameConstants.GAME_HEIGHT - 80,
                backButton.getPreferredSize().width, 30);
        add(backButton);
    }

    // Método para obtener el color seleccionado
    public Color getSelectedColor() {
        return selectedCarColor;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Dibuja el auto del jugador con el color seleccionado
        drawCar(g, selectedCarColor, GameConstants.GAME_WIDTH / 2 - GameConstants.CAR_WIDTH / 2, GameConstants.GAME_HEIGHT / 2 - GameConstants.CAR_HEIGHT / 2);
    }

    // Método auxiliar para dibujar un coche (puedes moverlo a una clase Car)
    private void drawCar(Graphics g, Color bodyColor, int x, int y) {
        g.setColor(bodyColor);
        g.fillRect(x, y, GameConstants.CAR_WIDTH, GameConstants.CAR_HEIGHT); // Cuerpo del coche

        g.setColor(Color.BLACK);
        ((Graphics2D)g).setStroke(new BasicStroke(2)); // Borde más grueso
        g.drawRect(x, y, GameConstants.CAR_WIDTH, GameConstants.CAR_HEIGHT); // Borde del cuerpo

        // Ventanas
        g.setColor(Color.BLUE);
        g.fillRect(x + 5, y + 10, GameConstants.CAR_WIDTH - 10, GameConstants.CAR_HEIGHT / 4); // Ventana frontal
        g.fillRect(x + 5, y + GameConstants.CAR_HEIGHT - GameConstants.CAR_HEIGHT / 4 - 10, GameConstants.CAR_WIDTH - 10, GameConstants.CAR_HEIGHT / 4); // Ventana trasera

        // Ruedas
        g.setColor(Color.BLACK);
        g.fillRect(x - 5, y + 10, 10, 20); // Rueda delantera izquierda
        g.fillRect(x + GameConstants.CAR_WIDTH - 5, y + 10, 10, 20); // Rueda delantera derecha
        g.fillRect(x - 5, y + GameConstants.CAR_HEIGHT - 30, 10, 20); // Rueda trasera izquierda
        g.fillRect(x + GameConstants.CAR_WIDTH - 5, y + GameConstants.CAR_HEIGHT - 30, 10, 20); // Rueda trasera derecha
    }
}