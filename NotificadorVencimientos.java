package com.mycompany.sistemacontableapp;

import java.sql.*;
import java.time.LocalDate;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JOptionPane;

public class NotificadorVencimientos extends Thread {

    private final SistemaContableGUI gui;

    public NotificadorVencimientos(SistemaContableGUI gui) {
        this.gui = gui;
    }

    @Override
    public void run() {
        String input = JOptionPane.showInputDialog(null,
            "¿Cada cuántos segundos deseas recibir notificaciones de vencimientos?",
            "Intervalo de notificación", JOptionPane.QUESTION_MESSAGE);

        int segundos = 30; // valor por defecto
        try {
            segundos = Integer.parseInt(input);
        } catch (NumberFormatException ignored) {}

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                verificarVencimientos();
            }
        }, 0, segundos * 1000L);
    }

    private void verificarVencimientos() {
        LocalDate hoy = LocalDate.now();
        LocalDate manana = hoy.plusDays(1);

        String sql = "SELECT * FROM DeclaracionFiscal WHERE fecha IN (?, ?) AND estatus IN ('Pendiente', 'Vencida')";

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(hoy));
            ps.setDate(2, Date.valueOf(manana));

            ResultSet rs = ps.executeQuery();

            StringBuilder mensaje = new StringBuilder("Declaraciones pendientes o vencidas para hoy y mañana:\n");

            boolean hayResultados = false;
            while (rs.next()) {
                mensaje.append("• RFC: ").append(rs.getString("rfc_contribuyente"))
                       .append(" - Fecha: ").append(rs.getDate("fecha"))
                       .append(" - Estatus: ").append(rs.getString("estatus"))
                       .append("\n");
                hayResultados = true;
            }

            if (hayResultados) {
                JOptionPane.showMessageDialog(null, mensaje.toString(),
                    "Recordatorio de Vencimientos", JOptionPane.WARNING_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


