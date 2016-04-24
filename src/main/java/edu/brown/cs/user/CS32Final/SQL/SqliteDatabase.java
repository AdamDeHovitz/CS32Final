package edu.brown.cs.user.CS32Final.SQL;

/**
 * Created by lc50 on 4/10/16.
 */
import edu.brown.cs.user.CS32Final.Entities.Account.Account;
import edu.brown.cs.user.CS32Final.Entities.Account.Profile;
import edu.brown.cs.user.CS32Final.Entities.Account.Review;
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
                "first_name TEXT, " +
                "last_name TEXT, " +
                "image TEXT, " +
                "date TEXT, " +
                "requestNotif INTEGER, " +
                "joinedNotif INTEGER)";

        String review = "CREATE TABLE IF NOT EXISTS review(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
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
                "user_id INTEGER, " +
                "message TEXT)";

        try {
            Statement prep = connection.createStatement();
            prep.addBatch(user);
            prep.addBatch(review);
            prep.addBatch(event);
            prep.addBatch(userEvent);
            prep.addBatch(message);
            prep.addBatch(userRequest);

            prep.executeBatch();

        } catch (SQLException e) {
            System.out.println("ERROR: SQL error");
        }
    }

    public void insertUser(String email, String password, String first_name, String last_name, String image, String date) {

        try {
            String sql = "INSERT INTO user VALUES (?, ?, ?, ?, ?)";
            PreparedStatement prep = connection.prepareStatement(sql);
            prep.setString(1, email);
            prep.setString(2, password);
            prep.setString(3, first_name);
            prep.setString(4, last_name);
            prep.setString(5, image);
            prep.setString(6, date);

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

    public void insertReview(int user_id, String message, double rating, String target_id) {

        try {
            String sql = "INSERT INTO review VALUES (?, ?, ?, ?)";
            PreparedStatement prep = connection.prepareStatement(sql);
            prep.setInt(1, user_id);
            prep.setString(2, message);
            prep.setDouble(3, rating);
            prep.setString(4, target_id);

            prep.executeQuery();

        } catch(Exception e) {
            System.out.println("ERROR: SQL error");
            e.printStackTrace();
        }
    }

    public void insertMessage(int event_id, int user_id, String message) {

        try {
            String sql = "INSERT INTO message VALUES (?, ?, ?)";
            PreparedStatement prep = connection.prepareStatement(sql);
            prep.setInt(1, event_id);
            prep.setInt(2, user_id);
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

    public List<String> findUsersByEventId(Integer eventId) {
        ResultSet rs = null;
        try {
            String sql = "SELECT user_id FROM user_event WHERE event_id = ?;";
            PreparedStatement prep = connection.prepareStatement(sql);
            prep.setInt(1, eventId);

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

    public List<Integer> findMessagesByEventId(Integer eventId) {
        ResultSet rs = null;
        try {
            String sql = "SELECT id FROM message WHERE event_id = ?;";
            PreparedStatement prep = connection.prepareStatement(sql);
            prep.setInt(1, eventId);

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

    public List<Integer> findEventsByUserId(Integer userId) {
        ResultSet rs = null;
        try {
            String sql = "SELECT event_id FROM user_event WHERE user_id = ?;";
            PreparedStatement prep = connection.prepareStatement(sql);
            prep.setInt(1, userId);

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

    public List<Integer> findReviewIDsByUserId(Integer userId) {
        ResultSet rs = null;
        try {
            String sql = "SELECT id FROM review WHERE target_id = ?;";
            PreparedStatement prep = connection.prepareStatement(sql);
            prep.setInt(1, userId);

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

    public List<Review> findReviewsByUserId(Integer userId) {ResultSet rs = null;
        try {
            String sql = "SELECT user_id, rating, message FROM review FROM review WHERE target_id = ?;";
            PreparedStatement prep = connection.prepareStatement(sql);
            prep.setInt(1, userId);

            List<Review> toReturn = new ArrayList<>();
            rs = prep.executeQuery();

            while (rs.next()) {
                int user_id = rs.getInt(1);
                double rating = rs.getDouble(2);
                String text = rs.getString(3);
                toReturn.add(new Review(user_id, userId, rating, text ));
            }
            return toReturn;
        } catch(Exception e){
            System.out.println("ERROR: SQL error");
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
        }
        return null;}

    public List<Integer> findEventsByOwnerId(Integer userId) {
        ResultSet rs = null;
        try {
            String sql = "SELECT id FROM event WHERE owner_id = ?;";
            PreparedStatement prep = connection.prepareStatement(sql);
            prep.setInt(1, userId);

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
    public List<Integer> findEventsByRequestedId(Integer userId) {
        ResultSet rs = null;
        try {
            String sql = "SELECT id FROM user_request WHERE owner_id = ?;";
            PreparedStatement prep = connection.prepareStatement(sql);
            prep.setInt(1, userId);

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
            String sql = "SELECT email, password, requestNotif, joinedNotif FROM user WHERE id = ?;";
            PreparedStatement prep = connection.prepareStatement(sql);
            prep.setInt(1, id);

            rs = prep.executeQuery();

            while (rs.next()) {
                String email = rs.getString(1);
                String password = rs.getString(2);
                int requestNotif = rs.getInt(3);
                int joinedNotif = rs.getInt(4);

                Profile profile = findUserProfileById(id);
                return new Account(profile, id, email, password, requestNotif, joinedNotif);
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
            String sql = "SELECT name, image, date FROM user WHERE id = ?;";
            PreparedStatement prep = connection.prepareStatement(sql);
            prep.setInt(1, id);

            rs = prep.executeQuery();

            while (rs.next()) {
                String name = rs.getString(1);
                String image = rs.getString(2);
                String date = rs.getString(3);
                List<Integer> reviews = findReviewsById(id);

                return new Profile(name, image, date, reviews);
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
            String sql = "SELECT id FROM review WHERE target_id = ?;";
            PreparedStatement prep = connection.prepareStatement(sql);
            prep.setInt(1, id);

            List<Integer> toReturn = new ArrayList<>();

            rs = prep.executeQuery();

            while (rs.next()) {
                int review_id = rs.getInt(1);

                toReturn.add(review_id);
            }

            return toReturn;
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
