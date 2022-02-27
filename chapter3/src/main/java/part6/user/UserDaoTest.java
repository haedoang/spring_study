package part6.user;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import part6.user.dao.UserDao;
import part6.user.domain.User;

import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * author : haedoang
 * date : 2022/02/17
 * description :
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext-part6.xml")
public class UserDaoTest {
    private User user1;
    private User user2;
    private User user3;

    @Autowired
    private UserDao userDao;

    @Before
    public void setUp() throws Exception {
        user1 = new User("gyumee", "유저1", "1234");
        user2 = new User("leegw700", "유저2", "1234");
        user3 = new User("bumjin", "유저3", "1234");
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getHandlingError() {
        // when
        userDao.deleteAll();

        // then
        assertThat(userDao.getCount(), is(0));

        // when
        userDao.get("NOT_EXIST");
    }

    @Test
    public void addAndGet() throws SQLException {
        // when
        userDao.deleteAll();

        // then
        assertThat(userDao.getCount(), is(0));

        // when
        userDao.add(user1);
        userDao.add(user2);

        // then
        assertThat(userDao.getCount(), is(2));

        // when
        User userGet1 = userDao.get(user1.getId());

        // then
        assertThat(user1.getName(), is(userGet1.getName()));
        assertThat(user1.getPassword(), is(userGet1.getPassword()));

        // when
        User userGet2 = userDao.get(user2.getId());

        // then
        assertThat(user2.getName(), is(userGet2.getName()));
        assertThat(user2.getPassword(), is(userGet2.getPassword()));
    }

    @Test
    public void getCount() throws SQLException {
        // when
        userDao.deleteAll();

        // then
        assertThat(userDao.getCount(), is(0));

        // when
        userDao.add(user1);

        // then
        assertThat(userDao.getCount(), is(1));

        // when
        userDao.add(user2);

        // then
        assertThat(userDao.getCount(), is(2));

        // when
        userDao.add(user3);

        // then
        assertThat(userDao.getCount(), is(3));
    }

    @Test
    public void getAll() {
        userDao.deleteAll();

        // when
        final List<User> users0 = userDao.getAll();
        assertThat(users0.size(), is(0));

        // when
        userDao.add(user1);
        List<User> users1 = userDao.getAll();

        // then
        assertThat(users1.size(), is(1));
        checkSameUser(user1, users1.get(0));

        // when
        userDao.add(user2);
        List<User> users2 = userDao.getAll();

        // then
        assertThat(users2.size(), is(2));
        checkSameUser(user1, users2.get(0));
        checkSameUser(user2, users2.get(1));


        // when
        userDao.add(user3);
        List<User> users3 = userDao.getAll();

        // then
        assertThat(users3.size(), is(3));
        checkSameUser(user3, users3.get(0));
        checkSameUser(user1, users3.get(1));
        checkSameUser(user2, users3.get(2));
    }

    private void checkSameUser(User user1, User user2) {
        assertThat(user1.getId(), is(user2.getId()));
        assertThat(user1.getName(), is(user2.getName()));
        assertThat(user1.getPassword(), is(user2.getPassword()));
    }

}