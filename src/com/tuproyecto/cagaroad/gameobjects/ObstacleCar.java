package com.tuproyecto.cagaroad.gameobjects;

import com.tuproyecto.cagaroad.utils.GameConstants;

import java.awt.*;

/**
 * Representa un coche obstáculo que aparece en el camino.
 * Gestiona su posición, color y movimiento.
 */
public class ObstacleCar {
    private int x, y; // Coordenadas X e Y de la esquina superior izquierda del obstáculo
    private Color color; // Color del cuerpo del obstáculo

    /**
     * Constructor del ObstacleCar.
     * @param x La coordenada X inicial del obstáculo.
     * @param y La coordenada Y inicial del obstáculo.
     * @param color El color del obstáculo.
     */
    public ObstacleCar(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    /**
     * Mueve el obstáculo hacia abajo en la pantalla, simulando que el jugador avanza.
     * @param speed La velocidad a la que se mueve el obstáculo.
     */
    public void move(int speed) {
        y += speed;
    }

    /**
     * Dibuja el coche obstáculo en su posición actual.
     * @param g2d El objeto Graphics2D usado para dibujar.
     */
    public void draw(Graphics2D g2d) {
        // Dibuja el cuerpo principal del coche
        g2d.setColor(color);
        g2d.fillRect(x, y, GameConstants.CAR_WIDTH, GameConstants.CAR_HEIGHT);

        // Dibuja un borde negro alrededor del cuerpo del coche
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(x, y, GameConstants.CAR_WIDTH, GameConstants.CAR_HEIGHT);

        // Dibuja las ventanas del coche
        g2d.setColor(new Color(135, 206, 235)); // Azul cielo para las ventanas
        g2d.fillRect(x + 5, y + 10, GameConstants.CAR_WIDTH - 10, GameConstants.CAR_HEIGHT / 4);
        g2d.fillRect(x + 5, y + GameConstants.CAR_HEIGHT - GameConstants.CAR_HEIGHT / 4 - 10, GameConstants.CAR_WIDTH - 10, GameConstants.CAR_HEIGHT / 4);

        // Dibuja las ruedas del coche
        g2d.setColor(Color.BLACK);
        g2d.fillRect(x - 5, y + 10, 10, 20);
        g2d.fillRect(x + GameConstants.CAR_WIDTH - 5, y + 10, 10, 20);
        g2d.fillRect(x - 5, y + GameConstants.CAR_HEIGHT - 30, 10, 20);
        g2d.fillRect(x + GameConstants.CAR_WIDTH - 5, y + GameConstants.CAR_HEIGHT - 30, 10, 20);
    }

    /**
     * Obtiene un objeto Rectangle que representa los límites de colisión del obstáculo.
     * La hitbox es ligeramente más pequeña que el modelo visual.
     * @return Un objeto Rectangle que define la caja delimitadora de la hitbox.
     */
    public Rectangle getBounds() {
        int hitboxX = x + GameConstants.HITBOX_MARGIN;
        int hitboxY = y + GameConstants.HITBOX_MARGIN;
        int hitboxWidth = GameConstants.CAR_WIDTH - (2 * GameConstants.HITBOX_MARGIN);
        int hitboxHeight = GameConstants.CAR_HEIGHT - (2 * GameConstants.HITBOX_MARGIN);
        return new Rectangle(hitboxX, hitboxY, hitboxWidth, hitboxHeight);
    }

    /**
     * Obtiene la coordenada Y actual del obstáculo.
     * Útil para saber cuándo el obstáculo ha salido de la pantalla.
     * @return La coordenada Y del obstáculo.
     */
    public int getY() {
        return y;
    }
}