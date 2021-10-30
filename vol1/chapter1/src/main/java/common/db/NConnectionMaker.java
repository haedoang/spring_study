package common.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * packageName : common.db
 * fileName : NConnectionMaker
 * author : haedoang
 * date : 2021/10/30
 * description : N사의 커넥션 연결 클래스
 */
public class NConnectionMaker implements ConnectionMaker {
    private static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/tobi?useSSL=false";
    private static final String DB_USER = "nuser";
    private static final String DB_PASS = "nuser";

    @Override
    public Connection makeConnection() throws ClassNotFoundException, SQLException {
        Class.forName(MYSQL_DRIVER);
        Connection c = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        return c;
    }
}
