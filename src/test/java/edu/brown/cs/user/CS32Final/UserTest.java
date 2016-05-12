package edu.brown.cs.user.CS32Final;

import edu.brown.cs.user.CS32Final.Entities.Account.Account;
import edu.brown.cs.user.CS32Final.Entities.Account.Profile;
import edu.brown.cs.user.CS32Final.SQL.SqliteDatabase;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class UserTest {

    @Test
    public void test_insertUser() {
        try {
            SqliteDatabase.getInstance().insertUser("me@email.com", "password", "firstname", "lastname", "", "");
            Account account = SqliteDatabase.getInstance().findUserByUsername("me@email.com");
            assertNotNull(account);
        } catch (SQLException e) {
            fail();
        }
        finally {
            try {
                SqliteDatabase.getInstance().runSql("DELETE FROM user WHERE email = 'me@email.com'");
            } catch (SQLException e) {
            }
        }
    }

    @Test
    public void test_findUserByUsername() {
        try {
            Account account = SqliteDatabase.getInstance().findUserByUsername("n@n");
            assertNotNull(account);
            assertEquals(1, account.getId());
            assertEquals("n@n", account.getEmail());
        } catch (SQLException e) {
            fail();
        }
    }

    @Test
    public void test_findUserAccountById() {
        try {
            Account account = SqliteDatabase.getInstance().findUserAccountById(1);
            assertNotNull(account);
            assertEquals(1, account.getId());
            assertEquals("n@n", account.getEmail());
        } catch (SQLException e) {
            fail();
        }
    }

    @Test
    public void test_findUserProfileById() {
        try {
            Profile profile = SqliteDatabase.getInstance().findUserProfileById(1);
            assertNotNull(profile);
            assertEquals("new kid", profile.getName());
            assertEquals("http://images.vectorhq.com/images/previews/ab9/magician-outline-clip-art-76335.jpg", profile.getImage());
            assertEquals(-1.0, profile.getRating(), 0.001);
        } catch (SQLException e) {
            fail();
        }
    }
}
