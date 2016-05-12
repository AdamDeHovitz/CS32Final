package edu.brown.cs.user.CS32Final;

import edu.brown.cs.user.CS32Final.Entities.Account.Account;
import edu.brown.cs.user.CS32Final.Entities.Event.Event;
import edu.brown.cs.user.CS32Final.SQL.SqliteDatabase;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class EventTest {

    @Test
    public void test_insertEvent() {
        try {
            SqliteDatabase.getInstance().insertEvent(1, "OPEN", "event1", "testing event", "img", 1, 9.9, "location", null, 1.0, 1.0);
            List<Event> events = SqliteDatabase.getInstance().findEventsByOwnerId(1);
            assertEquals(2, events.size());

        } catch (SQLException e) {
            fail();
        }
        finally {
            try {
                SqliteDatabase.getInstance().runSql("DELETE FROM event WHERE name = 'event1'");
            } catch (SQLException e) {
            }
        }
    }

    @Test
    public void test_findEventById() {
        try {
            Event event = SqliteDatabase.getInstance().findEventById(3);
            assertNotNull(event);
            assertEquals("wefs", event.getName());
            assertEquals("", event.getDescription());
            assertEquals(41.8270717, event.getLat(), 0.00001);
            assertEquals(-71.3999244, event.getLng(), 0.00001);
            assertEquals("sfdfsdf", event.getLocation());
        } catch (SQLException e) {
            fail();
        }
    }

    @Test
    public void test_findEventsByKeys() {
        try {
            List<Event> events = SqliteDatabase.getInstance().findEventsByKeys("wefs");
            assertEquals(1, events.size());
            Event event = events.get(0);
            assertNotNull(event);
            assertEquals("wefs", event.getName());
            assertEquals("", event.getDescription());
            assertEquals(41.8270717, event.getLat(), 0.00001);
            assertEquals(-71.3999244, event.getLng(), 0.00001);
            assertEquals("sfdfsdf", event.getLocation());
        } catch (SQLException e) {
            fail();
        }
    }

    @Test
    public void test_findEventsByOwnerId() {
        try {
            List<Event> events = SqliteDatabase.getInstance().findEventsByOwnerId(1);
            assertEquals(1, events.size());
            Event event = events.get(0);
            assertNotNull(event);
            assertEquals("wefs", event.getName());
            assertEquals("", event.getDescription());
            assertEquals(41.8270717, event.getLat(), 0.00001);
            assertEquals(-71.3999244, event.getLng(), 0.00001);
            assertEquals("sfdfsdf", event.getLocation());
        } catch (SQLException e) {
            fail();
        }
    }

    @Test
    public void test_findNewNearbyEvents() {
        try {
            List<Event> events = SqliteDatabase.getInstance().findNewNearbyEvents(new ArrayList<>(), 41.8270717, -71.3999244);
            assertEquals(1, events.size());
            Event event = events.get(0);
            assertNotNull(event);
            assertEquals("wefs", event.getName());
            assertEquals("", event.getDescription());
            assertEquals(41.8270717, event.getLat(), 0.00001);
            assertEquals(-71.3999244, event.getLng(), 0.00001);
            assertEquals("sfdfsdf", event.getLocation());
        } catch (SQLException e) {
            fail();
        }
    }
}


