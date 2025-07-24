package com.tuproyecto.cagaroad.panels;

import com.tuproyecto.cagaroad.GameFrame;
import com.tuproyecto.cagaroad.GameState;
import com.tuproyecto.cagaroad.utils.GameConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
// Eliminado: import java.awt.image.BufferedImage;
// Eliminado: import java.io.IOException;
// Eliminado: import com.tuproyecto.cagaroad.utils.AssetLoader;

/**
 * Representa la pantalla de personalización del coche del jugador.
 * Permite al usuario seleccionar un color para su coche.
 */
public class CustomizePanel extends JPanel {

    private GameFrame gameFrame;
    private Color selectedCarColor = Color.RED; // ¡VERIFICAR! Color inicial por defecto del coche
    private JLabel backButton;

    // Colores disponibles (eliminado Color.WHITE)
    private final Color[] AVAILABLE_COLORS = {Color.BLACK, Color.RED, Color.YELLOW, Color.CYAN, Color.MAGENTA};

    // ¡NUEVO! Margen desde el borde izquierdo para los cuadros de color
    private static final int COLOR_BOX_LEFT_MARGIN = 100;

    /**
     * Constructor del CustomizePanel.
     * @param gameFrame La referencia al GameFrame para cambiar de estado.
     */
    public CustomizePanel(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        setPreferredSize(new Dimension(GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT));
        setBackground(Color.DARK_GRAY);
        setLayout(null); // Usamos un layout nulo para posicionar manualmente

        JLabel title = new JLabel("Selecciona un color");
        title.setFont(new Font("Arial", Font.BOLD, 40));
        title.setForeground(Color.WHITE);
        title.setBounds((GameConstants.GAME_WIDTH - title.getPreferredSize().width) / 2, 50, title.getPreferredSize().width, 50);
        add(title);

        // ¡MODIFICADO! Usamos el nuevo margen para startX
        int startX = COLOR_BOX_LEFT_MARGIN;
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
                        g.setColor(Color.WHITE);
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
                    repaint(); // Redibuja el panel para actualizar el coche de preview y la selección
                }
            });
            add(colorBox);
        }

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

        // ¡NUEVO! Forzar el redibujado inicial para asegurar que el coche de preview se muestre
        // con el color rojo por defecto al cargar el panel.
        repaint();
    }

    public Color getSelectedColor() {
        return selectedCarColor;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Dibuja el auto del jugador con el color seleccionado usando formas geométricas
        // Asegúrate de que el coche preview esté centrado a la derecha de los cuadros de color
        // Calculamos la posición X para que el coche esté a la derecha de los cuadros y centrado verticalmente
        int carPreviewX = COLOR_BOX_LEFT_MARGIN + 200; // Unos 200px a la derecha de los cuadros de color
        int carPreviewY = GameConstants.GAME_HEIGHT / 2 - GameConstants.CAR_HEIGHT / 2;
        drawCar((Graphics2D) g, selectedCarColor, carPreviewX, carPreviewY);
    }

    private void drawCar(Graphics2D g2d, Color bodyColor, int x, int y) {
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