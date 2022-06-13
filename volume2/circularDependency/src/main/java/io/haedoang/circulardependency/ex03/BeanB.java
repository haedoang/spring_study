package io.haedoang.circulardependency.ex03;

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

    @Autowired
    public BeanB(BeanA beanA) {
        System.out.println("BeanB created!");
        this.beanA = beanA;
    }
}
