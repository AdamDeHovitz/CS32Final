package edu.brown.cs.user.CS32Final.SQL;

import java.sql.*;

public class SqliteDatabase {
    private Connection connection;

    public SqliteDatabase(String db) {
        try {
            makeConnection(db);
        } catch (SQLException e) {
            System.out.println("ERROR: SQL error");
        } catch (ClassNotFoundException e) {
            System.out.println("ERROR: Class not found");
        }
    }

    public void createTables() {
        // TODO: write query
        String sql = "CREATE TABLE ";
        try {
            PreparedStatement prep = connection.prepareStatement(sql);
            prep.executeUpdate();

        } catch (SQLException e) {
            System.out.println("ERROR: SQL error");
        }
    }

    private void makeConnection(String db) throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        String urlToDB = "jdbc:sqlite:" + db;
        connection = DriverManager.getConnection(urlToDB);
        Statement stat = connection.createStatement();
        stat.executeUpdate("PRAGMA foreign_keys = ON;");
    }
}
