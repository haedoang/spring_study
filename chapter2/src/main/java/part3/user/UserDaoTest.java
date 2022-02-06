package part3.user;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import part3.user.dao.DaoFactory;
import part3.user.dao.UserDao;
import part3.user.domain.User;

import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * author : haedoang
 * date : 2022/02/06
 * description :
 */
public class UserDaoTest {
    private UserDao userDao;
    private User user1;
    private User user2;
    private User user3;

    public static void main(String[] args) throws SQLException {
        final ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        final UserDao userDao = context.getBean("userDao", UserDao.class);

        User user = new User();
        user.setId("haedoang");
        user.setName("김해동");
        user.setPassword("할수있다");
        userDao.add(user);

        System.out.println(user.getId() + " 등록 성공!");

        User user2 = userDao.get(user.getId());
        System.out.println("조회하기: " + user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());
        System.out.println(user2.getId() + " 조회 성공!");

        if (!user.getName().equals(user2.getName())) {
            System.out.println("테스트 실패 (name)");
        } else if (!user.getPassword().equals(user2.getPassword())) {
            System.out.println("테스트 실패 (password)");
        } else {
            System.out.println("조회 테스트 성공");
        }

        userDao.truncate();
        System.out.println("Db 클리어 성공!");
    }

    @Before
    public void setUp() {
        final ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        userDao = context.getBean("userDao", UserDao.class);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getHandlingError() throws SQLException {
        // when
        userDao.deleteAll();

        // then
        assertThat(userDao.getCount(), is(0));

        // when
        userDao.get("NOT_EXIST");
    }

    @Test
    public void addAndGet() throws SQLException {
        // given
        user1 = new User("haedoang", "김해동", "할수있다");
        user2 = new User("lazy", "레이지", "1234");

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
        // given
        user1 = new User("user1", "유저1", "1234");
        user2 = new User("user2", "유저2", "1234");
        user3 = new User("user3", "유저3", "1234");

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
}
