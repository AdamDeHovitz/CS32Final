package edu.brown.cs.user.CS32Final.Entities.Chat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.jetty.websocket.api.Session;

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
