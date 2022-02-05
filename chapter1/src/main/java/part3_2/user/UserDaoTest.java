package part3_2.user;

import part3_2.db.DConnectionMaker;
import part3_2.user.dao.UserDao;
import part3_2.user.domain.User;

import java.sql.SQLException;

/**
 * packageName : part3
 * fileName : Main
 * author : haedoang
 * date : 2022/02/05
 * description :
 */
public class UserDaoTest {
    public static void main(String[] args)  throws ClassNotFoundException, SQLException {
        final UserDao userDao = new UserDao(new DConnectionMaker());
        User user = new User();
        user.setId("haedoang");
        user.setName("김해동");
        user.setPassword("할수있다");
        userDao.add(user);

        System.out.println(user.getId() + " 등록 성공!");

        User user2 = userDao.get(user.getId());
        System.out.println("조회하기: " + user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());
        System.out.println(user2.getId() + " 조회 성공");
    }
}
