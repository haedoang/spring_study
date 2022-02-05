package part4.user.dao;

import part4.db.SimpleConnectionMaker;

/**
 * packageName : part4.user.dao
 * fileName : AccountDao
 * author : haedoang
 * date : 2022/02/05
 * description :
 */
public class AccountDao {
    private final SimpleConnectionMaker simpleConnectionMaker;

    public AccountDao(SimpleConnectionMaker simpleConnectionMaker) {
        this.simpleConnectionMaker = simpleConnectionMaker;
    }
}
