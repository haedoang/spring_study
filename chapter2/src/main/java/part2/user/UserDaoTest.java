package part2.user;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import part2.user.dao.DaoFactory;
import part2.user.dao.UserDao;
import part2.user.domain.User;

import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * author : haedoang
 * date : 2022/02/06
 * description :
 */
public class UserDaoTest {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
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

    @Test
    public void addAndGet() throws SQLException, ClassNotFoundException {
        final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        final UserDao userDao = context.getBean("userDao", UserDao.class);

        User user = new User();
        user.setId("haedoang");
        user.setName("김해동");
        user.setPassword("할수있다");
        userDao.add(user);

        User user2 = userDao.get(user.getId());

        assertThat(user.getName(), is(user2.getName()));
    }
}
