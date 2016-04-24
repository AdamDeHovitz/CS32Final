package edu.brown.cs.user.CS32Final.Entities.Account;

/**
 * Created by adamdeho on 4/10/16.
 */
public class Review {
  private int author;
  private int subject;
  private double rating;
  private String text;

  public Review(int author, int subject, double rating, String text) {
    this.author = author;
    this.subject = subject;
    this.rating = rating;
    this.text = text;
  }

  public int getAuthor() {
    return author;
  }

  public void setAuthor(int author) {
    this.author = author;
  }

  public int getSubject() {
    return subject;
  }

  public void setSubject(int subject) {
    this.subject = subject;
  }

  public double getRating() {
    return rating;
  }

  public void setRating(double rating) {
    this.rating = rating;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }
}
