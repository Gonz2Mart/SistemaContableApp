package com.mycompany.sistemacontableapp;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;

public class VentanaInsertarDeclaracion extends JDialog {

    private JTextField txtRFC, txtTipo, txtMonto, txtFecha, txtEstatus;
    private JButton btnInsertar;
    private SistemaContableGUI parent;

    public VentanaInsertarDeclaracion(SistemaContableGUI parent) {
        super(parent, "Insertar Declaración Fiscal", true);
        this.parent = parent;

        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(6, 2, 5, 5));

        add(new JLabel("RFC:"));
        txtRFC = new JTextField();
        add(txtRFC);

        add(new JLabel("Tipo (Mensual, Anual, Complementaria):"));
        txtTipo = new JTextField();
        add(txtTipo);

        add(new JLabel("Monto:"));
        txtMonto = new JTextField();
        add(txtMonto);

        add(new JLabel("Fecha (YYYY-MM-DD):"));
        txtFecha = new JTextField();
        add(txtFecha);

        add(new JLabel("Estatus (Presentada, Pendiente, Vencida):"));
        txtEstatus = new JTextField();
        add(txtEstatus);

        btnInsertar = new JButton("Insertar");
        add(btnInsertar);

        btnInsertar.addActionListener(e -> {
            try {
                String rfc = txtRFC.getText().trim();
                String tipo = txtTipo.getText().trim();
                double monto = Double.parseDouble(txtMonto.getText().trim());
                Date fecha = Date.valueOf(txtFecha.getText().trim());
                String estatus = txtEstatus.getText().trim();

                if (parent.insertarDeclaracion(rfc, tipo, monto, fecha, estatus)) {
                    JOptionPane.showMessageDialog(this, "✅ Declaración insertada correctamente.");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Error al insertar la declaración.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage());
            }
        });
    }
}


