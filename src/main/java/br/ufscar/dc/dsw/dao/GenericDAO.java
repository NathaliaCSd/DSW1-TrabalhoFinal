package br.ufscar.dc.dsw.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

abstract public class GenericDAO {

    public GenericDAO() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    protected Connection getConnection() throws SQLException {
        String host = System.getenv().getOrDefault("POSTGRES_HOST", "localhost");
        String user = System.getenv().getOrDefault("POSTGRES_USER", "petbnbuser");
String password = System.getenv().getOrDefault("POSTGRES_PASSWORD", "admin");
        return DriverManager.getConnection("jdbc:postgresql://" + host + ":5432/PetBnB", user, password);
    }
}
