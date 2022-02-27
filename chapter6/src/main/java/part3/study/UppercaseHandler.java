package part3.study;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * author : haedoang
 * date : 2022/02/27
 * description :
 */
public class UppercaseHandler implements InvocationHandler {
    Object target;

    public UppercaseHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        final Object ret  = method.invoke(target, args);
        if (ret instanceof String && method.getName().startsWith("say")) {
            return ((String)ret).toUpperCase();
        }
        else {
            return ret;
        }
    }
}
