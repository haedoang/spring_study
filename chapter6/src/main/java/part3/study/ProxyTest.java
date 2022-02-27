package part3.study;

import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * author : haedoang
 * date : 2022/02/27
 * description :
 */
public class ProxyTest {

    @Test(expected = UnsupportedOperationException.class)
    public void unmodifiableListTest() {
        // given
        final List<Object> objects = Collections.unmodifiableList(new ArrayList<>());

        // when
        objects.add("unmodifiableList는 접근제권한 제어용 프록시 오브젝트를 반환한다");
    }

    @Test
    public void simpleProxy() {
        // given
        Hello hello = new HelloTarget();

        // then
        assertThat(hello.sayHello("Toby") ,is("hello Toby"));
        assertThat(hello.sayHi("Toby") ,is("hi Toby"));
        assertThat(hello.sayThankYou("Toby") ,is("thank you Toby"));
    }

    @Test
    public void simpleProxy2(){
        // given
        Hello proxiedHello = new HelloUppercase(new HelloTarget());

        // then
        assertThat(proxiedHello.sayHello("Toby") ,is("HELLO TOBY"));
        assertThat(proxiedHello.sayHi("Toby") ,is("HI TOBY"));
        assertThat(proxiedHello.sayThankYou("Toby") ,is("THANK YOU TOBY"));
    }

    @Test
    public void dynamicProxy(){
        Hello proxiedHello = (Hello) Proxy.newProxyInstance(
                getClass().getClassLoader(), //다이나믹 프록시가 정의되는 클래스 로더
                new Class[] { Hello.class }, // 프록시가 구현할 인터페이스
                new UppercaseHandler(new HelloTarget()) // 부가 기능
        );

        // then
        assertThat(proxiedHello.sayHello("Toby") ,is("HELLO TOBY"));
        assertThat(proxiedHello.sayHi("Toby") ,is("HI TOBY"));
        assertThat(proxiedHello.sayThankYou("Toby") ,is("THANK YOU TOBY"));
    }

}
