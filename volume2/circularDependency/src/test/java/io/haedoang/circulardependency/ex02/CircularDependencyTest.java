package io.haedoang.circulardependency.ex02;

import io.haedoang.circulardependency.ex02.config.Ex02Config;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * fileName : CircularDepencyTest
 * author : haedoang
 * date : 2022-06-13
 * description :
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = Ex02Config.class)
public class CircularDependencyTest {

    @Test
    @DisplayName("순환참조 시 BeanCurrentlyInCreationException 예외 발생 테스트")
    public void circularDependencyTest() {
        // throw BeanCurrentlyInCreationException
    }
}
