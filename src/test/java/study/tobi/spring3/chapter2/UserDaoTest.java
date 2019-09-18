package study.tobi.spring3.chapter2;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import study.tobi.spring3.chapter2.user.User;
import study.tobi.spring3.chapter2.user.dao.UserDao;

import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by Yoo Ju Jin(yjj@hanuritien.com)
 * Created Date : 2019-09-18
 */

public class UserDaoTest {
    private static ApplicationContext context;
    private UserDao dao;

    @Before
    public void setUp() throws Exception {
//        context = new AnnotationConfigApplicationContext(DaoFactory.class);
        context = new ClassPathXmlApplicationContext("applicationContext.xml");
        dao = context.getBean("userDao", UserDao.class);
    }

    @Test
    public void addAndGet() throws SQLException {

        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        User user = new User();
        user.setId("gymee");
        user.setPassword("springno1");
        user.setName("박성철");

        dao.add(user);
        assertThat(dao.getCount(), is(1));

        User user2 = dao.get(user.getId());
        assertThat(user2.getName(), is(user.getName()));
        assertThat(user2.getPassword(), is(user.getPassword()));
    }

    @Test
    public void test() throws SQLException {

        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        User user = new User("test1", "테스트1", "test1");
        dao.add(user);
        assertThat(dao.getCount(), is(1));

        User user2 = new User("test2", "테스트2", "test2");
        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        User user3 = new User("test3", "테스트3", "test3");
        dao.add(user3);
        assertThat(dao.getCount(), is(3));
    }
}