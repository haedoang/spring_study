package part3.user;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import part3.user.dao.UserDao;
import part3.user.domain.Level;
import part3.user.domain.User;
import part3.user.exception.TestUserServiceException;
import part3.user.service.UserService;
import part3.user.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static part1.user.service.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static part1.user.service.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;

/**
 * author : haedoang
 * date : 2022-03-11
 * description :
 */
@ContextConfiguration(locations = "/applicationContext-part3.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest {
    List<User> users;

    @Autowired
    UserService userService;

    @Autowired
    UserService testUserService;

    @Autowired
    UserDao userDao;

    @Autowired
    PlatformTransactionManager transactionManager;

    @Before
    public void setUp() {
        users = new ArrayList<User>();

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

    @Test(expected = TransientDataAccessResourceException.class)
    public void readOnlyTransactionAttributes() {
        testUserService.getAll();
    }

    @Test(expected = TransientDataAccessResourceException.class)
    public void transactionSync() {
        //트랜잭션 참여하는 방법 테스트
        //트랜잭션 생성
        final DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
        txDefinition.setReadOnly(true);
        final TransactionStatus status = transactionManager.getTransaction(txDefinition);


        userService.deleteAll(); // 트랜잭션 참여
        userService.add(users.get(0)); // 트랜잭션 참여
        userService.add(users.get(1)); // 트랜잭션 참여

        transactionManager.commit(status);
    }

    @Test
    @Transactional
    public void transactionSyncDaoWithTransactional() {
        userDao.deleteAll();
        userService.add(users.get(0));
        userService.add(users.get(1));
    }

    @Test(expected = TransientDataAccessResourceException.class)
    public void transactionSyncDao() {
        final DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
        txDefinition.setReadOnly(true);
        final TransactionStatus status = transactionManager.getTransaction(txDefinition);

        userDao.deleteAll();
        userService.add(users.get(0)); // 트랜잭션 참여
        userService.add(users.get(1)); // 트랜잭션 참여

        transactionManager.commit(status);
    }

    @Test(expected = TransientDataAccessResourceException.class)
    @Transactional(readOnly = true)
    public void transactionSyncDaoWithTransactionalReadOnly() {
        userDao.deleteAll();
        userService.add(users.get(0));
        userService.add(users.get(1));
    }

    @Test
    public void transactionRollback() {
        userDao.deleteAll();
        assertThat(userDao.getCount(), is(0));

        final DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
        final TransactionStatus status = transactionManager.getTransaction(txDefinition);

        userService.add(users.get(0));
        userService.add(users.get(1));
        assertThat(userDao.getCount(), is(2));

        transactionManager.rollback(status);
        assertThat(userDao.getCount(), is(0));

//        transactionManager.commit(status);
//        assertThat(userDao.getCount(), is(2));

    }

    static class TestUserServiceImpl extends UserServiceImpl {
        private String id = "4"; //users.get(3).getId();


        @Override
        public List<User> getAll() {
            for (User user : super.getAll()) {
                super.update(user);
            }
            return null;
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
