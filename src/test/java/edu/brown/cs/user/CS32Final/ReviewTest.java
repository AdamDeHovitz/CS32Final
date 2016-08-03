package edu.brown.cs.user.CS32Final;

import edu.brown.cs.user.CS32Final.Entities.Account.Review;
import edu.brown.cs.user.CS32Final.SQL.SqliteDatabase;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ReviewTest {

    @Test
    public void test_insertReview() {
        try {
            SqliteDatabase.getInstance().insertReview(1, "msg", 1.0, 1);
            List<Review> reviews = SqliteDatabase.getInstance().findReviewsByUserId(1);
            assertEquals(1, reviews.size());

        } catch (SQLException e) {
            fail();
        }
        finally {
            try {
                SqliteDatabase.getInstance().runSql("DELETE FROM review WHERE message = 'msg'");
            } catch (SQLException e) {
            }
        }
    }

    @Test
    public void test_findReviewsByUserId() {
        try {
            SqliteDatabase.getInstance().insertReview(1, "msg", 1.0, 1);
            List<Review> reviews = SqliteDatabase.getInstance().findReviewsByUserId(1);
            assertEquals(1, reviews.size());

            Review review = reviews.get(0);
            assertEquals("msg", review.getText());
            assertEquals(1.0, review.getRating(), 0.0001);
            assertEquals(1, review.getAuthor());
            assertEquals(1, review.getSubject());

        } catch (SQLException e) {
            fail();
        }
        finally {
            try {
                SqliteDatabase.getInstance().runSql("DELETE FROM review WHERE message = 'msg'");
            } catch (SQLException e) {
            }
        }
    }

    @Test
    public void test_findReviewIDsByUserId() {
        try {
            SqliteDatabase.getInstance().insertReview(1, "msg", 1.0, 1);
            List<Integer> ids = SqliteDatabase.getInstance().findReviewIDsByUserId(1);
            assertEquals(1, ids.size());
        } catch (SQLException e) {
            fail();
        }
        finally {
            try {
                SqliteDatabase.getInstance().runSql("DELETE FROM review WHERE message = 'msg'");
            } catch (SQLException e) {
            }
        }
    }

    @Test
    public void test_findReviewsById() {
        try {
            SqliteDatabase.getInstance().insertReview(1, "msg", 1.0, 1);
            List<Integer> ids = SqliteDatabase.getInstance().findReviewsById(1);
            assertEquals(1, ids.size());
        } catch (SQLException e) {
            fail();
        }
        finally {
            try {
                SqliteDatabase.getInstance().runSql("DELETE FROM review WHERE message = 'msg'");
            } catch (SQLException e) {
            }
        }
    }
}
