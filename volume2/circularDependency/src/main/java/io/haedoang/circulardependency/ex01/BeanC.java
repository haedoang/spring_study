package io.haedoang.circulardependency.ex01;

import org.springframework.stereotype.Component;

/**
 * fileName : BeanC
 * author : haedoang
 * date : 2022-06-13
 * description :
 */
@Component
public class BeanC {

    public BeanC() {
        System.out.println("beanC expected creation order : 1 ");
    }

    public void run() {
        System.out.println("beanC run method invoked!");
    }
}
