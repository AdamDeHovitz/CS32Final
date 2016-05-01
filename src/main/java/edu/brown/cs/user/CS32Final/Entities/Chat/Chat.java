package edu.brown.cs.user.CS32Final.Entities.Chat;

import org.json.JSONObject;
//import spark.Session;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static j2html.TagCreator.*;
import org.eclipse.jetty.websocket.api.Session;
import static spark.Spark.webSocket;

/**
 * Created by lc50 on 4/27/16.
 */
public class Chat {
    static Map<Session, String> usernameMap = new HashMap<>();
    static int nextUserNumber = 1; //Used for creating the next username

    //Sends a message from one user to all users
    public static void broadcastMessage(String sender, String message) {
        usernameMap.keySet().stream().filter(Session::isOpen).forEach(session -> {
            try {
                session.getRemote().sendString(String.valueOf(new JSONObject()
                                .put("userMessage", createHtmlMessageFromSender(sender, message)))
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    //Builds a HTML element with a sender-name, a message, and a timestamp,
    private static String createHtmlMessageFromSender(String sender, String message) {
        return article().with(
                b(sender + " says:"),
                p(message),
                span().withClass("timestamp").withText(new SimpleDateFormat("HH:mm:ss").format(new Date()))
        ).render();
    }
}
