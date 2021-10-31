package user.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
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
    /** fixture */
    private UserDao userDao;
    private User user1;
    private User user2;
    private User user3;

    @Before
    public void setUp() throws Exception {
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
        this.userDao = context.getBean("userDao", UserDao.class);
    }

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
//        /** GIVEN */
//        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
//        UserDao userDao = context.getBean("userDao", UserDao.class);
//
//        /** WHEN */
//        userDao.deleteAll();
//
//        /** THEN */
//        assertThat(userDao.getCount(), is(0));
//
//        /** WHEN */
//        User user = new User();
//        user.setId("haedoang");
//        user.setName("김해동");
//        user.setPassword("1234");
//        userDao.add(user);
//
//        /** THEN */
//        assertThat(userDao.getCount(), is(1));
//
//        /** WHEN */
//        User user1 = userDao.get(user.getId());
//
//        /**THEN */
//        assertThat(user.getName(), is(user1.getName()));
//        assertThat(user.getPassword(), is(user1.getPassword()));

        /** REFACTOR */
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");

        UserDao userDao = context.getBean("userDao", UserDao.class);
        this.user1 = new User("gyungri", "경리","8787");
        this.user2 = new User("mina", "미나","9999");

        userDao.deleteAll();
        assertThat(userDao.getCount(), is(0));

        userDao.add(user1);
        userDao.add(user2);
        assertThat(userDao.getCount(), is(2));

        User userget1 = userDao.get(user1.getId());
        assertThat(user1.getName(), is(userget1.getName()));
        assertThat(user1.getPassword(), is(userget1.getPassword()));


        User userget2 = userDao.get(user2.getId());
        assertThat(user2.getName(), is(userget2.getName()));
        assertThat(user2.getPassword(), is(userget2.getPassword()));

    }

    @Test
    public void count() throws Exception {
//        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
//        UserDao userDao = context.getBean("userDao", UserDao.class);

        this.user1 = new User("haedoang", "김해동", "1234");
        this.user2 = new User("chuu", "이달의소녀 츄", "5678");
        this.user3 = new User("sana", "트와이스 사나", "1357");

        userDao.deleteAll();
        assertThat(userDao.getCount(), is(0));

        userDao.add(user1);
        assertThat(userDao.getCount(),is(1));

        userDao.add(user2);
        assertThat(userDao.getCount(),is(2));

        userDao.add(user3);
        assertThat(userDao.getCount(),is(3));
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getUserFailure() throws SQLException {
//        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
//        UserDao userDao = context.getBean("userDao", UserDao.class);
        userDao.deleteAll();
        assertThat(userDao.getCount(), is(0));
        userDao.get("unknown_id");
    }



    public static void main(String[] args) {
        System.out.println("JUNIT TEST 실행.");
        JUnitCore.main("user.dao.UserDaoTest");
    }

    /**
     *  테스트 수행에 필요한 오브젝트를 픽스처라고 부르며 픽스처의 초기화를 @Before 에서 수행한다.
     *  JUNIT은 테스트 케이스 (@Test) 단위마다 테스트 오브젝트를 새로 만들어 독립적인 테스트 환경으로 돌아간다.
     * */

}
