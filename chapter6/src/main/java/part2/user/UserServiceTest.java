package part2.user;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import part2.user.dao.UserDao;
import part2.user.domain.Level;
import part2.user.domain.User;
import part2.user.service.MockMailSender;
import part2.user.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static part1.user.service.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static part1.user.service.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;

/**
 * author : haedoang
 * date : 2022/02/27
 * description :
 */
public class UserServiceTest {
    List<User> users;

    @Before
    public void setUp() {
        users = new ArrayList<>();

        users.add(new User("1", "monday", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER - 1, 0, "monday@lycos.com"));
        users.add(new User("2", "tuesday", "p2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0, "tuesday@lycos.com"));
        users.add(new User("3", "wednesday", "p3", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD - 1, "wednesday@lycos.com"));
        users.add(new User("4", "thursday", "p4", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD, "thursday@lycos.com"));
        users.add(new User("5", "friday", "p5", Level.GOLD, 100, Integer.MAX_VALUE, "friday@lycos.com"));
    }

    @Test
    public void upgradeLevels() {
        UserServiceImpl userServiceImpl = new UserServiceImpl();

        MockUserDao mockUserDao = new MockUserDao(this.users);
        userServiceImpl.setUserDao(mockUserDao);

        MockMailSender mockMailSender = new MockMailSender();
        userServiceImpl.setMailSender(mockMailSender);

        userServiceImpl.upgradeLevels();

        // dao test
        List<User> updated = mockUserDao.getUpdated();
        assertThat(updated.size(), is(2));
        checkUserAndLevel(updated.get(0), "2", Level.SILVER);
        checkUserAndLevel(updated.get(1), "4", Level.GOLD);

        //third-party mail test
        List<String> request = mockMailSender.getRequests();
        assertThat(request.size(), is(2));
        assertThat(request.get(0), is(users.get(1).getEmail()));
        assertThat(request.get(1), is(users.get(3).getEmail()));
    }

    @Test
    public void upgradeLevelsWithMockitoDao() {
        // given
        UserServiceImpl userServiceImpl = new UserServiceImpl();

        UserDao mockUserDao = mock(UserDao.class);
        when(mockUserDao.getAll()).thenReturn(this.users);
        userServiceImpl.setUserDao(mockUserDao);

        MockMailSender mockMailSender = new MockMailSender();
        userServiceImpl.setMailSender(mockMailSender);

        // when
        userServiceImpl.upgradeLevels();

        // then
        verify(mockUserDao, times(2)).update(any(User.class));
    }

    @Test
    public void upgradeLevelsWithMockito() {
        // given
        UserServiceImpl userServiceImpl = new UserServiceImpl();

        UserDao userDao = mock(UserDao.class);
        MailSender mailSender = mock(MailSender.class);
        when(userDao.getAll()).thenReturn(this.users);

        userServiceImpl.setUserDao(userDao);
        userServiceImpl.setMailSender(mailSender);

        // when
        userServiceImpl.upgradeLevels();

        // then
        verify(userDao, times(2)).update(any(User.class));

        verify(userDao).update(users.get(1));
        assertThat(users.get(1).getLevel(), is(Level.SILVER));

        verify(userDao).update(users.get(3));
        assertThat(users.get(3).getLevel(), is(Level.GOLD));

        ArgumentCaptor<SimpleMailMessage> mailMessageArg = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(2)).send(mailMessageArg.capture());
        List<SimpleMailMessage>  mailMessages = mailMessageArg.getAllValues();
        assertThat(mailMessages.get(0).getTo()[0], is(users.get(1).getEmail()));
        assertThat(mailMessages.get(1).getTo()[0], is(users.get(3).getEmail()));
    }

    private void checkUserAndLevel(User updated, String expectedId, Level expectedLevel) {
        assertThat(updated.getId(), is(expectedId));
        assertThat(updated.getLevel(), is(expectedLevel));
    }

    static class MockUserDao implements UserDao {
        private List<User> users;
        private List<User> updated = new ArrayList<>();

        public MockUserDao(List<User> users) {
            this.users = users;
        }

        public List<User> getUpdated() {
            return updated;
        }

        @Override
        public List<User> getAll() {
            return this.users;
        }

        @Override
        public void update(User user) {
            updated.add(user);
        }

        @Override
        public void add(User user) {
            throw new UnsupportedOperationException();
        }

        @Override
        public User get(String id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteAll() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getCount() {
            throw new UnsupportedOperationException();
        }
    }
}
