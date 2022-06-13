package io.haedoang.circulardependency.ex04;

import io.haedoang.circulardependency.ex04.BeanA;
import io.haedoang.circulardependency.ex04.BeanB;
import io.haedoang.circulardependency.ex04.config.Ex04Config;
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
 * description : Setter Injection 을 이용한 순환참조해결
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = Ex04Config.class)
public class CircularDependencySol2Test {
    @Autowired
    private BeanA beanA;
    @Autowired
    private BeanB beanB;

    @Test
    @DisplayName("Setter Inject 순환참조 해결 테스트")
    public void circularDependencyTest() {
        assertThat(beanA).isNotNull();
        assertThat(beanB).isNotNull();

        assertThat(beanA.getBeanB().getMessage()).isEqualTo("Hello!");
    }
}
