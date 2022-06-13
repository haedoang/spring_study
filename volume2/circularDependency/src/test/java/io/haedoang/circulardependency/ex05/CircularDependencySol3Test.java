package io.haedoang.circulardependency.ex05;

import io.haedoang.circulardependency.ex05.config.Ex05Config;
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
 *  BeanA created  -> BeanA postConstruct 실행 중 DI 필요한 BeanB created -> BeanA PostConstruct
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = Ex05Config.class)
public class CircularDependencySol3Test {
    @Autowired
    private BeanA beanA;
    @Autowired
    private BeanB beanB;

    @Test
    @DisplayName("PostConstruct 순환참조 해결 테스트")
    public void circularDependencyTest() {
        assertThat(beanA).isNotNull();
        assertThat(beanB).isNotNull();

        assertThat(beanA.getBeanB().getMessage()).isEqualTo("Hello!");
    }
}
