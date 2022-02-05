package part1.user.dao;

import part1.user.domain.User;

import java.sql.*;

/**
 * packageName : part1.user.dao
 * fileName : UserDao
 * author : haedoang
 * date : 2022/02/05
 * description :
 */
public class UserDao {

    public void add(User user) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/tobi?useSSL=false", "student", "student"
        );

        final PreparedStatement ps = conn.prepareStatement("insert into users(id, name, password) values(?, ?, ?)");
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        ps.executeUpdate();

        ps.close();
        conn.close();
    }

    public User get(String id) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/tobi?useSSL=false", "student", "student"
        );

        String query = "select * from users where id = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();
        rs.next();
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));

        rs.close();
        ps.close();
        conn.close();

        return user;
    }
}
