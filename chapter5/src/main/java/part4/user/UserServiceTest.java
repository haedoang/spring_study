package part4.user;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import part4.user.dao.UserDao;
import part4.user.domain.Level;
import part4.user.domain.User;
import part4.user.exception.TestUserServiceException;
import part4.user.service.MockMailSender;
import part4.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static part4.user.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static part4.user.service.UserService.MIN_RECOMMEND_FOR_GOLD;

/**
 * author : haedoang
 * date : 2022/02/23
 * description :
 */
@ContextConfiguration(locations = "/applicationContext-part4.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest {
    List<User> users;

    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    @Autowired
    PlatformTransactionManager platformTransactionManager;

    @Autowired
    MailSender mailSender;

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
        UserService testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(this.userDao);
        testUserService.setTransactionManager(this.platformTransactionManager);
        testUserService.setMailSender(this.mailSender);
        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        try {
            testUserService.upgradeLevels();
            fail("TestUserServiceException expected");
        } catch (TestUserServiceException e) {
        }

        checkLevelUpgraded(users.get(1), false);
    }

    @DirtiesContext
    @Test
    public void upgradeLevelsMock() {
        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        MockMailSender mailSender = new MockMailSender();
        userService.setMailSender(mailSender);

        userService.upgradeLevels();

        checkLevelUpgraded(users.get(0), false);
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(2), false);
        checkLevelUpgraded(users.get(3), true);
        checkLevelUpgraded(users.get(4), false);

        List<String> requests = mailSender.getRequests();
        assertThat(requests.size(), is(2));
        assertThat(requests.get(0), is(users.get(1).getEmail()));
        assertThat(requests.get(1), is(users.get(3).getEmail()));
    }


    static class TestUserService extends UserService {
        private String id;

        private TestUserService(String id) {
            this.id = id;
        }

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
