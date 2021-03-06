package edu.brown.cs.user.CS32Final.Entities.Account;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableMap;

/**
 * Created by adamdeho on 4/10/16.
 */
public class Profile {

  private int id;
  private String firstName;
  private String lastName;
  private String image; //maybe an image class?
  private String date;
  private List<Integer> reviews;
  private double rating;

  public Profile(int id, String firstName, String lastName, String image, String date) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    if (image == null) {
      this.image = "http://images.vectorhq.com/images/previews/ab9/" +
              "magician-outline-clip-art-76335.jpg";
    } else {
      this.image = image;
    }

    this.date = date;

  }

  public Profile(int id, String firstName, String lastName, String image, String date,
                 List<Integer> reviews) {
    this(id, firstName, lastName, image, date);

    reviews = new ArrayList(reviews);
  }

  public String getName() {
    return firstName + " " + lastName;
  }

  public String getImage() {
    return image;
  }

  public double getRating() {
    return rating;
  }

  public void setRating(double rating) {
    this.rating = rating;
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


  public void setImage(String image) {
    this.image = image;
  }

  public String getDate() {
    return date;
  }

  public void setReviews(List<Integer> reviews) {
    this.reviews = reviews;
  }

  public void getProfileData(ImmutableMap.Builder<String, Object> variables) {
    variables.put("picture", getImage())
            .put("date", getDate())
            .put("name", getName());
  }

}
