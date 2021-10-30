package user.dao;

import common.db.CountingConnectionMaker;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

/**
 * packageName : user.dao
 * fileName : UserDaoConnectionCountingTest
 * author : haedoang
 * date : 2021/10/30
 * description :
 */
public class UserDaoConnectionCountingTest {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        ApplicationContext context = new AnnotationConfigApplicationContext(CountingDaoFactory.class);
        UserDao userDao = context.getBean("userDao", UserDao.class);
        CountingConnectionMaker ccm = context.getBean("connectionMaker", CountingConnectionMaker.class);
        System.out.println("connection counter : " + ccm.getCounter());
        ccm.makeConnection();
        System.out.println("connection counter : " + ccm.getCounter());
    }
}
