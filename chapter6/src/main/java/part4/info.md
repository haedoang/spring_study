### 스프링의 프록시 팩토리 빈[⭐⭐⭐⭐⭐️️️]

#### ProxyFactoryBean
- 스프링이 프록시 오브젝트를 생성해주는 기술을 추상화한 팩토리 빈
- 순수하게 `프록시를 생성하는 작업`만을 담당한다
  - 부가 기능은 별도의 빈으로 등록한다
  - 싱글톤 으브젝트로 재사용 하도록 만들기 위해서  
  
#### 이전 장에서 학습한 팩토리 빈과의 차이점 ?
##### 팩토리 빈을 통한 프록시 생성
- 리플렉션을 통해 `InvocationHandler` 인터페이스를 구현해서 작성한다
- InvocationHandler의 `invoke()` 메서드는 타깃 오브젝트에 대한 정보를 제공하지 않는다
- InvocationHandler 구현체가 타깃에 대한 정보를 가지고 있어야 한다
    ```java
    public class TransactionHandler implements InvocationHandler {
        private Object target;
        private PlatformTransactionManager transactionManager;
        private String pattern;
    
        public void setTarget(Object target) {
            this.target = target;
        }
    
        public void setTransactionManager(PlatformTransactionManager transactionManager) {
            this.transactionManager = transactionManager;
        }
    
        public void setPattern(String pattern) {
            this.pattern = pattern;
        }
    
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (pattern.startsWith("upgradeLevels")) {
                return invokeInTransaction(method, args);
            } else {
                return method.invoke(target, args);
            }
        }
        ...
    }
    ```

##### ProxyFactoryBean 을 통한 프록시 생성
- `MethodInterceptor` 를 구현해서 작성한다
- MethodInterceptordml `invoke()` 메소드는 ProxyFactoryBean으로부터 타깃 오브젝트에 대한 정보도 함께 제공받는다
- 타깃 오브젝트에 상관없이 독립적으로 만들어질 수 있고 타깃이 다른 여러 프록시에서 함께 할 수 있고, `싱글톤`으로 등록이 가능하다
    ```java
    public class TransactionAdvice implements MethodInterceptor {
        PlatformTransactionManager transactionManager;
    
        public void setTransactionManager(PlatformTransactionManager transactionManager) {
            this.transactionManager = transactionManager;
        }
    
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            final TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
    
            try {
                final Object ret = invocation.proceed(); // template method pattern 
                this.transactionManager.commit(status);
                return ret;
            } catch (RuntimeException e) {
                this.transactionManager.rollback(status);
                throw e;
            }
        }
    }
    ```

#### 어드바이스(Advice): 타깃이 필요 없는 순수한 부가기능
- 타깃 오브젝트에 적용하는 부가기능을 담은 오브젝트를 말한다
- ProxyFactoryBean은 타깃 오브젝트에 대한 정보를 제공받기 때문에 타깃이 필요없는 순수한 부가기능을 등록할 수 있다
- MethodInterceptor는 부가기능을 제공하는데만 집중하면 된다
  - invoke()의 아규먼트인 `MethodInvocation`는 일종의 콜백 오브젝트로, proceed() 메소드를 실행하면 타깃 오븢게트의 메소드를 내부적으로 실행한다
  - 템플릿 메서드 패턴으로 동작한다 
- ProxyFactoryBean은 여러 개의 어드바이스를 가질 수 있다
    ```text
    ProxyFactoryBean pfBean = new ProxyFactoryBean();
    
    pfBean.setTarget(new HelloTarget());
    pfBean.addAdvice(new UppercaseAdvice()); // 부가 기능을 담당할 advice1
    pfBean.addAdvice(new TrimAdivce()); // 부가 기능을 담당할 advice2
    ```
- 구현해야할 인터페이스를 지정할 필요가 없다
  - 다이나믹 프록시에서는 구현할 인터페이스를 직접 작성했었다면, ProxyFactoryBean은 인터페이스 자동검출 기능으로 구현하는 오브젝트 정보를 알아낸다
- ProxyFactoryBean은 기본적으로 JDK가 제공하는 다이내믹 프록시를 만들어준다

#### 포인트컷(Pointcut): 부가기능 적용 대상 메서드 선정 방법
- ProxyFactoryBean은 InvocationHandler를 구현했을 때와 달리 메서드 선정 기능을 넣을 수 없다
  - 여러 프록시에서 공유해서 사용하도록 만들어졌기 때문에 타깃에 대한 정보를 가지지 않게 만들어졌다 
  - 대신 프록시에 부가기능 적용 메서드를 선택하는 기능을 넣는다 => `포인트컷`
- 부가기능 선정 방식 비교  
  1) InvocationHandler 프록시 방식 
  ```text
    XX 프록시 팩토리 빈
          |            모든 메서드 요청                              위임 
          ↓           -------------->  InvocationHandler   --------------->  타깃            
     다이나믹 프록시             (부가기능  + 메소드 선정 알고리즘(pattern))
  ```
  - InvocationHandler 가 메서드 선정 알고리즘을 가지고 있기 때문에, 타깃이 다르고 메서드 선정 방식이 다르면 해당 프록시를 공유하기 어렵다 
  2) ProxyFactoryBean 프록시 방식
  ```text
    ProxyFactoryBean
          |             
          ↓          기능부가 대상 확인[1]
     다이나믹 프록시     ---------------->  포인트컷  
                                   (메서드 선정 알고리즘)
                     
                     선정된 메서드만 요청[2]       
                     ---------------->  어드바이스(MethodInterceptor): 템플릿
                       ︎                   (부가기능) 
                        method.proceed()[3]                 
     [Invocation 콜백] <----------------                             위임[4]    
                      ---------------------------------------------------->   타깃 오브젝트
  ```
- 부가기능 제공 오브젝트를 `어드바이스`, 메서드 선정 알고리즘 오브젝트를 `포인트컷`(Pointcut 인터페이스 구현)이라 한다
- 어드바이스와 포인트컷은 모두 프록시에 DI로 주입되어 사용된다
- 어드바이스는 독립적인 싱글톤 빈으로 등록하고 DI를 주입해서 여러 프록시가 사용하도록 만들 수 있다
  - 프록시로 어드바이스와 포인트컷을 독립시키고 DI를 사용하게 하는 것은 `전략 패턴 구조` 라고 볼 수 있다
  - 부가기능(어드바이스)이나 메서드 선정 알고리즘(포인트컷)이 변경되면 프록시와 ProxyFactoyBean의 변경 없이 구현 클래스만 바꾸면 된다
- 포인트컷과 어드바이스를 묶은 오브젝트를 `어드바이저(Advisor)` 라고 한다 


#### 어드바이스와 포인트컷의 재사용
- ProxyFactoryBean은 스프링의 DI, 템플릿/콜백 패턴, 서비스 추상화 등의 기법이 적용되어 있다
- 독립적이기 때문에 여러 프록시가 공유할 수 있는 어드바이스와 포인트컷으로 확장 기능을 분리할 수 있다
