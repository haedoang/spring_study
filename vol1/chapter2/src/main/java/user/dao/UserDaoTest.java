package user.dao;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import user.domain.User;

/**
 * packageName : user.dao
 * fileName : UserDaoTest
 * author : haedoang
 * date : 2021/10/31
 * description :
 */
public class UserDaoTest {

    public static void main(String[] args) throws Exception {
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
        UserDao userDao = context.getBean("userDao", UserDao.class);

        User user = new User();
        user.setId("haedoang");
        user.setName("김해동");
        user.setPassword("1234");
        userDao.add(user);

        User user1 = userDao.get(user.getId());

        System.out.println("user1.getName() = " + user1.getName());
        System.out.println("user1.getPassword() = " + user1.getPassword());
    }

    /***
     *  테스트의 문제점
     *  1. 수동 확인 작업의 번거로움
     *    => 테스트의 결과에 대한 확인은 작성자에게 있다. 테스트를 돌려서 다시 확인하는 작업이 필요하다.
     *  2. 실행 작업의 번거로움
     *    => 테스트를 실행하려면 main() 메소드를 실행해야 한다. 이러한 테스트 클래스가 여러 개라면 실행 작업이 번거로울 수 밖에 없다.
     */
}
