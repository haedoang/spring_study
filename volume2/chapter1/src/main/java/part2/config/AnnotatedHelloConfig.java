package part2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import part2.bean.Hello;

/**
 * author : haedoang
 * date : 2022/06/19
 * description :
 */
@Configuration
public class AnnotatedHelloConfig {
    @Bean
    public Hello hello() {
        return new Hello();
    }
}
