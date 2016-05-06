package edu.brown.cs.user.CS32Final.GUI;

import static spark.Spark.webSocket;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import edu.brown.cs.user.CS32Final.Entities.Account.Account;
import edu.brown.cs.user.CS32Final.Entities.Account.Notification;
import edu.brown.cs.user.CS32Final.Entities.Account.NotificationType;
import edu.brown.cs.user.CS32Final.Entities.Account.Profile;
import edu.brown.cs.user.CS32Final.Entities.Account.Review;
import edu.brown.cs.user.CS32Final.Entities.Chat.ChatHandler;
import edu.brown.cs.user.CS32Final.Entities.Chat.Message;
import edu.brown.cs.user.CS32Final.Entities.Event.Event;
import edu.brown.cs.user.CS32Final.Entities.Event.EventRequest;
import edu.brown.cs.user.CS32Final.Entities.Event.EventState;
import edu.brown.cs.user.CS32Final.SQL.SqliteDatabase;
import freemarker.template.Configuration;
import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

/**
 * Created by adamdeho on 4/21/16.
 */
public class GUI {

  private SqliteDatabase database;

  private final Gson gson = new Gson();

  public GUI() {
    try {
      database = new SqliteDatabase("data/database.sqlite3");
      database.createTables();
    } catch(Exception e) {
      System.out.println("ERROR: SQL error");
      e.printStackTrace();
    }
    runSparkServer();
  }

  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf(
              "ERROR: Unable use %s for template loading.\n", templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  private static int getHerokuAssignedPort() {
    ProcessBuilder processBuilder = new ProcessBuilder();
    if (processBuilder.environment().get("PORT") != null) {
      return Integer.parseInt(processBuilder.environment().get("PORT"));
    }
    return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
  }

  /**
   * Runs the spark server.
   */
  private void runSparkServer() {
    Spark.port(getHerokuAssignedPort());

    Spark.externalStaticFileLocation("src/main/resources/static");
    //Spark.exception(Exception.class, new ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();

    webSocket("/chat", ChatHandler.class);

    // Setup Spark Routes
    Spark.get("/", new FrontHandler(), freeMarker);
    Spark.get("/home", new FrontHandler(), freeMarker);

    // Registration and profiles
    Spark.post("/register", new RegisterHandler());
    Spark.post("/login", new LoginHandler());
    Spark.post("/profile", new ProfileHandler());
    //Spark.post("/profile-reviews", new ReviewsHandler());

    // Event creation
    Spark.post("/event-create", new EventCreateHandler());

    // View events
    Spark.post("/event-view", new EventViewHandler());
    Spark.post("/event-feed", new EventFeedHandler());
    Spark.post("/event-owner", new EventOwnerHandler());
    Spark.post("/event-joined", new EventJoinedHandler());
    Spark.post("/event-pending", new EventPendingHandler());

    // View users
    Spark.post("/user-requests", new UserRequestsHandler());

    // Event actions
    Spark.post("/event-request", new RequestEventHandler());
    Spark.post("/event-join", new JoinEventHandler());

    // Changing event state
    Spark.post("/event-close", new CloseEventHandler());
    Spark.post("/event-remove", new RemoveEventHandler());
    Spark.post("/event-start", new StartEventHandler());
    Spark.post("/delete-event", new DeleteEventHandler());

    // Notifications
    Spark.post("/notification", new NotificationHandler());
    Spark.post("/notification-remove", new NotificationRemoveHandler());
  }

  /**
   * Exception Printer for errors I think. I didn't write it.
   */
  private static class ExceptionPrinter implements ExceptionHandler {
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(500);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }

  /**
   * Handles the main page.
   */
  private class FrontHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables =
              ImmutableMap.of("title", "Bulki", "content", "");
      return new ModelAndView(variables, "main.ftl");
    }
  }

  private class RegisterHandler implements Route {
    @Override
    public Object handle(final Request req, final Response arg1) {
      QueryParamsMap qm = req.queryMap();

      String email = qm.value("email");
      String password = qm.value("password");
      String first_name = qm.value("firstName");
      String last_name = qm.value("lastName");
      String image = qm.value("image");
      String date = "19 May, 2016";
      Account user = null;
      boolean hasError = false;
      String errorMsg = "";
      Map<String, Object> variables = null;
      ImmutableMap.Builder<String, Object> vars = new ImmutableMap.Builder();



      // check if email already in database

      try {
    	  database.insertUser(email, password, first_name, last_name, image, date);
      } catch(Exception e) {
    	hasError = true;
    	errorMsg = "There is a problem with adding to the database. Try again later.";
      }

      if (!hasError) {
    	  try {
    		  user = database.findUserByUsername(email);
          } catch (Exception e) {
        	  hasError = true;
        	  errorMsg = "There is a problem with adding to the database. Try again later.";
          }
    	  if (user == null) {
        	  hasError = true;
        	  errorMsg = "There is a problem with adding to the database. Try again later.";
    	  } else {
    		  user.getLoginData(vars);
    		  try {
    			  vars.put("reviews", database.findReviewsByUserId(user.getId()));
    	      } catch (Exception e) {
    	    	  hasError = true;
            	  errorMsg = "There is a problem with adding to the database. Try again later.";
    	      }
    	  }

      }

      vars.put("hasError", hasError);
      vars.put("errorMsg", errorMsg);
      variables = vars.build();
      return gson.toJson(variables);
    }
  }

  /**
   * For logging in
   */
  private class LoginHandler implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();
      String username = qm.value("username");
      String password = qm.value("password");

      Map<String, Object> variables = null;
      Account user;
      try {
        user = database.findUserByUsername(username);
      } catch (Exception e) {
        System.out.println("ERROR: SQL error in find username");
        e.printStackTrace();
        ImmutableMap.Builder<String, Object> vars = new ImmutableMap.Builder();
        vars.put("hasError", true);
        vars.put("errorMsg", "Username is incorrect.");
        variables = vars.build();
        return gson.toJson(variables);
      }
      if (user == null) {
        ImmutableMap.Builder<String, Object> vars = new ImmutableMap.Builder();
        vars.put("hasError", true);
        vars.put("errorMsg", "Username is incorrect.");
        variables = vars.build();
        return gson.toJson(variables);
      }

        if (user.authenticate(password)) {
          ImmutableMap.Builder<String, Object> vars = new ImmutableMap.Builder();

          user.getLoginData(vars);
          // TODO: account for sql error
          try {
            vars.put("reviews", database.findReviewsByUserId(user.getId()));
          } catch (Exception e) {
            e.printStackTrace();
          }
          vars.put("hasError", false);
          variables = vars.build();
          return gson.toJson(variables);
        } else {
          ImmutableMap.Builder<String, Object> vars = new ImmutableMap.Builder();
          vars.put("hasError", true);
          vars.put("errorMsg", "Username or password is incorrect.");
          variables = vars.build();
          return gson.toJson(variables);
        }

    }
  }

  private class EventCreateHandler implements Route {
    @Override
    public Object handle(final Request req, final Response res) {

      QueryParamsMap qm = req.queryMap();

      int owner_id = Integer.parseInt(qm.value("owner_id"));
      String state = "OPEN";
      String name = qm.value("name");
      String description = qm.value("description");
      String image = qm.value("image");
      int member_capacity = Integer.parseInt(qm.value("members"));
      String rawCost = qm.value("cost");
      rawCost = rawCost.replace(",", "");
      rawCost = rawCost.replace("$", "");
      double cost = Double.parseDouble(rawCost);
      String location = qm.value("location");
      double lat = Double.parseDouble(qm.value("lat"));
      double lng = Double.parseDouble(qm.value("lng"));
      String[][] tags = gson.fromJson(qm.value("tags"), String[][].class);

      System.out.println("about to go to db methods");
      int event_id = -1;
      try {
        database.insertEvent(owner_id, state, name, description, image, member_capacity, cost, location, tags, lat, lng);
      } catch(Exception e) {
        System.out.println("ERROR: SQL error");
        e.printStackTrace();
      }

      ImmutableMap.Builder<String, Object> vars = new ImmutableMap.Builder();
      vars.put("success", true);
      vars.put("id", event_id);
      return gson.toJson(vars.build());
    }
  }


  private class ProfileHandler implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();

      int id = Integer.parseInt(qm.value("id"));
      Profile user = null;

      user = database.findUserProfileById(id);


      ImmutableMap.Builder<String, Object> vars = new ImmutableMap.Builder();
      user.getProfileData(vars);
      List<Review> reviews = null;
      try {
        reviews = database.findReviewsByUserId(id);
      } catch(Exception e) {
        System.out.println("ERROR: SQL error in find reviews");
        e.printStackTrace();
      }

      double sum = 0;
      for (Review a : reviews) {
        sum += a.getRating();
      }

      double average = 0;
      if (!reviews.isEmpty()) {
        average = sum / reviews.size();
      }
      vars.put("rating", average);
      vars.put("reviews", reviews);

      Map<String, Object> variables = vars.build();

      return gson.toJson(variables);
    }
  }

  private class EventViewHandler implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();

      int id = Integer.parseInt(qm.value("id"));

      Event event = null;

      try {
        event = database.findEventById(id);
      } catch(Exception e) {
        System.out.println("ERROR: SQL error");
        e.printStackTrace();
      }

      ImmutableMap.Builder<String, Object> vars = new ImmutableMap.Builder();
      event.getEventData(vars);

      try {
        List<Integer> requests = database.findRequestsByEventId(event.getId());
        vars.put("requests", requests);
      } catch (SQLException e) {
        e.printStackTrace();
      }
      Map<String, Object> variables = vars.build();
      return gson.toJson(variables);
    }
  }

  private class EventFeedHandler implements Route {

    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();

      int id = Integer.parseInt(qm.value("id"));
      double lat = Double.parseDouble(qm.value("lat"));
      double lng = Double.parseDouble(qm.value("lng"));
      List<Event> events = null;
      try {
        List<Integer> handled = database.findEventIdsbyOwnerId(id);
        handled.addAll(database.findEventsByRequestedId(id));
        handled.addAll(database.findEventsByUserId(id));
        events = database.findNewNearbyEvents(handled, lat, lng);
      } catch(Exception e) {
        System.out.println("ERROR: SQL error");
        e.printStackTrace();
      }
      ImmutableMap.Builder<String, Object> vars = new ImmutableMap.Builder();
      vars.put("events", events);

      Map<String, Object> variables = vars.build();
      return gson.toJson(variables);
    }
  }

  private class EventOwnerHandler implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();

      int id = Integer.parseInt(qm.value("id"));
      List<Event> events = null;
      try {
        events = database.findEventsByOwnerId(id);
      } catch(Exception e) {
        System.out.println("ERROR: SQL error");
        e.printStackTrace();
      }

      ImmutableMap.Builder<String, Object> vars = new ImmutableMap.Builder();
      vars.put("events", events);
      Map<String, Object> variables = vars.build();
      return gson.toJson(variables);
    }
  }

  private class EventJoinedHandler implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      System.out.println("JOIN HANDLER");
      QueryParamsMap qm = req.queryMap();

      int id = Integer.parseInt(qm.value("id"));
      List<Event> events = null;
      try {
        events = database.findJoinedEventsByUserId(id);
      } catch(Exception e) {
        System.out.println("ERROR: SQL error");
        e.printStackTrace();
      }

      ImmutableMap.Builder<String, Object> vars = new ImmutableMap.Builder();
      vars.put("events", events);

      Map<String, Object> variables = vars.build();
      return gson.toJson(variables);
    }
  }

  private class EventPendingHandler implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();

      int id = Integer.parseInt(qm.value("id"));
      List<Event> events = null;
      try {
        events = database.findPendingEventsByUserId(id);
      } catch(Exception e) {
        System.out.println("ERROR: SQL error");
        e.printStackTrace();
      }
      ImmutableMap.Builder<String, Object> vars = new ImmutableMap.Builder();
      vars.put("events", events);

      Map<String, Object> variables = vars.build();
      return gson.toJson(variables);
    }
  }

  private class UserRequestsHandler implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();

      int eventId = Integer.parseInt(qm.value("id"));
      List<Integer> userIds = null;

      List<Profile> users = new ArrayList<>();
      int ownerId = -1;

      try {
        userIds = database.findRequestsByEventId(eventId);

        for (int userId : userIds) {
          users.add(database.findUserProfileById(userId));
        }
        ownerId = database.findOwnerIdByEventId(eventId);


      } catch(Exception e) {
        System.out.println("ERROR: SQL error");
        e.printStackTrace();
      }
      ImmutableMap.Builder<String, Object> vars = new ImmutableMap.Builder();
      vars.put("ownerId", ownerId);
      vars.put("requests", users);

      Map<String, Object> variables = vars.build();
      return gson.toJson(variables);
    }
  }


  private class RequestEventHandler implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();

      int id = Integer.parseInt(qm.value("id"));
      int eventId = Integer.parseInt(qm.value("eventId"));
      Event event = null;
      try {
      event = database.findEventById(eventId);
      } catch(Exception e) {
        System.out.println("ERROR: SQL error");
        e.printStackTrace();
      }
      if (event.getState() != EventState.OPEN) {
        ImmutableMap.Builder<String, Object> vars = new ImmutableMap.Builder();
        vars.put("hasError", true);
        vars.put("errorMsg", "Event is closed");
        Map<String, Object> variables = vars.build();
      }
      try {
        database.requestUserIntoEvent(eventId, id, event.getHost().getId());
        database.incrementHostRequestNotif(event.getHost().getId());
        database.insertNotification(event.getHost().getId(), eventId, "REQUEST");
      } catch(Exception e) {
        System.out.println("ERROR: SQL error");
        e.printStackTrace();
      }

      ImmutableMap.Builder<String, Object> vars = new ImmutableMap.Builder();
      //event.getEventData(vars);
      vars.put("hasError", false);
      Map<String, Object> variables = vars.build();
      return gson.toJson(variables);
    }
  }

  private class JoinEventHandler implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();

      int id = Integer.parseInt(qm.value("id"));
      int eventId = Integer.parseInt(qm.value("eventId"));


      Event event = null;
      try {
        event = database.findEventById(eventId);
        if (event.getState() != EventState.OPEN) {
          ImmutableMap.Builder<String, Object> vars = new ImmutableMap.Builder();
          vars.put("hasError", true);
          vars.put("errorMsg", "Event is closed");
          Map<String, Object> variables = vars.build();
        }
        database.insertUserIntoEvent(eventId, id, event.getHost().getId());
        database.incrementJoinedNotif(id);
      } catch(Exception e) {
        System.out.println("ERROR: SQL error");
        e.printStackTrace();
      }

      ImmutableMap.Builder<String, Object> vars = new ImmutableMap.Builder();
      //event.getEventData(vars);
      try {
        if (event.getMembers().size() + 1 == event.getMaxMembers()) {
          database.setEventState(eventId, "CLOSED");
          vars.put("state", "CLOSED");

          List<Integer> users = database.findUsersByEventId(eventId);

          for (int userId : users) {
            database.insertNotification(userId, eventId, "STATE");
          }
        }
        else {
          vars.put("state", "OPEN");
        }
      } catch(Exception e) {
        vars.put("state", "OPEN");
        System.out.println("ERROR: SQL error");
        e.printStackTrace();
      }

      vars.put("hasError", false);
      Map<String, Object> variables = vars.build();
      return gson.toJson(variables);
    }
  }

  private class RemoveEventHandler implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();

      int id = Integer.parseInt(qm.value("id"));
      int eventId = Integer.parseInt(qm.value("eventId"));

      try {
        Event event = database.findEventById(eventId);
        database.removeUserFromEvent(eventId, id, event.getHost().getId());
      } catch(Exception e) {
        System.out.println("ERROR: SQL error");
        e.printStackTrace();
      }

      ImmutableMap.Builder<String, Object> vars = new ImmutableMap.Builder();
      //event.getEventData(vars);
      Map<String, Object> variables = vars.build();
      return gson.toJson(variables);
    }
  }

  private class CloseEventHandler implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();

      int id = Integer.parseInt(qm.value("id"));
      int eventId = Integer.parseInt(qm.value("eventId"));

      try {
        Event event = database.findEventById(eventId);
        if (event.getHost().getId() == id) {
          database.setEventState(eventId, "CLOSED");
          List<Integer> users = database.findUsersByEventId(eventId);

          for (int userId : users) {
            database.insertNotification(userId, eventId, "STATE");
          }
        }
        else {
          //TODO: tell them they don't have permission
        }
      } catch(Exception e) {
        System.out.println("ERROR: SQL error");
        e.printStackTrace();
      }

      ImmutableMap.Builder<String, Object> vars = new ImmutableMap.Builder();
      Map<String, Object> variables = vars.build();
      return gson.toJson(variables);
    }
  }

  /**
   *  Sets an events state to start, and gives every user a notification in
   *  their joined events
   */
  private class StartEventHandler implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();

      int id = Integer.parseInt(qm.value("id"));
      int eventId = Integer.parseInt(qm.value("eventId"));

      try {
        Event event = database.findEventById(eventId);
        if (event.getHost().getId() == id) {
          database.setEventState(eventId, "STARTED");
          for (int member: event.getMembers()) {
            database.incrementJoinedNotif(member);
          }

          List<Integer> users = database.findUsersByEventId(eventId);

          for (int userId : users) {
            database.insertNotification(userId, eventId, "STATE");
          }
        }
        else {
          //TODO: tell them they don't have permission
        }
      } catch(Exception e) {
        System.out.println("ERROR: SQL error");
        e.printStackTrace();
      }

      ImmutableMap.Builder<String, Object> vars = new ImmutableMap.Builder();
      Map<String, Object> variables = vars.build();
      return gson.toJson(variables);
    }
  }

  private class DeleteEventHandler implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();

      int id = Integer.parseInt(qm.value("id"));
      int eventId = Integer.parseInt(qm.value("eventId"));

      try {
        Event event = database.findEventById(eventId);
        if (event.getHost().getId() == id) {
          database.removeEvent(eventId);
        }
        else {
          //TODO: tell them they don't have permission
        }
      } catch(Exception e) {
        System.out.println("ERROR: SQL error");
        e.printStackTrace();
      }

      ImmutableMap.Builder<String, Object> vars = new ImmutableMap.Builder();
      //event.getEventData(vars);
      Map<String, Object> variables = vars.build();
      return gson.toJson(variables);
    }
  }

  private class NotificationHandler implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();

      int userId = Integer.parseInt(qm.value("id"));

      List<Notification> notifications = new ArrayList<>();

      try {
        notifications = database.findNotificationsById(userId);

      } catch(Exception e) {
        System.out.println("ERROR: SQL error");
        e.printStackTrace();
      }

      ImmutableMap.Builder<String, Object> vars = new ImmutableMap.Builder();

      List<Message> messages = new ArrayList<>();
      List<EventRequest> requests = new ArrayList<>();
      List<Event> joined = new ArrayList<>();
      List<Event> state = new ArrayList<>();

      try {
        for (Notification notif : notifications) {
          if (notif.getType() == NotificationType.MESSAGE) {
            messages.add(database.findMessageById(notif.getNotifId()));
          } else if (notif.getType() == NotificationType.REQUEST) {
            requests.add(database.findEventRequestById(notif.getNotifId()));
          } else if (notif.getType() == NotificationType.JOINED) {
            joined.add(database.findJoinedEventById(notif.getNotifId()));
          } else if (notif.getType() == NotificationType.STATE) {
            state.add(database.findEventById(notif.getNotifId()));
          }
        }
      } catch (SQLException e) {
        System.out.println("ERROR: SQL error");
        e.printStackTrace();
      }

      vars.put("messages", messages);
      vars.put("requests", requests);
      vars.put("joinedEvents", joined);
      vars.put("eventState", state);

      Map<String, Object> variables = vars.build();
      return gson.toJson(variables);
    }
  }

  private class NotificationRemoveHandler implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();

      int userId = Integer.parseInt(qm.value("id"));

      try {
        database.removeNotificationsById(userId);

      } catch(Exception e) {
        System.out.println("ERROR: SQL error");
        e.printStackTrace();
      }

      ImmutableMap.Builder<String, Object> vars = new ImmutableMap.Builder();
      Map<String, Object> variables = vars.build();
      return gson.toJson(variables);
    }
  }

//  @WebSocket
//  private class ChatHandler {
//    private String sender, msg;
//    private Gson gson = new Gson();
//
//    private Map<Session, String> usernameMap = new HashMap<>();
//    private int nextUserNumber = 1;
//
//    @OnWebSocketConnect
//    public void onConnect(Session user) throws Exception {
//      String username = "User" + nextUserNumber++;
//      usernameMap.put(user, username);
//      //Chat.broadcastMessage(sender = "Server", msg = (username + " joined the chat"));
//    }
//
//    @OnWebSocketClose
//    public void onClose(Session user, int statusCode, String reason) {
//      System.out.println("closing user");
//      String username = usernameMap.get(user);
//      usernameMap.remove(user);
//      //Chat.broadcastMessage(sender = "Server", msg = (username + " left the chat"));
//    }
//
//    @OnWebSocketMessage
//    public void onMessage(Session user, String message) {
//      JsonObject obj = (JsonObject) new JsonParser().parse(message);
//      obj.get("eventId").getAsInt();
//      System.out.println(obj.get("eventId"));
//
//      broadcastMessage(sender = usernameMap.get(user), msg = message);
//    }
//
//    //Sends a message from one user to all users
//    public void broadcastMessage(String sender, String message) {
//        usernameMap.keySet().stream().filter(Session::isOpen).forEach(session -> {
//            try {
//                session.getRemote().sendString(message
//                );
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//    }
//  }

}
