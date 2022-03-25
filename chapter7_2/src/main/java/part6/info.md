## 스프링 3.1의 DI

### 자바 언어의 변화와 스프링
- 어노테이션의 메타 정보 활용
  - `리플렉션 API` 를 사용하여 자바 코드의 메타정보를 데이터로 많이 활용되었다 ex) 어노테이션


- 어노테이션의 기능
  - 어노테이션은 자바 코드가 실행되는데 직접 참여하지 못한다.
  - 어노테이션은 인터페이스처럼 오브젝트 타입에 부여하는 것도 아니고, 상속 오버라이딩이 가능하지도 않다
  - 클래스의 타입에 영향을 주지도 못하고 일반 코드에서 활용되지 못한다


- 그런데 왜 ? 어노테이션 사용 이유??
  - `핵심 로직을 담은 자바 코드`, `IoC 방식의 프레임워크`, `프레임워크가 참조하는 메타정보`로 구성하는 방식에 잘 어울리기 때문
  ```text
    <핵심 로직 코드>          <IoC 프레임워크>         <메타정보>      
      UserDao               UserDaoTest         DaoFactory <- 어노테이션이 대체
  ConnectionMaker
    Dconnection
  ```


- 메타 정보 구현의 변화
  1. 자바 코드로 작성. `팩토리 메서드 패턴`
  2. XML을 통해 간결한 형태로 전환
  3. 어노테이션


- 어노테이션의 장단점
  - +사용 범위: `타입`, `필드`, `메소드`, `파라미터`, `생성자`, `로컬변수`...
  - +타입에 선언된 것만으로 클래스의 패키지, 이름, 접근 제한자, 상속 클래스 등의 메타정보를 가져올 수 있다
  - -XML은 변경 시 재빌드가 필요 없지만, 어노테이션은 새로 컴파일을 해주어야 한다


### 정책과 관례를 이용한 프로그래밍
- 어노테이션 같은 메타정보를 활용하는 프로그래밍 방식은 미리 약속한 규칙 또는 관례에 따라 동작하게 만들어졌다
  - ex) `@Transactional`의 적용 우선순위
- 정책을 잘못 알고 있는 경우 의도한대로 동작하지 않을 수 있다


## 자바 코드를 이용한 빈 설정 

### 테스트 컨텍스트의 변경
- `@Configuration`: DI 정보로 사용할 자바 클래스에 사용한다
- `@Bean`: DI 정보에서 생성할 빈을 정의할 때 사용한다
- `@ImportResource` : 자바 클래스로 만들어진 DI 설정정보에서 XML의 설정정보를 가져올 수 있다

#### <context:annotation-config /> 제거 
- 빈의 후처리기를 담당하던 태그이다 ex) `@PostConstruct`
- `@Configuration` 설정 클래스를 사용하는 컨테이너가 사용할 경우 더이상 필요하지 않다(vol2 1.5장에서 설명할 예정)

#### &lt;bean&gt;의 전환 
- 빈 설정: 빈과 빈과의 DI는 빈 호출로 처리한다
  ```java
  @Bean
  public PlatformTransactionManager transactionManager() {
      DataSourceTransactionManager tm = new DataSourceTransactionManager();
      tm.setDataSource(dataSource());
  
      return tm;
  }  
  ```
  
#### 전용 태그 전환
- <b>1. 내장형 DB 전용 태그</b>
  ```xml
    <jdbc:embedded-database id="embeddedDatabase" type="HSQL">
        <jdbc:script location="classpath:sqlRegistrySchema.sql"/>
    </jdbc:embedded-database>
  ```
  ```java
  @Bean
  public DataSource embeddedDatabase() {
      return new EmbeddedDatabaseBuilder()
              .setName("embeddedDatabase")
              .setType(HSQL)
              .addScript("classpath:sqlRegistrySchema.sql")
              .build();
  }
  ```
- <b>2. &lt;tx:annotation-driven/&gt;</b>
  - annotation-driven default 생성 빈
    - `o.s.aop.framework.autoproy.InfrastructureAdvisorAutoProxyCreator`
    - `o.s.transaction.annotation.AnnotationTransactionAttributeSource`
    - `o.s.transaction.interceptor.TransactionInterceptor`
    - `o.s.transaction.interceptor.BeanFactoryTransactionAttributeSourceAdvisor`
  
  
  - `@EnableTransactionManagement` 어노테이션이 대체 가능하다



## 빈 스캐닝과 자동와이어링

### @Autowired를 이용한 자동 와이어링
- `@Autowired` 어노테이션은 조건에 맞는 빈을 찾아 자동으로 수정자 메소드나 필드에 넣어준다
- 같은 타입의 빈을 먼저 찾는다. 빈이 여러 개일 경우 프로퍼티와 동일한 이름의 빈을 찾고 존재하지 않는 경우 에러를 발생한다 
- `@Autowired`는 리플렉션API를 사용하여 빈을 주입하기 때문에 private 필드를 사용하는 경우에도 무방하다
- 빈 설정정보를 보고 다른 빈과 의존관계를 어떻게 맺고 있는지 한눈에 파악하기 힘든 단점이 있다


### @Component를 이용한 자동 빈 등록 
- `@Component`는 클래스에 부여된다 
- `@Component`를 메타 애노테이션으로 갖고 있는 애노테이션이 붙은 클래스가 자동 빈 등록 대상이 된다
- `@ComponentScan` 어노테이션은 `@Component` 메타 애노테이션의 스캔 범위를 지정한다 
  - @ComponentScan(basePackages="par6.user")
- `@Component`는 클래스의 첫 글자를 소문자로 바꾸어 빈의 아이디로 등록한다
  - 수동으로 변경방법 : @Component("MyUserService")
- 어노테이션은 상속할 수 없고, 인터페이스를 구현할 수 없다
  - 메타 어노테이션을 사용하여 확장한다
  ```java
    @Component
    public @interface AppService {}
  ```
- `@Repository`: `@Component` 메타 애노테이션. DAO 기능을 제공하는 클래스에 부여하는 것을 권장한다
- `@Service`: `@Component` 메타 애노테이션. 비즈니스 로직을 담고 있는 서비스 계층의 빈에 사용한다. `@Transactional` 함께 사용되는 경우가 많다


## 컨텍스트 분리와 @Import

### 테스트용 컨텍스트 분리 
- 별도의 DI 정보 클래스를 생성한다. 
  ```java
  @ContextConfig(classes={AppContext.class, TestAppContext.class})
  ```

### @Import
- DI 정보 클래스에서 다른 DI 정보 클래스를 로드한다
  ```java
  @Import(SqlServiceContext.class)
  ```

### 프로파일
- 환경이 각기 다른 빈 정의가 필요한 경우 사용한다

### @Profile, @ActiveProfile
- `@Profile`: 특정 환경에서 사용할 빈 설정을 정의한다 
- `@ActivePrifle`: 프로파일을 활성할때 사용한다 
- 활성 프로파일 목록에 자신의 프로파일 이름이 들어 있지 않으면 무시된다

### 컨테이너의 빈 등록 정보 확인
- `o.s.beans.factory.support.DefaultListableBeanFactory` 

### 중첩 클래스를 이용한 프로파일 적용
- 환경이 늘어날수록 클래스가 늘어나는 것은 효율적으로 관리하는 방법이 되지 못한다
- 빈 설정 정보를 하나로 모으고 중첩 클래스를 통해 구현하여 관리 포인트를 줄일 수 있다
- 중첩 클래스의 빈 설정 클래스는 `@Import` 없이 로드된다 



## 프로퍼티 소스

### @PropertySource 
- 스프링은 key,value 쌍의 properties 파일 형식을 빈 설정 작업에 사용할 수 있게 해준다
- `@PropertySource` 애노테이션으로 로드할 properties 파일의 경로를 지정할 수 있다
- `@PropertySource` 로 인해 로드된 설정 정보는 `Environment` 객체를 통해 가져올 수 있다

### PropertySourcesPlaceholderConfigurer
- `Environment`객체 사용 대신 프로퍼티 값을 직접 DI하는 `@Value` 어노테이션을 사용할 수 있게 해준다
- @Configuration 클래스의 개체보다 먼저 인스턴스화되어야 하기 때문에 `static` 메소드로 정의한다 
  ```java
  @Bean
  public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
      return new PropertySourcesPlaceholderConfigurer();
  }
  ```
- 타입변환이 필요한 프로퍼티를 스프링에서 알아서 처리해준다 
  - `db.driverClass`
- 필드에 정의해야 하는 것이 단점이라 할 수 있다



## 빈 설정의 재사용과 @Enable*
- `@Import(SqlServiceContext.class)` 단 한줄로 서비스 관련 빈을 등록할 수 있다

### 빈 설정자
- `@Configuration` 빈 DI 클래스 자체도 빈이기 때문에 인터페이스를 구현하여 빈으로 사용할 수 있다
  ```java
  @Configuration
  @EnableTransactionManagement
  @ComponentScan(basePackages = "part6.user")
  @EnableSqlService
  @PropertySource(value = "/database.properties")
  public class AppContext implements SqlMapConfig {
    @Override
    public Resource getSqlMapResource() {
      return new ClassPathResource("sqlmap.xml", UserDao.class);
    }
  }
  ```
  
### @Enable*애노테이션
- *설정 클래스를 `@Import`하는 메타 애노테이션
  - `@EnableTransactionManagement`