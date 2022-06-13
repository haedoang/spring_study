package io.haedoang.circulardependency.ex06;


import io.haedoang.circulardependency.ex06.config.Ex06Config;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * fileName : CircularDependencySol1Test
 * author : haedoang
 * date : 2022-06-13
 * description : @PostConstruct 을 이용한 순환참조해결
 *
 *
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = Ex06Config.class)
public class CircularDependencySol4Test {
    @Autowired
    private BeanA beanA;
    @Autowired
    private BeanB beanB;

    @Test
    @DisplayName("ApplicationContextAware, InitializingBean 순환참조 해결 테스트")
    public void circularDependencyTest() {
        assertThat(beanA).isNotNull();
        assertThat(beanB).isNotNull();

        assertThat(beanA.getBeanB().getMessage()).isEqualTo("Hello!");
    }
}
