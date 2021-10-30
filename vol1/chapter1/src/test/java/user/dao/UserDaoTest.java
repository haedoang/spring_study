package user.dao;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

/**
 * packageName : user.dao
 * fileName : UserDaoTest
 * author : haedoang
 * date : 2021/10/30
 * description : UserDao Test
 */
public class UserDaoTest {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao userDao = context.getBean("userDao", UserDao.class);
        System.out.println(userDao);

    }

}

