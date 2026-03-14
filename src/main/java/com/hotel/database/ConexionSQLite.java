package com.hotel.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class ConexionSQLite {
    private static final String URL = "jdbc:sqlite:db/hotel_precheckin.db";

    public static Connection conectar() {
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(URL);

            // Activar claves foráneas en SQLite
            Statement stmt = conn.createStatement();
            stmt.execute("PRAGMA foreign_keys = ON");

            System.out.println("Conexión establecida con la base de datos");

        } catch (SQLException e) {
            System.out.println("Error en la conexión");
            e.printStackTrace();
        }

        return conn;
    }
}
