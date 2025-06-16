package com.mycompany.sistemacontableapp;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;

public class SistemaContableGUI extends JFrame {

    private JButton btnInsertarDeclaracion;

    public SistemaContableGUI() {
        setTitle("Sistema Contable");
        setSize(400, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        btnInsertarDeclaracion = new JButton("Insertar Declaración Fiscal");
        add(btnInsertarDeclaracion);

        btnInsertarDeclaracion.addActionListener(e -> {
            VentanaInsertarDeclaracion ventanaInsertar = new VentanaInsertarDeclaracion(this);
            ventanaInsertar.setVisible(true);
        });
    }

    // Método para llamar al procedimiento almacenado
    public boolean insertarDeclaracion(String rfc, String tipo, double monto, Date fecha, String estatus) {
        return DatabaseHelper.insertarDeclaracion(rfc, tipo, monto, fecha, estatus);
    }
}




