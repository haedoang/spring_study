package part4.user.dao;

import part4.db.SimpleConnectionMaker;

/**
 * packageName : part4.user.dao
 * fileName : MessageDao
 * author : haedoang
 * date : 2022/02/05
 * description :
 */
public class MessageDao {
    private final SimpleConnectionMaker simpleConnectionMaker;

    public MessageDao(SimpleConnectionMaker simpleConnectionMaker) {
        this.simpleConnectionMaker = simpleConnectionMaker;
    }
}
