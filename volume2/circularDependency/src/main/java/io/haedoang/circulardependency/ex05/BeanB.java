package io.haedoang.circulardependency.ex05;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * fileName : BeanB
 * author : haedoang
 * date : 2022-06-13
 * description :
 */
@Component
public class BeanB {
    private BeanA beanA;
    private String message = "Hello!";

    public BeanB() {
        System.out.println("BeanB created!");
    }

    public void setBeanA(BeanA beanA) {
        this.beanA = beanA;
    }

    public String getMessage() {
        return message;
    }
}
