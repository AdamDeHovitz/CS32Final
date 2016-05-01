package edu.brown.cs.user.CS32Final.Entities.Chat;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

/**
 * Created by lc50 on 27/04/16.
 */

@WebSocket
public class ChatHandler {
    private String sender, msg;

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        String username = "User" + Chat.nextUserNumber++;
        Chat.usernameMap.put(user, username);
        //Chat.broadcastMessage(sender = "Server", msg = (username + " joined the chat"));
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        System.out.println("closing user");
        String username = Chat.usernameMap.get(user);
        Chat.usernameMap.remove(user);
        //Chat.broadcastMessage(sender = "Server", msg = (username + " left the chat"));
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {

        Chat.broadcastMessage(sender = Chat.usernameMap.get(user), msg = message);
    }
}
