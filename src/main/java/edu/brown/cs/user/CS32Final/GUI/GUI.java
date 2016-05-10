package edu.brown.cs.user.CS32Final.GUI;

import static spark.Spark.webSocket;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import edu.brown.cs.user.CS32Final.Hash;
import edu.brown.cs.user.CS32Final.Entities.Account.Account;
import edu.brown.cs.user.CS32Final.Entities.Account.Notification;
import edu.brown.cs.user.CS32Final.Entities.Account.NotificationType;
import edu.brown.cs.user.CS32Final.Entities.Account.PendingReview;
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

  // private SqliteDatabase database;

  private final Gson gson = new Gson();

  public GUI() {
    try {
      // database = new SqliteDatabase("data/database.sqlite3");
      SqliteDatabase.getInstance().createTables();
    } catch (Exception e) {
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
      System.out.printf("ERROR: Unable use %s for template loading.\n",
          templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  private static int getHerokuAssignedPort() {
    ProcessBuilder processBuilder = new ProcessBuilder();
    if (processBuilder.environment().get("PORT") != null) {
      return Integer.parseInt(processBuilder.environment().get("PORT"));
    }
    return 4567; // return default port if heroku-port isn't set (i.e. on
                 // localhost)
  }

  /**
   * Runs the spark server.
   */
  private void runSparkServer() {
    Spark.port(getHerokuAssignedPort());

    Spark.externalStaticFileLocation("src/main/resources/static");
    // Spark.exception(Exception.class, new ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();

    webSocket("/chat", ChatHandler.class);

    // Setup Spark Routes
    Spark.get("/", new FrontHandler(), freeMarker);
    Spark.get("/home", new FrontHandler(), freeMarker);

    // Registration and profiles
    Spark.post("/register", new RegisterHandler());
    Spark.post("/login", new LoginHandler());
    Spark.post("/profile", new ProfileHandler());
    //Spark.post("/profile-reviews", new ProfileReviewsHandler());

    // Event creation
    Spark.post("/event-create", new EventCreateHandler());

    // View events
    Spark.post("/event-search", new EventSearchHandler());
    Spark.post("/event-view", new EventViewHandler());
    Spark.post("/events-view", new EventsViewHandler());
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

    // Account
    Spark.post("/account-info", new AccountInfoHandler());
    Spark.post("/settings", new UpdateSettingsHandler());

    // Messages
    Spark.post("/messages", new MessageHandler());

    // Reviews
    Spark.post("/review", new ReviewHandler());
    Spark.post("/pending-reviews", new PendingReviewHandler());
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
      Map<String, Object> variables = ImmutableMap.of("title", "Bulki",
          "content", "");
      return new ModelAndView(variables, "main.ftl");
    }
  }

  private class RegisterHandler implements Route {
    @Override
    public Object handle(final Request req, final Response arg1) {
      /*
      System.out.println(req.attributes());
      System.out.println(req.attributes());
      System.out.println(req.params());
      System.out.println(req.toString());*/
      QueryParamsMap qm = req.queryMap();

      String email = qm.value("email");

      String password = qm.value("password");
      String hashedPassword = Hash.getHashedPassword(password);

      String first_name = qm.value("firstName");
      String last_name = qm.value("lastName");
      String image = qm.value("image");


      Date date = new Date();
      DateFormat df = new SimpleDateFormat("dd MMMM, yyyy");

      String dateString = df.format(date);

      Account user = null;
      boolean hasError = false;
      String errorMsg = "";
      Map<String, Object> variables = null;
      ImmutableMap.Builder<String, Object> vars = new ImmutableMap.Builder();

      // check if email already in database
      try {
        if (!SqliteDatabase.getInstance().isInUserTable(email)) {
          System.out.println("email not taken");
          try {
            SqliteDatabase.getInstance().insertUser(email, hashedPassword,
                first_name, last_name, image, dateString);
          } catch (Exception e) {
            hasError = true;
            errorMsg = "There is a problem with adding to the database. Try again later.";
          }

          if (!hasError) {
            try {
              user = SqliteDatabase.getInstance().findUserByUsername(email);
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
                vars.put("reviews", SqliteDatabase.getInstance()
                    .findReviewsByUserId(user.getId()));
              } catch (Exception e) {
                hasError = true;
                errorMsg = "There is a problem with adding to the database. Try again later.";
              }
            }
          }
        } else {
          hasError = true;
          errorMsg = "Email has already been registered.";
        }

      } catch (SQLException e) {
        e.printStackTrace();
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

      String hashedPassword = Hash.getHashedPassword(password);

      Map<String, Object> variables = null;
      Account user;
      try {
        user = SqliteDatabase.getInstance().findUserByUsername(username);
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

      if (user.authenticate(hashedPassword)) {
        ImmutableMap.Builder<String, Object> vars = new ImmutableMap.Builder();

        user.getLoginData(vars);
        // TODO: account for sql error
        try {
          vars.put("reviews",
              SqliteDatabase.getInstance().findReviewsByUserId(user.getId()));

          SqliteDatabase.getInstance().updateNotification(user.getId(), false);
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
        SqliteDatabase.getInstance().insertEvent(owner_id, state, name,
            description, image, member_capacity, cost, location, tags, lat,
            lng);
      } catch (Exception e) {
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
      try {
        user = SqliteDatabase.getInstance().findUserProfileById(id);
      } catch (SQLException e) {
        System.out.println("ERROR: SQL error");
        e.printStackTrace();
      }

      ImmutableMap.Builder<String, Object> vars = new ImmutableMap.Builder();
      user.getProfileData(vars);
      List<Review> reviews = null;
      try {
        reviews = SqliteDatabase.getInstance().findReviewsByUserId(id);
      } catch (Exception e) {
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
      int userId = Integer.parseInt(qm.value("userId"));
      boolean hasError = false;

      Event event = null;
      ImmutableMap.Builder<String, Object> vars = new ImmutableMap.Builder();

      try {
        event = SqliteDatabase.getInstance().findEventById(id);
      } catch (Exception e) {
        hasError = true;

        System.out.println("ERROR: SQL error");
        e.printStackTrace();
        vars.put("hasError", hasError);
        Map<String, Object> variables = vars.build();
        return gson.toJson(variables);
      }
      if (event == null) {
        vars.put("hasError", hasError);
        Map<String, Object> variables = vars.build();
        return gson.toJson(variables);
      }

      try {
        event.getEventData(vars);
        List<Integer> requests = SqliteDatabase.getInstance()
            .findRequestsByEventId(event.getId());
        int newMessageNum = SqliteDatabase.getInstance().getMessageNum(userId,
            event.getId());
        int newRequestNum = SqliteDatabase.getInstance().getRequestNum(userId,
            event.getId());
        boolean newlyAccepted = SqliteDatabase.getInstance()
            .getNewlyAccepted(userId, event.getId());

        if (newlyAccepted) {
          SqliteDatabase.getInstance().removeNewlyAccepted(userId,
              event.getId());
        }

        vars.put("requests", requests);
        vars.put("newlyAccepted", newlyAccepted);
        vars.put("newMessageNum", newMessageNum);
        vars.put("newRequestNum", newRequestNum);
      } catch (Exception e) {
        hasError = true;
      }
      vars.put("hasError", hasError);
      Map<String, Object> variables = vars.build();
      return gson.toJson(variables);
    }
  }

  private class EventsViewHandler implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();
      int myEventNotifNum = 0;
      int joinedEventNotifNum = 0;
      int newReviewNum = 0;

      int userId = Integer.parseInt(qm.value("userId"));
      boolean hasError = false;
      ImmutableMap.Builder<String, Object> vars = new ImmutableMap.Builder();

      try {
        myEventNotifNum = SqliteDatabase.getInstance()
            .userEventsNotifNum(userId);
        joinedEventNotifNum = SqliteDatabase.getInstance()
            .joinedEventsNotifNum(userId);
        newReviewNum = SqliteDatabase.getInstance().getNewPendingReviewsNum(userId);
      } catch (SQLException e) {
        hasError = true;
      }

      vars.put("hasError", hasError);
      vars.put("myEventNotifNum", myEventNotifNum);
      vars.put("joinedEventNotifNum", joinedEventNotifNum);
      vars.put("newReviewNum", newReviewNum);
      Map<String, Object> variables = vars.build();
      return gson.toJson(variables);
    }

  }

  private class EventFeedHandler implements Route {

    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();

      int id = Integer.parseInt(qm.value("id"));
      double lat;
      double lng;
      try {
        lat = Double.parseDouble(qm.value("lat"));
        lng = Double.parseDouble(qm.value("lng"));
      } catch(NumberFormatException e) {
        e.printStackTrace();
        ImmutableMap.Builder<String, Object> vars = new ImmutableMap.Builder();
        vars.put("hasError", true);
        Map<String, Object> variables = vars.build();
        return gson.toJson(variables);

      }
      List<Event> events = null;
      try {
        List<Integer> handled = SqliteDatabase.getInstance()
            .findEventIdsbyOwnerId(id);
        handled
            .addAll(SqliteDatabase.getInstance().findEventsByRequestedId(id));
        handled.addAll(SqliteDatabase.getInstance().findEventsByUserId(id));
        events = SqliteDatabase.getInstance().findNewNearbyEvents(handled, lat,
            lng);
      } catch (Exception e) {
        System.out.println("ERROR: SQL error");
        e.printStackTrace();
      }
      ImmutableMap.Builder<String, Object> vars = new ImmutableMap.Builder();
      vars.put("events", events);

      Map<String, Object> variables = vars.build();
      return gson.toJson(variables);
    }
  }

  private class EventSearchHandler implements Route {

    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();

      //int id = Integer.parseInt(qm.value("id"));
      String line = qm.value("search");
      System.out.println(line);
      List<Event> events = null;
      try {
        events = SqliteDatabase.getInstance().findEventsByKeys(line);
      } catch (Exception e) {
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
      Map<Integer, Integer> notifCount = new HashMap<>();
      List<Event> events = null;
      try {
        events = SqliteDatabase.getInstance().findEventsByOwnerId(id);
        List<Integer> eventIds = SqliteDatabase.getInstance()
            .findEventIdsbyOwnerId(id);
        notifCount = SqliteDatabase.getInstance().getEventNotifNums(eventIds,
            id);
      } catch (Exception e) {
        System.out.println("ERROR: SQL error");
        e.printStackTrace();
      }

      ImmutableMap.Builder<String, Object> vars = new ImmutableMap.Builder();
      vars.put("events", events);
      vars.put("eventNotifs", notifCount);
      Map<String, Object> variables = vars.build();
      return gson.toJson(variables);
    }
  }

  private class EventJoinedHandler implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();
      Map<Integer, Integer> eventNotifs = new HashMap<>();

      int id = Integer.parseInt(qm.value("id"));
      List<Event> events = null;
      try {
        events = SqliteDatabase.getInstance().findJoinedEventsByUserId(id);
        eventNotifs = SqliteDatabase.getInstance().joinedEventsNotifMap(id);
      } catch (Exception e) {
        System.out.println("ERROR: SQL error");
        e.printStackTrace();
      }

      ImmutableMap.Builder<String, Object> vars = new ImmutableMap.Builder();
      vars.put("events", events);
      vars.put("eventNotifs", eventNotifs);

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
        events = SqliteDatabase.getInstance().findPendingEventsByUserId(id);
      } catch (Exception e) {
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
        SqliteDatabase.getInstance().removeRequestNotifs(eventId);
        userIds = SqliteDatabase.getInstance().findRequestsByEventId(eventId);

        for (int userId : userIds) {
          users.add(SqliteDatabase.getInstance().findUserProfileById(userId));
        }
        ownerId = SqliteDatabase.getInstance().findOwnerIdByEventId(eventId);

      } catch (Exception e) {
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
        event = SqliteDatabase.getInstance().findEventById(eventId);
      } catch (Exception e) {
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
        SqliteDatabase.getInstance().requestUserIntoEvent(eventId, id,
            event.getHost().getId());
        SqliteDatabase.getInstance()
            .incrementHostRequestNotif(event.getHost().getId());
        SqliteDatabase.getInstance().insertNotification(event.getHost().getId(),
            eventId, eventId, "REQUEST");
      } catch (Exception e) {
        System.out.println("ERROR: SQL error");
        e.printStackTrace();
      }

      ImmutableMap.Builder<String, Object> vars = new ImmutableMap.Builder();
      // event.getEventData(vars);
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
        event = SqliteDatabase.getInstance().findEventById(eventId);
        if (event.getState() != EventState.OPEN) {
          ImmutableMap.Builder<String, Object> vars = new ImmutableMap.Builder();
          vars.put("hasError", true);
          vars.put("errorMsg", "Event is closed");
          Map<String, Object> variables = vars.build();
        } else {

          SqliteDatabase.getInstance().insertUserIntoEvent(eventId, id,
              event.getHost().getId());
          SqliteDatabase.getInstance().removeRequest(eventId, id);
          SqliteDatabase.getInstance().incrementJoinedNotif(id);
          SqliteDatabase.getInstance().insertNotification(id, eventId, eventId,
              "JOINED");
        }
      } catch (Exception e) {
        System.out.println("ERROR: SQL error");
        e.printStackTrace();
      }

      ImmutableMap.Builder<String, Object> vars = new ImmutableMap.Builder();
      // event.getEventData(vars);
      try {
        if (event.getMembers().size() + 2 == event.getMaxMembers()) {
          SqliteDatabase.getInstance().setEventState(eventId, "CLOSED");
          SqliteDatabase.getInstance().cancelRequests(eventId);
          vars.put("state", "CLOSED");

          List<Integer> users = SqliteDatabase.getInstance()
              .findUsersByEventId(eventId);

          for (int userId : users) {

            SqliteDatabase.getInstance().insertNotification(userId, eventId,
                eventId, "STATE");
          }
        } else {
          vars.put("state", "OPEN");
        }
      } catch (Exception e) {
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
        Event event = SqliteDatabase.getInstance().findEventById(eventId);
        SqliteDatabase.getInstance().removeUserFromEvent(eventId, id,
            event.getHost().getId());

        EventState state = SqliteDatabase.getInstance().getEventStateById(eventId);
        if (state != null && (state == EventState.CLOSED || state == EventState.STARTED)) {
          // add user relation to the leftEventUser table
          SqliteDatabase.getInstance().insertUserIntoLeftEvent(eventId, id, event.getHost().getId());
        }

      } catch (Exception e) {
        System.out.println("ERROR: SQL error");
        e.printStackTrace();
      }

      ImmutableMap.Builder<String, Object> vars = new ImmutableMap.Builder();
      // event.getEventData(vars);
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
        Event event = SqliteDatabase.getInstance().findEventById(eventId);
        if (event.getHost().getId() == id) {
          SqliteDatabase.getInstance().cancelRequests(eventId);
          SqliteDatabase.getInstance().setEventState(eventId, "CLOSED");
          List<Integer> users = SqliteDatabase.getInstance()
              .findUsersByEventId(eventId);

          for (int userId : users) {

            SqliteDatabase.getInstance().insertNotification(userId, eventId,
                eventId, "STATE");
          }
        } else {
          // TODO: tell them they don't have permission
        }
      } catch (Exception e) {
        System.out.println("ERROR: SQL error");
        e.printStackTrace();
      }

      ImmutableMap.Builder<String, Object> vars = new ImmutableMap.Builder();
      Map<String, Object> variables = vars.build();
      return gson.toJson(variables);
    }
  }

  /**
   * Sets an events state to start, and gives every user a notification in their
   * joined events
   */
  private class StartEventHandler implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();

      int id = Integer.parseInt(qm.value("id"));
      int eventId = Integer.parseInt(qm.value("eventId"));

      try {
        Event event = SqliteDatabase.getInstance().findEventById(eventId);
        if (event.getHost().getId() == id) {
          SqliteDatabase.getInstance().setEventState(eventId, "STARTED");
          for (int member : event.getMembers()) {
            SqliteDatabase.getInstance().incrementJoinedNotif(member);
          }

          List<Integer> users = SqliteDatabase.getInstance()
              .findUsersByEventId(eventId);

          for (int userId : users) {
            SqliteDatabase.getInstance().insertNotification(userId, eventId,
                eventId, "STATE");
          }
        } else {
          // TODO: tell them they don't have permission
        }
      } catch (Exception e) {
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
        Event event = SqliteDatabase.getInstance().findEventById(eventId);

        if (event.getHost().getId() == id) {
          // Event is done: delete event
          List<Integer> participantIds = SqliteDatabase.getInstance().findAllUsersInEvent(eventId);
          List<Integer> usersLeftIds = SqliteDatabase.getInstance().findUsersLeftInEvent(eventId);

          participantIds.add(event.getHost().getId());

          System.out.println(participantIds);
          System.out.println(usersLeftIds);

          for (int i = 0; i < participantIds.size(); i++) {
            int reviewerId = participantIds.get(i);
            for (int j = 0; j < participantIds.size(); j++) {
              int targetId = participantIds.get(j);
              if (reviewerId != targetId) {
                SqliteDatabase.getInstance().insertPendingReview(reviewerId, targetId, event.getName());
              }
            }

            for (int userLeftId : usersLeftIds) {
              SqliteDatabase.getInstance().insertPendingReview(reviewerId, userLeftId, event.getName());
            }

          }
          SqliteDatabase.getInstance().cancelRequests(eventId);
          SqliteDatabase.getInstance().removeLeft(eventId);
          SqliteDatabase.getInstance().removeNotifications(eventId);
          SqliteDatabase.getInstance().removeEvent(eventId);

        } else {
          System.out.println("ERROR: user lacks necessary permission");
        }
      } catch (Exception e) {
        System.out.println("ERROR: SQL error");
        e.printStackTrace();
      }

      ImmutableMap.Builder<String, Object> vars = new ImmutableMap.Builder();
      // event.getEventData(vars);
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
        notifications = SqliteDatabase.getInstance()
            .findNotificationsById(userId);

      } catch (Exception e) {
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
            messages.add(SqliteDatabase.getInstance()
                .findMessageById(notif.getNotifId()));
          } else if (notif.getType() == NotificationType.REQUEST) {
            requests.add(SqliteDatabase.getInstance()
                .findEventRequestById(notif.getNotifId()));
          } else if (notif.getType() == NotificationType.JOINED) {
            joined.add(SqliteDatabase.getInstance()
                .findJoinedEventById(notif.getNotifId()));
          } else if (notif.getType() == NotificationType.STATE) {
            state.add(
                SqliteDatabase.getInstance().findEventById(notif.getNotifId()));
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
        SqliteDatabase.getInstance().removeNotificationsById(userId);

      } catch (Exception e) {
        System.out.println("ERROR: SQL error");
        e.printStackTrace();
      }

      ImmutableMap.Builder<String, Object> vars = new ImmutableMap.Builder();
      Map<String, Object> variables = vars.build();
      return gson.toJson(variables);
    }
  }

  private class MessageHandler implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();

      int eventId = Integer.parseInt(qm.value("id"));
      int userId = Integer.parseInt(qm.value("userId"));

      ImmutableMap.Builder<String, Object> vars = new ImmutableMap.Builder();

      List<Message> messages = new ArrayList<>();
      try {
        messages = SqliteDatabase.getInstance().findMessagesByEventId(eventId);
        SqliteDatabase.getInstance().clearMessageNotifs(eventId, userId);

      } catch (Exception e) {
        System.out.println("ERROR: SQL error");
        e.printStackTrace();
      }

      vars.put("messages", messages);
      Map<String, Object> variables = vars.build();
      return gson.toJson(variables);
    }
  }

  private class ReviewHandler implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();

      int authorId = Integer.parseInt(qm.value("authorId"));
      int targetId = Integer.parseInt(qm.value("targetId"));
      int rating = Integer.parseInt(qm.value("rating"));
      int pendingId = Integer.parseInt(qm.value("pendingId"));
      String text = qm.value("text");

      try {
        SqliteDatabase.getInstance().insertReview(authorId, text, rating, targetId);
        SqliteDatabase.getInstance().removePendingReview(pendingId);
      } catch (Exception e) {
        e.printStackTrace();
      }

      ImmutableMap.Builder<String, Object> vars = new ImmutableMap.Builder();
      vars.put("success", true);
      Map<String, Object> variables = vars.build();
      return gson.toJson(variables);
    }
  }

  private class AccountInfoHandler implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();

      int userId = Integer.parseInt(qm.value("id"));
      Account user = null;
      boolean hasError = false;

      try {
        user = SqliteDatabase.getInstance().findUserAccountById(userId);
      } catch (SQLException e) {
        hasError = true;
      }

      ImmutableMap.Builder<String, Object> vars = new ImmutableMap.Builder();
      vars.put("hasError", hasError);
      vars.put("account", user);

      Map<String, Object> variables = vars.build();
      return gson.toJson(variables);
    }
  }

  private class UpdateSettingsHandler implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();

      Account user;
      int userId = Integer.parseInt(qm.value("id"));
      String firstName = qm.value("firstName");
      String lastName = qm.value("lastName");
      String email = qm.value("email");
      String oldPassword = qm.value("oldPassword");
      String newPassword = qm.value("newPassword");
      boolean hasError = false;
      boolean failedAuth = false;


      String hashedPassword = Hash.getHashedPassword(oldPassword);

      try {
        user = SqliteDatabase.getInstance().findUserAccountById(userId);
        if (user.authenticate(hashedPassword)) {
          SqliteDatabase.getInstance().updateSettings(userId, firstName, lastName, email);
         if (newPassword != null && !newPassword.isEmpty()) {
           SqliteDatabase.getInstance().updatePassword(userId, Hash.getHashedPassword(newPassword));
         }
        } else {
          hasError = true;
          failedAuth = true;
        }
      } catch (SQLException e) {
        hasError = true;
      }

      ImmutableMap.Builder<String, Object> vars = new ImmutableMap.Builder();
      vars.put("hasError", hasError);
      vars.put("firstName", firstName);
      vars.put("lastName", lastName);
      vars.put("email", email);
      vars.put("failedAuth", failedAuth);

      Map<String, Object> variables = vars.build();
      return gson.toJson(variables);
    }
  }

  private class PendingReviewHandler implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();

      int userId = Integer.parseInt(qm.value("id"));

      List<PendingReview> pendingReviews = new ArrayList<>();

      try {
        pendingReviews = SqliteDatabase.getInstance().findPendingReviewsByUserId(userId);
        SqliteDatabase.getInstance().setPendingReviewsSeen(userId);
      } catch(SQLException e) {
        e.printStackTrace();
      }

      ImmutableMap.Builder<String, Object> vars = new ImmutableMap.Builder();
      vars.put("pendingReviews", pendingReviews);

      Map<String, Object> variables = vars.build();
      return gson.toJson(variables);
    }
  }

}
