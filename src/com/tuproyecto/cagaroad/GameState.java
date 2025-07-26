// com.tuproyecto.cagaroad.GameState.java
package com.tuproyecto.cagaroad;
// gestionar el cambio de pantallas
public enum GameState {
    SPLASH,         // Pantalla de presentación
    MENU,           // Menú principal
    LEVEL_SELECT,   // Selección de niveles
    CUSTOMIZE,      // Personalizar el auto
    PLAYING,        // Juego en curso
    VICTORY,        // Victoria
    DEFEAT          // Derrota
}