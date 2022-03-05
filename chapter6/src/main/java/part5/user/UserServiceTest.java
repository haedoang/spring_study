package part5.user;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import part5.user.dao.UserDao;
import part5.user.domain.Level;
import part5.user.domain.User;
import part5.user.exception.TestUserServiceException;
import part5.user.service.UserService;
import part5.user.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static part3recap.user.service.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static part3recap.user.service.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;

/**
 * packageName : part5.user
 * fileName : UserDaoTest
 * author : haedoang
 * date : 2022-02-28
 * description :
 */
@ContextConfiguration(locations = "/applicationContext-part5.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest {
    List<User> users;

    @Autowired
    UserService userService;

    @Autowired
    UserService testUserService;

    @Autowired
    UserDao userDao;

    @Before
    public void setUp() {
        users = new ArrayList<>();

        users.add(new User("1", "monday", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER - 1, 0));
        users.add(new User("2", "tuesday", "p2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0));
        users.add(new User("3", "wednesday", "p3", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD - 1));
        users.add(new User("4", "thursday", "p4", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD));
        users.add(new User("5", "friday", "p5", Level.GOLD, 100, Integer.MAX_VALUE));
    }

    @Test
    public void bean() {
        assertThat(this.userService, is(notNullValue()));
    }

    @Test
    public void upgradeLevels() {
        userDao.deleteAll();

        for (User user : users) {
            userDao.add(user);
        }

        userService.upgradeLevels();

        checkLevelUpgraded(users.get(0), false);
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(2), false);
        checkLevelUpgraded(users.get(3), true);
        checkLevelUpgraded(users.get(4), false);
    }


    @Test
    public void add() {
        userDao.deleteAll();

        User userWithLevel = users.get(4);
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);


        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

        assertThat(userWithLevelRead.getLevel(), is(userWithLevel.getLevel()));
        assertThat(userWithoutLevelRead.getLevel(), is(Level.BASIC));
    }

    @Test
    public void upgradeAllOrNothing() {
        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        try {
            this.testUserService.upgradeLevels();
            fail("TestUserServiceException expected");
        } catch (TestUserServiceException e) {
        }

        checkLevelUpgraded(users.get(1), false);
    }

    @Test
    public void advisorAutoProxyCreator() {
        assertThat(testUserService, is(java.lang.reflect.Proxy.class));
        assertThat(userService, is(java.lang.reflect.Proxy.class));
    }


    static class TestUserServiceImpl extends UserServiceImpl {
        private String id = "4"; //users.get(3).getId();


        @Override
        protected void upgradeLevel(User user) {
            if (user.getId().equals(id)) {
                throw new TestUserServiceException();
            }
            super.upgradeLevel(user);
        }
    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        User userUpdate = userDao.get(user.getId());
        if (upgraded) {
            assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel()));
        } else {
            assertThat(userUpdate.getLevel(), is(user.getLevel()));
        }
    }
}
