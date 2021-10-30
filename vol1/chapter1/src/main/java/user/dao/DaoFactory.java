package user.dao;

import common.db.ConnectionMaker;
import common.db.DConnectionMaker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * packageName : user.dao
 * fileName : DaoFactory
 * author : haedoang
 * date : 2021/10/30
 * description : UserDao, ConnectionMaker 의 생성 작업을 담당할 클래스
 */
@Configuration
public class DaoFactory {

    @Bean
    public UserDao userDao() {
        return new UserDao(connectionMaker());
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
    public ConnectionMaker connectionMaker() {
        return new DConnectionMaker();
    }

    /***
     *
     *
     *  Spring Bean 등록하기
     *  각각의 Bean들을 담고 있는 DaoFactory 는 빈 팩토리라 한다.
     *  빈 팩토리에서 관리하는 빈들은 기본적으로 싱글톤방식을 사용한다.
     */

}
