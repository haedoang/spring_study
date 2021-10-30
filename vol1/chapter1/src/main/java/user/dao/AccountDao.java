package user.dao;

import common.db.ConnectionMaker;

/**
 * packageName : user.dao
 * fileName : AccountDao
 * author : haedoang
 * date : 2021/10/30
 * description :
 */
public class AccountDao {
    private ConnectionMaker connectionMaker;

    public AccountDao(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }
}
