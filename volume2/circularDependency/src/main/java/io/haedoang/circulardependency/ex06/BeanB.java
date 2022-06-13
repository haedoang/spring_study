package io.haedoang.circulardependency.ex06;

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

    @Autowired
    public void setBeanA(BeanA beanA) {
        this.beanA = beanA;
    }

    public String getMessage() {
        return message;
    }
}
