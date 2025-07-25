package com.tuproyecto.cagaroad.panels;

import com.tuproyecto.cagaroad.GameFrame;
import com.tuproyecto.cagaroad.GameState;
import com.tuproyecto.cagaroad.utils.GameConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

/**
 * Representa la pantalla de selección de niveles del juego.
 * Diseñado con un estilo consistente al menú principal, con botones interactivos y flotantes.
 */
public class LevelSelectPanel extends JPanel {

    private GameFrame gameFrame;

    // Colores y dimensiones para el diseño (reutilizados del MainMenuPanel)
    private static final Color SIGN_BROWN = new Color(139, 69, 19);
    private static final Color SKY_BLUE = new Color(135, 206, 235);
    private static final Color GRASS_GREEN = new Color(50, 150, 50);
    private static final Color TITLE_ORANGE = new Color(255, 140, 0);

    // Constantes específicas para el diseño de niveles
    private static final int LEVEL_BUTTON_NORMAL_WIDTH = 150;
    private static final int LEVEL_BUTTON_NORMAL_HEIGHT = 80;
    private static final int LEVEL_BUTTON_HOVER_WIDTH = 180; // Ancho agrandado
    private static final int LEVEL_BUTTON_HOVER_HEIGHT = 95; // Alto agrandado
    private static final int HORIZONTAL_BUTTON_GAP = 30; // Espacio entre botones de nivel
    private static final int LEVEL_TEXT_FONT_NORMAL_SIZE = 36;
    private static final int LEVEL_TEXT_FONT_HOVER_SIZE = 42; // Tamaño de fuente agrandado

    // ¡MODIFICADO! Tamaño fijo para el botón "Volver al Menú", no se agranda.
    private static final int BACK_BUTTON_WIDTH = 200; // Más pequeño
    private static final int BACK_BUTTON_HEIGHT = 50; // Más pequeño
    private static final int BACK_BUTTON_FONT_SIZE = 24;

    // Variables de estado para el hover
    private int hoveredLevelIndex = -1; // -1 si ninguno, 0 para Nivel 1, 1 para Nivel 2, 2 para Nivel 3
    private boolean hoveredBackButton = false;

    // Rectángulos para detectar interacciones (se inicializan en el constructor)
    private Rectangle[] levelButtonAreas;
    private Rectangle backButtonArea;

    // Dimensiones y posiciones del título "NIVELES"
    private int levelsTitleSignX, levelsTitleSignY, levelsTitleSignWidth, levelsTitleSignHeight;

    /**
     * Constructor del LevelSelectPanel.
     * @param gameFrame La referencia al GameFrame para cambiar de estado.
     */
    public LevelSelectPanel(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        setPreferredSize(new Dimension(GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT));
        setLayout(null); // Usamos layout nulo para posicionar manualmente

        // --- Calcular dimensiones y posiciones del título "NIVELES" ---
        Font titleFont = new Font("Arial", Font.BOLD, 70);
        FontMetrics fmTitle = getFontMetrics(titleFont);
        String titleText = "NIVELES";
        levelsTitleSignWidth = fmTitle.stringWidth(titleText) + 60;
        levelsTitleSignHeight = fmTitle.getHeight() + 20;
        levelsTitleSignX = (GameConstants.GAME_WIDTH - levelsTitleSignWidth) / 2;
        levelsTitleSignY = 50;

        // --- Calcular las áreas de los botones de nivel (para detección de mouse) ---
        levelButtonAreas = new Rectangle[3];
        int totalLevelsWidth = (3 * LEVEL_BUTTON_NORMAL_WIDTH) + (2 * HORIZONTAL_BUTTON_GAP);
        int startXLevels = (GameConstants.GAME_WIDTH - totalLevelsWidth) / 2;
        int startYLevels = levelsTitleSignY + levelsTitleSignHeight + 100;

        for (int i = 0; i < 3; i++) {
            levelButtonAreas[i] = new Rectangle(
                    startXLevels + i * (LEVEL_BUTTON_NORMAL_WIDTH + HORIZONTAL_BUTTON_GAP),
                    startYLevels,
                    LEVEL_BUTTON_NORMAL_WIDTH,
                    LEVEL_BUTTON_NORMAL_HEIGHT
            );
        }

        // --- Calcular el área del botón "Volver al Menú" ---
        int grassHeight = 50;
        backButtonArea = new Rectangle(
                (GameConstants.GAME_WIDTH - BACK_BUTTON_WIDTH) / 2,
                GameConstants.GAME_HEIGHT - grassHeight - BACK_BUTTON_HEIGHT - 30, // Posición arriba del césped
                BACK_BUTTON_WIDTH,
                BACK_BUTTON_HEIGHT
        );

        // --- Añadir MouseListener para interacciones ---
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (int i = 0; i < levelButtonAreas.length; i++) {
                    // Para clic en niveles, usamos el área de hover si está activa, o el área normal.
                    int currentWidth = (i == hoveredLevelIndex) ? LEVEL_BUTTON_HOVER_WIDTH : LEVEL_BUTTON_NORMAL_WIDTH;
                    int currentHeight = (i == hoveredLevelIndex) ? LEVEL_BUTTON_HOVER_HEIGHT : LEVEL_BUTTON_NORMAL_HEIGHT;
                    int currentX = levelButtonAreas[i].x + (LEVEL_BUTTON_NORMAL_WIDTH - currentWidth) / 2;
                    int currentY = levelButtonAreas[i].y + (LEVEL_BUTTON_NORMAL_HEIGHT - currentHeight) / 2;

                    Rectangle effectiveClickArea = new Rectangle(currentX, currentY, currentWidth, currentHeight);
                    if (effectiveClickArea.contains(e.getPoint())) {
                        gameFrame.restartGame(i + 1);
                        return;
                    }
                }
                // Clic en botón "Volver al Menú"
                if (backButtonArea.contains(e.getPoint())) {
                    gameFrame.setGameState(GameState.MENU);
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int oldHoveredLevelIndex = hoveredLevelIndex;
                boolean oldHoveredBackButton = hoveredBackButton;

                // Detectar hover en botones de Nivel
                hoveredLevelIndex = -1; // Resetear
                for (int i = 0; i < levelButtonAreas.length; i++) {
                    // Calculamos el tamaño real del botón si estuviera en hover o no
                    int currentWidth = (i == oldHoveredLevelIndex) ? LEVEL_BUTTON_HOVER_WIDTH : LEVEL_BUTTON_NORMAL_WIDTH;
                    int currentHeight = (i == oldHoveredLevelIndex) ? LEVEL_BUTTON_HOVER_HEIGHT : LEVEL_BUTTON_NORMAL_HEIGHT;

                    // Calculamos la posición real para mantenerlo centrado si se agranda
                    int currentX = levelButtonAreas[i].x + (LEVEL_BUTTON_NORMAL_WIDTH - currentWidth) / 2;
                    int currentY = levelButtonAreas[i].y + (LEVEL_BUTTON_NORMAL_HEIGHT - currentHeight) / 2;

                    Rectangle effectiveRect = new Rectangle(currentX, currentY, currentWidth, currentHeight);
                    if (effectiveRect.contains(e.getPoint())) {
                        hoveredLevelIndex = i;
                        break;
                    }
                }

                // Detectar hover en botón "Volver al Menú" (Solo color, no tamaño)
                hoveredBackButton = backButtonArea.contains(e.getPoint());

                // Solo redibujar si el estado de hover ha cambiado
                if (oldHoveredLevelIndex != hoveredLevelIndex || oldHoveredBackButton != hoveredBackButton) {
                    repaint();
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                // Cuando el mouse sale del panel, desactiva todos los hovers
                if (hoveredLevelIndex != -1 || hoveredBackButton) {
                    hoveredLevelIndex = -1;
                    hoveredBackButton = false;
                    repaint();
                }
            }
        });
    }

    /**
     * Dibuja los elementos del panel de selección de niveles.
     * @param g El objeto Graphics usado para dibujar.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
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

        // --- Dibujar el letrero del título "NIVELES" ---
        g2d.setColor(TITLE_ORANGE);
        g2d.fillRect(levelsTitleSignX, levelsTitleSignY, levelsTitleSignWidth, levelsTitleSignHeight);
        g2d.setColor(Color.BLACK); // Borde
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(levelsTitleSignX, levelsTitleSignY, levelsTitleSignWidth, levelsTitleSignHeight);

        // Dibujar el texto del título
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 70)); // Misma fuente que el MainMenu title
        drawCenteredString(g2d, "NIVELES", levelsTitleSignX, levelsTitleSignY, levelsTitleSignWidth, levelsTitleSignHeight);

        // --- Dibujar los botones de nivel (Nivel 1, 2, 3) y las líneas horizontales ---
        // Dibujar la línea horizontal principal que conecta los niveles
        g2d.setColor(SIGN_BROWN.darker());
        int horizontalLineY = levelButtonAreas[0].y + levelButtonAreas[0].height / 2; // Centro vertical de los botones de nivel
        int totalHorizontalLineLength = (levelButtonAreas[2].x + levelButtonAreas[2].width) - levelButtonAreas[0].x;
        // Ajustar el inicio y fin de la línea para que sobrepase un poco los botones laterales
        int lineStartX = levelButtonAreas[0].x - 20; // Extender 20px a la izquierda del Nivel 1
        int lineEndX = (levelButtonAreas[2].x + levelButtonAreas[2].width) + 20; // Extender 20px a la derecha del Nivel 3
        int lineThickness = 10; // Grosor de la línea
        g2d.fillRect(lineStartX, horizontalLineY - lineThickness / 2, lineEndX - lineStartX, lineThickness);

        for (int i = 0; i < levelButtonAreas.length; i++) {
            Rectangle currentArea = levelButtonAreas[i];
            String levelText = "Nivel " + (i + 1);

            // Ajustar tamaño si está en hover
            int drawWidth = (i == hoveredLevelIndex) ? LEVEL_BUTTON_HOVER_WIDTH : LEVEL_BUTTON_NORMAL_WIDTH;
            int drawHeight = (i == hoveredLevelIndex) ? LEVEL_BUTTON_HOVER_HEIGHT : LEVEL_BUTTON_NORMAL_HEIGHT;
            int drawX = currentArea.x + (LEVEL_BUTTON_NORMAL_WIDTH - drawWidth) / 2; // Mantener centrado
            int drawY = currentArea.y + (LEVEL_BUTTON_NORMAL_HEIGHT - drawHeight) / 2; // Mantener centrado

            // Dibujar tablón
            g2d.setColor(SIGN_BROWN);
            g2d.fillRect(drawX, drawY, drawWidth, drawHeight);

            // Dibujar borde
            g2d.setColor((i == hoveredLevelIndex) ? Color.CYAN : Color.WHITE);
            g2d.setStroke(new BasicStroke((i == hoveredLevelIndex) ? 3 : 2));
            g2d.drawRect(drawX, drawY, drawWidth, drawHeight);

            // Dibujar texto
            g2d.setColor((i == hoveredLevelIndex) ? Color.CYAN : Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, (i == hoveredLevelIndex) ? LEVEL_TEXT_FONT_HOVER_SIZE : LEVEL_TEXT_FONT_NORMAL_SIZE));
            drawCenteredString(g2d, levelText, drawX, drawY, drawWidth, drawHeight);
        }

        // --- Dibujar el botón "Volver al Menú" ---
        // NO SE AGRANDA, solo cambia de color
        int backDrawWidth = BACK_BUTTON_WIDTH;
        int backDrawHeight = BACK_BUTTON_HEIGHT;
        int backDrawX = backButtonArea.x;
        int backDrawY = backButtonArea.y;

        // Dibujar tablón
        g2d.setColor(SIGN_BROWN);
        g2d.fillRect(backDrawX, backDrawY, backDrawWidth, backDrawHeight);

        // Dibujar borde
        g2d.setColor((hoveredBackButton) ? Color.CYAN : Color.WHITE);
        g2d.setStroke(new BasicStroke((hoveredBackButton) ? 3 : 2));
        g2d.drawRect(backDrawX, backDrawY, backDrawWidth, backDrawHeight);

        // Dibujar texto
        g2d.setColor((hoveredBackButton) ? Color.CYAN : Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, BACK_BUTTON_FONT_SIZE)); // Siempre el tamaño normal
        drawCenteredString(g2d, "Volver al Menú", backDrawX, backDrawY, backDrawWidth, backDrawHeight);
    }

    /**
     * Método auxiliar para dibujar una cadena de texto centrada dentro de un rectángulo.
     */
    private void drawCenteredString(Graphics2D g2d, String text, int rectX, int rectY, int rectWidth, int rectHeight) {
        FontMetrics metrics = g2d.getFontMetrics();
        int x = rectX + (rectWidth - metrics.stringWidth(text)) / 2;
        int y = rectY + ((rectHeight - metrics.getHeight()) / 2) + metrics.getAscent();
        g2d.drawString(text, x, y);
    }

    /**
     * Este método se llama al resetear la selección de nivel.
     * Solo necesita forzar un redibujado.
     */
    public void resetSelection() {
        hoveredLevelIndex = -1;
        hoveredBackButton = false;
        repaint();
    }
}