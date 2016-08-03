package edu.brown.cs.user.CS32Final.Entities.Chat;

import edu.brown.cs.user.CS32Final.Entities.Account.Account;

import java.util.Date;

/**
 * Created by lc50 on 4/10/16.
 */
public class Message {
  private int id;
  private int userId;
  private int eventId;
  private String content;
  private String date;

  private String username;
  private String eventName;

  private String image;

  public Message(int notifId, int userId, int eventId, String message, String date, String username, String eventName, String image) {
    this.id = notifId;
    this.userId = userId;
    this.eventId = eventId;
    this.content = message;
    this.date = date;

    this.username = username;
    this.eventName = eventName;
    this.image = image;
  }

  public String getContent() {
    return content;
  }
}
