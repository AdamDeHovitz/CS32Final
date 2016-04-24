package edu.brown.cs.user.CS32Final.Entities.Account;

import com.google.common.collect.ImmutableMap;
import edu.brown.cs.user.CS32Final.Entities.Event.Event;

import java.util.List;
import java.util.Map;

/**
 * Created by adamdeho on 4/10/16.
 */
public class Account {

  private Profile prof;
  private String id;
  private String email;
  private String password;
  private List<Event> pending;
  private List<Event> joined;
  private List<Event> created;
  private int jnotif;
  private int hnotif;

  //private boolean hasFacebook


  public Account(Profile prof, String id, String email,
                 List<Event> pending, List<Event> joined,
                 List<Event> created, String password, int jnotif, int hnotif) {
    this.prof = prof;
    this.id = id;
    this.email = email;
    this.pending = pending;
    this.joined = joined;
    this.created = created;
    this.password = password;
    this.jnotif = jnotif;
    this.hnotif = hnotif;
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

  public boolean authenticate(String password) {
    return password.equals(this.password);
  }

  public Map<String, Object> getData() {
    Map<String, Object> variables = new ImmutableMap.Builder()
            .put("id", id)
            .put("picture", prof.getImage())
            .put("rating", prof.getRating()).build();
  return variables;
  }
}
