package com.tuproyecto.cagaroad.gameobjects;

import com.tuproyecto.cagaroad.utils.GameConstants;

import java.awt.*;

/**
 * Representa un coche obstáculo que aparece en el camino.
 * Gestiona su posición, color y movimiento.
 */
public class ObstacleCar {
    private int x, y;
    private Color color;

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
     * Dibuja el coche obstáculo usando formas geométricas.
     * @param g2d El objeto Graphics2D usado para dibujar.
     */
    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.fillRect(x, y, GameConstants.CAR_WIDTH, GameConstants.CAR_HEIGHT); // Cuerpo del coche

        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(x, y, GameConstants.CAR_WIDTH, GameConstants.CAR_HEIGHT); // Borde del cuerpo

        // Ventanas
        g2d.setColor(new Color(135, 206, 235)); // Azul cielo para ventanas
        g2d.fillRect(x + 5, y + 10, GameConstants.CAR_WIDTH - 10, GameConstants.CAR_HEIGHT / 4); // Ventana frontal
        g2d.fillRect(x + 5, y + GameConstants.CAR_HEIGHT - GameConstants.CAR_HEIGHT / 4 - 10, GameConstants.CAR_WIDTH - 10, GameConstants.CAR_HEIGHT / 4); // Ventana trasera

        // Ruedas
        g2d.setColor(Color.BLACK);
        g2d.fillRect(x - 5, y + 10, 10, 20); // Rueda delantera izquierda
        g2d.fillRect(x + GameConstants.CAR_WIDTH - 5, y + 10, 10, 20); // Rueda delantera derecha
        g2d.fillRect(x - 5, y + GameConstants.CAR_HEIGHT - 30, 10, 20); // Rueda trasera izquierda
        g2d.fillRect(x + GameConstants.CAR_WIDTH - 5, y + GameConstants.CAR_HEIGHT - 30, 10, 20); // Rueda trasera derecha
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