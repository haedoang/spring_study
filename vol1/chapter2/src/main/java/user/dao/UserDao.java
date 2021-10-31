package user.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import user.domain.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * packageName : user.dao
 * fileName : UserDao
 * author : haedoang
 * date : 2021/10/31
 * description :
 */
public class UserDao {
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void add(User user) throws SQLException {
        Connection c = dataSource.getConnection();

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


    public User get(String id) throws SQLException {
        Connection c = dataSource.getConnection();

        String query = new StringBuilder("select * from users where id = ?").toString();
        PreparedStatement ps = c.prepareStatement(query);
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();

        User user = null;
        if(rs.next()) {
            user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
        }

        rs.close();
        ps.close();
        c.close();

        //결과가 없으면 EmpltyResultDataAccessException을 리턴한다.
        if(user == null) throw new EmptyResultDataAccessException(1);

        return user;
    }

    public void deleteAll() throws SQLException {
        Connection c = dataSource.getConnection();
        String query = new StringBuilder("delete from users").toString();
        PreparedStatement ps = c.prepareStatement(query);
        ps.executeUpdate();

        ps.close();
        c.close();
    }

    public int getCount() throws SQLException {
        Connection c = dataSource.getConnection();
        String query = new StringBuilder("select count(*) from users").toString();
        PreparedStatement ps = c.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        rs.next();
        int count = rs.getInt(1);

        rs.close();
        ps.close();
        c.close();

        return count;
    }
}
