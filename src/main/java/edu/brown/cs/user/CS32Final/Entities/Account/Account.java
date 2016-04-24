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
  private int joinedNotif;
  private int requestNotif;

  //private boolean hasFacebook


  public Account(Profile prof, String id, String email,
                 String password, int requestNotif, int joinedNotif) {
    this.prof = prof;
    this.id = id;
    this.email = email;
    this.password = password;
    this.joinedNotif = joinedNotif;
    this.requestNotif = requestNotif;
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

//  private boolean removeFrom(Event toRemove, List<Event> list) {
//    boolean removed = false;
//    int c = 0;
//    while (!removed && c < list.size()) {
//      if (toRemove.equals(list.get(c))) {
//        list.remove(c);
//        removed = true;
//      }
//      c++;
//    }
//    return removed;
//  }

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
