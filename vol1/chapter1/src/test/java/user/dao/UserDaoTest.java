package user.dao;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.sql.SQLException;

/**
 * packageName : user.dao
 * fileName : UserDaoTest
 * author : haedoang
 * date : 2021/10/30
 * description : UserDao Test
 */
public class UserDaoTest {

    public static void main(String[] args) throws SQLException {
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao userDao = context.getBean("userDao", UserDao.class);
        System.out.println(userDao);
        System.out.println(userDao.getConnection());

        ApplicationContext context2 = new GenericXmlApplicationContext("applicationContext.xml");
        UserDao userDao1 = context2.getBean("userDao", UserDao.class);
        System.out.println(userDao1);
        System.out.println(userDao1.getConnection());
    }

}

