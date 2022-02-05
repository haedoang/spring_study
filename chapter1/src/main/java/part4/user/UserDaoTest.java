package part4.user;

import part4.user.dao.DaoFactory;
import part4.user.dao.UserDao;
import part4.user.domain.User;

import java.sql.SQLException;

/**
 * packageName : part4
 * fileName : UserDaoTest
 * author : haedoang
 * date : 2022/02/05
 * description :
 */
public class UserDaoTest {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        final UserDao userDao = new DaoFactory().userDao();
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

        userDao.truncate();
        System.out.println("Db 클리어 성공!");
    }
}
