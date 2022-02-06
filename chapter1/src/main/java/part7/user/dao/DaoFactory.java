package part7.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import part7.db.CountingConnectionMaker;
import part7.db.DConnectionMaker;
import part7.db.SimpleConnectionMaker;

import javax.xml.ws.WebEndpoint;

/**
 * author : haedoang
 * date : 2022/02/05
 * description :
 */
@Configuration
public class DaoFactory {

    @Bean
    public UserDao userDao() {
        final UserDao userDao = new UserDao();
        userDao.setConnectionMaker(connectionMaker());
        return userDao;
    }

    @Bean
    public SimpleConnectionMaker connectionMaker() {
        return new CountingConnectionMaker(realConnectionMaker());
    }

    @Bean
    public DConnectionMaker realConnectionMaker() {
        return new DConnectionMaker();
    }
}
