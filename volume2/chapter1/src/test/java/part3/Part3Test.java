package part3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * author : haedoang
 * date : 2022/08/21
 * description :
 */
public class Part3Test {

    @Test
    @DisplayName("싱글톤 빈 테스트")
    public void singletonScope() {
        // given
        ApplicationContext ac = new AnnotationConfigApplicationContext(
                SingletonBean.class, SingletonClientBean.class
        );

        // when DL에서 싱글톤 확인하기
        Set<SingletonBean> beans = new HashSet<>();
        beans.add(ac.getBean(SingletonBean.class));
        beans.add(ac.getBean(SingletonBean.class));

        // then
        assertThat(beans.size()).isEqualTo(1);

        // when DI로 싱글톤 확인하기
        beans.add(ac.getBean(SingletonClientBean.class).bean1);
        beans.add(ac.getBean(SingletonClientBean.class).bean2);

        // then
        assertThat(beans.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("프로토타입 빈 테스트")
    public void prototypeScope() {
        // given
        ApplicationContext ac = new AnnotationConfigApplicationContext(
                PrototypeBean.class, PrototypeClientBean.class
        );
        Set<PrototypeBean> bean = new HashSet<>();

        // when
        bean.add(ac.getBean(PrototypeBean.class));

        // then
        assertThat(bean.size()).isEqualTo(1);

        // when
        bean.add(ac.getBean(PrototypeBean.class));

        // then
        assertThat(bean.size()).isEqualTo(2);

        // when
        bean.add(ac.getBean(PrototypeClientBean.class).bean1);

        // then
        assertThat(bean.size()).isEqualTo(3);

        // when
        bean.add(ac.getBean(PrototypeClientBean.class).bean2);

        // then
        assertThat(bean.size()).isEqualTo(4);

    }

    static class SingletonBean {
    }

    static class SingletonClientBean {
        @Autowired
        SingletonBean bean1;
        @Autowired
        SingletonBean bean2;
    }

    @Scope("prototype")
    static class PrototypeBean {
    }

    static class PrototypeClientBean {
        @Autowired
        PrototypeBean bean1;
        @Autowired
        PrototypeBean bean2;
    }
}
