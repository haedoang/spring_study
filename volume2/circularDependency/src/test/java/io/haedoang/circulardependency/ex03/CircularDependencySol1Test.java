package io.haedoang.circulardependency.ex03;

import io.haedoang.circulardependency.ex03.config.Ex03Config;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * fileName : CircularDependencySol1Test
 * author : haedoang
 * date : 2022-06-13
 * description : @Lazy 프록시 객체를 사용한 DI 지연 테스트
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = Ex03Config.class)
public class CircularDependencySol1Test {

    @Autowired
    private ApplicationContext context;

    @Test
    @DisplayName("@Lazy 순환 참조 해결 테스트")
    public void circularDependencyTest() {
        BeanA beanA = context.getBean("beanA", BeanA.class);
        BeanB beanB = context.getBean("beanB", BeanB.class);

        assertThat(beanA).isNotNull();
        assertThat(beanB).isNotNull();
    }
}
