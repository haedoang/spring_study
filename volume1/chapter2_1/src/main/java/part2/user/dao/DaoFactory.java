package part2.user.dao;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import part2.configuration.DataSourceConfig;

import javax.sql.DataSource;

/**
 * author : haedoang
 * date : 2022/02/06
 * description :
 */
@Configuration
public class DaoFactory {
    @Bean
    public UserDao userDao() {
        final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DataSourceConfig.class);
        final DataSource dataSource = context.getBean("dataSource", DataSource.class);

        final UserDao userDao = new UserDao();
        userDao.setDataSource(dataSource);
        return userDao;
    }
}
