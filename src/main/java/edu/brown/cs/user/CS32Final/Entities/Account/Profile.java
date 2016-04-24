package edu.brown.cs.user.CS32Final.Entities.Account;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adamdeho on 4/10/16.
 */
public class Profile {

  private String name;
  private String image; //maybe an image class?
  private double rating;
  private List<Integer> reviews;

  public Profile(String name, String image) {
    this.name = name;
    this.image = image;
  }

  public Profile(String name, String image,
                 double rate, List<Integer> reviews) {
    this(name, image);
    rating = rate;
    reviews = new ArrayList(reviews);
  }

  public String getName() {
    return name;
  }

  public String getImage() {
    return image;
  }

  public double getRating() {
    return rating;
  }
//  public double getRating() {
//    double average = 0;
//    for (int i: ratings) {
//      average += i;
//    }
//    return average/ratings.size();
//  }

  public List<Integer> getReviews() {
    return reviews;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public void setRatings(double ratings) {
    this.rating = ratings;
  }

  public void setReviews(List<Integer> reviews) {
    this.reviews = reviews;
  }


}
