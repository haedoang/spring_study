package user.dao;

import common.db.ConnectionMaker;
import common.db.DConnectionMaker;
import common.db.NConnectionMaker;

import java.sql.SQLException;

/**
 * packageName : user.dao
 * fileName : UserDaoTest
 * author : haedoang
 * date : 2021/10/30
 * description : UserDao Test
 */
public class UserDaoTest {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        ConnectionMaker c = new DConnectionMaker();
        //ConnectionMaker c = new NConnectionMaker();

        UserDao dUserDao = new UserDao(c);
        System.out.println(dUserDao);
    }
}

