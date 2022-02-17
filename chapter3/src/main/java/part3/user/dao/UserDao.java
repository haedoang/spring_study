package part3.user.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import part3.user.domain.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Objects;

/**
 * author : haedoang
 * date : 2022/02/17
 * description :
 */
public class UserDao {
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void jdbcContextWithStatementStrategy(StatementStrategy stmt) throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;

        try {
            c = dataSource.getConnection();

            ps = stmt.makePreparedStatement(c);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    //ps.close() 에서 예외가 발생할 수 있음. 예외처리를 하지 않으면 connection 을 닫지 못한다.
                }
            }
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException e) {
                }
            }
        }

        ps.close();
        c.close();
    }

    public void add(final User user) throws SQLException {
        //        로컬 클래스
        //        class AddStatement implements StatementStrategy {
        //            @Override
        //            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
        //                final PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?,?,?)");
        //                ps.setString(1, user.getId());
        //                ps.setString(2, user.getName());
        //                ps.setString(3, user.getPassword());
        //
        //                return ps;
        //            }
        //        }
        //        final StatementStrategy stmt = new AddStatement();
        //        jdbcContextWithStatementStrategy(stmt);

        // 익명 내부 클래스
        jdbcContextWithStatementStrategy(new StatementStrategy() {
            @Override
            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                final PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?,?,?)");
                ps.setString(1, user.getId());
                ps.setString(2, user.getName());
                ps.setString(3, user.getPassword());
                return ps;
            }
        });
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

    public void deleteAll() throws SQLException {
        jdbcContextWithStatementStrategy(new StatementStrategy() {
            @Override
            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                final PreparedStatement ps = c.prepareStatement("delete from users");
                return ps;
            }
        });
    }

    public int getCount() throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            c = dataSource.getConnection();
            ps = c.prepareStatement("select count(*) from users");

            rs = ps.executeQuery();
            rs.next();

            return rs.getInt(1);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                }
            }
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException e) {
                }
            }
        }
    }

}