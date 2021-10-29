package user.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import user.domain.User;

import java.sql.Connection;
import java.sql.DriverManager;

import static org.assertj.core.api.Assertions.*;


/**
 * packageName : user.dao
 * fileName : UserDaoTest
 * author : haedoang
 * date : 2021/10/29
 * description :
 */

@TestMethodOrder(MethodOrderer.MethodName.class)
class UserDaoTest {

    private static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/tobi?useSSL=false";
    private static final String DB_USER = "student";
    private static final String DB_PASS = "student";

    private UserDao dao;
    @BeforeEach
    void setUp() {
        dao = new UserDao();
    }

    @Test
    void T1_커넥션() throws Exception {
        /** GIVEN */
        Class.forName(MYSQL_DRIVER);
        /** WHEN */
        Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        /** THEN */
        assertThat(connection).isNotNull();
    }

    @Test
    void T2_사용자추가하기() throws Exception {
        /** GIVEN */
        User user = new User();
        user.setId("1");
        user.setName("haedong");
        user.setPassword("1234");

        /** WHEN */
        dao.add(user);
    }

    @Test
    void T3_사용자조회하기() throws Exception {
        /** GIVEN */
        String id = "1";
        User user = dao.get(id);
        assertThat(user.getId()).isEqualTo("1");
        assertThat(user.getName()).isEqualTo("haedong");
    }


}