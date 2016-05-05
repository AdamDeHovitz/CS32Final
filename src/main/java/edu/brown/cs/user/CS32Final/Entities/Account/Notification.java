package edu.brown.cs.user.CS32Final.Entities.Account;

/**
 * Created by lc50 on 3/05/16.
 */
public class Notification {

    private int userId;
    private int notifId;
    private NotificationType type;

    public Notification(int userId, int notifId, String type) {
        this.userId = userId;
        this.notifId = notifId;
        this.type = NotificationType.valueOf(type);
    }

    public NotificationType getType() {
        return type;
    }

    public int getNotifId() {
        return notifId;
    }

}
