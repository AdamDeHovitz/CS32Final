package edu.brown.cs.user.CS32Final.Entities.Event;

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
  private Account Host;
  private List<Account> members;
  private List<Request> requests;
  private int maxMembers;
  private int costPP;
  private String location;
  private List<String> tags;
  private Chat chat;

  public Event(int state, String name, String id,
               String desc, String image, Account host,
               List<Account> members, List<Request> requests,
               int maxMembers, int costPP, String location,
               List<String> tags, Chat chat) {
    this.state = state;
    this.name = name;
    this.id = id;
    this.desc = desc;
    this.image = image;
    Host = host;
    this.members = members;
    this.requests = requests;
    this.maxMembers = maxMembers;
    this.costPP = costPP;
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
    return Host;
  }

  public void setHost(Account host) {
    Host = host;
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
}
