package common.db;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * packageName : common.db
 * fileName : CountingConnectionMaker
 * author : haedoang
 * date : 2021/10/30
 * description : 연결의 개수를 가지는 클래스
 */
public class CountingConnectionMaker implements ConnectionMaker {
    int counter = 0;
    private ConnectionMaker realConnectionMaker;

    public CountingConnectionMaker(ConnectionMaker realConnectionMaker) {
        this.realConnectionMaker = realConnectionMaker;
    }

    @Override
    public Connection makeConnection() throws ClassNotFoundException, SQLException {
        counter++;
        return realConnectionMaker.makeConnection();
    }

    public int getCounter() {
        return this.counter;
    }
}
