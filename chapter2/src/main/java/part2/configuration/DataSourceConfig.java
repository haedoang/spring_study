package part2.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

/**
 * author : haedoang
 * date : 2022/02/06
 * description :
 */
@Configuration
public class DataSourceConfig {
    @Bean
    public DataSource dataSource() {
        final SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(com.mysql.jdbc.Driver.class);
        dataSource.setUrl("jdbc:mysql://localhost:3306/tobi?useSSL=false");
        dataSource.setUsername("student");
        dataSource.setPassword("student");
        return dataSource;
    }
}
