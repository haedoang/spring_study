package user.dao;

import common.db.ConnectionMaker;

/**
 * packageName : user.dao
 * fileName : MessageDao
 * author : haedoang
 * date : 2021/10/30
 * description :
 */
public class MessageDao {
    private ConnectionMaker connectionMaker;

    public MessageDao(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }
}
