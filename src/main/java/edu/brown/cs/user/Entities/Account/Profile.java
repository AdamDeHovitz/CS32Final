package edu.brown.cs.user.Entities.Account;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adamdeho on 4/10/16.
 */
public class Profile {

  private String name;
  private String image; //maybe an image class?
  private List<Integer> ratings;
  private List<Review> reviews;

  public Profile(String name, String image) {
    this.name = name;
    this.image = image;
  }

  public Profile(String name, String image,
                 List<Integer> ratings, List<Review> reviews) {
    this(name, image);
    ratings = new ArrayList(ratings);
    reviews = new ArrayList(reviews);
  }

  public String getName() {
    return name;
  }

  public String getImage() {
    return image;
  }

  public List<Integer> getRatings() {
    return ratings;
  }
  public double getRating() {
    double average = 0;
    for (int i: ratings) {
      average += i;
    }
    return average/ratings.size();
  }

  public List<Review> getReviews() {
    return reviews;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public void setRatings(List<Integer> ratings) {
    this.ratings = ratings;
  }

  public void setReviews(List<Review> reviews) {
    this.reviews = reviews;
  }

  public void addRating(int rate) {
    ratings.add(rate);
  }

}
