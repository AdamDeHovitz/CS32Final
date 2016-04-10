package edu.brown.cs.user.Entities.Account;

import edu.brown.cs.user.Entities.Event.Event;

import java.util.List;

/**
 * Created by adamdeho on 4/10/16.
 */
public class Account {

  private Profile prof;
  private String id;
  private String email;
  private List<Event> pending;
  private List<Event> joined;
  private List<Event> created;
  //private boolean hasFacebook


  public Account(Profile prof, String id, String email,
                 List<Event> pending, List<Event> joined,
                 List<Event> created) {
    this.prof = prof;
    this.id = id;
    this.email = email;
    this.pending = pending;
    this.joined = joined;
    this.created = created;
  }

  public Profile getProf() {
    return prof;
  }

  public void setProf(Profile prof) {
    this.prof = prof;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public List<Event> getPending() {
    return pending;
  }

  public void setPending(List<Event> pending) {
    this.pending = pending;
  }

  private boolean removeFrom(Event toRemove, List<Event> list) {
    boolean removed = false;
    int c = 0;
    while (!removed && c < list.size()) {
      if (toRemove.equals(list.get(c))) {
        list.remove(c);
        removed = true;
      }
      c++;
    }
    return removed;
  }

  public boolean removePending(Event a) {
    return removeFrom(a, getPending());
  }

  public List<Event> getJoined() {
    return joined;
  }

  public void setJoined(List<Event> joined) {
    this.joined = joined;
  }
  public boolean removeJoined(Event a) {
    return removeFrom(a, getJoined());
  }

  public List<Event> getCreated() {
    return created;
  }

  public void setCreated(List<Event> created) {
    this.created = created;
  }
  public boolean removeCreated(Event a) {
    return removeFrom(a, getCreated());
  }
}
