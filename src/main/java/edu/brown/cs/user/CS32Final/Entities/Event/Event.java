package edu.brown.cs.user.CS32Final.Entities.Event;

import java.util.List;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.user.CS32Final.Entities.Account.Account;

/**
 * Created by adamdeho on 4/10/16.
 */
public class Event {

  private int id;
  private EventState state;
  private String name;
  private int host_id;
  private String description;
  private String image = null;
  private Account host;
  private List<Account> members;
  private List<Request> requests;
  private int maxMembers;
  private double cost;
  private String location;
  private List<String> tags;

  public Event(int id, int host_id, EventState state, String name,
               String description, String image,
               int maxMembers, double cost, String location,
               List<String> tags) {
    this.id = id;
    this.host_id = host_id;
    this.state = state;
    this.name = name;
    this.description = description;
    this.image = image;
    this.maxMembers = maxMembers;
    this.cost = cost;
    this.location = location;
    this.tags = tags;
  }

  //read from db

  public EventState getState() {
    return state;
  }

  public void setState(EventState state) {
    this.state = state;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getId() {
    return host_id;
  }

  public void setId(int id) {
    this.host_id = id;
  }

  public String getDesc() {
    return description;
  }

  public void setDesc(String desc) {
    this.description = desc;
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

  public double getCostPP() {
    return cost;
  }

  public void setCostPP(double costPP) {
    this.cost = costPP;
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
/*
  public Chat getChat() {
    return chat;
  }

  public void setChat(Chat chat) {
    this.chat = chat;
  }*/

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
