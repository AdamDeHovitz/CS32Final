package edu.brown.cs.user.CS32Final;


import edu.brown.cs.user.CS32Final.Entities.Account.Account;
import edu.brown.cs.user.CS32Final.Entities.Account.Notification;
import edu.brown.cs.user.CS32Final.Entities.Account.NotificationType;
import edu.brown.cs.user.CS32Final.SQL.SqliteDatabase;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class NotificationTest {

    @Test
    public void test_insertNotification() {
        try {
            SqliteDatabase.getInstance().insertNotification(3, 1, 1, "STATE");
            List<Notification> notifications = SqliteDatabase.getInstance().findNotificationsById(3);
            assertEquals(1, notifications.size());
        } catch (SQLException e) {
            fail();
        }
        finally {
            try {
                SqliteDatabase.getInstance().runSql("DELETE FROM notification WHERE user_id = 3");
            } catch (SQLException e) {
            }
        }
    }

    @Test
    public void test_findNotificationsById() {
        try {
            SqliteDatabase.getInstance().insertNotification(3, 2, 1, "STATE");
            List<Notification> notifications = SqliteDatabase.getInstance().findNotificationsById(3);
            Notification notification = notifications.get(0);
            assertEquals(2, notification.getNotifId());
            assertEquals(NotificationType.STATE, notification.getType());
        } catch (SQLException e) {
            fail();
        }
        finally {
            try {
                SqliteDatabase.getInstance().runSql("DELETE FROM notification WHERE user_id = 3");
            } catch (SQLException e) {
            }
        }
    }
}
