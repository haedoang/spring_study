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
     *  빈 팩토리에서 관리하는 빈들은 기본적으로 싱글톤방식을 사용한다.(빈의 스코프)
     *
     *  Spring Bean 스코프
     *  스프링이 관리하는 빈의 생성과 적용되는 범위를 빈의 스코프라 한다.
     *  기본 스프링 빈 스코프는 싱글톤이다. 참고1) prototype 스코프 : 빈을 요청할 때마다 새로운 오브젝트를 만들어준다.
     *  Http 요청이 생길 때마다 생성되는 Request 스코프, 웹의 세션과 유사한 스코프인 Session 스코프 가있다.
     *
     */

}
