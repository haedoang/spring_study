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

## 1.2.3 빈 의존관계 설정 방법
--- 
- 빈 의존관계 주입 8가지 방법 
    - DI 할 대상을 선정하는 방법으로 분류
        1. 명시적으로 구체적인 빈을 지정하는 방법 => DI 할 빈의 아이디를 직접 지정 
        2. 일정한 규칙에 따라 자동으로 선정하는 방법 => `자동와이어링`
    - 메타 정보 작성 방법으로 분류 
        3. XML <bean> 태그
        4. 스키마를 가진 전용 태그
        5. 애노테이션
        6. 자바 코드에 의한 직접적인 DI
    - 3 ~ 6번 작성 방법을 세분화
        7. 명시적으로 빈을 지지정하는 방식
        8. 자동 선정 방식
 
### XML: < property>, <constructor-arg>
- DI 방식 2가지 => 아래 두 가지 방식 모두 파라미터로 의존 오브젝트 또는 값을 주입받는다 
    - 수정자 메서드 사용 방식 => <property>
    - 클래스의 생성자 이용 방식 => <constructor-arg>
        - 파라미터의 순서에 주의하여야 한다

### XML: 자동와이어링
- 자동와이어링 방식 2가지
    - byName: 빈 이름 자동와이어링
        - 빈의 모든 프로퍼티에 대해 이름이 동일한 빈을 찾아서 자동으로 등록하는 방식
        - 같은 이름이 없는 경우 무시된다
        - ex) `<bean id="hello" class="..." autowired="byName"/>`
    - byType: 타입에 의한 자동와이어링
        - 빈의 모든 프로퍼티에 대해 타입이 동일한 빈을 찾아서 자동으로 등록하는 방식
        - 타입이 같은 빈이 두 개 이상 존재하는 경우 적용할 수 없다
        - ex) `<bean id="hello class-"..." autowired="byType"/>`
- XML만 봐서는 빈 사이의 의존관계를 알기 어렵다
- 코드만 보고 어떤 프로퍼티에 DI가 일어날 지 알 수 없다

### XML: 네임스페이스와 전용 태그
- 스프링 지원 네임스페이스 사용 방식 
    - ex) `<aop:config> ... </aop-config>`
- 전용 태그 사용 
    - ex) `<oxm:jaxb2-marshaller id="unmarshaller" contextPath="..."/>`

### 애너테이션:@Resource
- 애너테이션 의존 관계 정보를 이용해 DI하는 방법 3가지
    - XML의 `<context:annotation-config/>`
    - XML의 `<context:component-scan/>`
    - `AnnotationConfigApplicationContext` or `AnnotationConfigWebApplicationContext`
- 주입할 빈을 아이디로 지정하는 방법으로 클래스의 `수정자`, `필드`에도 붙일 수 있다
-XML 자동와이어링과 달리 @Resource는 참조할 빈이 반드시 존재해야 한다

### 애너테이션:@Autowired/@Inject
- 기본적으로 `타입`에 의한 자동와이어링 방식이다
- @Autowired는 XML의 타입에의한 자동와이어링 방식을 아래 네 가지로 확장함
    - <b>수정자 메서드</b>와 <b>필드</b>
        - @Resource 사용 방식과 비슷하다
    - <b>생성자</b>
        - 생성자의 모든 파라미터에 타입에 의한 자동와이어링이 적용된다
        ```java
        public class BasSqlService implements SqlService {
            protected SqlReader sqlReader;
            protected SqlRegistry sqlRegistry;
      
            @Autowired //단 한 개의 생성자만 사용 가능
            public BasSqlService(SqlReader sqlReader, SqlRegistry sqlRegistry) {
                this.sqlReader = sqlReader;
                this.sqlRegistry = sqlRegistry;
            }
        }
        ```
    - <b>일반 메서드</b>
        - 생성자 주입과 수정자 주입의 단점을 보완하기 위해 사용
        - 오브젝트 생성 후에 호출되기 때문에 여러 개를 만들 수 있다
        ```java
        public class BasSqlService implements SqlService {
            protected SqlReader sqlReader;
            protected SqlRegistry sqlRegistry;
            
            @Autowired
            public void config(SqlReader sqlReader, SqlRegistry sqlRegistry) {
                this.sqlReader = sqlReader;
                this.sqlRegistry = sqlRegistry;
            }
        }
        ```
- 동일한 타입을 가진 빈이 여러 개인 경우 @Autowired 사용하는 방법
    - 컬렉션과 배열 타입으로 받는다 
    - `@Qualifier` 애너테이션을 사용한다 
        - ex) @Autowired @Qualifier("mainDB") DataSource dataSource;

### 자바 코드에 의한 의존관계 설정
1. 애노테이션에 의한 설정 @Autowired, @Resource
    - 빈은 자바 코드에 의해 생성하고, 의존 관계는 빈 클래스의 애노테이션을 이용
2. @Bean 메서드 호출
    - 메소드로 정의된 다른 빈의 메소드 호출을 참조하는 방식
    - 실제 자바 코드와 동작 방식이 다르기 때문에 오해할 소지가 있음 => 비 권장 방식
    - @Configuration 이 붙지 않은 클래스의 @Bean 메서드에 이 방식을 사용해서는 안 된다
        => @Configuration이 붙지않은 @Bean은 단일 인스턴스를 보장하지 않음
3. @Bean 메서드와 자동와이어링 
    - 다른 빈의 레퍼런스를 파라미터로 받기 때문에 코드가 자연스럽다
    - 한 개 이상의 파라미터를 받으며, 다른 클래스에 선언된 빈도 참조 가능하다

### 빈 의존관계 설정 전략
- XML 단독
- XML과 애노테이션 설정의 혼합
- 애노테이션 단독


