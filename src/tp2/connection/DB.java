package tp2.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
    // private static final String URL = "jdbc:mysql://i4g.h.filess.io:3307/klinikTP2_overzebra";
    // private static final String USERNAME = "klinikTP2_overzebra";
    // private static final String PASSWORD = "5453bdf12f8c5b9708467890c1c914b20260ca3b";

    // private static final String URL = "jdbc:mysql://127.0.0.1:3306/klinikTP2_overzebra";
    // private static final String USERNAME = "root";
    // private static final String PASSWORD = null;

    private static final String URL = "jdbc:mysql://bq1yh7aui3srpezp1trx-mysql.services.clever-cloud.com:3306/bq1yh7aui3srpezp1trx";
    private static final String USERNAME = "uj9qybjr8b4gihyj";
    private static final String PASSWORD = "V9XRzsJHFW0IUsx64pGL";

    public static Connection connect() {
        Connection connection = null;
        try {
            // Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Mencoba membuat koneksi ke database
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            if (connection != null) {
                // System.out.println("Koneksi berhasil.");
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Koneksi gagal: " + e.getMessage());
        }
        return connection;
    }

    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                // System.out.println("Koneksi ditutup.");
            } catch (SQLException e) {
                System.out.println("Gagal menutup koneksi: " + e.getMessage());
            }
        }
    }
}
