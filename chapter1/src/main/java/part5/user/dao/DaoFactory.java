package part5.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import part5.db.DConnectionMaker;
import part5.db.SimpleConnectionMaker;

/**
 * author : haedoang
 * date : 2022/02/05
 * description :
 */
@Configuration
public class DaoFactory {

    @Bean
    public UserDao userDao() {
        return new UserDao(connectionMaker());
    }

    @Bean
    public SimpleConnectionMaker connectionMaker() {
        return new DConnectionMaker();
    }
}
