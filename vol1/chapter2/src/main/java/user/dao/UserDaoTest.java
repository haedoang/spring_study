package user.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import user.domain.User;

import javax.sql.DataSource;
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
@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations="/applicationContext.xml") //테스트 클래스의 컨텍스트는 공유된다. 클래스 내 뿐 아니라 각기 다른 클래스에서도
@ContextConfiguration(locations = "/test-applicationContext.xml")
//@DirtiesContext  Test 내에서 ApplicationContext 변경이 된다를 선언한다.
public class UserDaoTest {
    /** fixture */
    @Autowired
    private UserDao userDao;

    @Autowired
    SimpleDriverDataSource dataSource;

    private User user1;
    private User user2;
    private User user3;

//    @Autowired
//    private ApplicationContext context;

    @Before
    public void setUp() {
        //context와 test 클래스는 각기 다른 오브젝트(독립적)이다.
        //System.out.println("this.context : " + this.context);
        //System.out.println("this : " + this);

        //this.userDao = this.context.getBean("userDao", UserDao.class);

        // TEST용 dataSource
//        DataSource dataSource = new SingleConnectionDataSource(
//                "jdbc:mysql://localhost:3306/testdb?useSSL=false", "student" ,"student",true
//        );
//        userDao.setDataSource(dataSource);
    }

    @Test
    public void addAndGet() throws SQLException {
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
        userDao.deleteAll();
        assertThat(userDao.getCount(), is(0));
        userDao.get("unknown_id");
    }

    /***
     *  스프링 테스트 적용.
     *  @RunWith는 JUnit 테스트 실행 방법을 확장하는 어노테이션이다.
     *  @ConfigurationContext 에 등록한 applicationContext를 ApplicationContext에 등록한다.
     *  테스트의 Context는 공유된다.
     *
     *  @Autowired 컨텍스트 내의 빈을 자동으로 주입해준다.
     *
     *  @DirtiesContext 테스트 메소드에서 어플리케이션 컨텍스트의 구성이나 상태의 변경을 테스트 컨텍스트 프레임워크에 알려준다.
     *                  => 이 어노테이션이 붙은 테스트에는 어플리케이션 컨텍스트 공유를 하지 않는다.
     *
     */
}
