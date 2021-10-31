package learningtest.junit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.matchers.JUnitMatchers.either;
import static org.junit.matchers.JUnitMatchers.hasItem;

/**
 * packageName : learningtest.junit
 * fileName : JUnitTest
 * author : haedoang
 * date : 2021/10/31
 * description :
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/junit.xml")
public class JUnitTest {
// 독립적인 오브젝트를 생성하는 지에 대한 테스트1
//    static JUnitTest testObject;
//
//    @Test
//    public void test1() {
//        assertThat(this, is(not(sameInstance(testObject))));
//        testObject = this;
//    }
//
//    @Test
//    public void test2() {
//        assertThat(this, is(not(sameInstance(testObject))));
//        testObject = this;
//    }
//
//
//    @Test
//    public void test3() {
//        assertThat(this, is(not(sameInstance(testObject))));
//        testObject = this;
//    }

    @Autowired
    ApplicationContext context;

    static Set<JUnitTest> testObjects = new HashSet<>();
    static ApplicationContext contextObject = null;

    @Test
    public void test1() {
        assertThat(testObjects, not(hasItem(this)));
        testObjects.add(this);

        assertThat(contextObject == null || contextObject == this.context, is(true));
        contextObject = this.context;

    }

    @Test
    public void test2() {
        assertThat(testObjects, not(hasItem(this)));
        testObjects.add(this);

        assertTrue(contextObject == null || contextObject == this.context);
        contextObject = this.context;
    }


    @Test
    public void test3() {
        assertThat(testObjects, not(hasItem(this)));
        testObjects.add(this);

        assertThat(contextObject, either(is(nullValue())).or(is(this.context)));
        contextObject = this.context;
    }

    /**
     *  Junit Test 사용법 정리
     * */


}
