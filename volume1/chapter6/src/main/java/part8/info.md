### 트랜잭션 지원 테스트

### 선언적 트랜잭션과 트랜잭션 전파 속성
- 트랜잭션 전파 방식을 통해 우리는 비즈니스 로직 간의 트랜잭션 연관 관계를 유연하게 가져갈 수 있다
- 독자적인 트랜잭션 단위가 될 수 있고, 다른 트랜잭션의 일부로 참여할 수 있다 
    ```text   
       클라이언트 ----- |--------------------------------------------> add() (Propagation.REQUIRED) 
                     | ----> processEventRegistration() --------- ↗   ↑  
                     |                 (tx)                           |  트랜잭션이 시작된 상태에서 메서드 호출이 일어나면 
                     | -----> method() -------------------------------|  기존 트랜잭션에 참여한다 
              트랜잭션 시작/종료 경계        (tx)     
    ```
- AOP를 이용해 코드 외부에서 트랜잭션의 기능을 부여해주고 속성을 지정하는 방법을 `선언적 트랜잭션`이라고 한다. `@Transactional`
- TransactionTemplate 이나 개별 데이터 기술의 트랜잭션 API를 사용하는 방법을 `프로그램에 의한 트랜잭션`이라고 한다. 
- `선언적 트랜잭션` 을 권장한다 
---

### 트랜잭션 동기화와 테스트 

#### 트랜잭션 매니저와 트랜잭션 동기화
- `PlatformTransactionManager` 인터페이스를 구현하여 일괄된 트랜잭션을 제어할 수 있다
- `트랜잭션 동기화` 기술로 인해 트랜잭션 정보를 보관하였다가 DAO에서 공유할 수 있다
- `선언적 트랜잭션`, `프로그램에 의한 트랜잭션` 방식 중 선언적 트랜잭션이 사용하기 훨씬 편리하다
- `프로그램에 의한 트랜잭션`은 테스트 사용시 유용하게 사용할 수 있다

#### 트랜잭션 매니저를 이용한 테스트용 트랜잭션 제어 
- `프로그램에 의한 트랜잭션`을 이용하여 `Propagation.REQUIRED` 메소드들을 트랜잭션 내에 참여시킬 수 있다
- 즉, 프로그램에 의한 트랜잭션을 호출하여 참여시킬 수 있다는 뜻이다
    ```text
            final DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
            final TransactionStatus status = transactionManager.getTransaction(txDefinition); //트랜잭션 시작 
            
            userService.addUser(user);  //트랜잭션 참여
    ```
#### 트랜잭션 동기화 검출
- `Propagation.Required`은 시작된 트랜잭션에 참여하는 경우 속성이 무시되기 때문에 시작된 트랜잭션에서 속성을 변경한 것이 영향을 미치는 지 검증해볼 수 있다

#### 롤백 테스트 
- 테스트 코드로 트랜잭션을 제어해서 적용할 수 있는 테스트 기법이다
- 테스트 내의 모든 DB 작업을 하나의 트랜잭션 안에서 동작하게하고 테스틑가 끝나면 무조건 롤백해버리는 테스트이다 
- DB 작업이 포함된 테스트가 수행돼도 DB에 영향을 주지 않는다

---
### 테스트를 위한 트랜잭션 애노테이션

#### @Transactional
- 테스트 클래스 또는 메소드에도 트랜잭션 경계를 적용할 수 있다

#### @Rollback
- `@Transactional` 은 테스트가 끝나는 경우 자동으로 롤백이 되게 설정되어있다
- 필요에 따라 롤백 설정을 변경하기 위해 사용된다

#### TransactionConfiguration
- `@Transactional` 은 클래스, 메소드 레벨에 적용이 가능하고, `@Rollback`은 메소드 레벨에만 적용이 가능하다
- `@Rollback` 기능을 일괄적으로 클래스 레벨에 작성하고 싶은 경우 `@TransactionalConfiguration` 을 사용한다

#### NotTransactional & Propagation.NEVER
- 트랜잭션이 필요하지 않은 경우 설정할 수 있다
- `@NotTransactional`: 테스트 메소드에 부여하면 클래스 레벨의 `@Transactional`설정을 무시하고 트랜잭션을 시작하지 않은 채로 시작한다
  - 스프링3.0에서 제거 대상이다
- `@Transactional(propagation=Propagation.NEVER)` 를 사용하자 

#### 효과적인 DB 테스트
- DB가 사용되는 통합 테스트는 가능한 한 롤백 테스트로 만드는게 좋다.
- 테스트가 테스트 사이에서 서로 영향을 주지 않아야 하기 때문에 의존적이지 않아야 한다