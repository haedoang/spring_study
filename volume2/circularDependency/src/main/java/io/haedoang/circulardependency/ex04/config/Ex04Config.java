package io.haedoang.circulardependency.ex04.config;

import io.haedoang.circulardependency.ex04.BeanA;
import io.haedoang.circulardependency.ex04.BeanB;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * author : haedoang
 * date : 2022-06-13
 * description :
 */
@Configuration
@ComponentScan(basePackages = "io.haedoang.circulardependency.ex04")
public class Ex04Config {

    @Bean
    public BeanA beanA() {
        return new BeanA();
    }

    @Bean
    public BeanB beanB() {
        return new BeanB();
    }
}
