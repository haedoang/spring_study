package user.dao;

import common.db.SimpleConnectionMaker;
import user.domain.User;

import java.sql.*;

/**
 * packageName : user.dao
 * fileName : UserDao
 * author : haedoang
 * date : 2021/10/30
 * description : 관심사의 분리를 적용한 UserDao
 */
public class UserDao {
    private SimpleConnectionMaker simpleConnectionMaker;

    public UserDao() {
        this.simpleConnectionMaker = new SimpleConnectionMaker();
    }
    public void add(User user) throws ClassNotFoundException, SQLException {
        Connection c = simpleConnectionMaker.makeNewConnection();

        String query = new StringBuilder("insert into users(id, name, password)")
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
        Connection c = simpleConnectionMaker.makeNewConnection();

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

    public static void main(String[] args) throws Exception {
        User user = new User();
        user.setId("haedoang");
        user.setName("김해동");
        user.setPassword("1234");

        UserDao dao = new UserDao();
        dao.add(user);

        User user1 = dao.get(user.getId());
        System.out.println(user1.getName());
        System.out.println(user1.getPassword());
    }

    /***
     *  관심사를 독립적으로 분리하였다.
     *  발생할 수 있는 문제점은 무엇일까?
     *  현재 UserDao 에서 N사와 S사에게 상속을 통해 기능을 구현하기가 어려워졌다.
     *  커넥션 연결을 변경할 경우 UserDao를 직접 수정해야할 문제가 다시 생겼다.
     *  어떤 클래스가 쓰일지와 어떤 메소드가 쓰여야하는지까지 알아야하는 문제점이 발생한다.
     */
}
