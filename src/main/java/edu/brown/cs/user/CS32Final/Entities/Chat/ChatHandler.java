package edu.brown.cs.user.CS32Final.Entities.Chat;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import edu.brown.cs.user.CS32Final.SQL.SqliteDatabase;

/**
 * Created by lc50 on 27/04/16.
 */

@WebSocket
public class ChatHandler {
    private Gson gson = new Gson();
    private SqliteDatabase database;

    public ChatHandler() {
        try {
            database = new SqliteDatabase("data/database.sqlite3");
        } catch (Exception e) {
            System.out.println("ERROR: can't connect to database in chat");
        }
    }

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        //Chat.broadcastMessage(sender = "Server", msg = (username + " joined the chat"));
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        int eventId = Chat.usernameMap.get(user)[0];
        int userId = Chat.usernameMap.get(user)[1];
        System.out.println("closing user " + userId);
        Chat.usernameMap.remove(user);

        Chat.roomMap.get(eventId).remove(userId);

        //Chat.broadcastMessage(sender = "Server", msg = (username + " left the chat"));
    }

    @OnWebSocketMessage
     public void onMessage(Session user, String message) {
        System.out.println(message);

        JsonObject obj = (JsonObject) new JsonParser().parse(message);
        int eventId = obj.get("eventId").getAsInt();
        int userId = obj.get("userId").getAsInt();

        if (!Chat.roomMap.containsKey(eventId)) {
            Chat.roomMap.put(eventId, new ArrayList<>());
        }

        List<Integer> usersInRoom = Chat.roomMap.get(eventId);

        if (!usersInRoom.contains(userId)) {
            int[] val = {eventId, userId};
            Chat.usernameMap.put(user, val);
            usersInRoom.add(userId);
        }

        Chat.broadcastMessage(eventId, message);

        try {
            int messageId = database.insertMessage(eventId, userId, obj.get("text").getAsString());

            List<Integer> participants = database.findUsersByEventId(eventId);
            for (int participant : participants) {
                if (!Chat.roomMap.get(eventId).contains(participant)) {
                    database.insertNotification(participant, messageId, eventId, "MESSAGE");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
