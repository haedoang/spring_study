package io.haedoang.circulardependency.ex05.config;

import io.haedoang.circulardependency.ex05.BeanA;
import io.haedoang.circulardependency.ex05.BeanB;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * author : haedoang
 * date : 2022-06-13
 * description :
 */
@Configuration
@ComponentScan(basePackages = "io.haedoang.circulardependency.ex05")
public class Ex05Config {

    @Bean
    public BeanA beanA() {
        return new BeanA();
    }

    @Bean
    public BeanB beanB() {
        return new BeanB();
    }
}
