package part6.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import part6.user.UserServiceTest;
import part6.user.dao.UserDao;
import part6.user.infra.DummyMailSender;
import part6.user.service.UserService;

import javax.sql.DataSource;

/**
 * author : haedoang
 * date : 2022/03/23
 * description :
 */
@Configuration
//@ImportResource("/test-applicationContext.xml")
@EnableTransactionManagement
@ComponentScan(basePackages = "part6.user")

// EnableSqlService 리팩토링
//@Import({SqlServiceContext.class,
//        AppContext.TestAppContext.class, AppContext.ProductionAppContext.class 중첩 클래스 시 제거 가능
//})
@EnableSqlService
@PropertySource(value = "/database.properties")
public class AppContext implements SqlMapConfig {
    /**
     * value properties
     */
    @Value("${db.driverClass}")
    Class<? extends java.sql.Driver> driverClass;

    @Value("${db.url}")
    String url;

    @Value("${db.username}")
    String username;

    @Value("${db.password}")
    String password;

    //@Autowired
    //SqlService sqlService; //xml bean

    /**
     * Environment null => 4.0에서 고쳐짐 (DI 상위로 올릴 것)
     * <a href="https://stackoverflow.com/questions/19454289/spring-boot-environment-autowired-throws-nullpointerexception">Environment null issue</a>
     */
    @Autowired
    Environment env;

    @Autowired
    UserDao userDao;


    /**
     * sql config
     */
//    @Bean
//    public SqlMapConfig sqlMapConfig() {
//        return new UserSqlMapConfig();
//    }

    @Override
    public Resource getSqlMapResource() {
        return new ClassPathResource("sqlmap.xml", UserDao.class);
    }

    /**
     * static 으로 정의해야 한다
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    /**
     * DB CONNECT & TRANSACTION
     */

    @Bean
    public DataSource dataSource() {
//        프로퍼티로 리팩터링
//        final SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
//        dataSource.setDriverClass(Driver.class);
//        dataSource.setUrl("jdbc:mysql://localhost:3306/tobi?useSSL=false");
//        dataSource.setUsername("student");
//        dataSource.setPassword("student");

        final SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        // 1. environment
//        try {
//            dataSource.setDriverClass((Class<? extends java.sql.Driver>)Class.forName(env.getProperty("db.driverClass")));
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//
//        dataSource.setUrl(env.getProperty("db.url"));
//        dataSource.setUsername(env.getProperty("db.username"));
//        dataSource.setPassword(env.getProperty("db.password"));

        // 2. value properties

        dataSource.setDriverClass(this.driverClass);
        dataSource.setUrl(this.url);
        dataSource.setUsername(this.username);
        dataSource.setPassword(this.password);

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager tm = new DataSourceTransactionManager();
        tm.setDataSource(dataSource());

        return tm;
    }

    /** APPLICATION LOGIC & TEST */
//    @Component 대체

//    @Bean
//    public UserDao userDao() {
//        final UserDaoJdbc userDaoJdbc = new UserDaoJdbc();
//            //userDaoJdbc.setSqlService(sqlService()); 오토와이어링
//            //userDaoJdbc.setDataSource(dataSource());  오토와이어링
//        return userDaoJdbc;
//    }

//    @Bean
//    public UserService userService() {
//        final UserServiceImpl userService = new UserServiceImpl();
//        userService.setMailSender(mailSender());
////        userService.setUserDao(userDao());
//        userService.setUserDao(this.userDao);
//
//        return userService;
//    }


    /**
     * author : haedoang
     * date : 2022/03/25
     * description :
     */
    @Configuration
    @Profile("production")
    public static class ProductionAppContext {
        @Bean
        public MailSender mailSender() {
            final JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
            javaMailSender.setHost("localhost");

            return javaMailSender;
        }
    }

    /**
     * author : haedoang
     * date : 2022/03/25
     * description :
     */
    @Configuration
    @Profile("test")
    public static class TestAppContext {
        @Bean
        public UserService testUserService() {
            return new UserServiceTest.TestUserService();
        }

        @Bean
        public MailSender mailSender() {
            return new DummyMailSender();
        }
    }
}
