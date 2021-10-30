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

//    public UserDao() {
//        DaoFactory daoFactory = new DaoFactory();
//        this.connectionMaker = daoFactory.connectionMaker();
//    }
//    public UserDao(ConnectionMaker connectionMaker) {
//        this.connectionMaker = connectionMaker;
//    }

    //수정자 메소드
    public void setConnectionMaker(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }

    public void add(User user) throws ClassNotFoundException, SQLException {
        Connection c = connectionMaker.makeConnection();

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
        Connection c = connectionMaker.makeConnection();

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

}
