package io.haedoang.circulardependency.ex03;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * fileName : BeanA
 * author : haedoang
 * date : 2022-06-13
 * description :
 */
@Component
public class BeanA {
    private BeanB beanB;

    @Autowired
    public BeanA(@Lazy BeanB beanB) {
        System.out.println("BeanA created!");
        this.beanB = beanB;
    }
}
