# 1.3 프로토타입과 스코프

## 1.3.1 프로토타입 스코프
- 컨테이너에게 빈을 요청할 떄마다 매번 새로운 오브젝트를 생성한다
- `DL`, `DL` 상관없이 매번 새로운 오브젝트가 만들어진다

### 프로토타입 빈의 생명주기와 종속성
- IoC의 기본 개념은 애플리케이션 핵심 오브젝트를 코드가 아니라 컨테이너가 관리한다는 것이다
  - 단, 프로토타입 빈은 이 IoC 원칙을 따르지 않는다.
- 프로토타입 빈은 요청이 있을 때마다 컨테이너가 생성하고 초기화하고 DI를 해주지만 <br/>
  빈을 제공하고 나면 컨테이너는 더 이상 빈 오브젝트를 관리하지 않는다
  - 한번 DI나 DL을 통해 컨테이너 밖으로 전달된 후에는 더 이상 스프링이 관리하는 빈이 아니다
- 프로토타입 빈 오브젝트의 관리는 전적으로 DI 받은 오브젝트에 달려 있다
- 프로토타입 빈을 주입받은 빈이 싱글톤이라면, 주입된 프로토타입 빈도 싱글톤 생명주기를 가진다.

### 프로토타입 빈의 용도
- new로 오브젝트를 생성하는 것을 대신하는 용도
- 오브젝트가 컨테이너 내의 빈을 사용해야 하는 경우 프로토타입 빈을 사용한다

### DI와 DL 
- DI 작업은 빈 오브젝트가 처음 만들어질 때 단 한번만 진행된다
  - 여러 사용자가 동시에 요청을 보내면 오브젝트 데이터가 덮어 쓰여질 수 있다 
  - 같은 컨트롤러에서도 매번 요청이 있을 때마다 새롭게 오브젝트을 만들어지는 경우는 적합하지 않다
- 프로토타입 빈은 DI 방식으로 사용하기 적합하지 않다


### 프로토타입 빈의 DL 전략

#### ApplicationContext, BeanFactory
- `@Autowired`, `@Resource` 를 이용해 DI를 받은 뒤 getBean() 메서드를 호출해 빈을 가져오는 방식
- XML만을 사용한다면 ApplicationContextAware, BeanFactoryAware 를 이용해도 된다 
- 코드에 스프링 API가 직접 등장한다는 단점이 있다 

#### ObjectFactory, ObjectFactoryCreatingFactoryBean
- 직접 애플리케이션 커텍스트를 사용하지 않고 중간에 getBean()을 호출해주는 오브젝트를 사용한다 
    ```text
    Controller -->   ServiceRequest  --> ApplicationContext  
                     (ObjectFactory)
    ```
    ```xml
    <bean id="serviceRequestFactory" class="...ObjectFactoryCreatingFactoryBean">
        <property name="targetBeanName" value="serviceRequest"/>
    </bean>
    ```
    ```java
    import javax.annotation.Resource;
  
    @Resource
    private ObjectFactory<ServiceRequest> serviceRequestFactory;
  
    public void serviceRequestFormSubmit(HttpServletRequest request) {
        ServiceRequest serviceRequest = this.serviceRequestFactory.getObject();
        serviceRequest.setCustomerByCustomerNo(request.getParameter("custno"));
    }
    ```

#### ServiceLocatorFactoryBean
- ObjectFactory처럼 스프링이 정의해둔 인터페이스를 사용하지 않아도 된다
- DL 방식으로 가져올 빈을 리턴하는 임의의 이름을 가진 메소드가 정의된 인터페이스가 있으면 된다
    ```java
    public interface ServiceRequestFactory {
        ServiceRequest getServiceFactory();
    }
    ```
    ```xml
    <bean class="...ServiceLocatorFactoryBean">
        <property name="serviceLocatorInterface" value="..ServiceRequestFactory"/>
    </bean>
    ```
    ```java
    import org.springframework.beans.factory.annotation.Autowired;
  
    @Autowired
    ServiceRequestFactory serviceReqeuestFactory;
   
    public void serviceReqeustFormSubmit(HttpServletRequest request) {
        ServiceRequest serviceRequest = 
            this.serviceRequestFactory.getServiceFactory();
        serviceRequest.setCustomerByCustomerNo(request.getParameter("custNo"));
    } 
    ```
#### 메소드 주입
- 메서드 코드 자체를 주입하는 것을 말한다
- 새로운 프로토타입 빈을 가져오는 기능을 담당하는 메소드를 런타임에 추가해주는 기술이다
    ```java
    abstract public ServiceRequest getServiceRequest();
    
    public void serviceRequestFormSubmit(HttpServletRequest request) {
        ServiceRequest serviceRequest = this.getServiceRequest();
        serviceRequest.setCustomerByCustomerNo(request.getParameter("custNo"));
    }
    ```
    ```xml
    <bean id="serviceRequestController" class="...ServiceRequestController">
        <lookup-method name="getServiceRequest" bean="serviceRequest" />
    </bean>
    ```

#### Provider< T>
- `@Inject`, `@Autowired`, `@Resource` 중의 하나를 이용해 DI하는 방식
    ```java
    @Inject Provider<ServiceRequest> serviceRequestProvider;
    
    public void serviceRequestFormSubmit(HtppServletRequest request) {
        ServiceRequest serviceRequest = this.serviceRequestProvider.get();
        serviceRequest.setCustomerByCustomerNo(request.getParameter("custNo"));    
    }
    ```

## 1.3.2 스코프 
### 스코프의 종류
- 싱글톤, 프로토타입, 요청, 세션, 글로벌세션, 애플리케이션

#### 요청 스코프 
- 하나의 웹 요청 안에서 만들어지고 요청이 끝날 때 제거된다
- 요청별로 독립적인 빈이 만들어지기 때문에 오브젝트 내에 상태 값을 저장해도 안전하다
- 동시에 여러 사용자가 많은 요청을 보내도 안전하다
- 애플리케이션 코드에서 생성한 정보를 프레임워크 레벨의 서비스나 인터셉터 등에 전달하는 것이다 

#### 세션 스코프, 글로벌 스코프
- HTTP 세션과 같이 존재 범위를 갖는 빈으로 만들어주는 스코프
- 사용자별로 만들어지고 브라우저를 닫거나 세션 타임이 종료될 때까지 유지
  - 로그인 정보나 사용자별 선택 옵션 등을 저장하는 용도로 사용한다
- HTTP 세션은 계층 제한적인데 반해 세션 스코프는 그렇지 않다
- 글로벌 세션은 포틀릿에만 존재하는 글로벌 세션에 저장되는 빈이다

#### 애플리케이션 스코프
- 서블릿 컨텍스트에 저장되는 빈 오브젝트로 웹 애플리케이션마다 만들어진다
- 애플리케이션 스코프는 컨텍스트가 존재하는 동안 유지되는 싱글톤 스코프와 비슷한 존재 범위를 가진다
- 웹 애플리케이션과 애플리케이션 컨텍스트의 존재 범위가 다른 경우에 사용된다
- 애플리케이션 스코프는 싱글톤 스코프와 마찬가지로 상태를 갖지 않거나 상태가 있다 하더라도 읽기전용으로 <br/>
  만들거나 멀티스레드 환경에서 안전하도록 만들어야 한다

### 스코프 빈의 사용 방법
- 애플리케이션 스코프를 제외한 나머지 세가지 스코프는 프로토타입 빈과 마찬가지로 한개 이상의 빈 오브젝트가 생성된다
- 싱글톤에 DI를 해주는 방법으로 사용할 수 없기 때문에 `스코프 프록시`를 이용한다
    ```text
        Client ---> Scoped Proxy --------->  Session Scope Object1
       (LoginService)                        Session Scope Object2
                                             Session Scope Object3
                                                      ...
    ```
- 스코프 프록시는 클라이언트의 호출을 위임해주는 역할을 한다
    ```java
    @Scope(value="session", proxyMode=ScopedProxyMode.TARGET_CLASS)
    public class LoginUser {}
  
    public class LoginService {
        @Autowired
        LoginUser loginUser;
    
        public void login(Login login) {
            this.loginUser.setLoginId(...);
        }   
    } 
    ```
  - 프록시 방식의 DI는 스코프 빈이지만 싱글톤 빈을 사용하듯 쓸수 있다는 장점이 있다
  - 주입되는 빈의 스코프를 모르면 코드가 이해하기 어려울 수 있다

### 커스텀 스코프와 상태를 저장하는 빈 사용하기
- 임의의 스코프를 만들어 사용할 수 있다.
- `Scope` 인터페이스를 구현해서 새로운 스코프를 작성할 수 있으나 스코프를 새로 작성하고 적용하는 것은 복잡하다