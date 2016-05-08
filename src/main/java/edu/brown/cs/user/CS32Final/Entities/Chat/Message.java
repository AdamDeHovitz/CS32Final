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
  private String time;

  public Message(int notifId, int userId, int eventId, String message) {
    this.id = notifId;
    this.userId = userId;
    this.eventId = eventId;
    this.content = message;
  }
}
