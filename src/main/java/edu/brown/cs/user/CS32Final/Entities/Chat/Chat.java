package edu.brown.cs.user.CS32Final.Entities.Chat;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import edu.brown.cs.user.CS32Final.JsonUtil;
import org.json.JSONObject;
//import spark.Session;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static j2html.TagCreator.*;
import org.eclipse.jetty.websocket.api.Session;
import static spark.Spark.webSocket;

/**
 * Created by lc50 on 4/27/16.
 */
public class Chat {
    static Map<Session, int[]> usernameMap = new HashMap<>();
    static Map<Integer, List<Integer>> roomMap = new HashMap<>();

    //Sends a message from one user to all users
    public static void broadcastMessage(int eventId, String message) {

        for (Session session : usernameMap.keySet()) {
            if (usernameMap.get(session)[0] == eventId && session.isOpen()) {
                try {
                    session.getRemote().sendString(message
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

//        usernameMap.keySet().stream().filter(Session::isOpen).forEach(session -> {
//            try {
//                session.getRemote().sendString(message
//                );
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
    }
}
