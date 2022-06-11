package part1;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * author : haedoang
 * date : 2022/06/11
 * description :
 */
@RunWith(SpringJUnit4ClassRunner.class) //junit5 => ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/test-applicationContext.xml")
public class SpringApplicationContextTest {
    @Autowired
    ApplicationContext context;

    @Test
    @DisplayName("applicationContext 가져오기")
    public void getApplicationContext() {
        final Hello hello = context.getBean("hello", Hello.class);

        // then
        hello.print();
        assertThat(context.getBean("printer").toString()).isEqualTo("Hello Spring")
                .as("ContextConfiguration은 application 컨텍스트 생성과 동시에 파일을 읽고 초기화까지 한다");
    }
}
