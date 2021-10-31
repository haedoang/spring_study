package user.dao;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import user.domain.User;

import java.sql.SQLException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * packageName : user.dao
 * fileName : UserDaoTest
 * author : haedoang
 * date : 2021/10/31
 * description : userDaoTest Junit
 */
public class UserDaoTest {

    /** BEFORE JUNIT */
//    public static void main(String[] args) throws Exception {
//        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
//        UserDao userDao = context.getBean("userDao", UserDao.class);
//
//        User user = new User();
//        user.setId("haedoang");
//        user.setName("김해동");
//        user.setPassword("1234");
//        userDao.add(user);
//
//        User user1 = userDao.get(user.getId());
//
//        if(!user.getName().equals(user1.getName())) {
//            System.out.println("테스트 실패(name)");
//        } else if(!user.getPassword().equals(user1.getPassword())) {
//            System.out.println("테스트 실패(password");
//        } else {
//            System.out.println("조회 테스트 성공");
//        }
//    }
    @Test
    public void addAndGet() throws SQLException {
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
        UserDao userDao = context.getBean("userDao", UserDao.class);

        User user = new User();
        user.setId("haedoang");
        user.setName("김해동");
        user.setPassword("1234");
        userDao.add(user);

        User user1 = userDao.get(user.getId());


        assertThat(user.getName(), is(user1.getName()));
        assertThat(user.getPassword(), is(user1.getPassword()));
    }


    public static void main(String[] args) {
        System.out.println("JUNIT TEST 실행.");
        JUnitCore.main("user.dao.UserDaoTest");
    }

    /**
     *  2.1에서는 사용자가 테스트의 결과를 확인했던 반면
     *  2.2에 적용한 JUNIT 프레임워크를 통해 테스트의 결과를 확인할 수 있게 되었다.
     *  사용자는 테스트이ㅡ 결과를 성공 실패의 결과로 확인할 수 있고, 실패한 곳을 확인할 수 있게 되었다.
     * */
}
