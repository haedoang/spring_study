package user.dao;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import user.domain.User;

import static org.assertj.core.api.Assertions.*;

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

    }

    /***
     *  ApplicationContext의 getBean 메소드는 기본적으로 Object를 리턴한다, 2번쨰 argument에 타입을 주면 캐스팅을 한다.
     */
}

