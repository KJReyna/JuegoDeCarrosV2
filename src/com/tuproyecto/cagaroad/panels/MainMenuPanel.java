package com.tuproyecto.cagaroad.panels;

import com.tuproyecto.cagaroad.GameFrame;
import com.tuproyecto.cagaroad.GameState;
import com.tuproyecto.cagaroad.utils.GameConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa el menú principal del juego "CAGAROAD".
 * Diseñado como una señal de carretera con el título y las opciones de menú.
 */
public class MainMenuPanel extends JPanel {

    private GameFrame gameFrame;

    // Colores para el diseño de la señal
    private static final Color SIGN_BROWN = new Color(139, 69, 19); // Marrón de la señal
    private static final Color SKY_BLUE = new Color(135, 206, 235); // Azul cielo
    private static final Color GRASS_GREEN = new Color(50, 150, 50); // Verde césped (como en GameConstants)
    private static final Color TITLE_ORANGE = new Color(255, 140, 0); // Color naranja para el título

    // Dimensiones y posiciones de los elementos de la señal
    private static final int SIGN_POLE_WIDTH = 20; // Ancho de los postes
    // Eliminado: SIGN_POLE_HEIGHT_TITLE, ya no hay postes directos debajo del título

    // Panel del título de la señal (JLabel)
    private JLabel titleLabel;
    private int titleSignX, titleSignY, titleSignWidth, titleSignHeight;

    // Lista para almacenar los paneles de botones y poder dibujarlos con postes
    private List<JPanel> menuButtonPanels;

    /**
     * Constructor del MainMenuPanel.
     * @param gameFrame La referencia al GameFrame para cambiar de estado.
     */
    public MainMenuPanel(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        setPreferredSize(new Dimension(GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT));
        setLayout(null); // Usar layout nulo para posicionar manualmente

        // --- Configuración del Título de la Señal (JLabel) ---
        titleLabel = new JLabel("CAGAROAD");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 70));
        titleLabel.setForeground(Color.BLACK); // Texto negro para el título
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setVerticalAlignment(SwingConstants.CENTER);

        // Calcular dimensiones y posición del letrero del título
        FontMetrics fm = titleLabel.getFontMetrics(titleLabel.getFont());
        titleSignWidth = fm.stringWidth(titleLabel.getText()) + 60; // Ancho del texto + margen
        titleSignHeight = fm.getHeight() + 20; // Alto del texto + margen
        titleSignX = (GameConstants.GAME_WIDTH - titleSignWidth) / 2; // Centrado horizontalmente
        titleSignY = 50; // Posición Y del letrero del título

        titleLabel.setBounds(titleSignX, titleSignY, titleSignWidth, titleSignHeight);
        add(titleLabel); // Añade el JLabel del título al panel

        // --- Configuración de los Botones de Opción ---
        int buttonWidth = 250;
        int buttonHeight = 50;
        int startYButtons = titleSignY + titleSignHeight + 60; // Posición Y inicial para los botones
        int buttonX = (GameConstants.GAME_WIDTH - buttonWidth) / 2; // Centrado horizontalmente
        int buttonSpacing = 15; // Espacio vertical entre tablones de botones

        menuButtonPanels = new ArrayList<>();

        // Crear y añadir los paneles-botón
        menuButtonPanels.add(createMenuButtonPanel("NIVELES", GameState.LEVEL_SELECT, buttonX, startYButtons, buttonWidth, buttonHeight));
        menuButtonPanels.add(createMenuButtonPanel("CUSTOMIZAR", GameState.CUSTOMIZE, buttonX, startYButtons + (buttonHeight + buttonSpacing), buttonWidth, buttonHeight));
        menuButtonPanels.add(createMenuButtonPanel("MODO INFINITO", GameState.PLAYING, buttonX, startYButtons + 2 * (buttonHeight + buttonSpacing), buttonWidth, buttonHeight));
        menuButtonPanels.add(createMenuButtonPanel("SALIR", null, buttonX, startYButtons + 3 * (buttonHeight + buttonSpacing), buttonWidth, buttonHeight));

        for (JPanel panel : menuButtonPanels) {
            add(panel); // Añade cada panel-botón al MainMenuPanel
        }

        // Listener especial para el botón "SALIR"
        menuButtonPanels.get(menuButtonPanels.size() - 1).addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0); // Cierra la aplicación
            }
        });
    }

    /**
     * Método auxiliar para crear un JPanel que actúa como botón de menú.
     * Gestiona su posición, tamaño y eventos del mouse.
     */
    private JPanel createMenuButtonPanel(String text, GameState targetState, int x, int y, int width, int height) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout()); // Para centrar el JLabel dentro
        buttonPanel.setBounds(x, y, width, height); // Posición y tamaño
        buttonPanel.setBackground(SIGN_BROWN); // Fondo marrón para el tablón
        buttonPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2)); // Borde blanco
        buttonPanel.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Cursor de mano

        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 24)); // Fuente para el texto del botón
        label.setForeground(Color.WHITE); // Texto blanco
        buttonPanel.add(label);

        // Añadir listeners para los efectos de hover y el clic
        buttonPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                buttonPanel.setBackground(SIGN_BROWN.darker()); // Oscurece el marrón al pasar el mouse
                buttonPanel.setBorder(BorderFactory.createLineBorder(Color.CYAN, 3)); // Borde cian más grueso
                label.setForeground(Color.CYAN); // Texto cian
            }

            @Override
            public void mouseExited(MouseEvent e) {
                buttonPanel.setBackground(SIGN_BROWN); // Vuelve al estado normal
                buttonPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2)); // Borde blanco
                label.setForeground(Color.WHITE); // Texto blanco
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (targetState != null) {
                    if (targetState == GameState.PLAYING && text.equals("MODO INFINITO")) { // Verificamos si es modo infinito por el texto
                        gameFrame.restartGame(0); // 0 para modo infinito
                    } else {
                        gameFrame.setGameState(targetState);
                    }
                }
            }
        });
        return buttonPanel;
    }


    /**
     * Dibuja los elementos de fondo y la señal de carretera.
     * @param g El objeto Graphics usado para dibujar.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Dibuja el fondo del JPanel
        Graphics2D g2d = (Graphics2D) g;

        // Mejorar el renderizado
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // --- Dibujar el fondo de Cielo y Césped ---
        g2d.setColor(SKY_BLUE);
        g2d.fillRect(0, 0, GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT); // Cielo

        int grassHeight = 50;
        g2d.setColor(GRASS_GREEN);
        g2d.fillRect(0, GameConstants.GAME_HEIGHT - grassHeight, GameConstants.GAME_WIDTH, grassHeight); // Césped

        // --- Dibujar el rectángulo naranja del título ---
        g2d.setColor(TITLE_ORANGE);
        g2d.fillRect(titleSignX, titleSignY, titleSignWidth, titleSignHeight);
        g2d.setColor(Color.BLACK); // Borde negro para el título
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(titleSignX, titleSignY, titleSignWidth, titleSignHeight);

        // --- Dibujar los dos postes principales que sostienen TODO EL CONJUNTO de botones ---
        // Estos postes van desde el punto Y del primer botón hasta el césped.
        if (!menuButtonPanels.isEmpty()) {
            JPanel firstButton = menuButtonPanels.get(0);

            // Posición X de los postes (centrados con los botones)
            int mainPolesX1 = firstButton.getX() + firstButton.getWidth() / 4 - SIGN_POLE_WIDTH / 2;
            int mainPolesX2 = firstButton.getX() + 3 * firstButton.getWidth() / 4 - SIGN_POLE_WIDTH / 2;

            // Posición Y de los postes (desde la parte superior del primer botón hasta el césped)
            int mainPolesY1 = firstButton.getY(); // Inicia desde la parte superior del primer botón
            int mainPolesHeight = (GameConstants.GAME_HEIGHT - grassHeight) - mainPolesY1; // Altura hasta el césped

            g2d.fillRect(mainPolesX1, mainPolesY1, SIGN_POLE_WIDTH, mainPolesHeight);
            g2d.fillRect(mainPolesX2, mainPolesY1, SIGN_POLE_WIDTH, mainPolesHeight);

            // Dibujar las "conexiones" horizontales de los postes a los botones
            g2d.setColor(SIGN_BROWN.darker()); // Color de las uniones
            int connectionBarHeight = 5; // Grosor de las barras de conexión

            for (JPanel buttonPanel : menuButtonPanels) {
                // Barra horizontal izquierda: desde el borde derecho del poste izquierdo hasta el borde izquierdo del botón
                int leftBarX = mainPolesX1 + SIGN_POLE_WIDTH; // Empieza justo después del poste izquierdo
                int leftBarWidth = buttonPanel.getX() - leftBarX; // Ancho para llegar hasta el botón
                g2d.fillRect(leftBarX, buttonPanel.getY() + buttonPanel.getHeight() / 2 - connectionBarHeight / 2, leftBarWidth, connectionBarHeight);

                // Barra horizontal derecha: desde el borde derecho del botón hasta el borde izquierdo del poste derecho
                int rightBarX = buttonPanel.getX() + buttonPanel.getWidth(); // Empieza justo después del botón
                int rightBarWidth = mainPolesX2 - rightBarX; // Ancho para llegar hasta el poste derecho
                g2d.fillRect(rightBarX, buttonPanel.getY() + buttonPanel.getHeight() / 2 - connectionBarHeight / 2, rightBarWidth, connectionBarHeight);
            }
        }

        // Los JLabels y JPanels de botones se dibujan automáticamente por Swing.
    }
}