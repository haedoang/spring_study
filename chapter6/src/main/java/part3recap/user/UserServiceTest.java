package part3recap.user;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.MailSender;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import part3recap.user.service.TxProxyFactoryBean;
import part3recap.user.dao.UserDao;
import part3recap.user.domain.Level;
import part3recap.user.domain.User;
import part3recap.user.exception.TestUserServiceException;
import part3recap.user.infra.MockMailSender;
import part3recap.user.service.UserService;
import part3recap.user.service.UserServiceImpl;
import part3recap.user.service.UserServiceTx;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static part3recap.user.service.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static part3recap.user.service.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;

/**
 * packageName : part4.user
 * fileName : UserDaoTest
 * author : haedoang
 * date : 2022-02-28
 * description :
 */
@ContextConfiguration(locations = "/applicationContext-part3recap.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest {

    List<User> users;

    @Autowired
    UserService userService; //userServiceTx 데코레이터 패턴  client -> userServiceTx -> userServiceImpl

    @Autowired
    UserServiceImpl userServiceImpl;

    @Autowired
    UserDao userDao;

    @Autowired
    PlatformTransactionManager platformTransactionManager;

    @Autowired
    MailSender mailSender;

    @Autowired
    ApplicationContext context;

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
        UserServiceImpl testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(this.userDao);
        testUserService.setMailSender(this.mailSender);

        UserServiceTx txUserService = new UserServiceTx();
        txUserService.setTransactionManager(this.platformTransactionManager);
        txUserService.setUserService(testUserService);

        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        try {
            txUserService.upgradeLevels();
            fail("TestUserServiceException expected");
        } catch (TestUserServiceException e) {
        }

        checkLevelUpgraded(users.get(1), false);
    }

    @Test
    public void useDynamicProxy() throws Exception {
        UserServiceImpl testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(this.userDao);
        testUserService.setMailSender(this.mailSender);

//        UserServiceTx txUserService = new UserServiceTx();
//        txUserService.setTransactionManager(this.platformTransactionManager);
//        txUserService.setUserService(testUserService);

// refactoring --> 프록시 빈을 직접 생성할ㅇ 수 없기 떄문에 팩토리빈을 사용한다
//        UserService txUserService = (UserService) Proxy.newProxyInstance(
//                getClass().getClassLoader(),
//                new Class[] { UserService.class },
//                new TransactionHandler()
//        );
// refactoring --> 프록시 빈을 생성하기 위해 팩토리 빈을 사용함
        TxProxyFactoryBean txProxyFactoryBean =
                context.getBean("&userService", TxProxyFactoryBean.class);
        txProxyFactoryBean.setTarget(testUserService);
        UserService txUserService = (UserService) txProxyFactoryBean.getObject();

        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        try {
            txUserService.upgradeLevels();
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
        userServiceImpl.setMailSender(mailSender);


        userServiceImpl.upgradeLevels();

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


    static class TestUserService extends UserServiceImpl {
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
