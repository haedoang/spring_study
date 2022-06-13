package io.haedoang.circulardependency.ex01;

import org.springframework.beans.factory.annotation.Autowired;
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
    public BeanA(BeanB beanB) {
        System.out.println("beanA expected creation order : 3 ");
        this.beanB = beanB;
    }
}
