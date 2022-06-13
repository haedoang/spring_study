package io.haedoang.circulardependency.ex06;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * fileName : BeanA
 * author : haedoang
 * date : 2022-06-13
 * description :
 */
@Component
public class BeanA implements ApplicationContextAware, InitializingBean {
    private BeanB beanB;

    private ApplicationContext context;

    public BeanB getBeanB() {
        return beanB;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        beanB = context.getBean(BeanB.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
