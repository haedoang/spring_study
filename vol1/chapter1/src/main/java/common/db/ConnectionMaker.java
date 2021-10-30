package common.db;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * packageName : common.db
 * fileName : ConnectionMaker
 * author : haedoang
 * date : 2021/10/30
 * description : 커넥션 연결 인터페이스
 */
public interface ConnectionMaker {
    Connection makeConnection() throws ClassNotFoundException, SQLException;
}
