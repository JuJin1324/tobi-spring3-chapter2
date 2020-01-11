package study.tobi.spring3.chapter2.db.access;

import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import study.tobi.spring3.chapter2.user.db.access.UserDao;
import study.tobi.spring3.chapter2.user.db.entity.User;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Yoo Ju Jin(jujin1324@daum.net)
 * Created Date : 2019-09-18
 */

public class UserDaoTest {

    private static final String MYSQL_TESTDB_URL = "jdbc:mysql://localhost:3306/testdb?useSSL=false";

    private UserDao dao;

    private User user1;
    private User user2;
    private User user3;

    @Before
    public void setUp() {
        DataSource dataSource = new SingleConnectionDataSource(MYSQL_TESTDB_URL, "scott", "tiger", true);

        dao = new UserDao();
        dao.setDataSource(dataSource);

        user1 = new User("gyumee", "박성철", "springno1");     // 픽스터
        user2 = new User("leegw700", "이길원", "springno2");   // 픽스터
        user3 = new User("bumjin", "박범진", "springno3");     // 픽스터
    }

    @Test
    public void addAndGet() throws SQLException {

        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.add(user1);
        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        User userget1 = dao.get(user1.getId());
        assertThat(userget1.getName(), is(user1.getName()));
        assertThat(userget1.getPassword(), is(user1.getPassword()));

        User userget2 = dao.get(user2.getId());
        assertThat(userget2.getName(), is(user2.getName()));
        assertThat(userget2.getPassword(), is(user2.getPassword()));
    }

    @Test
    public void count() throws SQLException {

        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.add(user1);
        assertThat(dao.getCount(), is(1));

        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        dao.add(user3);
        assertThat(dao.getCount(), is(3));
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getUserFailure() throws SQLException {

        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.get("unknown_id");
    }
}