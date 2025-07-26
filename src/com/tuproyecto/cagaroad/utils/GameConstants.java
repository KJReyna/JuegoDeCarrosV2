package com.tuproyecto.cagaroad.utils;

import java.awt.*;

/**
 * Clase que contiene constantes globales para el juego.
 * Facilita la configuración y el mantenimiento de valores importantes.
 */
public class GameConstants {
    // Dimensiones de la ventana
    public static final int GAME_WIDTH = 800;
    public static final int GAME_HEIGHT = 600;

    // Colores del juego
    public static final Color ROAD_COLOR = Color.DARK_GRAY; // Color de la carretera
    public static final Color ROAD_LINE_COLOR = Color.YELLOW; // Color de las líneas de la carretera
    public static final Color GRASS_COLOR = new Color(50, 150, 50); // Color del "césped" lateral

    // Dimensiones y propiedades de los autos (volver a las dimensiones que quieres, ajusta si es necesario)
    public static final int CAR_WIDTH = 60; // Ancho del auto (vuelve al original o ajusta a tu gusto)
    public static final int CAR_HEIGHT = 100; // Alto del auto (vuelve al original o ajusta a tu gusto)

    public static final int HITBOX_MARGIN = 5; // Margen para las hitboxes

    // Velocidades
    public static final int PLAYER_SPEED = 10; // Velocidad de movimiento lateral del jugador
    public static final int GAME_SPEED_BASE = 6; // Velocidad base de desplazamiento del camino y enemigos hacia el jugador

    // Configuración del bucle de juego
    public static final int DELAY = 20; // Milisegundos entre cada actualización del juego (aprox. 50 FPS)

    // Configuración de la puntuación
    public static final int SCORE_INTERVAL_MS = 5000; // Intervalo de tiempo (en ms) para sumar puntos
    public static final int SCORE_PER_INTERVAL = 10; // Puntos ganados por cada intervalo de tiempo

    // Configuración de los carriles de la carretera
    public static final int NUM_LANES = 3; // Número de carriles en la carretera
    public static final int LANE_WIDTH = (GameConstants.GAME_WIDTH - 2 * (GameConstants.GAME_WIDTH / 6)) / GameConstants.NUM_LANES;

    // Configuración para la generación de obstáculos
    public static final long OBSTACLE_SPAWN_INTERVAL_MS = 1000; // Cada cuánto se intenta generar una nueva "oleada" de obstáculos (en ms)
    public static final int MIN_LANES_FREE_IN_WAVE = 1; // Mínimo de carriles libres en cada oleada

    // Longitudes de los niveles
    public static final int BASE_LEVEL_LENGTH = 5000; // Longitud base para el Nivel 1
    public static final int[] LEVEL_LENGTHS = {
            Integer.MAX_VALUE, // Índice 0: Usado para Modo Infinito
            BASE_LEVEL_LENGTH, // Índice 1: Nivel 1
            (int)(BASE_LEVEL_LENGTH * 1.5), // Índice 2: Nivel 2 (50% más largo)
            (int)(BASE_LEVEL_LENGTH * 2.0)  // Índice 3: Nivel 3 (100% más largo)
    };
}