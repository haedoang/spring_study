package part4.user.dao;

import part4.db.DConnectionMaker;
import part4.db.SimpleConnectionMaker;

/**
 * packageName : part4.user.dao
 * fileName : DaoFactory
 * author : haedoang
 * date : 2022/02/05
 * description :
 */
public class DaoFactory {
// 중복코드 발생    
//    public UserDao userDao() {
//        return new UserDao(new DConnectionMaker());
//    }
//
//    public AccountDao accountDao() {
//        return new AccountDao(new DConnectionMaker());
//    }
//
//    public MessageDao messageDao() {
//        return new MessageDao(new DConnectionMaker());
//    }

    public UserDao userDao() {
        return new UserDao(connectionMaker());
    }

    public AccountDao accountDao() {
        return new AccountDao(connectionMaker());
    }

    public MessageDao messageDao() {
        return new MessageDao(connectionMaker());
    }

    public SimpleConnectionMaker connectionMaker() {
        return new DConnectionMaker();
    }
}
