package user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * packageName : user.dao
 * fileName : NUserDao
 * author : haedoang
 * date : 2021/10/29
 * description : D사의 UserDao 객체
 */
public class DUserDao extends UserDao {
    /** NUserInfo */
    private static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/tobi?useSSL=false";
    private static final String DB_USER = "duser";
    private static final String DB_PASS = "duser";

    @Override
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(MYSQL_DRIVER);
        Connection c = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        return c;
    }

}
