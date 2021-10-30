package user.dao;

import common.db.ConnectionMaker;
import common.db.DConnectionMaker;

/**
 * packageName : user.dao
 * fileName : DaoFactory
 * author : haedoang
 * date : 2021/10/30
 * description : UserDao, ConnectionMaker의 생성 작업을 담당할 클래스
 */
public class DaoFactory {

    public UserDao userDao() {
        return new UserDao(connectionMaker());
    }

    public AccountDao accountDao() {
        return new AccountDao(connectionMaker());
    }

    public MessageDao messageDao() {
        return new MessageDao(connectionMaker());
    }

    public ConnectionMaker connectionMaker() {
        return new DConnectionMaker();
    }

    /***
     * UserDaoTest에서 하던 객체 주입을 처리할 DaoFactory를 생성하였다.
     * 이제 UserDao 오브젝트도 DaoFactory에 의해 생성되므로 제어의 역전이 되었다고 할 수 있다.
     * 오브젝트 팩토리를 통해 제어권을 오브젝트 팩토리로 넘겨 스프링의 IoC(Inversion Of Control)을 적용하였다.
     */
}
