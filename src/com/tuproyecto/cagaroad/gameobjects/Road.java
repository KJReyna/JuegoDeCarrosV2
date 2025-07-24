// com.tuproyecto.cagaroad.gameobjects.Road.java
package com.tuproyecto.cagaroad.gameobjects;

import com.tuproyecto.cagaroad.utils.GameConstants;

import java.awt.*;

public class Road {
    private int roadYOffset = 0; // Para simular el desplazamiento vertical del camino

    public Road() {
        // Constructor, no necesita mucha lógica aquí por ahora
    }

    // Mueve el camino para crear el efecto de desplazamiento
    public void move(int speed) {
        roadYOffset += speed;
        // Cuando el offset es igual o mayor a la altura de un "segmento" del camino,
        // lo reiniciamos para crear un bucle infinito
        if (roadYOffset >= GameConstants.GAME_HEIGHT) { // Puedes ajustar esto para que sea más fluido
            roadYOffset = 0;
        }
    }

    // Dibuja el camino y sus líneas
    public void draw(Graphics2D g2d) {
        // Dibuja el fondo del camino (gris oscuro)
        int roadWidth = GameConstants.GAME_WIDTH - 2 * (GameConstants.GAME_WIDTH / 6); // Ancho de la carretera (sin los "céspedes" laterales)
        int roadX = GameConstants.GAME_WIDTH / 6; // Posición X donde empieza la carretera

        g2d.setColor(GameConstants.ROAD_COLOR);
        g2d.fillRect(roadX, 0, roadWidth, GameConstants.GAME_HEIGHT);

        // Dibuja las líneas divisorias de carriles (amarillas, punteadas)
        g2d.setColor(GameConstants.ROAD_LINE_COLOR);
        int dashLength = 40; // Longitud de cada guion de la línea
        int gapLength = 40;  // Espacio entre guiones

        // Itera para dibujar las líneas para cada carril (si hay más de uno)
        // Por ejemplo, para 3 carriles, hay 2 líneas divisorias
        for (int i = 0; i < GameConstants.NUM_LANES - 1; i++) {
            // Calcula la posición X de la línea divisoria
            int lineX = roadX + (i + 1) * GameConstants.LANE_WIDTH;
            // Dibuja los guiones, teniendo en cuenta el desplazamiento para la animación
            for (int j = -2; j * (dashLength + gapLength) < GameConstants.GAME_HEIGHT + dashLength; j++) {
                int y = (j * (dashLength + gapLength) + roadYOffset) % (GameConstants.GAME_HEIGHT + dashLength + gapLength) - gapLength;
                g2d.fillRect(lineX - 5, y, 10, dashLength); // Dibuja un rectángulo para el guion
            }
        }
    }
}