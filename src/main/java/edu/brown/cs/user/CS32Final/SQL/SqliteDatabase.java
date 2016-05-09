package edu.brown.cs.user.CS32Final.SQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.brown.cs.user.CS32Final.Entities.Account.*;
import edu.brown.cs.user.CS32Final.Entities.Chat.Message;
import edu.brown.cs.user.CS32Final.Entities.Event.Event;
import edu.brown.cs.user.CS32Final.Entities.Event.EventRequest;
import edu.brown.cs.user.CS32Final.Entities.Event.EventState;

public class SqliteDatabase {
  private Connection connection;

  private SqliteDatabase(String db) {
    try {
      makeConnection(db);
    } catch (SQLException e) {
      System.out.println("ERROR: SQL exception");
    } catch (ClassNotFoundException e) {
      System.out.println("ERROR: Class not found");
    }
  }

  private static SqliteDatabase Instance = new SqliteDatabase(
      "data/database.sqlite3");

  public static SqliteDatabase getInstance() {
    return Instance;
  }

  public void createTables() throws SQLException {
    String user = "CREATE TABLE IF NOT EXISTS user("
        + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "email TEXT, "
        + "password TEXT, " + "first_name TEXT, " + "last_name TEXT, "
        + "image TEXT, " + "date TEXT, " + "requestNotif INTEGER DEFAULT 0, "
        + "joinedNotif INTEGER DEFAULT 0)";

    String review = "CREATE TABLE IF NOT EXISTS review("
        + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "user_id INTEGER, "
        + "message TEXT, " + "rating REAL, " + "target_id INTEGER)";

    String event = "CREATE TABLE IF NOT EXISTS event("
        + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "owner_id INTEGER, "
        + "state TEXT, " + "name TEXT, " + "description TEXT, " + "image TEXT, "
        + "member_capacity INT, " + "cost REAL, " + "location TEXT, "
        + "lat REAL, " + "lng REAL)";

    String eventTags = "CREATE TABLE IF NOT EXISTS event_tags("
        + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "event_id INTEGER, "
        + "tag TEXT)";

    String userEvent = "CREATE TABLE IF NOT EXISTS user_event("
        + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "event_id INTEGER, "
        + "user_id INTEGER, " + "owner_id INTEGER)";

    String userRequest = "CREATE TABLE IF NOT EXISTS user_request("
        + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "event_id INTEGER, "
        + "user_id INTEGER, " + "owner_id INTEGER)";

    String message = "CREATE TABLE IF NOT EXISTS message("
        + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "event_id INTEGER, "
        + "user_id INTEGER, " + "message TEXT, time TEXT)";

    String notification = "CREATE TABLE IF NOT EXISTS notification("
        + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "user_id INTEGER, "
        + "notif_id INTEGER, " + "event_id INTEGER, " + "type TEXT, "
        + "is_new BOOLEAN)";

    String userLeftEvent = "CREATE TABLE IF NOT EXISTS user_left_event("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT, event_id INTEGER, "
            + "user_id INTEGER, owner_id INTEGER)";

    String pendingReview = "CREATE TABLE IF NOT EXISTS pending_review("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "reviewer_id INTEGER, "
            + "target_id, event_name TEXT)";

    Statement prep = connection.createStatement();
    prep.addBatch(user);
    prep.addBatch(review);
    prep.addBatch(event);
    prep.addBatch(eventTags);
    prep.addBatch(userEvent);
    prep.addBatch(userRequest);
    prep.addBatch(message);
    prep.addBatch(notification);
    prep.addBatch(userLeftEvent);
    prep.addBatch(pendingReview);

    prep.executeBatch();
  }

  /**
   * Method for inserting a user, with all the following information, into the
   * db.
   *
   * @param owner_id
   * @param state
   * @param name
   * @param description
   * @param image
   * @param member_capacity
   * @param cost
   * @param location
   * @param tags
   * @throws SQLException
   */
  public void insertEvent(int owner_id, String state, String name,
      String description, String image, int member_capacity, double cost,
      String location, String[][] tags, double lat, double lng)
          throws SQLException {

    String sql = "INSERT INTO event (owner_id, state, name, description, "
        + "image, member_capacity, cost, location, lat, lng) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    PreparedStatement prep = connection.prepareStatement(sql);
    prep.setInt(1, owner_id);
    prep.setString(2, state);
    prep.setString(3, name);
    prep.setString(4, description);
    prep.setString(5, image);
    prep.setInt(6, member_capacity);
    prep.setDouble(7, cost);
    prep.setString(8, location);
    prep.setDouble(9, lat);
    prep.setDouble(10, lng);
    prep.executeUpdate();
  }

  public void insertUser(String email, String password, String first_name,
      String last_name, String image, String date) throws SQLException {
    String sql = "INSERT INTO user (email, password, first_name,"
        + " last_name, image, date) VALUES (?, ?, ?, ?, ?, ?)";
    try (PreparedStatement prep = connection.prepareStatement(sql)) {
      prep.setString(1, email);
      prep.setString(2, password);
      prep.setString(3, first_name);
      prep.setString(4, last_name);
      prep.setString(5, image);
      prep.setString(6, date);

      prep.executeUpdate();
    }

  }

  public void updatePassword(int id, String password) throws SQLException {
    String sql = "UPDATE user SET password = ? WHERE id = ?;";
    try (PreparedStatement prep = connection.prepareStatement(sql)) {
      prep.setString(1, password);
      prep.setInt(2, id);
      prep.executeUpdate();
    }
  }

  public void updateSettings(int id, String firstName, String lastName, String email)
    throws SQLException {
    String sql = "UPDATE user SET email = ?, first_name = ?, last_name = ? WHERE id = ?;";
    try (PreparedStatement prep = connection.prepareStatement(sql)) {
      prep.setString(1, email);
      prep.setString(2, firstName);
      prep.setString(3, lastName);
      prep.setInt(4, id);
      prep.executeUpdate();
    }

  }

  public void insertReview(int user_id, String message, double rating,
      int target_id) throws SQLException {
    String sql = "INSERT INTO review (user_id, message, rating, target_id) VALUES (?, ?, ?, ?)";
    try (PreparedStatement prep = connection.prepareStatement(sql)) {
      prep.setInt(1, user_id);
      prep.setString(2, message);
      prep.setDouble(3, rating);
      prep.setInt(4, target_id);

      prep.executeUpdate();
    }
  }

  public int insertMessage(int event_id, int user_id, String message,
      String time) throws SQLException {
    String sql = "INSERT INTO message (event_id, user_id, message, time) VALUES (?, ?, ?, ?)";
    try (PreparedStatement prep = connection.prepareStatement(sql)) {
      prep.setInt(1, event_id);
      prep.setInt(2, user_id);
      prep.setString(3, message);
      prep.setString(4, time);

      prep.executeUpdate();
      int id = findIdOfLastMessage(event_id, user_id, message, time);
      return id;
    }
  }

  public int findIdOfLastMessage(int event_id, int user_id, String message,
      String time) throws SQLException {

    String sql = "SELECT id FROM message WHERE event_id = ? AND user_id = ? AND message = ? AND time = ?";

    try (PreparedStatement prep = connection.prepareStatement(sql)) {

      prep.setInt(1, event_id);
      prep.setInt(2, user_id);
      prep.setString(3, message);
      prep.setString(4, time);

      try (ResultSet rs = prep.executeQuery()) {
        if (rs.next()) {
          return rs.getInt(1);
        }
      }
    }
    return -1;
  }

  public void insertUserIntoEvent(int event_id, int user_id, int owner_id)
      throws SQLException {

    String sql = "INSERT INTO user_event (event_id, user_id, owner_id) VALUES (?, ?, ?)";
    try (PreparedStatement prep = connection.prepareStatement(sql)) {
      prep.setInt(1, event_id);
      prep.setInt(2, user_id);
      prep.setInt(3, owner_id);

      prep.executeUpdate();
    }

  }

  public void insertNotification(int user_id, int notif_id, int event_id,
      String type) throws SQLException {

    String sql = "INSERT INTO notification (user_id, notif_id, event_id, type, is_new) VALUES (?, ?, ?, ?, ?)";
    try (PreparedStatement prep = connection.prepareStatement(sql)) {
      prep.setInt(1, user_id);
      prep.setInt(2, notif_id);
      prep.setInt(3, event_id);
      prep.setString(4, type);
      prep.setBoolean(5, true);

      prep.executeUpdate();
    }

  }

  public int getMessageNum(int user_id, int event_id) throws SQLException {
    int toReturn = 0;
    String sql = "SELECT id FROM notification WHERE type == 'MESSAGE' AND user_id == ? AND event_id == ?";
    try (PreparedStatement prep = connection.prepareStatement(sql)) {
      prep.setInt(1, user_id);
      prep.setInt(2, event_id);
      try (ResultSet rs = prep.executeQuery()) {
        while (rs.next()) {
          toReturn++;
        }
      }
    }
    return toReturn;
  }

  public int getRequestNum(int user_id, int event_id) throws SQLException {
    String sql = "SELECT id FROM notification WHERE type == 'REQUEST' AND user_id == ? AND event_id == ?";
    int toReturn = 0;

    try (PreparedStatement prep = connection.prepareStatement(sql)) {
      prep.setInt(1, user_id);
      prep.setInt(2, event_id);
      try (ResultSet rs = prep.executeQuery()) {
        while (rs.next()) {
          toReturn++;
        }
      }
    }
    return toReturn;
  }

  public boolean getNewlyAccepted(int user_id, int event_id)
      throws SQLException {
    String sql = "SELECT id FROM notification WHERE type == 'JOINED' AND user_id == ? AND event_id == ?";
    boolean toReturn = false;

    try (PreparedStatement prep = connection.prepareStatement(sql)) {
      prep.setInt(1, user_id);
      prep.setInt(2, event_id);
      try (ResultSet rs = prep.executeQuery()) {
        while (rs.next()) {
          return true;
        }
      }
    }
    return toReturn;
  }

  public void removeNewlyAccepted(int user_id, int event_id)
      throws SQLException {
    String sql = "DELETE FROM notification WHERE type == 'JOINED' AND user_id == ? AND event_id == ?";

    try (PreparedStatement prep = connection.prepareStatement(sql)) {
      prep.setInt(1, user_id);
      prep.setInt(2, event_id);

      prep.executeUpdate();
    }
  }

  public void clearMessageNotifs(int eventId, int userId) throws SQLException {
    String sql = "DELETE FROM notification WHERE type == 'MESSAGE' AND user_id == ? and event_id == ?";

    try (PreparedStatement prep = connection.prepareStatement(sql)) {
      prep.setInt(1, userId);
      prep.setInt(2, eventId);

      prep.executeUpdate();
    }
  }

  public int userEventsNotifNum(int userId) throws SQLException {
    int notifNum = 0;
    String sql = "SELECT id FROM event WHERE owner_id = ?;";
    try (PreparedStatement prep = connection.prepareStatement(sql)) {
      prep.setInt(1, userId);

      try (ResultSet rs = prep.executeQuery()) {

        while (rs.next()) {
          int id = rs.getInt(1);
          notifNum += eventNotifNum(id, userId);
        }
      }
    }

    return notifNum;
  }

  public int joinedEventsNotifNum(int userId) throws SQLException {
    String sql = "SELECT event_id FROM user_event WHERE user_id = ?;";
    int notifNum = 0;
    try (PreparedStatement prep = connection.prepareStatement(sql)) {
      prep.setInt(1, userId);

      try (ResultSet rs = prep.executeQuery()) {
        while (rs.next()) {
          int event_id = rs.getInt(1);
          notifNum += eventNotifNum(event_id, userId);
        }
      }

    }
    return notifNum;
  }

  public Map<Integer, Integer> getEventNotifNums(List<Integer> eventIds,
      int userId) throws SQLException {
    Map<Integer, Integer> notifs = new HashMap<>();
    for (Integer i : eventIds) {
      notifs.put(i, eventNotifNum(i, userId));
    }
    return notifs;
  }

  public int eventNotifNum(int eventId, int userId) throws SQLException {
    int notifNum = 0;
    String query = "SELECT id FROM notification WHERE user_id == ? AND event_id == ?";
    try (PreparedStatement prep = connection.prepareStatement(query)) {
      prep.setInt(1, userId);
      prep.setInt(2, eventId);
      try (ResultSet rs = prep.executeQuery()) {
        while (rs.next()) {
          notifNum++;
        }
      }
    }
    return notifNum;
  }

  public void removeUserFromEvent(int event_id, int user_id, int owner_id)
      throws SQLException {
    String sql = "DELETE FROM user_event WHERE event_id = ?"
        + "AND user_id = ? AND owner_id = ?;";
    try (PreparedStatement prep = connection.prepareStatement(sql)) {
      prep.setInt(1, event_id);
      prep.setInt(2, user_id);
      prep.setInt(3, owner_id);

      prep.executeUpdate();
    }

  }

  public void removeNotificationsById(int userId) throws SQLException {
    String sql = "DELETE FROM notification WHERE user_id = ?";

    try (PreparedStatement prep = connection.prepareStatement(sql)) {
      prep.setInt(1, userId);

      prep.executeUpdate();
    }

  }

  public void removeRequestNotifs(int eventId) throws SQLException {
    String sql = "DELETE FROM notification where type == 'REQUEST' AND event_id == ?";
    try (PreparedStatement prep = connection.prepareStatement(sql)) {
      prep.setInt(1, eventId);
      prep.executeUpdate();
    }
  }

  public void setEventState(int event_id, String state) throws SQLException {

    String sql = "UPDATE event SET state = ? WHERE id = ?;";
    try (PreparedStatement prep = connection.prepareStatement(sql)) {
      prep.setString(1, state);
      prep.setInt(2, event_id);
      prep.executeUpdate();
    }
  }

  public void removeEvent(int event_id) throws SQLException {
    String sql = "DELETE FROM user_event WHERE event_id = ?;";
    try (PreparedStatement prep = connection.prepareStatement(sql)) {
      prep.setInt(1, event_id);
      prep.executeUpdate();
    }

    String sqll = "DELETE FROM event WHERE id = ?;";
    try (PreparedStatement prepped = connection.prepareStatement(sqll)) {
      prepped.setInt(1, event_id);
      prepped.executeUpdate();
    }
  }

  public void deleteAllEventUser(int event_id) throws SQLException {
    String sql = "DELETE FROM user_event WHERE event_id = ?;";
    try (PreparedStatement prep = connection.prepareStatement(sql)) {
      prep.setInt(1, event_id);
      prep.executeUpdate();
    }
  }


  public void requestUserIntoEvent(int event_id, int user_id, int owner_id)
      throws SQLException {
    String sql = "INSERT INTO user_request (event_id, user_id, owner_id) VALUES (?, ?, ?)";
    try (PreparedStatement prep = connection.prepareStatement(sql)) {
      prep.setInt(1, event_id);
      prep.setInt(2, user_id);
      prep.setInt(3, owner_id);

      prep.executeUpdate();
    }

  }

  public void insertPendingReview(int reviewerId, int targetId, String eventName) throws SQLException {
    String sql = "INSERT INTO pending_review (reviewer_id, target_id, event_name) VALUES (?, ?, ?)";
    try (PreparedStatement prep = connection.prepareStatement(sql)) {
      prep.setInt(1, reviewerId);
      prep.setInt(2, targetId);
      prep.setString(3, eventName);

      prep.executeUpdate();
    }
  }

  public void removeRequest(int event_id, int user_id) throws SQLException {
    String sql = "DELETE FROM user_request WHERE event_id = ? AND user_id = ?";
    try (PreparedStatement prep = connection.prepareStatement(sql)) {
      prep.setInt(1, event_id);
      prep.setInt(2, user_id);
      prep.executeUpdate();
    }
  }

  public void incrementHostRequestNotif(int user_id) throws SQLException {

    String sql = "UPDATE user " + "SET requestNotif = requestNotif + 1 "
        + "WHERE id = ?;";
    try (PreparedStatement prep = connection.prepareStatement(sql)) {
      // prep.setInt(1, notif);
      prep.setInt(1, user_id);
      prep.executeUpdate();
    }

  }

  public void incrementJoinedNotif(int user_id) throws SQLException {
    String sql = "UPDATE user " + "SET joinedNotif = joinedNotif + 1 "
        + "WHERE id = ?;";
    try (PreparedStatement prep = connection.prepareStatement(sql)) {
      // prep.setInt(1, notif);
      prep.setInt(1, user_id);
      prep.executeUpdate();
    }

  }

  // "UPDATE user SET joinedNotif = joinedNotif + 1 WHERE id = ?;";

  public void setHostRequestNotif(int user_id, int notif) throws SQLException {
    String sql = "UPDATE user " + "SET requestNotif = ? " + "WHERE id = ?;";
    try (PreparedStatement prep = connection.prepareStatement(sql)) {
      prep.setInt(1, notif);
      prep.setInt(2, user_id);
      prep.executeUpdate();
    }

  }

  public void setJoinedNotif(int user_id, int notif) throws SQLException {

    String sql = "UPDATE user " + "SET joinedNotif = ? " + "WHERE id = ?;";
    try (PreparedStatement prep = connection.prepareStatement(sql)) {
      prep.setInt(1, notif);
      prep.setInt(2, user_id);
      prep.executeUpdate();
    }

  }

  public List<Integer> findUsersByEventId(Integer eventId) throws SQLException {
    String sql = "SELECT user_id FROM user_event WHERE event_id = ?;";
    List<Integer> toReturn = new ArrayList<>();

    try (PreparedStatement prep = connection.prepareStatement(sql)) {
      prep.setInt(1, eventId);

      try (ResultSet rs = prep.executeQuery()) {

        while (rs.next()) {
          int id = rs.getInt(1);
          toReturn.add(id);
        }
      }
    }
    return toReturn;

  }

  public List<Message> findMessagesByEventId(Integer eventId)
      throws SQLException {
    String sql = "SELECT id, user_id, message, time FROM message WHERE event_id = ?;";
    List<Message> toReturn = new ArrayList<>();

    try (PreparedStatement prep = connection.prepareStatement(sql)) {
      prep.setInt(1, eventId);

      try (ResultSet rs = prep.executeQuery()) {

        while (rs.next()) {
          int id = rs.getInt(1);
          int userId = rs.getInt(2);
          String message = rs.getString(3);
          String time = rs.getString(4);

          Profile user = findUserProfileById(userId);
          String username = user.getName();
          String eventName = findEventById(eventId).getName();
          String image = user.getImage();
          toReturn.add(new Message(id, userId, eventId, message, time, username,
              eventName, image));
        }
      }
    }
    return toReturn;

  }

  public List<Integer> findEventsByUserId(Integer userId) throws SQLException {
    String sql = "SELECT event_id FROM user_event WHERE user_id = ?;";
    List<Integer> toReturn = new ArrayList<>();

    try (PreparedStatement prep = connection.prepareStatement(sql)) {
      prep.setInt(1, userId);

      try (ResultSet rs = prep.executeQuery()) {

        while (rs.next()) {
          int id = rs.getInt(1);
          toReturn.add(id);
        }
      }
    }
    return toReturn;

  }

  public List<Integer> findReviewIDsByUserId(Integer userId)
      throws SQLException {
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
    } finally {
      closeResultSet(rs);
    }
  }

  public List<Review> findReviewsByUserId(Integer userId) throws SQLException {
    ResultSet rs = null;
    try {
      String sql = "SELECT user_id, rating, message FROM review WHERE target_id = ?;";
      PreparedStatement prep = connection.prepareStatement(sql);
      prep.setInt(1, userId);

      List<Review> toReturn = new ArrayList<>();
      rs = prep.executeQuery();

      while (rs.next()) {
        int user_id = rs.getInt(1);
        double rating = rs.getDouble(2);
        String text = rs.getString(3);
        toReturn.add(new Review(user_id, userId, rating, text));
      }
      return toReturn;
    } finally {
      closeResultSet(rs);
    }
  }

  public List<Integer> findEventIdsbyOwnerId(Integer userId)
      throws SQLException {
    ResultSet rs = null;
    try {
      String sql = "SELECT id FROM event WHERE owner_id = ? ORDER BY id DESC;";
      PreparedStatement prep = connection.prepareStatement(sql);
      prep.setInt(1, userId);

      List<Integer> toReturn = new ArrayList<>();
      rs = prep.executeQuery();

      while (rs.next()) {
        int event_id = rs.getInt(1);
        toReturn.add(event_id);
      }
      return toReturn;
    } finally {
      closeResultSet(rs);
    }
  }

  public List<Event> findEventsByOwnerId(Integer userId) throws SQLException {
    List<Event> toReturn = new ArrayList<>();
    for (Integer id : findEventIdsbyOwnerId(userId)) {
      toReturn.add(findEventById(id));
    }
    return toReturn;
  }

  public List<Integer> findEventsByRequestedId(Integer userId)
      throws SQLException {
    ResultSet rs = null;
    try {
      String sql = "SELECT event_id FROM user_request WHERE user_id = ?;";
      PreparedStatement prep = connection.prepareStatement(sql);
      prep.setInt(1, userId);

      List<Integer> toReturn = new ArrayList<>();
      rs = prep.executeQuery();

      while (rs.next()) {
        int id = rs.getInt(1);
        toReturn.add(id);
      }
      return toReturn;
    } finally {
      closeResultSet(rs);
    }
  }

  public Account findUserByUsername(String username) throws SQLException {
    ResultSet rs = null;
    try {
      String sql = "SELECT id FROM user WHERE email = ?;";
      PreparedStatement prep = connection.prepareStatement(sql);
      prep.setString(1, username);

      rs = prep.executeQuery();

      if (rs.next()) {
        return findUserAccountById(rs.getInt(1));
      }
    } finally {
      closeResultSet(rs);
    }
    return null;
  }

  public List<Event> findNewNearbyEvents(List<Integer> taken, double lat,
      double lng) throws SQLException {
    List<Event> toReturn = new ArrayList();
    ResultSet rs = null;

    double fudge = Math.pow(Math.cos(Math.toRadians(lat)), 2);
    try {
      String sql = "SELECT id, owner_id, state, name, description, image,"
          + " member_capacity, cost, location, lat, lng FROM event "
          + "WHERE ABS(lat - ?) < 0.2 AND " + "ABS(lng - ?) < 0.2 ORDER BY"
          + "((? - lat) * (? - lat) + " + "(? - lng) * (? - lng) * ?);";
      PreparedStatement prep = connection.prepareStatement(sql);
      prep.setDouble(1, lat);
      prep.setDouble(2, lng);
      prep.setDouble(3, lat);
      prep.setDouble(4, lat);
      prep.setDouble(5, lng);
      prep.setDouble(6, lng);
      prep.setDouble(7, fudge);

      rs = prep.executeQuery();

      while (rs.next() && toReturn.size() < 10) {
        Integer eventId = rs.getInt(1);
        if (!taken.contains(eventId)) {
          int owner_id = rs.getInt(2);
          Account host = findUserAccountById(owner_id);
          EventState state = EventState.valueOf(rs.getString(3));
          String name = rs.getString(4);
          String description = rs.getString(5);
          String image = rs.getString(6);
          int member_capacity = rs.getInt(7);
          double cost = rs.getDouble(8);
          String location = rs.getString(9);
          List<String> tags = findTagsByEventId(eventId);
          List<Integer> members = findUsersByEventId(eventId);
          double nlat = rs.getDouble(10);
          double nlng = rs.getDouble(11);
          if (members.size() + 1 < member_capacity) {
            toReturn.add(new Event(eventId, state, name, description, image, host,
                    members, member_capacity, cost, location, tags, nlat, nlng));
          }
        }
      }
    } finally {
      closeResultSet(rs);
    }
    return toReturn;
  }

  public Account findUserAccountById(int id) throws SQLException {
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
        return new Account(profile, id, email, password, requestNotif,
            joinedNotif);
      }
    } finally {
      closeResultSet(rs);
    }
    return null;
  }

  public Profile findUserProfileById(int id) {
    String sql = "SELECT first_name, last_name, image, date FROM user WHERE id = ?;";

    try (PreparedStatement prep = connection.prepareStatement(sql)) {
      prep.setInt(1, id);

      try (ResultSet rs = prep.executeQuery()) {

        if (rs.next()) {
          String firstName = rs.getString(1);
          String lastName = rs.getString(2);
          String image = rs.getString(3);
          String date = rs.getString(4);
          List<Integer> reviews = findReviewsById(id);

          return new Profile(id, firstName, lastName, image, date, reviews);
        }
      }
    } catch (SQLException e) {
      return null;
    }
    return null;
  }

  public List<Integer> findReviewsById(int id) throws SQLException {
    String sql = "SELECT id FROM review WHERE target_id = ?;";

    try (PreparedStatement prep = connection.prepareStatement(sql)) {
      prep.setInt(1, id);

      List<Integer> toReturn = new ArrayList<>();

      try (ResultSet rs = prep.executeQuery()) {

        while (rs.next()) {
          int review_id = rs.getInt(1);

          toReturn.add(review_id);
        }

        return toReturn;
      }
    }
  }

  public Event findEventById(int id) throws SQLException {

    ResultSet rs = null;
    try {
      String sql = "SELECT owner_id, state, name, description, image, member_capacity, cost, location, lat, lng FROM event WHERE id = ?;";
      PreparedStatement prep = connection.prepareStatement(sql);
      prep.setInt(1, id);

      rs = prep.executeQuery();

      while (rs.next()) {
        int owner_id = rs.getInt(1);
        Account host = findUserAccountById(owner_id);
        EventState state = EventState.valueOf(rs.getString(2));
        String name = rs.getString(3);
        String description = rs.getString(4);
        String image = rs.getString(5);
        int member_capacity = rs.getInt(6);
        double cost = rs.getDouble(7);
        String location = rs.getString(8);
        double lat = rs.getDouble(9);
        double lng = rs.getDouble(10);

        List<String> tags = findTagsByEventId(id);
        List<Integer> members = findUsersByEventId(id);

        return new Event(id, state, name, description, image, host, members,
            member_capacity, cost, location, tags, lat, lng);
      }
    } finally {
      closeResultSet(rs);
    }
    return null;
  }

  public List<String> findTagsByEventId(int id) throws SQLException {
    ResultSet rs = null;
    try {
      String sql = "SELECT tag FROM event_tags WHERE event_id = ?;";
      PreparedStatement prep = connection.prepareStatement(sql);
      prep.setInt(1, id);

      List<String> toReturn = new ArrayList<>();

      rs = prep.executeQuery();

      while (rs.next()) {
        String tag = rs.getString(1);

        toReturn.add(tag);
      }

      return toReturn;
    } finally {
      closeResultSet(rs);
    }
  }

  public List<Event> findJoinedEventsByUserId(int userId) throws SQLException {
    String sql = "SELECT event_id FROM user_event WHERE user_id = ? ORDER BY event_id DESC;";

    try (PreparedStatement prep = connection.prepareStatement(sql)) {
      prep.setInt(1, userId);

      List<Event> toReturn = new ArrayList<>();
      try (ResultSet rs = prep.executeQuery()) {

        while (rs.next()) {
          int event_id = rs.getInt(1);
          toReturn.add(findEventById(event_id));
        }
        return toReturn;
      }
    }

  }

  public Map<Integer, Integer> joinedEventsNotifMap(int userId)
      throws SQLException {

    String sql = "SELECT event_id FROM user_event WHERE user_id = ?;";
    Map<Integer, Integer> toReturn = new HashMap<>();

    try (PreparedStatement prep = connection.prepareStatement(sql)) {
      prep.setInt(1, userId);

      try (ResultSet rs = prep.executeQuery()) {

        while (rs.next()) {
          int event_id = rs.getInt(1);
          toReturn.put(event_id, eventNotifNum(event_id, userId));
        }
      }
    }

    return toReturn;
  }

  public List<Event> findPendingEventsByUserId(int userId) throws SQLException {
    ResultSet rs = null;
    try {
      String sql = "SELECT event_id FROM user_request WHERE user_id = ? ORDER BY event_id DESC;";
      PreparedStatement prep = connection.prepareStatement(sql);
      prep.setInt(1, userId);

      List<Event> toReturn = new ArrayList<>();
      rs = prep.executeQuery();

      while (rs.next()) {
        int event_id = rs.getInt(1);
        toReturn.add(findEventById(event_id));
      }
      return toReturn;
    } finally {
      closeResultSet(rs);
    }
  }

  public List<Notification> findNotificationsById(int userId)
      throws SQLException {
    ResultSet rs = null;
    try {
      String sql = "SELECT id, notif_id, type FROM notification WHERE user_id = ? AND is_new = 1;";
      PreparedStatement prep = connection.prepareStatement(sql);
      prep.setInt(1, userId);

      List<Notification> toReturn = new ArrayList<>();
      rs = prep.executeQuery();

      while (rs.next()) {
        int id = rs.getInt(1);
        int notif_id = rs.getInt(2);
        String type = rs.getString(3);
        toReturn.add(new Notification(userId, notif_id, type));

      }
      updateNotification(userId, false);

      return toReturn;
    } finally {
      closeResultSet(rs);
    }
  }

  public void updateNotification(int userId, boolean isNew) {
    try {
      String sql = "UPDATE notification SET is_new = ? WHERE user_id = ?;";
      PreparedStatement prep = connection.prepareStatement(sql);
      prep.setBoolean(1, isNew);
      prep.setInt(2, userId);

      prep.executeUpdate();

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public Message findMessageById(int notifId) throws SQLException {
    ResultSet rs = null;
    try {
      String sql = "SELECT event_id, user_id, message, time FROM message WHERE id = ?;";
      PreparedStatement prep = connection.prepareStatement(sql);
      prep.setInt(1, notifId);

      rs = prep.executeQuery();

      if (rs.next()) {
        int eventId = rs.getInt(1);
        int userId = rs.getInt(2);
        String message = rs.getString(3);
        String time = rs.getString(4);

        Profile user = findUserProfileById(userId);
        String username = user.getName();
        String eventName = findEventById(eventId).getName();
        String image = user.getImage();

        return (new Message(notifId, userId, eventId, message, time, username,
            eventName, image));
      }
    } finally {
      closeResultSet(rs);
    }
    return null;
  }

  public EventRequest findEventRequestById(int notifId) throws SQLException {
    ResultSet rs = null;
    try {
      String sql = "SELECT event_id, user_id FROM user_request WHERE id = ?;";
      PreparedStatement prep = connection.prepareStatement(sql);
      prep.setInt(1, notifId);

      rs = prep.executeQuery();

      if (rs.next()) {
        int eventId = rs.getInt(1);
        int userId = rs.getInt(2);

        Event event = findEventById(eventId);
        Account user = findUserAccountById(userId);

        return (new EventRequest(event, user));
      }
    } finally {
      closeResultSet(rs);
    }
    return null;
  }

  public Event findJoinedEventById(int notifId) throws SQLException {
    ResultSet rs = null;
    try {
      String sql = "SELECT event_id FROM user_event WHERE id = ?;";
      PreparedStatement prep = connection.prepareStatement(sql);
      prep.setInt(1, notifId);

      rs = prep.executeQuery();

      if (rs.next()) {
        int eventId = rs.getInt(1);

        Event event = findEventById(eventId);

        return event;
      }
    } finally {
      closeResultSet(rs);
    }
    return null;
  }

  public List<Integer> findRequestsByEventId(int eventId) throws SQLException {
    ResultSet rs = null;
    try {
      String sql = "SELECT user_id FROM user_request WHERE event_id = ?;";
      PreparedStatement prep = connection.prepareStatement(sql);
      prep.setInt(1, eventId);

      rs = prep.executeQuery();
      List<Integer> toReturn = new ArrayList<>();

      while (rs.next()) {
        int userId = rs.getInt(1);
        toReturn.add(userId);
      }
      return toReturn;
    } finally {
      closeResultSet(rs);
    }
  }

  public int findOwnerIdByEventId(int eventId) throws SQLException {
    ResultSet rs = null;
    try {
      String sql = "SELECT owner_id FROM event WHERE id = ?;";
      PreparedStatement prep = connection.prepareStatement(sql);
      prep.setInt(1, eventId);

      rs = prep.executeQuery();

      if (rs.next()) {
        return rs.getInt(1);
      }
    } finally {
      closeResultSet(rs);
    }
    return -1;
  }

  public boolean isInUserTable(String val) throws SQLException {
    ResultSet rs = null;
    try {
      String sql = "SELECT first_name FROM user WHERE email = ?;";
      PreparedStatement prep = connection.prepareStatement(sql);
      prep.setString(1, val);

      rs = prep.executeQuery();

      return rs.next();

    } finally {
      closeResultSet(rs);
    }
  }

  public EventState getEventStateById(int eventId) throws SQLException {
    ResultSet rs = null;

    try {
      String sql = "SELECT state FROM event WHERE id = ?;";
      PreparedStatement prep = connection.prepareStatement(sql);
      prep.setInt(1, eventId);

      rs = prep.executeQuery();

      if (rs.next()) {
        return EventState.valueOf(rs.getString(1));
      }

    } finally {
      closeResultSet(rs);
    }
    return null;
  }

  public void insertUserIntoLeftEvent(int event_id, int user_id, int owner_id) throws SQLException {
    String sql = "INSERT INTO user_left_event (event_id, user_id, owner_id) VALUES (?, ?, ?)";
    try (PreparedStatement prep = connection.prepareStatement(sql)) {
      prep.setInt(1, event_id);
      prep.setInt(2, user_id);
      prep.setInt(3, owner_id);

      prep.executeUpdate();
    }
  }

  public List<Integer> findAllUsersInEvent(int eventId) throws SQLException {
    ResultSet rs = null;
    List<Integer> users = new ArrayList<>();

    try {
      String sql = "SELECT user_id FROM user_event WHERE event_id = ?;";
      PreparedStatement prep = connection.prepareStatement(sql);
      prep.setInt(1, eventId);

      rs = prep.executeQuery();

      while (rs.next()) {
        users.add(rs.getInt(1));
      }

      return users;

    } finally {
      closeResultSet(rs);
    }
  }

  public List<Integer> findUsersLeftInEvent(int eventId) throws SQLException {
    ResultSet rs = null;
    List<Integer> users = new ArrayList<>();

    try {
      String sql = "SELECT user_id FROM user_left_event WHERE event_id = ?;";
      PreparedStatement prep = connection.prepareStatement(sql);
      prep.setInt(1, eventId);

      rs = prep.executeQuery();

      while (rs.next()) {
        users.add(rs.getInt(1));
      }

      return users;

    } finally {
      closeResultSet(rs);
    }
  }

  public List<PendingReview> findPendingReviewsByUserId(int userId) throws SQLException {
    ResultSet rs = null;
    List<PendingReview> pendingReviews = new ArrayList<>();

    try {
      String sql = "SELECT id, target_id, event_name FROM pending_review WHERE reviewer_id = ?;";
      PreparedStatement prep = connection.prepareStatement(sql);
      prep.setInt(1, userId);

      rs = prep.executeQuery();

      while (rs.next()) {
        int id = rs.getInt(1);
        int targetId = rs.getInt(2);
        String eventName = rs.getString(3);
        Profile target = findUserProfileById(targetId);

        PendingReview pending = new PendingReview(id, userId, targetId, target.getName(), target.getImage(), eventName);
        pendingReviews.add(pending);
      }

      return pendingReviews;

    } finally {
      closeResultSet(rs);
    }
  }

  private void makeConnection(String db)
      throws SQLException, ClassNotFoundException {
    Class.forName("org.sqlite.JDBC");
    String urlToDB = "jdbc:sqlite:" + db;
    connection = DriverManager.getConnection(urlToDB);
    Statement stat = connection.createStatement();
    stat.executeUpdate("PRAGMA foreign_keys = ON; PRAGMA journal_mode=WAL;");
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
