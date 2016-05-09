package edu.brown.cs.user.CS32Final.Entities.Account;

/**
 * Created by lc50 on 9/05/16.
 */
public class PendingReview {

    private int id;
    private int userId;
    private int targetId;
    private String name;
    private String image;
    private String eventName;

    public PendingReview(int id, int userId, int targetId, String name, String image, String eventName) {
        this.id = id;
        this.userId = userId;
        this.targetId = targetId;
        this.name = name;
        this.image = image;
        this.eventName = eventName;
    }
}
