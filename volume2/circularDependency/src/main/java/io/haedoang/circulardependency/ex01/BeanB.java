package io.haedoang.circulardependency.ex01;

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
    private BeanC beanC;

    @Autowired
    public BeanB(BeanC beanC) {
        System.out.println("beanB expected creation order : 2 ");
        this.beanC = beanC;
    }
}
