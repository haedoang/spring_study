package part7.db;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * author : haedoang
 * date : 2022/02/06
 * description :
 */
public class CountingConnectionMaker implements SimpleConnectionMaker {
    private SimpleConnectionMaker simpleConnectionMaker;
    int counter;

    public CountingConnectionMaker(SimpleConnectionMaker simpleConnectionMaker) {
        this.simpleConnectionMaker = simpleConnectionMaker;
    }

    @Override
    public Connection makeConnection() throws ClassNotFoundException, SQLException {
        this.counter++;
        return simpleConnectionMaker.makeConnection();
    }

    public int getCounter() {
        return this.counter;
    }
}
