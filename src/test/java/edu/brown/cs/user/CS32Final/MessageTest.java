package edu.brown.cs.user.CS32Final;

import edu.brown.cs.user.CS32Final.Entities.Chat.Message;
import edu.brown.cs.user.CS32Final.Entities.Event.Event;
import edu.brown.cs.user.CS32Final.SQL.SqliteDatabase;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class MessageTest {

    @Test
    public void test_insertMessage() {
        try {
            SqliteDatabase.getInstance().insertMessage(3, 1, "testing", "2016-05-09T21:23:45.142Z");
            List<Message> messages = SqliteDatabase.getInstance().findMessagesByEventId(3);
            assertEquals(1, messages.size());
        } catch (SQLException e) {
            fail();
        }
        finally {
            try {
                SqliteDatabase.getInstance().runSql("DELETE FROM message WHERE message = 'testing'");
            } catch (SQLException e) {
            }
        }
    }
}
