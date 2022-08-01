# 1.2 IOC/DI를 위한 빈 설정 메타정보 작성
- IoC컨테이너의 기본적인 역할은 코드를 대신해서 애플리케이션을 구성하는 오브젝트를 생성하고 관리하는 것
  - POJO로 만들어진 애플리케이션 클래스, 서비스 오브젝트 
- `빈 설정 메타정보`를 통해 컨테이너는 자신이 만들 오브젝트를 식별한다
- 리소스로부터 전용 리더를 통해 읽힌 후 `BeanDefinition` 타입의 오브젝트로 변환된다
  ```text
      XML 문서   ------->
      애노테이션   ------->   BeanDefinition    ------>    IoC 컨테이너    
      자바 코드   ------->        메타정보                  (애플리케이션)
  ```

## 1.2.1 빈 설정 메타정보
- BeanDefinition 
  - IoC 컨테이너가 빈을 만들 때 필요한 핵심 정보가 담겨 있음
  - 여러 개의 빈을 만드는 데 재사용될 수 있음
  - 빈의 이름이나 아이디를 나타내는 정보는 포함되지 않음 
    - 대신 IoC 컨테이너에 BeanDefinition 정보가 등록될 때 이름을 부여할 수 있음 


- 빈 메타정보 항목 
  - beanClassName: 빈 오브젝트 클래스 이름. 필수항목
  - parentName: 빈 메타정보를 상속받을 부모 BeanDefinition 이름 
  - factoryBeanName: 팩토리 역할을 하는 빈 오브젝트를 생성하는 경우 팩토리 빈의 이름
  - scope: 빈 생명 주기. 기본값: singleton
  - lazyInit: 빈 생성을 사용 시점까지 지연하는 역할. 기본값: false
  - dependsOn: 먼저 만들어져야 하는 빈 지정 
  - autowiredCandidate: 명시적 설정 없이 미리 정해진 규칙을 가지고 자동와이어링 대상에 포함시킬 지 여부. 기본값: true
  - primary: 자동와이어링 작업 중 DI 후보 중 우선권을 가질지 여부. 기본값: false
  - abstract: 메타정보 상속에만 사용할 추상 빈으로 만들지 여부. 기본값: false
  - autowireMode: 오토와이어링 전략
  - dependencyCheck: 프로퍼티 값 또는 레퍼런스가 모두 설정되어 있는지 검증하는 작업의 종류 
  - initMethod: 빈 생성되고 DI를 마친 뒤에 실행할 초기화 메서드의 이름
  - destroyMethod: 빈 생명 주기가 다 돼서 제거하기 전 호출할 메서드 
  - propertyValues: 프로퍼티의 이름과 설정 값 또는 레퍼런스. 수정자 메소드를 통한 DI 작업에 사용
  - constructorArgumentValues: 생성자의 이름과 설정 값 또는 레퍼런스. 생성자를 통한 DI 작업에 사용
  - annotationMetadata: 빈 클래스에 담긴 애너테이션과 그 애트리뷰트 값. 애너테이션을 이용하는 설정에서 활용


- 빈 설정 메타정보에서 가장 중요한 것은 `클래스 이름`이다

## 1.2.2 빈 등록 방법
- 빈 메타정보를 작성하여 컨테이너에게 건내주면 된다
- `XML문서`, `프로퍼티 파일`, `소스코드 애너테이션`과 같은 외부 리소스를 사용하여 메타 정보 작성을 권장한다
- 작성한 메타정보를 적절한 리더나 변환기를 통해 애플리케이션 컨텍스트가 사용할 수 있는 정보로 변환하는 방법을 사용한다

### 자주 사용되는 빈 등록 방법 5가지
#### 1. XML: <Bean>태그 
  ```xml
    <bean id="hello" class="io.haedoang.spring.ioc.bean.Hello"/>
  ```
  - 가장 단순하면서 가장 강력한 설정 방법
  - 스프링 빈 메타정보의 거의 모든 항목을 지정할 수 있다. 세밀한 제어 가능
#### 2. XML: 네임스페이스와 전용태그
  ```xml
    <!-- namespace 적용 이전의 빈은 개발자가 작성한 빈과 구별이 어려운 단점이 있다 -->
    <bean id="mypointcut" class="org.springframework.aop.aspectj.AspectJExpressionPointcut">
        <property name="expression" value="execution(* *..*ServiceImpl.upgrade*(..))"/>
    </bean>

    <!-- namespace 적용 -->
    <aop:pointcut id="mypointcut" expression="execution(* *..*ServiceImpl.upgrade*(..))" />
  ```
- 네임태그를 통해 직접 작성한 코드와 컨텍스트 설정 정보를 담은 빈을 구분할 수 있다
- 전용 태그 하나로 여러 개의 빈을 만들 수 있다
  - `<context:annotation-config/>`
  
#### 3. 스테레오타입 에너태이션과 빈 스캐너
  ```java
  import org.springframework.stereotype.Component;
  
  @Component("myHello") // 빈의 이름을 별도로 지정할 수 있다
  public class Hello {
  }
  ```
- 빈으로 사용될 클래스에 애너테이션을 부여하여 자동으로 빈으로 등록하는 방식 => `빈 스캐닝`
- XML 파일 처럼 명시적으로 선언하지 않고 빈 등록이 가능하다
- 빈 스캐닝 역할을 담당하는 오브젝트를 `빈 스캐너` 라고한다
- 빈의 아이디는 클래스의 첫글자를 소문자로 바꾼 것을 사용한다
- `AnnotationConfigApplicationContext`는 빈 스캐너를 내장하고 있는 애플리케이션 컨텍스트 구현 클래스이다
  - 생성자에 빈 스캔 대상 패키지를 지정해주면 스테레오타입 애너테이션이 붙은 클래스를 빈으로 등록해준다
- XML 문서 생성과 관리에 따른 수고를 덜어주고 개발 속도를 향상시킬 수 있는 장점이 있다
- 애플리케이션에 등록될 빈이 어떤것인지 한눈에 파악하기 어렵다 
- 클래스 당 한 개 이상의 빈을 등록할 수 없다
- 자동인식을 통ㅎ란 빈 등록 사용 방법 두가지 
  1. XML을 이용하여 빈 스캐너 등록
  ```xml
    <context:component-scan base-package="io.haedoang.ioc.bean"/>
  ```
  2. 빈 스캐너를 내장한 애플리케이션 컨텍스트 사용
  ```xml
    <context-param>
        <param-name>contextClass</param-name>
        <param-value>
            org.springframework.web.context.support.AnnotationConfigWebApplicationContext
        </param-value>
    </context-param>
  
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>springbook.learningtest.spring.ioc.bean</param-value>
    </context-param>
  ```
  - 루트 컨텍스트가 XML 대신 빈 스캐너를 이용해 빈을 등록하는 방법
- 스테레오 애너테이션 종류 
  - `@Repository`
  - `@Service`
  - `@Controller`
  - `@Component`: 특정 계층으로 분류하기 힘든 경우

#### 4. 자바 코드에 의한 빈 등록: @Configuration 클래스의 @Bean 메서드
  ```java
  import org.springframework.context.annotation.Bean;
  import org.springframework.context.annotation.Configuration;
  
  @Configuration
  public class AnnotationHelloConfig {
    @Bean
    public AnnotatedHello annotatedHello() {
    }
  }
  ```
- `@Configuration`: 빈 설정 메타정보를 담고 있는 자바 코드임을 명시하는 애너테이션. 해당 클래스도 빈으로 등록된다
- `@Bean`: 빈을 정의하는 애너테이션 
- `@Configuration`, `@Bean` 애너테이션이 붙으면 스프링 컨테이너가 인식할 수 있는 빈 메타정보 겸 빈 오브젝트 팩터리가 된다
- `@Bean` 으로 정의한 빈은 기본적으로 싱글톤 오브젝트로 동작한다
- 자바 코드를 이용한 설정이 XML보다 유용한 점 
  - 컴파일러나 IDE를 통해 타입 검증이 가능하다
  - 자동완성과 같은 IDE 지원 기능을 이용할 수 있다
  - 이해하기 쉽다
  - 복잡한 빈 설정이나 초기화 작업을 손쉽게 할 수 있다

#### 5. 자바 코드에 의한 빈 등록: 빈 클래스의 @Bean 메서드  => 권장하지 않음 
  ```java
  import org.springframework.context.annotation.Bean;
  import part2.bean.Hello;
  import part2.bean.StringPrinter;
  
  //잘못된 예 
  public class HelloService {
    @Bean
    public Hello hello() {
      Hello hello = new Hello();
      hello.setPrinter(printer());
      return hello;
    }
  
    @Bean
    public Hello hello2() {
      Hello hello = new Hello();
      hello.setPrinter(printer());
      return hello;
    }
  
    @Bean
    public Printer printer() {
      return new StringPrinter();
    }
  
  }
  ```
- POJO에서 `@Bean`은 다르게 동작한다. 즉, 싱글톤을 보장하지 않는다 
```java
  import org.springframework.context.annotation.Bean;
  import part2.bean.Hello;
  import part2.bean.StringPrinter;
  
  //개선된 예 
  public class HelloService {
    private Printer printer;
    
    public void setPrinter(Printer printer) {
        this.printer = printer;
    }
      
    @Bean
    public Hello hello() {
      Hello hello = new Hello();
      hello.setPrinter(this.printer);
      return hello;
    }
  
    @Bean
    public Hello hello2() {
      Hello hello = new Hello();
      hello.setPrinter(this.printer);
      return hello;
    }
  
    @Bean
    public Printer printer() {
      return new StringPrinter();
    }
  }
  ```
  - POJO 클래스의 경우 `@Bean` DI 시 주의해야 한다
  - POJO 클래스가 직접 DI를 받은 뒤 DI를 하는 경우 싱글톤을 보장할 수 있다
  - POJO 클래스에서 `@Bean`사용하는 이유
    - `@Bean`메서드를 통해 생성되는 빈이 매우 밀접한 관계가 있는 경우나 특별히 종속적인 경우 사용한다
    - XML 설정종보 등을 통해 외부로 빈이 노출되지 않고 빈의 존재를 감출 수 있다.
    - 그러나 설정 정보를 일반 애플리케이션 코드와 함께 존재하기 때문에 유연성이 떨어진다 
    
### 빈 등록 메타정보 구성 전략
- XML 단독 사용 
  - 모든 빈을 명시적으로 XML에 등록
  - 빈의 개수가 많아지면 관리해야할 XML 파일이 많아진다
  - DI에 필요한 수정자 또는 생성자가 필요하다
  - 모든 설정 정보를 자바 코드에서 분리하고 싶은 경우 사용된다
- XML과 빈 스캐닝 혼용
  - 복잡한 빈 메타 정보를 XML로 작성하고, 핵심 로직을 담고 있는 빈 클래스를 빈 스캐닝 방식으로 등록
  - 스캔 대상이 될 클래스를 위치시킬 패키지를 미리 결정해야 한다 
  - 중복 빈 스캐닝이 발생할 우려가 있다
    - 버그를 찾기 어렵다 
- XML없이 빈 스캔이 단독
  - 스프링 3.0 이상부터 가능
  - 루트 컨텍스트와 서블릿 컨텍스트 모두 contextClass 파라미터를 추가해 AnnotationConfigWebApplicationContext로 컨텍스트 클래스를 변경해야 한다
    - contextLocations 파라미터로 스캔 대상 패키지를 넣어주어야 한다
  - 스프링이 제공하는 스키마에 정의된 전용 태그를 사용할 수 없다
    - aop, tx 등 10 여개의 스키마와 그 안에 정의된 전용 태그를 쓸 수 없는 것이 큰 단점
    - XML 전용태그에 대응하는 `@Configuration` 설정 방식은 스프링3.1부터 제공한다