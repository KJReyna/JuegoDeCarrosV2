// com.tuproyecto.cagaroad.panels.SplashPanel.java
package com.tuproyecto.cagaroad.panels;

import com.tuproyecto.cagaroad.GameFrame;
import com.tuproyecto.cagaroad.GameState;
import com.tuproyecto.cagaroad.utils.AssetLoader; // Para cargar imágenes
import com.tuproyecto.cagaroad.utils.GameConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class SplashPanel extends JPanel {

    private GameFrame gameFrame;
    private BufferedImage utpLogo;
    private BufferedImage fiscLogo;

    public SplashPanel(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        setPreferredSize(new Dimension(GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT));
        setBackground(Color.BLUE); // Un color base

        // Cargar logos
        try {
            utpLogo = AssetLoader.loadImage("utp_logo.png"); // Asegúrate de tener estas imágenes en tu proyecto
            fiscLogo = AssetLoader.loadImage("fisc_logo.png");
        } catch (Exception e) {
            System.err.println("Error al cargar logos: " + e.getMessage());
            // Manejo de excepción: Si los logos no cargan, se dibuja un placeholder
            utpLogo = null;
            fiscLogo = null;
        }

        // Después de un tiempo, cambiar al menú principal
        Timer timer = new Timer(3000, new ActionListener() { // 3 segundos de splash
            @Override
            public void actionPerformed(ActionEvent e) {
                gameFrame.setGameState(GameState.MENU);
                ((Timer)e.getSource()).stop(); // Detiene el timer
            }
        });
        timer.setRepeats(false); // Solo se ejecuta una vez
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Opcional: Para texto más suave
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 36));
        drawCenteredString(g2d, "UNIVERSIDAD TECNOLÓGICA DE PANAMÁ", GameConstants.GAME_HEIGHT / 4 - 30);
        drawCenteredString(g2d, "FACULTAD DE INGENIERÍA DE SISTEMAS COMPUTACIONALES", GameConstants.GAME_HEIGHT / 4);
        drawCenteredString(g2d, "DEPARTAMENTO DE PROGRAMACIÓN", GameConstants.GAME_HEIGHT / 4 + 30);
        drawCenteredString(g2d, "Proyecto Final - CAGAROAD", GameConstants.GAME_HEIGHT / 4 + 60);

        g2d.setFont(new Font("Arial", Font.PLAIN, 20));
        drawCenteredString(g2d, "Facilitador(a): Rodrigo Yángüez", GameConstants.GAME_HEIGHT / 2);
        drawCenteredString(g2d, "Estudiante(s): [Nombres de los integrantes]", GameConstants.GAME_HEIGHT / 2 + 30);
        drawCenteredString(g2d, "Grupo: [Tu Grupo]", GameConstants.GAME_HEIGHT / 2 + 60);
        drawCenteredString(g2d, "Fecha: I Semestre 2025", GameConstants.GAME_HEIGHT / 2 + 90);

        // Dibujar logos (ajusta posiciones y tamaños)
        if (utpLogo != null) {
            g2d.drawImage(utpLogo, GameConstants.GAME_WIDTH / 4 - utpLogo.getWidth() / 2, 50, null);
        } else {
            g2d.drawString("UTP Logo Missing", GameConstants.GAME_WIDTH / 4, 100);
        }
        if (fiscLogo != null) {
            g2d.drawImage(fiscLogo, 3 * GameConstants.GAME_WIDTH / 4 - fiscLogo.getWidth() / 2, 50, null);
        } else {
            g2d.drawString("FISC Logo Missing", 3 * GameConstants.GAME_WIDTH / 4, 100);
        }
    }

    // Método de utilidad para centrar texto
    private void drawCenteredString(Graphics2D g2d, String text, int y) {
        FontMetrics metrics = g2d.getFontMetrics();
        int x = (GameConstants.GAME_WIDTH - metrics.stringWidth(text)) / 2;
        g2d.drawString(text, x, y);
    }
}