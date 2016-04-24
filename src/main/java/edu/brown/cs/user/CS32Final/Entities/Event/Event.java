package edu.brown.cs.user.CS32Final.Entities.Event;

import com.google.common.collect.ImmutableMap;
import edu.brown.cs.user.CS32Final.Entities.Account.Account;
import edu.brown.cs.user.CS32Final.Entities.Chat.Chat;

import java.util.List;

/**
 * Created by adamdeho on 4/10/16.
 */
public class Event {

  private int state;
  private String name;
  private String id;
  private String desc;
  private String image = null;
  private Account host;
  private List<Account> members;
  private List<Request> requests;
  private int maxMembers;
  private double cost;
  private String location;
  private List<String> tags;
  private Chat chat;

  public Event(String host_id, int state, String name,
               String description, String image,
               int maxMembers, double cost, String location,
               List<String> tags) {
    this.state = state;
    this.name = name;
    this.id = id;
    this.desc = desc;
    this.image = image;
    this.host = host;
    this.members = members;
    this.requests = requests;
    this.maxMembers = maxMembers;
    this.cost = costPP;
    this.location = location;
    this.tags = tags;
    this.chat = chat;
  }

  //read from db

  public int getState() {
    return state;
  }

  public void setState(int state) {
    this.state = state;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public Account getHost() {
    return host;
  }

  public void setHost(Account host) {
    this.host = host;
  }

  public List<Account> getMembers() {
    return members;
  }

  public void setMembers(List<Account> members) {
    this.members = members;
  }

  public List<Request> getRequests() {
    return requests;
  }

  public void setRequests(List<Request> requests) {
    this.requests = requests;
  }

  public int getMaxMembers() {
    return maxMembers;
  }

  public void setMaxMembers(int maxMembers) {
    this.maxMembers = maxMembers;
  }

  public int getCostPP() {
    return costPP;
  }

  public void setCostPP(int costPP) {
    this.costPP = costPP;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  public Chat getChat() {
    return chat;
  }

  public void setChat(Chat chat) {
    this.chat = chat;
  }

  public void getEventData(ImmutableMap.Builder<String, Object> variables) {
    variables.put("eventId", id)
            .put("title", getName())
            .put("author", getHost().getProf().getName())
            .put("authorId", getHost().getId())
            .put("authorImg", getHost().getProf().getImage())
            .put("img", getImage())
            .put("location", getLocation())
            .put("description", getDesc())
            .put("price", getCostPP())
            .put("CurMemberNum", getMembers().size())
            .put("desiredMembers", getMaxMembers())
            .put("tags", new String[0]);
  }

}
