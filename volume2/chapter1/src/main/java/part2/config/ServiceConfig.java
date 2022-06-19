package part2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import part2.service.HelloService;

import javax.sql.DataSource;

/**
 * author : haedoang
 * date : 2022/06/19
 * description :
 */
@Configuration
public class ServiceConfig {

    @Bean
    public DataSource dataSource() {
        final SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(com.mysql.jdbc.Driver.class);
        dataSource.setUrl("jdbc:mysql://localhost/tobi");
        dataSource.setUsername("student");
        dataSource.setPassword("student");

        return dataSource;
    }

    @Bean
    public HelloService helloService() {
        return new HelloService();
    }
}
