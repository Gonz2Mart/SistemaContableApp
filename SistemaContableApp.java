package com.mycompany.sistemacontableapp;

import javax.swing.SwingUtilities;

public class SistemaContableApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SistemaContableGUI gui = new SistemaContableGUI();
            gui.setVisible(true);

            // Iniciar hilo notificador para vencimientos
            NotificadorVencimientos notificador = new NotificadorVencimientos(gui);
            notificador.start();
        });
    }
}


