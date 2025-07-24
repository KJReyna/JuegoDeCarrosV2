package com.tuproyecto.cagaroad.utils;

import java.awt.*;

/**
 * Clase que contiene constantes globales para el juego.
 * Facilita la configuración y el mantenimiento de valores importantes.
 */
public class GameConstants {
    // Dimensiones de la ventana del juego
    public static final int GAME_WIDTH = 800;
    public static final int GAME_HEIGHT = 600;

    // Colores usados en el juego
    public static final Color ROAD_COLOR = Color.DARK_GRAY; // Color de la carretera
    public static final Color ROAD_LINE_COLOR = Color.YELLOW; // Color de las líneas de la carretera
    public static final Color GRASS_COLOR = new Color(50, 150, 50); // Color del "césped" lateral

    // Dimensiones y propiedades de los autos (jugador y obstáculos)
    public static final int CAR_WIDTH = 60;
    public static final int CAR_HEIGHT = 100;

    // ¡NUEVO! Margen para las hitboxes (los bordes del área de colisión serán 5px más pequeños)
    public static final int HITBOX_MARGIN = 5;

    // Velocidades de movimiento
    public static final int PLAYER_SPEED = 10; // Velocidad de movimiento lateral del jugador (en píxeles, aunque no se usa directamente en este diseño de movimiento por carril)
    public static final int GAME_SPEED_BASE = 5; // Velocidad base de desplazamiento del camino y enemigos hacia el jugador

    // Configuración del bucle de juego
    public static final int DELAY = 20; // Milisegundos entre cada actualización del juego (aprox. 50 FPS)

    // Configuración de la puntuación
    public static final int SCORE_INTERVAL_MS = 5000; // Intervalo de tiempo (en ms) para sumar puntos
    public static final int SCORE_PER_INTERVAL = 10; // Puntos ganados por cada intervalo de tiempo

    // Configuración de los carriles de la carretera
    public static final int NUM_LANES = 3; // Número de carriles en la carretera
    // Calcula el ancho de cada carril. Los 2 * (GAME_WIDTH / 6) son los márgenes de "césped".
    public static final int LANE_WIDTH = (GameConstants.GAME_WIDTH - 2 * (GameConstants.GAME_WIDTH / 6)) / GameConstants.NUM_LANES;

    // ¡NUEVO! Configuración para la generación de obstáculos
    public static final long OBSTACLE_SPAWN_INTERVAL_MS = 1000; // Cada cuánto se intenta generar una nueva "oleada" de obstáculos (en ms)
    public static final int MIN_LANES_FREE_IN_WAVE = 1; // Mínimo de carriles libres en cada oleada (ej: si son 3 carriles, al menos 1 quedará libre)
    // Esto evita patrones imposibles (todos los carriles bloqueados).

    // ¡NUEVO! Longitudes de los niveles
    public static final int BASE_LEVEL_LENGTH = 5000; // Longitud base para el Nivel 1 (en "unidades de progreso")
    public static final int[] LEVEL_LENGTHS = {
            Integer.MAX_VALUE, // Índice 0: Usado para Modo Infinito (nunca termina)
            BASE_LEVEL_LENGTH, // Índice 1: Nivel 1
            (int)(BASE_LEVEL_LENGTH * 1.5), // Índice 2: Nivel 2 (50% más largo)
            (int)(BASE_LEVEL_LENGTH * 2.0)  // Índice 3: Nivel 3 (100% más largo)
    };
}