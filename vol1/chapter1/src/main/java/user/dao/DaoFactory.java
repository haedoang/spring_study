package user.dao;

import common.db.ConnectionMaker;
import common.db.DConnectionMaker;
import common.db.LocalDBConnectionMaker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import user.domain.User;

/**
 * packageName : user.dao
 * fileName : DaoFactory
 * author : haedoang
 * date : 2021/10/30
 * description : UserDao, ConnectionMaker 의 생성 작업을 담당할 클래스
 */
@Configuration
public class DaoFactory {
//
//    @Bean
//    public UserDao userDao() {
//        return new UserDao(connectionMaker());
//    }

    @Bean
    public UserDao userDao() {
        return new UserDao();
    }

    @Bean
    public AccountDao accountDao() {
        return new AccountDao(connectionMaker());
    }

    @Bean
    public MessageDao messageDao() {
        return new MessageDao(connectionMaker());
    }

    @Bean
    public ConnectionMaker connectionMaker () {
//        return new ProductionDBConnectionMaker(); //운영용 DB Connect
        return new LocalDBConnectionMaker();        //개발용 DB Connect
    }

}
