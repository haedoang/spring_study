package part5.template;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * author : haedoang
 * date : 2022/02/19
 * description :
 */
public class CalcSumTest {
    Calculator calculator;
    String numFilePath;

    @Before
    public void setUp() throws Exception {
        this.calculator = new Calculator();
        this.numFilePath = getClass().getResource("/numbers.txt").getPath();
    }

    @Test
    public void sumOfNumbers() throws IOException {
        // then
        assertThat(calculator.calcSum(numFilePath), is(10));
    }

    @Test
    public void multiplyOfNumbers() throws IOException {
        // then
        assertThat(calculator.calcMultiply(numFilePath), is(24));
    }

    @Test
    public void concateString() throws IOException {
        // then
        assertThat(calculator.concatenate(numFilePath), is("1234"));
    }

}
