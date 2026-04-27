/*package org.example.repository.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL      = "jdbc:mysql://localhost:3306/labaratorijski_eksperimenti_db";
    private static final String USER     = "root";
    private static final String PASSWORD = "projekat_dataB";

    private static DatabaseConnection instance;
    private final Connection connection;

    private DatabaseConnection() throws SQLException {
        this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
    }
    //Dodali smo synchronized kako bismo izbegli kreiranje dve konekcije
    public static synchronized DatabaseConnection getInstance() throws SQLException {
        if (instance == null || instance.connection.isClosed()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}*/

package org.example.repository.connection;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    static {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream("db.cfg"));
            String host = props.getProperty("host").trim();
            String port = props.getProperty("port").trim();
            String db   = props.getProperty("db").trim();
            URL      = "jdbc:mysql://" + host + ":" + port + "/" + db;
            USER     = props.getProperty("user").trim();
            PASSWORD = props.getProperty("password").trim();
        } catch (IOException e) {
            throw new RuntimeException("Ne mogu da učitam db.cfg fajl!", e);
        }
    }

    private static DatabaseConnection instance;
    private final Connection connection;

    private DatabaseConnection() throws SQLException {
        this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Dodali smo synchronized kako bismo izbegli kreiranje dve konekcije
    public static synchronized DatabaseConnection getInstance() throws SQLException {
        if (instance == null || instance.connection.isClosed()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}