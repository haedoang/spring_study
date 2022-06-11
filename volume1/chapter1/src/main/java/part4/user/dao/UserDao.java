package part4.user.dao;


import part4.db.SimpleConnectionMaker;
import part4.user.domain.User;

import java.sql.*;

/**
 * packageName : part4.user.dao
 * fileName : UserDao
 * author : haedoang
 * date : 2022/02/05
 * description :
 */
public class UserDao {
    private final SimpleConnectionMaker simpleConnectionMaker;

    public UserDao(SimpleConnectionMaker simpleConnectionMaker) {
        this.simpleConnectionMaker = simpleConnectionMaker;
    }

    public void add(User user) throws ClassNotFoundException, SQLException {
        Connection c = simpleConnectionMaker.makeConnection();

        String query = "insert into users(id, name, password) " +
                " values (?,?,?)";

        PreparedStatement ps = c.prepareStatement(query);
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        ps.executeUpdate();

        ps.close();
        c.close();
    }


    public User get(String id) throws ClassNotFoundException, SQLException {
        Connection c = simpleConnectionMaker.makeConnection();

        String query = "select * from users where id = ?";
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

    public void truncate() throws ClassNotFoundException, SQLException {
        Connection c = simpleConnectionMaker.makeConnection();
        String query = "truncate users";
        final Statement stmt = c.createStatement();

        stmt.execute(query);
        stmt.close();
        c.close();
    }
}
