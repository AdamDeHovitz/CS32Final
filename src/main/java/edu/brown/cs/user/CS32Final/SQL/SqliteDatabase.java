package edu.brown.cs.user.CS32Final.SQL;

/**
 * Created by lc50 on 4/10/16.
 */
import edu.brown.cs.user.CS32Final.Entities.Account.Account;
import edu.brown.cs.user.CS32Final.Entities.Account.Profile;
import edu.brown.cs.user.CS32Final.Entities.Event.Event;

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
        String user = "CREATE TABLE IF NOT EXISTS user(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "email TEXT, " +
                "password TEXT, " +
                "name TEXT, " +
                "image TEXT)";

        String review = "CREATE TABLE IF NOT EXISTS review(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "name TEXT, " +
                "message TEXT, " +
                "rating REAL, " +
                "target_id INTEGER)";

        String event = "CREATE TABLE IF NOT EXISTS event(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "owner_id INTEGER, " +
                "state TEXT, " +
                "name TEXT, " +
                "description TEXT, " +
                "image TEXT, " +
                "member_capacity INT, " +
                "cost REAL, " +
                "location TEXT, " +
                "tags TEXT)";

        String userEvent = "CREATE TABLE IF NOT EXISTS user_event(" +
                "event_id INTEGER, " +
                "user_id INTEGER, " +
                "owner_id INTEGER)";

        String userRequest = "CREATE TABLE IF NOT EXISTS user_request(" +
                "event_id INTEGER, " +
                "user_id INTEGER, " +
                "owner_id INTEGER)";

        String message = "CREATE TABLE IF NOT EXISTS message(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "event_id INTEGER, " +
                "name TEXT, " +
                "message TEXT)";

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

    public void insertUser(String email, String password, String name, String image) {

        try {
            String sql = "INSERT INTO user VALUES (?, ?, ?, ?)";
            PreparedStatement prep = connection.prepareStatement(sql);
            prep.setString(1, email);
            prep.setString(2, password);
            prep.setString(3, name);
            prep.setString(4, image);

            prep.executeQuery();

        } catch(Exception e) {
            System.out.println("ERROR: SQL error");
            e.printStackTrace();
        }
    }

    public void insertEvent(int owner_id, String state, String name, String description,
                            String image, int member_capacity, double cost, String location, String tags) {

        try {
            String sql = "INSERT INTO event VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement prep = connection.prepareStatement(sql);
            prep.setInt(1, owner_id);
            prep.setString(2, state);
            prep.setString(3, name);
            prep.setString(4, description);
            prep.setString(5, image);
            prep.setInt(6, member_capacity);
            prep.setDouble(7, cost);
            prep.setString(8, location);
            prep.setString(9, tags);

            prep.executeQuery();

        } catch(Exception e){
            System.out.println("ERROR: SQL error");
            e.printStackTrace();
        }
    }

    public void insertReview(int user_id, String name, String message, double rating, String target_id) {

        try {
            String sql = "INSERT INTO review VALUES (?, ?, ?, ?, ?)";
            PreparedStatement prep = connection.prepareStatement(sql);
            prep.setInt(1, user_id);
            prep.setString(2, name);
            prep.setString(3, message);
            prep.setDouble(4, rating);
            prep.setString(5, target_id);

            prep.executeQuery();

        } catch(Exception e){
            System.out.println("ERROR: SQL error");
            e.printStackTrace();
        }
    }

    public void insertMessage(int event_id, String name, String message) {

        try {
            String sql = "INSERT INTO message VALUES (?, ?, ?)";
            PreparedStatement prep = connection.prepareStatement(sql);
            prep.setInt(1, event_id);
            prep.setString(2, name);
            prep.setString(3, message);

            prep.executeQuery();

        } catch(Exception e){
            System.out.println("ERROR: SQL error");
            e.printStackTrace();
        }
    }

    public void insertUserIntoEvent(int event_id, int user_id, int owner_id) {

        try {
            String sql = "INSERT INTO user_event VALUES (?, ?, ?)";
            PreparedStatement prep = connection.prepareStatement(sql);
            prep.setInt(1, event_id);
            prep.setInt(2, user_id);
            prep.setInt(3, owner_id);

            prep.executeQuery();

        } catch(Exception e){
            System.out.println("ERROR: SQL error");
            e.printStackTrace();
        }
    }

    public List<String> findUsersByEventId(String eventId) {
        ResultSet rs = null;
        try {
            String sql = "SELECT user_id FROM user_event WHERE event_id = ?;";
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

    public List<Integer> findMessagesByEventId(String eventId) {
        ResultSet rs = null;
        try {
            String sql = "SELECT id FROM message WHERE event_id = ?;";
            PreparedStatement prep = connection.prepareStatement(sql);
            prep.setString(1, eventId);

            List<Integer> toReturn = new ArrayList<>();
            rs = prep.executeQuery();

            while (rs.next()) {
                int id = rs.getInt(1);
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

    public List<Integer> findEventsByUserId(String userId) {
        ResultSet rs = null;
        try {
            String sql = "SELECT event_id FROM user_event WHERE user_id = ?;";
            PreparedStatement prep = connection.prepareStatement(sql);
            prep.setString(1, userId);

            List<Integer> toReturn = new ArrayList<>();
            rs = prep.executeQuery();

            while (rs.next()) {
                int id = rs.getInt(1);
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

    public List<Integer> findReviewsByUserId(String userId) {
        ResultSet rs = null;
        try {
            String sql = "SELECT id FROM review WHERE target_id = ?;";
            PreparedStatement prep = connection.prepareStatement(sql);
            prep.setString(1, userId);

            List<Integer> toReturn = new ArrayList<>();
            rs = prep.executeQuery();

            while (rs.next()) {
                int id = rs.getInt(1);
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

    public List<Integer> findEventsByOwnerId(String userId) {
        ResultSet rs = null;
        try {
            String sql = "SELECT id FROM event WHERE owner_id = ?;";
            PreparedStatement prep = connection.prepareStatement(sql);
            prep.setString(1, userId);

            List<Integer> toReturn = new ArrayList<>();
            rs = prep.executeQuery();

            while (rs.next()) {
                int id = rs.getInt(1);
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

    public Account findUserByUsername(String username) {
        ResultSet rs = null;
        try {
            String sql = "SELECT id FROM user WHERE email = ?;";
            PreparedStatement prep = connection.prepareStatement(sql);
            prep.setString(1, username);

            rs = prep.executeQuery();

            if (rs.next()) {
                return findUserAccountById(rs.getInt(1));
            }
        } catch(Exception e){
            System.out.println("ERROR: SQL error");
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
        }
        return null;
    }

    public Account findUserAccountById(int id) {
        ResultSet rs = null;
        try {
            String sql = "SELECT email, password FROM user WHERE id = ?;";
            PreparedStatement prep = connection.prepareStatement(sql);
            prep.setInt(1, id);

            rs = prep.executeQuery();

            while (rs.next()) {
                String email = rs.getString(1);
                String password = rs.getString(2);
                Profile profile = findUserProfileById(id);

            }
        } catch(Exception e){
            System.out.println("ERROR: SQL error");
        } finally {
            closeResultSet(rs);
        }
        return null;
    }

    public Profile findUserProfileById(int id) {
        ResultSet rs = null;
        try {
            String sql = "SELECT name, image FROM user WHERE id = ?;";
            PreparedStatement prep = connection.prepareStatement(sql);
            prep.setInt(1, id);

            rs = prep.executeQuery();

            while (rs.next()) {

            }
        } catch(Exception e){
            System.out.println("ERROR: SQL error");
        } finally {
            closeResultSet(rs);
        }
        return null;
    }

    public List<Integer> findRatingsById(int id) {
        ResultSet rs = null;
        try {
            String sql = "SELECT rating FROM review WHERE target_id = ?;";
            PreparedStatement prep = connection.prepareStatement(sql);
            prep.setInt(1, id);

            rs = prep.executeQuery();

            while (rs.next()) {

            }
        } catch(Exception e){
            System.out.println("ERROR: SQL error");
        } finally {
            closeResultSet(rs);
        }
        return null;
    }

    public List<Integer> findReviewsById(int id) {
        ResultSet rs = null;
        try {
            String sql = "SELECT user_id, target_id, rating, message FROM review WHERE id = ?;";
            PreparedStatement prep = connection.prepareStatement(sql);
            prep.setInt(1, id);

            rs = prep.executeQuery();

            while (rs.next()) {
                int user_id = rs.getInt(1);
                int target_id = rs.getInt(2);
                double rating = rs.getDouble(3);
                String text = rs.getString(4);
            }
        } catch(Exception e){
            System.out.println("ERROR: SQL error");
        } finally {
            closeResultSet(rs);
        }
        return null;
    }

    public Event findEventById(int id) {
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
