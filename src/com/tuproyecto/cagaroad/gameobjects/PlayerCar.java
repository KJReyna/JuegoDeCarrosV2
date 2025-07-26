package com.tuproyecto.cagaroad.gameobjects;

import com.tuproyecto.cagaroad.utils.GameConstants;

import java.awt.*;

/**
 * Representa el coche del jugador en el juego.
 * Gestiona su posición, color, movimiento entre carriles y detección de colisiones.
 */
public class PlayerCar {
    private int x, y;
    private Color color;
    private int lane;

    /**
     * Constructor del PlayerCar.
     * @param initialColor El color inicial del coche.
     */
    public PlayerCar(Color initialColor) {
        reset(initialColor);
    }

    /**
     * Reinicia la posición del coche del jugador y actualiza su color.
     * @param newColor El nuevo color para el coche.
     */
    public void reset(Color newColor) {
        this.color = newColor;
        this.lane = GameConstants.NUM_LANES / 2;
        this.x = (GameConstants.GAME_WIDTH / 6) + (lane * GameConstants.LANE_WIDTH) + (GameConstants.LANE_WIDTH / 2) - (GameConstants.CAR_WIDTH / 2);
        this.y = GameConstants.GAME_HEIGHT - GameConstants.CAR_HEIGHT - 50;
    }

    public void moveLeft() {
        if (lane > 0) {
            lane--;
            x = (GameConstants.GAME_WIDTH / 6) + (lane * GameConstants.LANE_WIDTH) + (GameConstants.LANE_WIDTH / 2) - (GameConstants.CAR_WIDTH / 2);
        }
    }

    public void moveRight() {
        if (lane < GameConstants.NUM_LANES - 1) {
            lane++;
            x = (GameConstants.GAME_WIDTH / 6) + (lane * GameConstants.LANE_WIDTH) + (GameConstants.LANE_WIDTH / 2) - (GameConstants.CAR_WIDTH / 2);
        }
    }

    /**
     * Dibuja el coche del jugador usando formas geométricas.
     * @param g2d El objeto Graphics2D usado para dibujar.
     */
    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.fillRect(x, y, GameConstants.CAR_WIDTH, GameConstants.CAR_HEIGHT); // Cuerpo del coche

        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2)); // Borde más grueso
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
     * Obtiene un objeto Rectangle que representa los límites de colisión del coche.
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
     * Verifica si este coche (el jugador) colisiona con otro coche (un obstáculo).
     * @param otherCar El ObstacleCar con el que se va a verificar la colisión.
     * @return true si hay colisión, false en caso contrario.
     */
    public boolean checkCollision(ObstacleCar otherCar) {
        return getBounds().intersects(otherCar.getBounds());
    }
}