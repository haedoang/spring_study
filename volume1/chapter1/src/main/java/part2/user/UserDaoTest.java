package part2.user;

import part2.user.dao.NUserDao;
import part2.user.domain.User;

import java.sql.SQLException;

/**
 * packageName : part2.user
 * fileName : Main
 * author : haedoang
 * date : 2022/02/05
 * description :
 */
public class UserDaoTest {
    public static void main(String[] args)  throws ClassNotFoundException, SQLException {
        final NUserDao nUserDao = new NUserDao();
        User user = new User();
        user.setId("haedoang");
        user.setName("김해동");
        user.setPassword("할수있다");
        nUserDao.add(user);

        System.out.println(user.getId() + " 등록 성공!");

        User user2 = nUserDao.get(user.getId());
        System.out.println("조회하기: " + user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());
        System.out.println(user2.getId() + " 조회 성공");
    }
}
