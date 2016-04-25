package edu.brown.cs.user.CS32Final.GUI;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
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
    Spark.post("/event-request", new RequestEventHandler());
    Spark.post("/event-join", new JoinEventHandler());
    Spark.post("/event-remove", new RemoveEventHandler());
    Spark.post("/close-event", new CloseEventHandler());
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

      try {
      database.insertUser(email, password, first_name, last_name, image, date);
      } catch(Exception e) {
        System.out.println("ERROR: SQL error");
        e.printStackTrace();
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
      double cost = Double.parseDouble(qm.value("cost"));
      String location = qm.value("location");
      String[][] tags = gson.fromJson(qm.value("tags"), String[][].class);

      System.out.println("about to go to db methods");
      int event_id = -1;
      try {
        database.insertEvent(owner_id, state, name, description, image, member_capacity, cost, location, tags);
      } catch(Exception e) {
        System.out.println("ERROR: SQL error");
        e.printStackTrace();
      }

      ImmutableMap.Builder<String, Object> vars = new ImmutableMap.Builder();
      vars.put("success", true);
      vars.put("id", event_id);
      return vars.build();
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

  private class CloseEventHandler implements Route {
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

}
