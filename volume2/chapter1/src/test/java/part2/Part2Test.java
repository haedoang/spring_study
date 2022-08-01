package part2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import part2.bean.AnnotatedHello;
import part2.bean.AnnotatedHi;
import part2.bean.Hello;
import part2.config.AnnotatedHelloConfig;
import part2.config.HelloConfig;
import part2.config.ServiceConfig;
import part2.service.HelloService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatObject;

/**
 * author : haedoang
 * date : 2022/06/19
 * description :
 */
public class Part2Test {

    @Test
    @DisplayName("애너테이션 빈 스캐닝 테스트")
    public void annotationConfigApplicationContextTest() {
        // given
        final AnnotationConfigApplicationContext beanFactory = new AnnotationConfigApplicationContext("part2.bean");

        // when
        final AnnotatedHello hello = beanFactory.getBean("annotatedHello", AnnotatedHello.class);

        // then
        assertThat(hello).isNotNull();
    }

    @Test
    @DisplayName("애너테이션 빈 스캐닝 - 빈 ID 변경 테스트")
    public void annotationConfigApplicationContextTest2() {
        // given
        final AnnotationConfigApplicationContext beanFactory = new AnnotationConfigApplicationContext("part2.bean");

        // when
        final AnnotatedHi hi = beanFactory.getBean("myAnnotatedHi", AnnotatedHi.class);

        // then
        assertThat(hi).isNotNull();
    }

    @Test
    @DisplayName("빈 정의 클래스 테스트")
    public void beanDefinitionClassTest() {
        // given
        final AnnotationConfigApplicationContext beanFactory = new AnnotationConfigApplicationContext(AnnotatedHelloConfig.class);

        // when
        final Hello hello = beanFactory.getBean("hello", Hello.class);

        // then
        assertThat(hello).isNotNull();
        assertThat(beanFactory.getBeanDefinitionNames()).contains("hola", "annotatedHolaConfig")
                .as("빈 정의 클래스도 빈으로 등록된다");
        assertThat(beanFactory.getBean("annotatedHolaConfig", AnnotatedHelloConfig.class).hello()).isSameAs(hello);
    }

    @Test
    @DisplayName("싱글톤 빈 테스트")
    public void defaultSingletonBeanTest() {
        // given
        final AnnotationConfigApplicationContext beanFactory = new AnnotationConfigApplicationContext(HelloConfig.class);

        // when
        final Hello hello1 = beanFactory.getBean("hello1", Hello.class);
        final Hello hello2 = beanFactory.getBean("hello2", Hello.class);

        // then
        assertThat(hello1).isNotSameAs(hello2);
        assertThat(hello1.getPrinter()).isSameAs(hello2.getPrinter())
                .as("동일한 printer 빈을 참조한다");
    }

    @Test
    @DisplayName("SimpleDriverDataSource class configuration 테스트")
    public void simpleDriverDataSourceConfigTest() {
        // given
        final AnnotationConfigApplicationContext beanFactory = new AnnotationConfigApplicationContext(ServiceConfig.class);

        // when
        final SimpleDriverDataSource dataSource = beanFactory.getBean("dataSource", SimpleDriverDataSource.class);
        // then
        assertThat(dataSource).isNotNull();
    }

    @Test
    @DisplayName("일반 빈에서 @Bean 클래스는 싱글톤으로 동작하지 않는다")
    public void normalBeanClassConfigTest() {
        // given
        final AnnotationConfigApplicationContext beanFactory = new AnnotationConfigApplicationContext(ServiceConfig.class);

        // when
        final HelloService helloService = beanFactory.getBean("helloService", HelloService.class);

        // then
        final Hello hello1 = helloService.hello1();
        final Hello hello2 = helloService.hello2();

        assertThatObject(hello1.getPrinter()).isNotSameAs(hello2.getPrinter())
                .as("Printer 객체 호출 시 마다 새 인스턴스륿 반환한다");
    }
}
