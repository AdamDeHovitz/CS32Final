package edu.brown.cs.user.CS32Final.Entities.Account;

import com.google.common.collect.ImmutableMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adamdeho on 4/10/16.
 */
public class Profile {

  private String firstName;
  private String lastName;
  private String image; //maybe an image class?
  private String date;
  private List<Integer> reviews;

  public Profile(String firstName, String lastName, String image, String date) {
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

  public Profile(String firstName, String lastName, String image, String date,
                 List<Integer> reviews) {
    this(firstName, lastName, image, date);

    reviews = new ArrayList(reviews);
  }

  public String getName() {
    return firstName + " " + lastName;
  }

  public String getImage() {
    return image;
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
