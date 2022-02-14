package part4.user;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import part4.user.dao.UserDao;
import part4.user.domain.User;

import javax.sql.DataSource;
import javax.swing.*;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * author : haedoang
 * date : 2022/02/06
 * description :
 */
@DisplayName("테스트를 위한 수동DI를 적용한 userDaoTest")
@DirtiesContext // 스프링의 테스트 컨텍스트 프레임워크에게 어플리케이션 컨텍스트 상태를 변경한다는 것을 알려준다(DataSource)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class UserDaoManualTest {
    private User user1;
    private User user2;
    private User user3;

    @Autowired
    private UserDao userDao;

    @Before
    public void setUp() {
        // 테스트용 DataSource를 수동으로 주입받는다.
        DataSource dataSource
                = new SingleConnectionDataSource(
                        "jdbc:mysql://localhost:3306/tobi", "student", "student", true);

        userDao.setDataSource(dataSource);
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
