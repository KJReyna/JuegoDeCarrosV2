// com.tuproyecto.cagaroad.Main.java
package com.tuproyecto.cagaroad;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameFrame gameFrame = new GameFrame();
            gameFrame.setVisible(true); // Hace visible la ventana
        });
    }}

