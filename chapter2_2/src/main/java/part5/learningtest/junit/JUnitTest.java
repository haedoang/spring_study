package part5.learningtest.junit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;

/**
 * author : haedoang
 * date : 2022/02/13
 * description :
 */
@DisplayName("JUnit 테스트 단위별로 각각 독립적으로 객체를 생성한다는 것을 증명하는 테스트 코드")
@TestMethodOrder(MethodOrderer.MethodName.class)
public class JUnitTest {
    static Set<JUnitTest> testObjects = new HashSet<>();

    @Test
    public void test1() {
        assertThat(testObjects, not(hasItem(this)));
        testObjects.add(this);
    }

    @Test
    public void test2() {
        assertThat(testObjects, not(hasItem(this)));
        testObjects.add(this);
    }

    @Test
    public void test3() {
        assertThat(testObjects, not(hasItem(this)));
        testObjects.add(this);
    }
}
