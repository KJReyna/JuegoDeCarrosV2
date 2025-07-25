// com.tuproyecto.cagaroad.Main.java
package com.tuproyecto.cagaroad;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Asegúrate de que la interfaz de usuario se cree y actualice en el Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            GameFrame gameFrame = new GameFrame();
            gameFrame.setVisible(true); // Hace visible la ventana
        });
    }
} //añadi esto nama pa ver como se agrega al commit