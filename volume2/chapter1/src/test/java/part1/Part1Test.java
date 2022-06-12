package part1;

import org.assertj.core.internal.bytebuddy.description.type.TypeList;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.context.support.StaticApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * author : haedoang
 * date : 2022/06/11
 * description :
 */
public class Part1Test {
    /**
     * StaticApplicationTest
     */
    @Test
    @DisplayName("IoC컨테이너 생성하기")
    public void createIoCContainer() {
        // when
        StaticApplicationContext context = new StaticApplicationContext();

        // then
        assertThat(context.getBeanDefinitionCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("Hello 클래스 빈 등록하기")
    public void registerBean() {
        // given
        final StaticApplicationContext context = new StaticApplicationContext();

        // when
        context.registerSingleton("hello1", Hello.class);
        final Hello hello1 = context.getBean("hello1", Hello.class);

        // then
        assertThat(hello1).isNotNull();
        assertThat(context.getBeanDefinitionCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("BeanDefinition 빈 등록하기")
    public void registerBeanByBeanDefinition() {
        // given
        final StaticApplicationContext context = new StaticApplicationContext();
        BeanDefinition helloDef = new RootBeanDefinition(); // 빈의 메타정보를 담은 오브젝트
        context.registerSingleton("hello1", Hello.class);
        final Hello hello1 = context.getBean("hello1", Hello.class);

        // when
        helloDef.getPropertyValues().addPropertyValue("name", "Spring");
        context.registerSingleton("hello2", Hello.class);
        final Hello hello2 = context.getBean("hello2", Hello.class);

        // then
        assertThat(hello1).isNotSameAs(hello2);
        assertThat(context.getBeanDefinitionCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("DI 정보 테스트")
    public void diTest() {
        // given
        final StaticApplicationContext ac = new StaticApplicationContext();
        ac.registerBeanDefinition("printer", new RootBeanDefinition(StringPrinter.class));

        // when
        BeanDefinition helloDef = new RootBeanDefinition(Hello.class);
        helloDef.getPropertyValues().addPropertyValue("name", "Spring");
        helloDef.getPropertyValues().addPropertyValue("printer", new RuntimeBeanReference("printer")); //id 가 printer 인 빈을 참조

        ac.registerBeanDefinition("hello", helloDef);
        final Hello hello = ac.getBean("hello", Hello.class);
        final StringPrinter printer = ac.getBean("printer", StringPrinter.class);

        // then
        hello.print();
        assertThat(ac.getBeanDefinitionCount()).isEqualTo(2);
        assertThat(printer.toString()).isEqualTo("Hello Spring");
        assertThat(hello.printer).isSameAs(printer);
    }

    /**
     * GenericApplicationTest
     */
    @Test
    @DisplayName("GenericApplicaiton xml 빈 설정 테스트")
    public void genericApplicationContextXmlTest() {
        // given
        GenericApplicationContext ac = new GenericApplicationContext();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(ac);
        reader.loadBeanDefinitions("part1/genericApplicationContext.xml");
        ac.refresh(); // 메타 정보 등록이 완료됐음을 알림. 애플리케이션 컨텍스트 초기화하라는 명령이다

        // when
        Hello hello = ac.getBean("hello", Hello.class);

        // then
        hello.print();
        assertThat(ac.getBeanDefinitionCount()).isEqualTo(2);
        assertThat(ac.getBean("printer").toString()).isEqualTo("Hello Spring");
    }

    //FIXME 안되네;; 왜그럴까?
    @Disabled
    @Test
    @DisplayName("GenericApplicaiton property 빈 설정 테스트")
    public void genericApplicationContextPropTest() {
        // given
        GenericApplicationContext ac = new GenericApplicationContext();
        PropertiesBeanDefinitionReader reader = new PropertiesBeanDefinitionReader(ac);
        reader.loadBeanDefinitions("part1/genericApplicationContext.properties");

        ac.refresh();

        // when
        Hello hello = ac.getBean("hello", Hello.class);

        // then
        hello.print();
        assertThat(ac.getBeanDefinitionCount()).isEqualTo(2);
        assertThat(ac.getBean("printer").toString()).isEqualTo("Hello Spring");
    }

    @Test
    @DisplayName("GenericXmlApplicationContext")
    public void genericXmlApplicationContextText() {
        // given
        GenericXmlApplicationContext ac = new GenericXmlApplicationContext("part1/genericApplicationContext.xml");

        // when
        Hello hello = ac.getBean("hello", Hello.class);

        // then
        hello.print();
        assertThat(ac.getBeanDefinitionCount()).isEqualTo(2);
        assertThat(ac.getBean("printer").toString()).isEqualTo("Hello Spring");
    }

    @Test
    @DisplayName("계층 구조의 빈 컨테이너 테스트")
    public void parentContextTest() {
        // given
        ApplicationContext parent = new GenericXmlApplicationContext("parentContext.xml");
        GenericApplicationContext child = new GenericApplicationContext(parent);
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(child);
        reader.loadBeanDefinitions("childContext.xml");

        // when
        final Printer printer = child.getBean("printer", Printer.class);

        // then
        assertThat(printer).isNotNull();

        // when
        final Hello hello = child.getBean("hello", Hello.class);

        // then
        hello.print();
        assertThat(printer.toString()).isEqualTo("Hello Child");
    }
}
