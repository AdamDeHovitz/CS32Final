package edu.brown.cs.user.CS32Final.Entities.Account;

import com.google.common.collect.ImmutableMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adamdeho on 4/10/16.
 */
public class Profile {

  private String name;
  private String image; //maybe an image class?
  private String date;
  private List<Integer> reviews;

  public Profile(String name, String image, String date) {
    this.name = name;
    if (image == null) {
      this.image = "http://41.media.tumblr.com/5e24cd5a9db4856a7fc5" +
              "6a76efa2c9c2/tumblr_ndgox2cITl1r2wp90o1_1280.jpg";
    }
    else {
      this.image = image;
    }
    this.date = date;

  }

  public Profile(String name, String image, String date,
                 List<Integer> reviews) {
    this(name, image, date);

    reviews = new ArrayList(reviews);
  }

  public String getName() {
    return name;
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

  public void setName(String name) {
    this.name = name;
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
            .put("data", getDate())
            .put("name", getName());
  }

}
