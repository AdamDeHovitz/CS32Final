package edu.brown.cs.user.Entities.Account;

/**
 * Created by adamdeho on 4/10/16.
 */
public class Review {
  private Account author;
  private Account subject;
  private int rating;
  private String text;

  public Review(Account author, Account subject, int rating, String text) {
    this.author = author;
    this.subject = subject;
    this.rating = rating;
    this.text = text;
  }

  public Account getAuthor() {
    return author;
  }

  public void setAuthor(Account author) {
    this.author = author;
  }

  public Account getSubject() {
    return subject;
  }

  public void setSubject(Account subject) {
    this.subject = subject;
  }

  public int getRating() {
    return rating;
  }

  public void setRating(int rating) {
    this.rating = rating;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }
}
