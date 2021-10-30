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
        UserDao userDao2 = context.getBean("userDao", UserDao.class);
        System.out.println("spring bean ");
        System.out.println(userDao);
        System.out.println(userDao2);
        System.out.println(userDao == userDao2); //동일성 체크
        System.out.println(userDao.equals(userDao2)); //동등성 체크

        System.out.println("-----------------------------");
        DaoFactory factory = new DaoFactory();
        UserDao dao1 = factory.userDao();
        UserDao dao2 = factory.userDao();

        System.out.println("create object");
        System.out.println(dao1);
        System.out.println(dao2);
        System.out.println(dao1 == dao2);
        System.out.println(dao1.equals(dao2));
    }

    /***
     *  스프링은 기본적으로 싱글톤 방식으로 객체를 관리한다.
     *  스프링은 보통 서버 환경에서 사용하기 떄문에 클라이언트의 요청에 의한 오브젝트의 생성에 대한
     *  부하를 줄이기 위해 싱글톤 방식으로 설정되었다(변경 가능)
     *
     *  싱글톤 패턴의 단점..
     *  1. private 생성자를 가지고 있기 떄문에 상속할 수 없음. 클래스 자신만의 오브젝트만 사용이 가능.
     *  2. 테스트가 불가함.
     *  3. 서버환경에서 싱글톤 하나만 만들어지는 것을 보장할 수 없다.
     *    => 클래스 로더의 구성에 따라 싱글톤 클래스임에도 하나 이상의 오브젝트가 만들어질 수 있다.
     *       여러개의 JVM에 분산되어 설치되는 경우에도 독립적으로 오브젝트가 생기므로 싱글톤의 가치를 떨어뜨린다.
     *  4. 싱글톤의 상태는 전역 상태를 만들 수 있기 때문에 바람직하지 못함.
     *
     *  싱글톤 레지스트리란 ?
     *  스프링은 직접 싱글톤 형태의 오브젝트를 만들고 관리하는 기능을 제공한다.
     *  스태틱 메소드와 private 생성자를 사용해야하는 비정상적인 클래스가 아니라 평범한 자바 클래스를 싱글톤으로 활용할 수 있다.
     *  오브젝트 생성에 대한 권한을 ApplicationContext 에 위임하기 때문임.
     *  싱글톤 오브젝트라도 public 생성자를 가질 수 있다.
     *  이러한 싱글톤 오브젝트를 만들고 관리하는 것이 싱글톤 레지스트리이다.
     */
}

