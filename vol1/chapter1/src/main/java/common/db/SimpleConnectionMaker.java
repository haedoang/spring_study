package common.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * packageName : common.db
 * fileName : SimpleConnectionMaker
 * author : haedoang
 * date : 2021/10/30
 * description : 공통으로 사용할 커넥션 연결 클래스
 */
public class SimpleConnectionMaker {
    private static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/tobi?useSSL=false";
    private static final String DB_USER = "student";
    private static final String DB_PASS = "student";

    public Connection makeNewConnection() throws ClassNotFoundException, SQLException {
        Class.forName(MYSQL_DRIVER);
        Connection c = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        return c;
    }
}
