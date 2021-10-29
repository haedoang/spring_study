package user.dao;

import user.domain.User;

import java.sql.*;

/**
 * packageName : user.dao
 * fileName : UserDao
 * author : haedoang
 * date : 2021/10/29
 * description : 사용자 정보를 DB에 넣고 관리할 DAO
 */
public class UserDao {
    private static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/tobi?useSSL=false";
    private static final String DB_USER = "student";
    private static final String DB_PASS = "student";

    public void add(User user) throws ClassNotFoundException, SQLException {
        Class.forName(MYSQL_DRIVER);
        Connection c = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);

        String query = new StringBuilder("insert into users(id, name, password) ")
                                    .append(" values (?,?,?)").toString();

        PreparedStatement ps = c.prepareStatement(query);
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        ps.executeUpdate();

        ps.close();
        c.close();
    }


    public User get(String id) throws ClassNotFoundException, SQLException {
        Class.forName(MYSQL_DRIVER);
        Connection c = DriverManager.getConnection(DB_URL,DB_USER,DB_PASS);

        String query = new StringBuilder("select * from users where id = ?").toString();
        PreparedStatement ps = c.prepareStatement(query);
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();
        rs.next();
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));

        rs.close();
        ps.close();
        c.close();

        return user;

    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        UserDao dao = new UserDao();

        User user = new User();
        user.setId("haedoang");
        user.setName("김해동");
        user.setPassword("notMarried");

        dao.add(user);
        System.out.println(user.getId() + " registered");

        User user2 = dao.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());

        System.out.println(user2.getId() + " search succeed");

    }

    /**
     *
     *  해당 Dao의 문제점
     *  1. 중복된 코드가 발생한다. connection을 가져오는 부분이 메소드마다 동일하다. 만약 메소드의 개수가 많아진다면
     *     connection 정보가 바뀌었을 경우 메소드 모두를 수정해야할 문제가 생기게 된다.
     *  2. 예외 처리를 하지 않고 있다.
     *  3. 확장성이 떨어진다.
     * */

}
