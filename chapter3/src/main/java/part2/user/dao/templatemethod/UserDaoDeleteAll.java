package part2.user.dao.templatemethod;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * author : haedoang
 * date : 2022/02/17
 * description :
 */
public class UserDaoDeleteAll extends UserDao {

    @Override
    protected PreparedStatement makeStatement(Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement("delte from users");
        return ps;
    }
}
