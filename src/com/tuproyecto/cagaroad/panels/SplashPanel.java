package com.tuproyecto.cagaroad.panels;

import com.tuproyecto.cagaroad.GameFrame;
import com.tuproyecto.cagaroad.GameState;
import com.tuproyecto.cagaroad.utils.AssetLoader;
import com.tuproyecto.cagaroad.utils.GameConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Representa la pantalla de presentación (splash screen) del juego.
 * Muestra información del proyecto y pasa automáticamente al menú principal después de un tiempo.
 */
public class SplashPanel extends JPanel {

    private GameFrame gameFrame;
    private BufferedImage utpLogo; // Imagen del logo de la UTP
    private BufferedImage fiscLogo; // Imagen del logo de la FISC

    // Dimensiones deseadas para los logos
    private static final int LOGO_WIDTH = 120;
    private static final int LOGO_HEIGHT = 120;
    private static final int LOGO_TOP_MARGIN = 20;

    // ¡MODIFICADO! Tamaños de fuente para los textos
    private static final int MAIN_TITLE_FONT_SIZE = 24; // <-- ¡AJUSTADO: Reducido de 28 a 24!
    private static final int PROJECT_TITLE_FONT_SIZE = 32;
    private static final int INFO_FONT_SIZE = 18;

    /**
     * Constructor del SplashPanel.
     * @param gameFrame La referencia al GameFrame para cambiar de estado.
     */
    public SplashPanel(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        setPreferredSize(new Dimension(GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT));
        setBackground(new Color(0, 51, 102)); // Un color azul oscuro para el fondo

        // Intenta cargar los logos.
        try {
            utpLogo = AssetLoader.loadImage("utp_logo.png");
            fiscLogo = AssetLoader.loadImage("fisc_logo.png");
        } catch (IOException e) {
            System.err.println("Error al cargar logos: " + e.getMessage());
            utpLogo = null;
            fiscLogo = null;
        }

        // Configura un Timer para cambiar automáticamente al menú principal después de 10 segundos
        Timer timer = new Timer(10000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameFrame.setGameState(GameState.MENU);
                ((Timer)e.getSource()).stop();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * Dibuja los componentes del panel: texto del proyecto y logos.
     * @param g El objeto Graphics usado para dibujar.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2d.setColor(Color.WHITE); // Color del texto

        // --- Dibujar los logos primero, en la parte superior ---
        if (utpLogo != null) {
            int utpLogoX = GameConstants.GAME_WIDTH / 4 - LOGO_WIDTH / 2;
            g2d.drawImage(utpLogo, utpLogoX, LOGO_TOP_MARGIN, LOGO_WIDTH, LOGO_HEIGHT, null);
        } else {
            g2d.setFont(new Font("Arial", Font.PLAIN, INFO_FONT_SIZE));
            g2d.drawString("UTP Logo Missing", GameConstants.GAME_WIDTH / 4 - 50, LOGO_TOP_MARGIN + LOGO_HEIGHT / 2);
        }
        if (fiscLogo != null) {
            int fiscLogoX = 3 * GameConstants.GAME_WIDTH / 4 - LOGO_WIDTH / 2;
            g2d.drawImage(fiscLogo, fiscLogoX, LOGO_TOP_MARGIN, LOGO_WIDTH, LOGO_HEIGHT, null);
        } else {
            g2d.setFont(new Font("Arial", Font.PLAIN, INFO_FONT_SIZE));
            g2d.drawString("FISC Logo Missing", 3 * GameConstants.GAME_WIDTH / 4 - 50, LOGO_TOP_MARGIN + LOGO_HEIGHT / 2);
        }

        // --- Dibujar los títulos principales (debajo de los logos) ---
        int currentY = LOGO_TOP_MARGIN + LOGO_HEIGHT + 30; // Espacio entre logos y primer título

        g2d.setFont(new Font("Arial", Font.BOLD, MAIN_TITLE_FONT_SIZE)); // Usa el tamaño de fuente ajustado
        drawCenteredString(g2d, "UNIVERSIDAD TECNOLÓGICA DE PANAMÁ", currentY);
        currentY += g2d.getFontMetrics().getHeight(); // Mueve Y a la siguiente línea

        drawCenteredString(g2d, "FACULTAD DE INGENIERÍA DE SISTEMAS COMPUTACIONALES", currentY);
        currentY += g2d.getFontMetrics().getHeight();

        drawCenteredString(g2d, "DEPARTAMENTO DE PROGRAMACIÓN", currentY);
        currentY += g2d.getFontMetrics().getHeight() + 20; // Más espacio después del departamento

        g2d.setFont(new Font("Arial", Font.BOLD, PROJECT_TITLE_FONT_SIZE));
        drawCenteredString(g2d, "Proyecto Final - CAGAROAD", currentY);
        currentY += g2d.getFontMetrics().getHeight() + 40; // Más espacio antes de la información del grupo

        // --- Dibujar la información del proyecto ---
        g2d.setFont(new Font("Arial", Font.PLAIN, INFO_FONT_SIZE));
        drawCenteredString(g2d, "Profesor: Rodrigo Yángüez", currentY);
        currentY += g2d.getFontMetrics().getHeight();

        drawCenteredString(g2d, "Estudiante(s): Kahil Reyna, Rogelin Cortez, Mario Guillen", currentY);
        currentY += g2d.getFontMetrics().getHeight();

        drawCenteredString(g2d, "Grupo: 1SF125", currentY);
        currentY += g2d.getFontMetrics().getHeight();
    }

    /**
     * Método auxiliar para dibujar una cadena de texto centrada horizontalmente.
     * @param g2d El objeto Graphics2D para dibujar.
     * @param text La cadena de texto a centrar.
     * @param y La coordenada Y donde se dibujará la línea base del texto.
     */
    private void drawCenteredString(Graphics2D g2d, String text, int y) {
        FontMetrics metrics = g2d.getFontMetrics();
        int x = (GameConstants.GAME_WIDTH - metrics.stringWidth(text)) / 2;
        g2d.drawString(text, x, y);
    }
}