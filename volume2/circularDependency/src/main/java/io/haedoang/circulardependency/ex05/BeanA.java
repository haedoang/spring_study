package io.haedoang.circulardependency.ex05;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * fileName : BeanA
 * author : haedoang
 * date : 2022-06-13
 * description :
 */
@Component
public class BeanA implements InitializingBean {
    @Autowired
    private BeanB beanB;

    public BeanA() {
        System.out.println("BeanA created!");
    }

    @PostConstruct
    public void init() {
        System.out.println("BeanA postConstruct!!");
        beanB.setBeanA(this);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("afetPropertiesSet invoke!");
    }

    public BeanB getBeanB() {
        return beanB;
    }


}
