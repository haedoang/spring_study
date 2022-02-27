### 다이내믹 프록시와 팩토리 빈[⭐⭐⭐⭐⭐️️️]

#### 프록시와 프록시패턴, 데코레이터 패턴
- 프록시: 자신이 클라이언트가 사용하려고 하는 실제 대상인 것처럼 위장해서 클라이언트의 요청을 받아주는 것
- 타깃, 실체: 프록시를 통해 위최종적으로 요청을 위임받아 처리하는 실제 오브젝트 
  ```text
    Client -----> 프록시 -------> 타깃 
  ```

##### 데코레이터 패턴
- 타깃에 부가적인 기능을 런타임 시 `다이나믹`하게 부여해주기 위한 프록시 패턴
- 어떤 방법과 순서로 프록시와 타깃이 연결되어 사용되는지 정해져있지 않다
- 프록시가 직접 타깃을 사용하도록 고정시킬 필요도 없다
- 데코레이터 패턴에서는 프록시를 여러 개를 사용할 수 있고, 순서를 정해서 단계적으로 위임하는 구조로 만들어야 한다
  ```text
    Client ---> 라인넘버 데코레이터 ---> 컬러 데코레이터 ---> 페이징 데코레이터 ---> 타깃
  ```
- ex) InputStream, OutputStream `BufferedInputStream`
  ```text
    InputStream is = new BufferedInputStream(new FileInputStream("a.txt"));
  ```
- `UserServiceTx`가 데코레이터 패턴을 적용한 예이다

##### 프록시 패턴
- `타깃에 대한 접근 방법을 제어` 하려는 목적으로 프록시를 사용하는 방법
- 프록시는 타깃의 기능을 확장하거나 추가하지 않는다. 대신 클라이언트가 타깃에 접근 방식을 변경해준다
- 타깃 오브젝트를 생성하기가 복잡하거나 필요하지 않는 경우 필요 시점까지 생성하지 않는 편이 좋다
- 클라이언트에게 타깃에 대한 레퍼런스를 넘겨야할 경우, 프록시를 전달해준다
- ex) Collections.unmodifiableCollection()
  - 접근권한 제어용 프록시. 파라미터로 전달된 Collection 오브젝트를 프록시로 만든다
  - add(), remove() 시 UnsupportedOperationException() 예외를 발생한다
- 타깃의 기능 자체에는 관여하지 않으면서 접근방법을 제어해준다
 
##### 프록시패턴과 데코레이터 패턴의 혼용
```text
  Client --> 접근제어 프록시 --> 컬러 데코레이터 --> 페이징 데코레이터 --> 타깃 
              (프록시패턴)           
```
---
####  다이내믹 프록시
- 프록시의 구성과 프록시 작성의 문제점
- 프록시의 역할: 1) 위임 2) 부가작업
```java
public class UserServiceTx implements UserService {
    UserService userService;
    PlatformTransactionManager transactionManager;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    
    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
    
    public void add(User user) {
        userService.add(user);
    }

    public void upgradeLevels() {
        final TransactionStatus status = this.transactionManager        //부가작업
                .getTransaction(new DefaultTransactionDefinition());    

        try {
            userService.upgradeLevels(); // => 위임 

            this.transactionManager.commit(status);    // 부가작업
        } catch (RuntimeException e) {                      
            this.transactionManager.rollback(status);
            throw e;
        }
    }
}
```
- 타깃의 인터페이스를 구현하고 위임하는 코드를 작성하기 번거롭다
- 부가기능 코드가 중복될 가능성이 많다.

##### 리플렉션
- 다이나믹 프록시는 리플렉션 기능을 이용하여 프록시를 만든다. 리플렉션은 자바의 코드 자체를 추상화해서 접근하도록 만든다

##### 다이나믹 프록시 적용
```text
                  InvocationHandler  -----------> 타깃
          메서드 처리 요청 ↑  ↓ 결과 리턴      (위임)
  Client --------> Dynamic Proxy 
    |    메서드 호출       ↑ 프록시 생성 
    |___________-> Proxy Factory
       프록시 요청          
```
- 다이나믹 프록시는 프록시 팩토리에 의해 런타임 시 다이나믹(리플렉션)하게 만들어지는 오브젝트다.
- 다이나믹 프록시 오브젝트는 `타깃의 인터페이스와 같은 타입`으로 만들어진다
- 다이나믹 프록시가 인터페이스 구현 클래스의 오브젝트는 만들어주지만, 부가기능은 프록시 오브젝트와 독립적으로 InvocationHandler를 구현한 오브젝트에 담는다
  ```text
    public Object invoke(Object proxy, Method method, Object[] args)
  ```
- `invoke()`는 리플렉션의 Method 인터페이스와 메소드를 호출할때 전달되는 args를 파라미터로 받는다. 
- 다이나믹 프록시 오브젝트는 클라이언트의 `모든 요청을 리플렉션 정보로 변환`해서 InvocationHandler 구현 오브젝트의 invoke() 메소드로 넘기는 것이다
  ```text
    다이나믹 프록시              InvocationHandler               타깃
      sayHello()      ----->                    ----->    sayHello()           
      sayHi()         ----->   invoke(Method)   ----->    sayHi()
      sayThankYou()   ----->                    ----->    sayThankYou()
  ```
- 다이나믹 프록시 생성
  ```text
      Hello proxiedHello = (Hello) Proxy.newProxyInstance(
          getClass().getClassLoader(), //다이나믹 프록시가 정의되는 클래스 로더
          new Class[] { Hello.class }, // 프록시가 구현할 인터페이스
          new UppercaseHandler(new HelloTarget()) // 부가 기능
      );
  ```
  - 첫번째 파라미터: 다이내믹 프록시가 정의되는 클래스 로더를 지정
  - 두번째 파라미터: 다이나믹 프록시가 구현할 인터페이스(하나 이상)
  - 세번째 파라미터: 부가기능과 위임 관련 코드를 담고 있는 InvocationHandler 구현 오브젝트 

##### 다이나믹 프록시의 확장
- InvocationHandler 구현하기 
  ```text
  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      final Object ret  = method.invoke(target, args);
          
      //부가 기능 
      if (ret instanceof String && method.getName().startsWith("say")) {
          return ((String)ret).toUpperCase();
      }
      else {
          return ret;
      }
  }
---

#### 다이내믹 프록시를 위한 팩토리 빈
- 다이내믹 프록시는 일반적인 스프링의 빈으로는 등록할 방법이 없다. 
- 스프링은 내부적으로 리플렉션 API를 이용하여 빈 오브젝트를 생성하는데 다이내믹 프로젝트는 이런식으로 프록시 오브젝트가 생성되지 않는다
- 팩토리빈: 스프링을 대신해서 오브젝트의 생성로직을 담당하도록 만들어진 빈

##### 팩토리 빈을 가져오는 방법
- 스프링은 `&` 빈 이름 앞에 붙여주면 팩토리 빈 자체를 돌려준다
  ```text
    @Test
      public void getFactoryBean() {
          final Object messageFactoryBean = context.getBean("&message");
          assertThat(messageFactoryBean, is(MessageFactoryBean.class));
    }
  ```

#### 프록시 팩토리 빈 방식의 장점과 한계 
- 하나의 핸들러 메소드를 구현하는 것만으로 수많은 메소드에 부가기능을 부여할 수 있다
- 번거로운 다이내믹 프록시 생성코드도 제거할 수 있다 

##### 프록시 팩토리 빈의 한계
- 한번에 여러 개의 클래스에 공통적인 부가기능을 제공하는 것
- 하나의 타깃에 여러 개의 부가기능을 적용하는 것
- 소스 수정은 없지만 설정 파일이 늘어나기때문에 설정 파일관리의 어려움 