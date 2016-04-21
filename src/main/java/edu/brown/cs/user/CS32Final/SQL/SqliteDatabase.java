package edu.brown.cs.user.CS32Final.SQL;

/**
 * Created by lc50 on 4/10/16.
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
        String user = "CREATE TABLE user(" +
                "id TEXT, " +
                "email TEXT, " +
                "name TEXT, " +
                "image TEXT, " +
                "rating REAL, " +
                "PRIMARY KEY (id))";

        String review = "CREATE TABLE review(" +
                "id TEXT, " +
                "user_id TEXT, " +
                "name TEXT, " +
                "message TEXT, " +
                "target_id TEXT, " +
                "PRIMARY KEY(id))";

        String event = "CREATE TABLE event(" +
                "id TEXT, " +
                "state TEXT, " +
                "name TEXT, " +
                "description TEXT, " +
                "image TEXT, " +
                "owner_id TEXT, " +
                "member_capacity INT, " +
                "cost REAL, " +
                "location TEXT, " +
                "tags TEXT, " +
                "PRIMARY KEY (id))";

        String userEvent = "CREATE TABLE user_event(" +
                "user_id TEXT, " +
                "event_id TEXT, " +
                "owner_id TEXT)";

        String message = "CREATE TABLE message(" +
                "id TEXT, " +
                "name TEXT, " +
                "message TEXT, " +
                "event_id TEXT, " +
                "PRIMARY KEY (id))";

        try {
            Statement prep = connection.createStatement();
            prep.addBatch(user);
            prep.addBatch(review);
            prep.addBatch(event);
            prep.addBatch(userEvent);
            prep.addBatch(message);

            prep.executeBatch();

        } catch (SQLException e) {
            System.out.println("ERROR: SQL error");
        }
    }

    public List<String> findUsersByEventId(String eventId) {
        ResultSet rs = null;
        try {
            String sql = "SELECT user_id FROM user_event WHERE event_id >= ?;";
            PreparedStatement prep = connection.prepareStatement(sql);
            prep.setString(1, eventId);

            List<String> toReturn = new ArrayList<>();
            rs = prep.executeQuery();

            while (rs.next()) {
                String id = rs.getString(1);
                toReturn.add(id);
            }
            return toReturn;
        } catch(Exception e){
            System.out.println("ERROR: SQL error");
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
        }
        return null;
    }

    public List<String> findMessagesByEventId(String eventId) {
        ResultSet rs = null;
        try {
            String sql = "SELECT id FROM message WHERE event_id >= ?;";
            PreparedStatement prep = connection.prepareStatement(sql);
            prep.setString(1, eventId);

            List<String> toReturn = new ArrayList<>();
            rs = prep.executeQuery();

            while (rs.next()) {
                String id = rs.getString(1);
                toReturn.add(id);
            }
            return toReturn;
        } catch(Exception e){
            System.out.println("ERROR: SQL error");
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
        }
        return null;
    }

    public List<String> findEventsByUserId(String userId) {
        ResultSet rs = null;
        try {
            String sql = "SELECT event_id FROM user_event WHERE user_id >= ?;";
            PreparedStatement prep = connection.prepareStatement(sql);
            prep.setString(1, userId);

            List<String> toReturn = new ArrayList<>();
            rs = prep.executeQuery();

            while (rs.next()) {
                String id = rs.getString(1);
                toReturn.add(id);
            }
            return toReturn;
        } catch(Exception e){
            System.out.println("ERROR: SQL error");
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
        }
        return null;
    }

    public List<String> findReviewsByUserId(String userId) {
        ResultSet rs = null;
        try {
            String sql = "SELECT id FROM review WHERE target_id >= ?;";
            PreparedStatement prep = connection.prepareStatement(sql);
            prep.setString(1, userId);

            List<String> toReturn = new ArrayList<>();
            rs = prep.executeQuery();

            while (rs.next()) {
                String id = rs.getString(1);
                toReturn.add(id);
            }
            return toReturn;
        } catch(Exception e){
            System.out.println("ERROR: SQL error");
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
        }
        return null;
    }

    public List<String> findEventsByOwnerId(String userId) {
        ResultSet rs = null;
        try {
            String sql = "SELECT id FROM event WHERE owner_id >= ?;";
            PreparedStatement prep = connection.prepareStatement(sql);
            prep.setString(1, userId);

            List<String> toReturn = new ArrayList<>();
            rs = prep.executeQuery();

            while (rs.next()) {
                String id = rs.getString(1);
                toReturn.add(id);
            }
            return toReturn;
        } catch(Exception e){
            System.out.println("ERROR: SQL error");
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
        }
        return null;
    }

    private void makeConnection(String db) throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        String urlToDB = "jdbc:sqlite:" + db;
        connection = DriverManager.getConnection(urlToDB);
        Statement stat = connection.createStatement();
        stat.executeUpdate("PRAGMA foreign_keys = ON;");
    }

    private void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.out.println("ERROR: can't close result set");
            }
        }
    }
}
