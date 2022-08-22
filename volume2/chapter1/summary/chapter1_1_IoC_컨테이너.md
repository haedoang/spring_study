## 1.1 IoC 컨테이너: 빈 팩토리와 애플리케이션 컨텍스트
- 스프링 애플리케이션은 오브젝트의 생성, 관계설정, 사용, 제거 등의 작업을 독립된 컨테이너가 담당한다
- 이 컨테이너는 코드 대신 오브젝트에 대한 제어권을 가지기 때문에 `IoC`라 부른다
- `IoC`를 담당하는 컨테이너를 `빈 팩토리` 또는 `애플리케이션 컨텍스트`라 부른다
  - 빈 팩토리: 오브젝트의 생성과 오븢게트 사이의 런타임 관계 설정하는 DI 관점에서 부른다
  - 애플리케이션 컨텍스트: DI를 위한 빈 팩토리에 엔터프라이즈 애플리케이션 개발을 위한 기능을 컨테이너에 추가한 것을 말한다
- 스프링의 IoC 컨테이너는 일반적으로 애플리케이션 컨텍스트라 말한다
  - `BeanFactory`, `ApplicatonContext` 인터페이스로 정의되어 있다
  - `ApplicationContext` 가 `BeanFactory`에 상속되어 있다
   
### 1.1.1 IoC 컨테이너를 이용해 애플리케이션 만들기
#### IoC 컨테이너 생성 및 메타정보 설정
  ```java
  StaticApplicationContext context = new StaticApplicationContext();
  BeanDefinition helloDef = new RootBeanDefinition(); // 빈의 메타정보를 담은 오브젝트
  
  helloDef.getPropertyValues().addPropertyValue("name", "Spring");
  context.registerSingleton("hello2", Hello.class);
  final Hello hello2 = context.getBean("hello2", Hello.class);
  ```
  - 빈 메타정보
    - 빈 아이디 이름, 별칭
    - 클래스 또는 클래스 이름
    - 스코프
    - 프로퍼티 값 또는 참조
    - 생성자 파라미터 값 또는 참조
    - 지연 로딩 여부, 우선 빈 여부, 자동와이어링 여부, 부모 빈 정보, 빈팩토리 이름 등

#### IoC 컨테이너를 통해 애플리케이션이 만들어지는 방식
- 메타정보 리소스 
  - ex) xml, properties, annotation..
- 메타정보 리더로 메타정보 리소스 로드
  - ex) XmlBeanDefinitionReader
- POJO 클래스
- 설정 메타정보
- IoC 컨테이너는 각 빈에 대한 정보를 담은 설정 메타정보를 읽어서 빈 오브젝트를 생성
- 프로퍼티와 생성자를 통해 DI 작업을 수행한다
- 스프링 애플리케이션은 POJO 클래스와 설정 메타정보를 이용해 IoC 컨테이너가 만들어주는 오브젝트의 조합이다


### 1.1.2 IoC 컨테이너 종류와 사용 방법
#### StaticApplicationContext
- 코드를 통해 빈 메타정보를 등록하기 위해 사용 
- 학습 테스트용으로 적합

#### GenericApplicationContext
- 가장 일반적인 애플리케이션 컨텍스트 구현 클래스
- `XML` 파일과 같은 외부 리소스에 있는 빈 설정 메타정보를 읽어서 사용

#### GenericXmlApplicationContext
- GenericApplicationContext + XmlBeanDefinitionReader 조합

#### WebApplicationContext
- 가장 많이 사용되는 애플리케이션 컨텍스트
- ApplicationContext 를 확장함
  - XmlWebApplicationContext, AnnotationConfigWebApplicationContext 
--- 
- IoC 컨테이너를 사용하려면 `getBean()` 등의 메서드를 통해 객체를 찾아서 클라이언트에 반환을 해야 한다
- 스프링은 이러한 기능을 `DispacherServlet` 이라는 이름의 서블릿이 역할을 담당하고 있다


### 1.1.3 IoC 컨테이너 계층 구조
- 한 개 이상의 IoC 컨테이너를 사용할 경우 계층 구조를 구현할 수 있다

#### 부모 컨텍스트를 이용한 계층구조 효과
- 계층구조 안의 모든 컨텍스트는 각자 독립적인 설정정보를 이용해 빈 오브젝트를 만들고 관리한다
- 각자 독립적으로 관리하는 빈을 가지고 있지만 DI를 위해 부모 애플리케이션 컨텍스트의 빈까지 검색한다

#### 빈 컨텍스트 탐색 방식
- 자식은 부모 컨텍스트의 빈을 DI할 수 있지만 반대는 안 된다
- 같은 계층의 컨텍스트는 탐색할 수 없다
- 자식과 부모 컨텍스트에 같은 빈이 있으면 자신의 빈 컨텍스트가 사용된다


### 1.1.4 웹 애플리케이션의 IoC 컨테이너 구성
- 스프링 IoC 컨테이너 사용 방법
  1. 웹 애플리케이션 모듈 안에 컨테이너를 두는 방식
     - 요청을 대표로 받는 대표 서블릿과 핸들러를 통한 요청 후처리 기능을 담당하는 `프론트 컨트롤러 패턴`의 사용 
     - IoC 컨테이너 생성은 아래 두 가지 방법이 있다. 스프링 웹 애플리케이션은 두 개의 컨테이너가 만들어진다 
       1. 스프링 요청을 처리하는 서블릿 안에서 만들어진다
       2. 웹 에플리케이션 레벨에서 만들어진다
  2. 엔터프레이즈 애플리케이션 레벨에 두는 방식
  
#### 웹 애플리케이션 컨텍스트 계층구조
- 웹 애플리케이션 레벨에 등록되는 컨테이너는 `루트 웹 애플리케이션 컨텍스트`라고 부른다
- 이 컨텍스트는 서블릿 레벨에 등록되는 컨테이너들의 부모 컨테이너가 되고, 전체 계층구조 내 최상단에 위치한다
- 하나 이상의 프론트 컨트롤러 역할을 하는 서블릿이 등록될 수 있으나 일반적으로는 하나만 사용한다
    ```text
        ---->          서블릿           ---->        웹 애플리케이션
                (애플리케이션 컨텍스트)             (루트 애플리케이션 컨텍스트)
    ```
- 계층 구조로 구성하는 이유는 애플리케이션에서 웹 기술에 의존적인 부분과 그렇지 않은 부분을 분리하기 위해서이다
    ```text
        스프링 외의 웹 프레임워크       ---->
        webService/AJAX Engine    ---->     
        Model1 JSP/Servlet        ---->            웹 애플리케이션
        스프링 서블릿                ---->        (루트 애플리케이션 컨텍스트)
    ```
- 스프링은 서블릿 컨텍스트를 통해 루트 컨텍스트를 접근하는 방식을 제공한다
    ```java
        WebapplicationContextUtils.getWebApplicationContext(ServletContext sc);
    ```
#### 계층 구조 컨텍스트 사용 시 주의사항 
  1. 서블릿 컨텍스트의 빈은 루트 컨텍스트의 빈을 참조할 수 있지만 반대는 안 된다
  2. 루트 컨텍스트에 정의된 빈은 이름이 같은 서블릿 컨텍스트의 빈이 존재하면 무시될 수 있다
  3. 하나의 컨텍스트에 정의된 AOP 설정은 다른 컨텍스트의 빈에는 영향을 미치지 않는다 

#### 웹 애플리케이션 컨텍스트 구성 방법
1. 서블릿 컨텍스트와 루트 애플리케이션 컨텍스트 계층 구조
   - 스프링 웹 기술 관련 빈들은 서블릿 컨텍스트에 두고 나머지는 루트 애플리케이션 컨텍스트에 등록
2. 루트 애플리케이션 컨텍스트 단일 구조 
   - 스프링 웹 기술을 사용하지 않고 서드파티 웹 프레임워크나 서비스 엔진만 사용하는 애플리케이션
3. 서블릿 컨텍스트 단일 구조
   - 스프링 웹 기술을 사용하면서 스프링 외의 프레임워크나 서비스엔진에서 스프링 빈을 이용하지 않는 애플리케이션
   
#### 루트 애플리케이션 컨텍스트 등록 
- 서블릿의 `이벤트 리스너`를 이용하는 방식
- 웹 애플리케이션의 시작과 종료 시 발생하는 이벤트를 처리하는 리스너인 `ServletContextListener` 를 사용한다
- 웹 애플리케이션이 종료될 때 컨텍스트를 함게 종료하는 기능을 가진 리스너인 `ContextLoaderListener` 를 사용하여 등록한다
    ```xml
    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>
    ```
- contextConfigLocation
    ```xml
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>
            /WEB-INF/daoContext.xml
            /WEB-INF/applicationContext.xml
        </param-value>    
    </context-param>
    ```
  - 웹 애플리케이션이 시작할 때 자동으로 루트 애플리케이션 컨텍스트를 만들고 초기화한다
  - 애플리케이션 컨텍스트 클래스 : xmlWebApplicationContext (default)
  - XML 설정 파일 위치: /WEB-INF/applicationContext.xml (default)
  
#### 서블릿 애플리케이션 컨텍스트 등록
- 스프링이 지원하는 프론트 컨트롤러 서블릿은 `DispatherServlet`이다
- `DispatherServlet`은 서블릿이 초기화될 때 자신만의 컨텍스트를 생성하고 초기화한다
- 동시에 웹 애플리케이션 레벨에 등록된 루트 애플리케이션 컨텍스트를 찾아서 자신의 부모 컨텍스트로 사용한다
    ```xml
        <servlet>
            <servlet-name>spring</servlet-name>
            <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
            <load-on-startup>1</load-on-startup>
        </servlet>
    ```
    - `DispatcherServlet` 에 의해 만들어지는 애플리케이션 컨텍스트는 모두 독립적인 네임스페이스를 가진다
    - `<servlet-name>`으로 지정한 서블릿 이름에 `-servlet`을 붙여서 만든다 
      - ex) /WEB-INF/spring-servlet.xml
    - load-on-startup 
      - 생략 또는 음수의 경우 해당 서블릿은 서블릿 ㅋ너테이너가 정한 시점에 만들어지고 초기화된다
      - 0 이상의 값을 넣으면 애플리케이션 시작되는 시점에 로딩하고 초기화되며 여러 서블릿이 있는 경우 작은 수를 기준으로 우선순위를 가진다
  