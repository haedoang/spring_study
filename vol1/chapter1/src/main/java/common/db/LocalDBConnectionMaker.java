package common.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * packageName : common.db
 * fileName : LocalDBConnectionMaker
 * author : haedoang
 * date : 2021/10/30
 * description : 개발용 DB connection maker class
 */
public class LocalDBConnectionMaker implements ConnectionMaker {
    private static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/tobi?useSSL=false";
    private static final String DB_USER = "local";
    private static final String DB_PASS = "local";

    @Override
    public Connection makeConnection() throws ClassNotFoundException, SQLException {
        Class.forName(MYSQL_DRIVER);
        Connection c = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        return c;
    }
}
