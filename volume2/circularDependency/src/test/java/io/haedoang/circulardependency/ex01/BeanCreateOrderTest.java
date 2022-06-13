package io.haedoang.circulardependency.ex01;

import io.haedoang.circulardependency.ex01.config.Ex01Config;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * fileName : BeanCreateTest
 * author : haedoang
 * date : 2022-06-13
 * description :
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = Ex01Config.class)
public class BeanCreateOrderTest {

    @Test
    @DisplayName("빈 생성 순서 테스트 : C -> B -> A")
    public void test() {
    }
}
