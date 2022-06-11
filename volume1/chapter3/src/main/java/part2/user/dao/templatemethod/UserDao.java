package part2.user.dao.templatemethod;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * author : haedoang
 * date : 2022/02/17
 * description :
 */
public abstract class UserDao {
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    protected abstract PreparedStatement makeStatement(Connection c) throws SQLException;

    public void deleteAll() throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;

        try {
            c = dataSource.getConnection();
            ps = makeStatement(c); // <-
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
}
