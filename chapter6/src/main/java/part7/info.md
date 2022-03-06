### 애노테이션 트랜잭션 속성과 포인트컷
- `@Transactional` 어노테이션은 제각각 속성이 다른 클래스나 메소드에 세밀하게 튜닝된 트랜잭션을 적용할 수 있다

### 트랜잭션 어노테이션
- 타깃에 부여할 수 있는 트랜잭션 어노테이션

#### @Transactional 
  ```java 
  @Target({ElementType.METHOD, ElementType.TYPE})
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Documented
  public @interface Transactional {
      String value() default "";
      Propagation propagation() default Propagation.REQUIRED;
      Isolation isolation() default Isolation.DEFAULT;
      int timeout() default TransactionDefinition.TIMEOUT_DEFAULT;
      boolean readOnly() default false;
      Class<? extends Throwable>[] rollbackFor() default {};
      String[] rollbackForClassName() default {};
      Class<? extends Throwable>[] noRollbackFor() default {};
      String[] noRollbackForClassName() default {};
  }
  ```
  - 타깃 : 메서드와 타입 
    - 우선순위 : 1) 타깃 메서드 2) 타깃 클래스 3) 선언 메서드 4) 선언 타입 
  - `@Transactional` 어노테이션을 트랜잭션 속성정보로 사용하도록 지정하면 스프링은 해당 어노테이션이 부여된 모든 오브젝트를 타깃 오브젝트로 인식한다 
  - `TransactionalAttributeSourcePointcut` 포인트컷이 사용된다
  - 표현식같은 설정기준이 없고, 어노테이션을 기준으로 찾아서 선정 결과를 돌려준다 

#### 트랜잭션 속성을 이용하는 포인트컷
- `TransactionInterceptor`는 메서드 이름 패턴을 통해 부여되는 트랜잭션 속성대신 <br/>
  `@Transactional` 엘리먼트에서 속성을 가져오는 `AnnotationTransactionAttributeSource`를 사용한다
- `@Transactional` 어노테이션은 메서드마다 다르게 설정할 수 있어 유연하게 트랜잭션 설정을 할 수 있다
  ```text
       ------------>      TransactionAttributeSourcePointcut  
       | 포인트컷      속성이 부여된 대상을 확인해서 |
       |             포인트컷을 한다           | 
    Advisor                                |
       |                                   |
       | 어드바이스                           ↓        어노테이션에 담긴 트랜잭션 속성정보를 사용한다 
       ------------->    TxInterceptor | AnnotationTransactionAttributeSource     ----------> @Transactional(readOnly=true)
  ```
- 위 방식을 사용하면 포인트컷과 트랜잭션 속성을 어노테이션 하나로 지정할 수 있다(포인트컷 -> 어노테이션 적용, 트랜잭션속성 -> 어노테이션의 설정)
- 트랜잭션 부가기능 적용 단위는 메서드인데, 동일한 속성 정보를 가진 애노테이션이 반복적으로 부여될 수 있다(아래 대체 정책에서 문제점을 해결할 수 있다)
#### 대체 정책
- 4단계의 대체 정책(우선 순위)
  1) 타깃의 메소드
  2) 타깃의 클래스
  3) 선언의 메소드`
  4) 선언의 타입
- 선언의 타입, 즉 인터페이스에서 구현하는 것이 확장성을 고려했을 때 가장 이상적이지만, 프록시 방식의 AOP가 아닌 방식으로 적용하는 경우 무시될 수 있다
  - 안전하게 타깃 클래스에 `@Transactional`을 두는 방법을 권장한다 

#### 트랜잭션 애노테이션 사용을 위한 설정
- 어드바이저, 어드바이스, 포인트컷, 애노테이션을 이용하는 트랜잭션 속성정보가 아래 태그하나로 설정된다
  ```xml
  <tx:annotation-driven />
  ```