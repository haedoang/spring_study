package part5.db;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * packageName : part5.db
 * fileName : SimpleConnectionMaker
 * author : haedoang
 * date : 2022/02/05
 * description :
 */
public interface SimpleConnectionMaker {
    Connection makeConnection() throws ClassNotFoundException, SQLException;
}
