package part3.study;


import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * author : haedoang
 * date : 2022/02/27
 * description :
 */
public class ReflectionTest {

    @Test
    public void StringReflectionTest() throws Exception {
        // given
        String str = "Spring";

        // then
        assertThat(str.length(), is(6));

        // when
        final Method lengthMethod = String.class.getMethod("length");

        // then
        assertThat((Integer) lengthMethod.invoke(str), is(6));

        // charAt
        assertThat(str.charAt(0), is('S'));

        Method charAtMethod = String.class.getMethod("charAt", int.class);
        assertThat((Character) charAtMethod.invoke(str, 0 ), is('S'));
    }

}
