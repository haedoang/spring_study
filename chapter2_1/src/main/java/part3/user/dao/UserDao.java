package part3.user.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import part3.user.domain.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Objects;

/**
 * author : haedoang
 * date : 2022/02/06
 * description :
 */
public class UserDao {
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void add(User user) throws SQLException {
        Connection c = dataSource.getConnection();

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


    public User get(String id) throws SQLException {
        Connection c = dataSource.getConnection();

        String query = "select * from users where id = ?";
        PreparedStatement ps = c.prepareStatement(query);
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();
        User user = null;
        if (rs.next()) {
            user = new User(rs.getString("id"), rs.getString("name"), rs.getString("password"));
        }

        rs.close();
        ps.close();
        c.close();

        if (Objects.isNull(user)) {
            throw new EmptyResultDataAccessException(1);
        }

        return user;
    }

    public void truncate() throws SQLException {
        Connection c = dataSource.getConnection();
        String query = "truncate users";
        final Statement stmt = c.createStatement();

        stmt.execute(query);
        stmt.close();
        c.close();
    }

    public void deleteAll() throws SQLException {
        Connection c = dataSource.getConnection();
        final PreparedStatement ps = c.prepareStatement("delete from users");

        ps.executeUpdate();

        ps.close();
        c.close();
    }

    public int getCount() throws SQLException {
        Connection c = dataSource.getConnection();
        final PreparedStatement ps = c.prepareStatement("select count(*) from users");
        final ResultSet rs = ps.executeQuery();

        rs.next();
        int count = rs.getInt(1);

        rs.close();;
        ps.close();
        c.close();

        return count;
    }
}