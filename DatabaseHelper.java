package com.mycompany.sistemacontableapp;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

public class DatabaseHelper {
    public static boolean insertarDeclaracion(String rfc, String tipo, double monto, Date fecha, String estatus) {
        String sql = "{CALL insertarDeclaracion(?, ?, ?, ?, ?)}";
        try (Connection con = ConexionBD.getConexion();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, rfc);
            cs.setString(2, tipo);
            cs.setDouble(3, monto);
            cs.setDate(4, fecha);
            cs.setString(5, estatus);

            cs.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}


