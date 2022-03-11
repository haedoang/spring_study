package part2.user;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import part2.user.dao.UserDao;
import part2.user.domain.Level;
import part2.user.domain.User;

import javax.sql.DataSource;
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
@ContextConfiguration(locations = "/applicationContext-part2.xml")
public class UserDaoTest {
    private User user1;
    private User user2;
    private User user3;

    @Autowired
    private UserDao userDao;

    @Autowired
    private DataSource dataSource;

    @Before
    public void setUp() throws Exception {
        user1 = new User("gyumee", "유저1", "1234", Level.BASIC, 1, 0);
        user2 = new User("leegw700", "유저2", "1234", Level.SILVER, 55, 10);
        user3 = new User("bumjin", "유저3", "1234", Level.GOLD, 100, 40);
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
    public void addAndGet() {
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
        checkSameUser(userGet1, user1);

        // then
        assertThat(user1.getName(), is(userGet1.getName()));
        assertThat(user1.getPassword(), is(userGet1.getPassword()));

        // when
        User userGet2 = userDao.get(user2.getId());
        checkSameUser(userGet2, user2);

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

    @Test(expected = DuplicateKeyException.class) //(expected = DuplicateUserIdException.class)
    public void duplicateKey() {
        // given
        userDao.add(user1);

        // when
        userDao.add(user1);
    }

    @Test
    public void sqlExceptionTranslate() {
        userDao.deleteAll();

        try {
            userDao.add(user1);
            userDao.add(user1);
        } catch (DuplicateKeyException ex) {
            final SQLException sqlEx = (SQLException) ex.getRootCause();
            SQLExceptionTranslator set =
                    new SQLErrorCodeSQLExceptionTranslator(this.dataSource);

            assertThat(set.translate(null, null, sqlEx), is(DataAccessException.class));
        }
    }

    @Test
    public void update() {
        userDao.deleteAll();

        userDao.add(user1);
        userDao.add(user2);

        user1.setName("해도앙");
        user1.setPassword("springzzang");
        user1.setLevel(Level.GOLD);
        user1.setLogin(1000);
        user1.setRecommend(999);

        userDao.update(user1);

        User user1update = userDao.get(user1.getId());
        checkSameUser(user1, user1update);

        User user2same = userDao.get(user2.getId());
        checkSameUser(user2, user2same);
    }

    private void checkSameUser(User user1, User user2) {
        //assertThat(user1.getId(), is(user2.getId()));
        assertThat(user1.getName(), is(user2.getName()));
        assertThat(user1.getPassword(), is(user2.getPassword()));
        assertThat(user1.getLevel(), is(user2.getLevel()));
        assertThat(user1.getLogin(), is(user2.getLogin()));
        assertThat(user1.getRecommend(), is(user2.getRecommend()));
    }

}