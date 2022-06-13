package io.haedoang.circulardependency.ex06.config;

import io.haedoang.circulardependency.ex06.BeanA;
import io.haedoang.circulardependency.ex06.BeanB;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * fileName : TestConfig05
 * author : haedoang
 * date : 2022-06-13
 * description :
 */
@Configuration
@ComponentScan(basePackages = "io.haedoang.circulardependency.ex06")
public class Ex06Config {
    @Bean
    public BeanA beanA() {
        return new BeanA();
    }

    @Bean
    public BeanB beanB() {
        return new BeanB();
    }
}
