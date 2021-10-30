package user.dao;

import common.db.ConnectionMaker;
import user.domain.User;

import java.sql.*;

/**
 * packageName : user.dao
 * fileName : UserDao
 * author : haedoang
 * date : 2021/10/30
 * description : 관심사의 분리를 적용한 UserDao, Singleton 적용하기
 */
public class UserDao {
    private ConnectionMaker connectionMaker; //가능. 읽기전용이기 떄문
    private Connection c; // 매번 바뀌는 인스턴스 변수
    private User user;    // 매번 바뀌는 인스턴스 변수
    //개별적으로 바뀌는 변수는 로컬 변수를 활용해야 멀티스레드 환경에서 문제가 발생하지 않는다.

    public UserDao(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }

    public void add(User user) throws ClassNotFoundException, SQLException {
        this.c = connectionMaker.makeConnection();

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
        this.c = connectionMaker.makeConnection();

        String query = new StringBuilder("select * from users where id = ?").toString();
        PreparedStatement ps = c.prepareStatement(query);
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();
        rs.next();
        this.user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));

        rs.close();
        ps.close();
        c.close();

        return this.user;

    }

}
