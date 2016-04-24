package edu.brown.cs.user.CS32Final.Entities.Account;

import com.google.common.collect.ImmutableMap;
import edu.brown.cs.user.CS32Final.Entities.Event.Event;
import edu.brown.cs.user.CS32Final.SQL.SqliteDatabase;

import java.util.List;
import java.util.Map;

/**
 * Created by adamdeho on 4/10/16.
 */
public class Account {

  private Profile prof;
  private int id;
  private String email;
  private String password;
  private int joinedNotif;
  private int requestNotif;

  //private boolean hasFacebook


  public Account(Profile prof, int id, String email,
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

  public int getJoinedNotif() {
    return joinedNotif;
  }

  public void setJoinedNotif(int joinedNotif) {
    this.joinedNotif = joinedNotif;
  }

  public int getRequestNotif() {
    return requestNotif;
  }

  public void setRequestNotif(int requestNotif) {
    this.requestNotif = requestNotif;
  }

  public void setProf(Profile prof) {
    this.prof = prof;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
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

  public void getLoginData(ImmutableMap.Builder<String, Object> variables) {
    variables.put("id", id)
            .put("picture", prof.getImage())
            .put("data", prof.getDate())
            .put("name", prof.getName());
  }
}
