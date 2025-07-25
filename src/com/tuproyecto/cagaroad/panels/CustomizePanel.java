package com.tuproyecto.cagaroad.panels;

import com.tuproyecto.cagaroad.GameFrame;
import com.tuproyecto.cagaroad.GameState;
import com.tuproyecto.cagaroad.utils.GameConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.HashSet;
import java.util.Set;

/**
 * Pantalla de personalización con animaciones y sombras.
 */
public class CustomizePanel extends JPanel {

    private GameFrame gameFrame;
    private Color selectedCarColor = Color.RED;
    private RippleButton backButton;
    private final Color[] AVAILABLE_COLORS = {
            Color.BLACK, Color.RED, Color.YELLOW, Color.CYAN, Color.MAGENTA
    };
    private static final int COLOR_BOX_LEFT_MARGIN = 100;

    public CustomizePanel(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        setLayout(null);
        setPreferredSize(new Dimension(GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT));

        // Título con sombra
        JLabel title = new JLabel("Elige tu color");
        title.setFont(new Font("Segoe UI", Font.BOLD, 46));
        title.setForeground(Color.WHITE);
        title.setBounds(0, 40, GameConstants.GAME_WIDTH, 60);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(new EmptyBorder(0,0,0,0));
        add(title);

        // Cuadros con animación y sombra
        int startX = COLOR_BOX_LEFT_MARGIN;
        int startY = GameConstants.GAME_HEIGHT / 2 - 120;
        int boxSize = 60;
        int spacing = 30;

        for (int i = 0; i < AVAILABLE_COLORS.length; i++) {
            Color color = AVAILABLE_COLORS[i];
            AnimatedColorBox colorBox = new AnimatedColorBox(color);
            colorBox.setBounds(startX, startY + i * (boxSize + spacing), boxSize, boxSize);
            colorBox.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    selectedCarColor = color;
                    repaint();
                }
            });
            add(colorBox);
        }

        // Botón “volver” con efecto ripple básico
        backButton = new RippleButton("⮐ Volver al menú");
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        backButton.setForeground(Color.WHITE);
        backButton.setBounds(GameConstants.GAME_WIDTH / 2 - 150, GameConstants.GAME_HEIGHT - 100, 300, 40);
        backButton.addActionListener(e -> gameFrame.setGameState(GameState.MENU));
        add(backButton);

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Fondo degradado con tarjeta semitransparente
        Graphics2D g2 = (Graphics2D) g;
        GradientPaint bg = new GradientPaint(0, 0, new Color(15, 15, 40),
                0, getHeight(), new Color(40, 15, 60));
        g2.setPaint(bg);
        g2.fillRect(0, 0, getWidth(), getHeight());

        // “Tarjeta” de preview del coche
        int cardW = 350, cardH = 200;
        int cardX = COLOR_BOX_LEFT_MARGIN + 180, cardY = GameConstants.GAME_HEIGHT/2 - cardH/2;
        RoundRectangle2D card = new RoundRectangle2D.Float(cardX, cardY, cardW, cardH, 20, 20);
        g2.setColor(new Color(0, 0, 0, 160));
        g2.fill(card);

        // Dibuja coche encima de la tarjeta
        int carX = cardX + (cardW - GameConstants.CAR_WIDTH)/2;
        int carY = cardY + (cardH - GameConstants.CAR_HEIGHT)/2;
        drawCar(g2, selectedCarColor, carX, carY);
    }

    private void drawCar(Graphics2D g2d, Color bodyColor, int x, int y) {
        // Sombra del coche
        g2d.setColor(new Color(0,0,0,80));
        g2d.fillRoundRect(x+5, y+GameConstants.CAR_HEIGHT-10, GameConstants.CAR_WIDTH, 10, 5, 5);

        // Cuerpo
        g2d.setColor(bodyColor);
        g2d.fillRoundRect(x, y, GameConstants.CAR_WIDTH, GameConstants.CAR_HEIGHT, 10, 10);

        // Detalles
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(x, y, GameConstants.CAR_WIDTH, GameConstants.CAR_HEIGHT, 10, 10);

        g2d.setColor(new Color(135,206,235));
        g2d.fillRect(x+5, y+10, GameConstants.CAR_WIDTH-10, GameConstants.CAR_HEIGHT/4);
        g2d.fillRect(x+5, y+GameConstants.CAR_HEIGHT-GameConstants.CAR_HEIGHT/4-10,
                GameConstants.CAR_WIDTH-10, GameConstants.CAR_HEIGHT/4);

        // Ruedas esquemáticas
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillOval(x-5, y+10, 12, 12);
        g2d.fillOval(x+GameConstants.CAR_WIDTH-7, y+10, 12, 12);
        g2d.fillOval(x-5, y+GameConstants.CAR_HEIGHT-22, 12, 12);
        g2d.fillOval(x+GameConstants.CAR_WIDTH-7, y+GameConstants.CAR_HEIGHT-22, 12, 12);
    }

    public Color getSelectedColor() {
        return null;
    }

    /** Caja de color con animación de escala y sombra interna */
    private static class AnimatedColorBox extends JPanel {
        private final Color color;
        private float scale = 1f;

        public AnimatedColorBox(Color color) {
            this.color = color;
            setOpaque(false);
            addMouseListener(hoverListener);
            addMouseMotionListener(hoverListener);
        }

        private final MouseAdapter hoverListener = new MouseAdapter() {
            private final float targetScale = 1.2f;
            private final float normalScale = 1f;
            private Timer animator;

            @Override
            public void mouseEntered(MouseEvent e) {
                startAnimation(scale, targetScale);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                startAnimation(scale, normalScale);
            }

            private void startAnimation(float from, float to) {
                if (animator != null && animator.isRunning()) animator.stop();
                animator = new Timer(15, null);
                animator.addActionListener(evt -> {
                    scale += (to - from) * 0.2f;
                    if (Math.abs(scale - to) < 0.02) {
                        scale = to;
                        animator.stop();
                    }
                    repaint();
                });
                animator.start();
            }
        };

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            int w = getWidth(), h = getHeight();
            int sw = (int) (w * scale), sh = (int) (h * scale);
            int dx = (w - sw) / 2, dy = (h - sh) / 2;

            // Sombra interior
            g2.setColor(new Color(0,0,0,60));
            g2.fillRoundRect(dx+3, dy+3, sw, sh, 20, 20);

            // Círculo de color
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.fillRoundRect(dx, dy, sw, sh, 20, 20);

            // Borde de selección
            if (color.equals(((CustomizePanel) SwingUtilities.getAncestorOfClass(CustomizePanel.class, this)).selectedCarColor)) {
                g2.setStroke(new BasicStroke(4));
                g2.setColor(Color.WHITE);
                g2.drawRoundRect(dx, dy, sw - 1, sh - 1, 20, 20);
            }
            g2.dispose();
        }
    }

    /** Botón simple con efecto “ripple” en JLabel */
    private static class RippleButton extends JButton {
        private final Set<Point> ripples = new HashSet<>();

        public RippleButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(false);
            setForeground(Color.WHITE);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    ripples.add(e.getPoint());
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            int w = getWidth(), h = getHeight();

            // Fondo y hover simple
            if (getModel().isRollover()) {
                g2.setColor(new Color(255,255,255,30));
                g2.fillRoundRect(0, 0, w, h, 15, 15);
            }

            // Dibuja ripples
            g2.setClip(new RoundRectangle2D.Float(0,0,w,h,15,15));
            g2.setColor(new Color(255,255,255,100));
            for (Point p : ripples) {
                int size = (int) ((System.currentTimeMillis() % 400) / 400.0 * w);
                g2.fillOval(p.x - size/2, p.y - size/2, size, size);
            }
            ripples.clear();

            // Texto
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int tx = (w - fm.stringWidth(getText()))/2;
            int ty = (h + fm.getAscent())/2 - 2;
            g2.setColor(getForeground());
            g2.drawString(getText(), tx, ty);

            g2.dispose();
        }
    }
}
