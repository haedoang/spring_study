package part3.user.service;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import java.lang.reflect.Proxy;

/**
 * author : haedoang
 * date : 2022/02/27
 * description :
 */
public class TxProxyFactoryBean implements FactoryBean<Object> {
    Object target;
    PlatformTransactionManager transactionManager;
    String pattern;
    Class<?> serviceInterface;

    public void setTarget(Object target) {
        this.target = target;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public void setServiceInterface(Class<?> serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    @Override
    public Object getObject() throws Exception {
        //다이나믹 프록시 오브젝트 생성하기
        TransactionHandler txHandler = new TransactionHandler();
        txHandler.setTarget(target);
        txHandler.setTransactionManager(transactionManager);
        txHandler.setPattern(pattern);

        return Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[] { serviceInterface },
                txHandler
        );
    }

    @Override
    public Class<?> getObjectType() {
        return this.serviceInterface;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
