package edu.brown.cs.user.CS32Final.GUI;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import edu.brown.cs.user.CS32Final.Entities.Account.Account;
import edu.brown.cs.user.CS32Final.Entities.Account.Profile;
import edu.brown.cs.user.CS32Final.Entities.Account.Review;
import edu.brown.cs.user.CS32Final.Entities.Event.Event;
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

  SqliteDatabase database;

  private final Gson gson = new Gson();

  public GUI() {
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

  /**
   * Runs the spark server.
   */
  private void runSparkServer() {
    System.out.println("spark server running");
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();

    try {
      database = new SqliteDatabase("data/database.sqlite3");
      database.createTables();
    } catch(Exception e) {
      System.out.println("ERROR: SQL error");
      e.printStackTrace();
    }

    // Setup Spark Routes
    // Setup Spark Routes
    Spark.get("/", new FrontHandler(), freeMarker);

    Spark.post("/register", new RegisterHandler());
    Spark.post("/login", new LoginHandler());
    Spark.post("/profile", new ProfileHandler());

    Spark.post("/event-create", new EventCreateHandler());
    Spark.post("/event-view", new EventViewHandler());
    Spark.post("/event-feed", new EventFeedHandler());
    Spark.post("/event-owner", new EventOwnerHandler());
    Spark.post("/event-joined", new EventJoinedHandler());
    Spark.post("/event-pending", new EventPendingHandler());
    Spark.post("/event-creating", new EventCreationHandler());
    Spark.post("/event-request", new RequestEventHandler());
    Spark.post("/event-join", new JoinEventHandler());
    Spark.post("/event-remove", new RemoveEventHandler());
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
              ImmutableMap.of("title", "Type Away!", "content", "");
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

      try {
      database.insertUser(email, password, first_name, last_name, image, date);
      } catch(Exception e) {
        System.out.println("ERROR: SQL error");
        //TODO: handle this
      }

      return true;
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
      try {
        Account user = database.findUserByUsername(username);

        if (user.authenticate(password)) {
          ImmutableMap.Builder<String, Object> vars = new ImmutableMap.Builder();

          user.getLoginData(vars);
          // TODO: account for sql error
          vars.put("reviews", database.findReviewsByUserId(user.getId()));
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
      } catch (Exception e) {
        System.out.println("ERROR: SQL error");
        //TODO: handle it
        return null;
      }

    }
  }

  private class EventCreateHandler implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();

      int owner_id = Integer.parseInt(qm.value("owner_id"));
      String state = qm.value("state");
      String name = qm.value("name");
      String description = qm.value("image");
      String image = qm.value("image");
      int member_capacity = Integer.parseInt(qm.value("image"));
      double cost = Double.parseDouble(qm.value("cost"));
      String location = qm.value("location");
      String tags = qm.value("tags");

      try {
      database.insertEvent(owner_id, state, name, description, image, member_capacity, cost, location, tags);
      } catch(Exception e) {
        System.out.println("ERROR: SQL error");
        //handle this
      }
      return true;
    }
  }


  private class ProfileHandler implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();

      int id = Integer.parseInt(qm.value("id"));
      Profile user = null;
      try {
        user = database.findUserProfileById(id);
      } catch(Exception e) {
        System.out.println("ERROR: SQL error");
        e.printStackTrace();
      }

      ImmutableMap.Builder<String, Object> vars = new ImmutableMap.Builder();
      user.getProfileData(vars);
      List<Review> reviews = null;
      try {
        reviews = database.findReviewsByUserId(id);
      } catch(Exception e) {
        System.out.println("ERROR: SQL error");
        e.printStackTrace();
      }
      double sum = 0;
      for (Review a : reviews) {
        sum += a.getRating();
      }
      double average = sum / reviews.size();
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
      Map<String, Object> variables = vars.build();
      return gson.toJson(variables);
    }
  }

  private class EventFeedHandler implements Route {

    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();

      int id = Integer.parseInt(qm.value("id"));
      List<Event> events = null;
      try {
      List<Integer> handled = database.findEventIdsbyOwnerId(id);
      handled.addAll(database.findEventsByRequestedId(id));
      handled.addAll(database.findEventsByUserId(id));
      events = database.findNewNearbyEvents(handled);
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
        // TODO: event isn't open
      }
      try {
        database.requestUserIntoEvent(eventId, id, event.getHost().getId());
        database.incrementHostRequestNotif(event.getHost().getId());
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

  private class JoinEventHandler implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();

      int id = Integer.parseInt(qm.value("id"));
      int eventId = Integer.parseInt(qm.value("eventId"));

      try {
      Event event = database.findEventById(eventId);
      database.insertUserIntoEvent(eventId, id, event.getHost().getId());
      database.incrementJoinedNotif(id);
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

  private class EventCreationHandler implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();

      int id = Integer.parseInt(qm.value("id"));

      try {
      List<Event> events = database.findEventsByOwnerId(id);
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
}
