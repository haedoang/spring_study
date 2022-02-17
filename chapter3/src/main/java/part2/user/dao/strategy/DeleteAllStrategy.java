package part2.user.dao.strategy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * author : haedoang
 * date : 2022/02/17
 * description :
 */
public class DeleteAllStrategy implements StatementStrategy {
    @Override
    public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
        final PreparedStatement ps = c.prepareStatement("delete from users");
        return ps;
    }
}
