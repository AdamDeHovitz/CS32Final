package edu.brown.cs.user.Entities.Event;

import edu.brown.cs.user.Entities.Account.Account;

/**
 * Created by adamdeho on 4/10/16.
 */
public class Request {
  private Account user;
  private String message;

  public Request(Account user, String message) {
    this.user = user;
    this.message = message;
  }

  public Account getUser() {
    return user;
  }

  public void setUser(Account user) {
    this.user = user;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
