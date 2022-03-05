package part5.study.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * author : haedoang
 * date : 2022/03/05
 * description :
 */
public class UppercaseHandler implements InvocationHandler {
    public UppercaseHandler(Object target) {
        this.target = target;
    }

    private final Object target;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        final Object ret = method.invoke(target, args);
        if (ret instanceof String && method.getName().startsWith("say")) {
            return ((String) ret).toUpperCase();
        }
        return ret;
    }
}
