package com.tuproyecto.cagaroad.panels;

import com.tuproyecto.cagaroad.GameFrame;
import com.tuproyecto.cagaroad.GameState;
import com.tuproyecto.cagaroad.utils.GameConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Representa la pantalla de personalización del coche del jugador.
 * Permite al usuario seleccionar un color para su coche, con un diseño limpio y centrado.
 */
public class CustomizePanel extends JPanel {

    private GameFrame gameFrame;
    private Color selectedCarColor = Color.RED; // Color inicial por defecto del coche
    private JLabel backButton; // Etiqueta para el botón "Volver al Menú"

    // Colores disponibles (sin Color.WHITE)
    private final Color[] AVAILABLE_COLORS = {Color.BLACK, Color.RED, Color.YELLOW, Color.CYAN, Color.MAGENTA};

    // Constantes para el diseño y posicionamiento
    private static final int TITLE_TOP_MARGIN = 50;
    private static final int COLOR_BOX_SIZE = 50;
    private static final int COLOR_BOX_SPACING_VERTICAL = 20;
    private static final int BACK_BUTTON_BOTTOM_MARGIN = 80;

    // ¡NUEVO! Posición horizontal para los cuadros de color y el coche de preview
    private static final int COLOR_BOX_START_X = GameConstants.GAME_WIDTH / 4 - COLOR_BOX_SIZE / 2; // Centrado en el primer cuarto de la pantalla
    private static final int CAR_PREVIEW_X = GameConstants.GAME_WIDTH / 2 + 50; // Posición X para el coche de preview

    /**
     * Constructor del CustomizePanel.
     * @param gameFrame La referencia al GameFrame para cambiar de estado.
     */
    public CustomizePanel(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        setPreferredSize(new Dimension(GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT));
        setBackground(Color.DARK_GRAY); // Fondo gris oscuro para un buen contraste
        setLayout(null); // Usamos layout nulo para posicionar manualmente

        // --- Configuración del Título ---
        JLabel title = new JLabel("Selecciona un color");
        title.setFont(new Font("Arial", Font.BOLD, 40));
        title.setForeground(Color.WHITE);
        title.setBounds((GameConstants.GAME_WIDTH - title.getPreferredSize().width) / 2, TITLE_TOP_MARGIN, title.getPreferredSize().width, 50);
        add(title);

        // --- Configuración de los Cuadros de Selección de Color ---
        // Calcular la posición Y inicial para centrar verticalmente el bloque de colores
        int totalColorsHeight = AVAILABLE_COLORS.length * COLOR_BOX_SIZE + (AVAILABLE_COLORS.length - 1) * COLOR_BOX_SPACING_VERTICAL;
        int colorsBlockStartY = (GameConstants.GAME_HEIGHT - totalColorsHeight) / 2;

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
            colorBox.setBounds(COLOR_BOX_START_X, colorsBlockStartY + i * (COLOR_BOX_SIZE + COLOR_BOX_SPACING_VERTICAL), COLOR_BOX_SIZE, COLOR_BOX_SIZE);
            colorBox.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Cambia el cursor al pasar el mouse
            colorBox.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    selectedCarColor = color; // Actualiza el color seleccionado
                    repaint(); // Redibuja el panel para reflejar la nueva selección y el coche de preview
                }
            });
            add(colorBox);
        }

        // --- Configuración del Botón "Volver al Menú" ---
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
            public void mouseEntered(MouseEvent e) { backButton.setForeground(Color.CYAN); } // Efecto de hover
            @Override
            public void mouseExited(MouseEvent e) { backButton.setForeground(Color.WHITE); }
        });
        backButton.setBounds(GameConstants.GAME_WIDTH / 2 - backButton.getPreferredSize().width / 2,
                GameConstants.GAME_HEIGHT - BACK_BUTTON_BOTTOM_MARGIN,
                backButton.getPreferredSize().width, 30);
        add(backButton);

        // Fuerza un redibujado inicial para asegurar que el coche de preview
        // se muestre con el color rojo por defecto al cargar el panel.
        repaint();
    }

    public Color getSelectedColor() {
        return selectedCarColor;
    }

    /**
     * Dibuja el contenido del panel, incluyendo el coche del jugador con el color seleccionado.
     * @param g El objeto Graphics usado para dibujar.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Dibuja el fondo del JPanel

        // Dibuja el auto del jugador con el color seleccionado usando formas geométricas.
        // El coche de preview se centra verticalmente.
        int carPreviewY = GameConstants.GAME_HEIGHT / 2 - GameConstants.CAR_HEIGHT / 2;
        drawCar((Graphics2D) g, selectedCarColor, CAR_PREVIEW_X, carPreviewY);
    }

    /**
     * Método auxiliar para dibujar un coche genérico (usando formas geométricas).
     * @param g2d El objeto Graphics2D usado para dibujar.
     * @param bodyColor El color principal del cuerpo del coche.
     * @param x La coordenada X de la esquina superior izquierda del coche.
     * @param y La coordenada Y de la esquina superior izquierda del coche.
     */
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
}//