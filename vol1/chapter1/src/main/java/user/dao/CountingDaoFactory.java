package user.dao;

import common.db.ConnectionMaker;
import common.db.CountingConnectionMaker;
import common.db.DConnectionMaker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import user.domain.User;

/**
 * packageName : user.dao
 * fileName : CountingDaoFactory
 * author : haedoang
 * date : 2021/10/30
 * description :
 */
@Configuration // <Beans>
public class CountingDaoFactory {

    @Bean  // <Bean>
    public UserDao userDao() {
        UserDao userDao = new UserDao();
        userDao.setConnectionMaker(connectionMaker()); //수정자 메소드로 구현
        //return new UserDao(connectionMaker());
        return userDao;
    }

    @Bean ConnectionMaker connectionMaker() {
        return new CountingConnectionMaker(realConnectionMaker());
    }

    @Bean
    public ConnectionMaker realConnectionMaker() {
        return new DConnectionMaker();
    }

}
