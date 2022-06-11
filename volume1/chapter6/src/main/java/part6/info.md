### 트랜잭션 속성[⭐⭐⭐⭐⭐️️️]

### 트랜잭션 정의
- 트랜잭션은 모두 같은 방식으로 동작하지 않는다.
- `DefaultTransactionDefinition`이 구현하고 있는 `TransationDefinition` 인터페이스는 트랜잭션 동작방식에 영향을 줄 수 있는 네가지 속성을 정의하고 있다   

#### 트랜잭션 전파(Transaction Propagation) 
- 트랜잭션 의 경계에서 이미 진행중인 트랜잭션이 있을 때 또는 없을 때 어떻게 동작할 것인지 결정하는 방식
  ```text
        A                       B
    트랜잭션 시작
        |
        ↓       --------->   트랜잭션 시작
    B.method()                   |
                                 ↓     
                <---------   트랜잭션 종료                                
        ↓
    트랜잭션 종료     
  ```
- ##### PROPAGATION_REQUIRED
  - 진행 중인 트랜잭션이 없으면 새로 시작, 이미 시작된 트랜잭션이 있으면 `참여`한다(DefaultTransactionDefinition의 default 설정)
- ##### PROPAGATION_REQUIRES_NEW 
  - 항상 새로운 트랜잭션을 시작한다. 독립적인 트랜잭션이 보장돼야하는 코드에 적용한다
- ##### PROPAGATION_NOT_SUPPORTED
  - 트랜잭션 없이 동작하도록 한다. 진행 중인 트랜잭션이 있어도 무시한다
  - 트랜잭션 경계설정은 보통 AOP이용하여 작성하는데 트랜잭션이 필요하지 않은 특별한 메소드를 분리하기는 쉽지 않다(포인트컷 표현식에서 제외하기 등)
  - 이러한 경우 트랜잭션 전파 설정을 `PROPAGATION_NOT_SUPPORTED`로 작성하여 필터하는 방식이 낫다

#### 격리수준
- 모든 DB 트랜잭션은 격리수준을 가지고 있어야 한다.
- 트랜잭션 격리수준이란 동시에 여러 트랜잭션이 처리될 때, 트랜잭션끼리 얼마나 서로 고립되어 있는지를 나타내는 것이다.
- 특정 트랜잭션이 다른 트랜잭션에 변경한 데이터를 볼 수 있도록 허용할 지를 결정하는 것
- 격리 수준 종류
  - READ UNCOMMITTED: 어떤 트랜잭션의 변경내용이 COMMIT 이나 ROLLBACK에 상관없이 보여진다
  - READ COMMITTED: 어떤 트랜잭션의 변경내용이 COMMIT이 되어야만 보여진다
  - REPEATABLE READ: 트랜잭션이 시작되기 전에 커밋된 내용에 대해서만 보여진다(MySQL default)
  - SERIALIZABLE: 읽기 작업에도 `공유 잠금`을 설정하여 동시에 다른 트랜잭션이 레코드를 변경하지 못하게 한다(성능저하가 발생할 수 있다) 

#### 제한시간
- 트랜잭션을 수행하는 제한시간을 설정한다
- `DefaultTransactionDefinition`의 제한시간은 없다
- 트랜잭션을 직접 시작할 수 있는 `PROPAGATION_REQUIRED`, `PROPAGATION_REQUIRES_NEW`와 함께 사용해야만 의미가 있다

#### 읽기전용
- 데이터를 조작하는 시도를 막아준다(TransientDataAccessResourceException 예외 발생)
- 또한, 데이터 액세스 기술에 따라 성능이 향상될 수 있다

---
### 트랜잭션 인터셉터와 트랜잭션 속성 
- 메서드별로 다른 트랜잭션 정의를 적용하려면 어드바이스의 기능을 확장해야 한다 

#### TransactionInterceptor
- 트랜잭션 정의를 메서드 이름 패턴을 이용해서 다르게 지정할 수 있는 방법을 제공한다
- `PlatformTransactionManager`, `Properties`(트랜잭션 부가 기능 동작 방식을 제어)
  ```xml
    <bean id="transactionAdvice" class="org.springframework.transaction.interceptor.TransactionInterceptor">
        <property name="transactionManager" ref="platformTransactionManager"/>
        <property name="transactionAttributes">
            <props>
                <prop key="get*">PROPAGATION_REQUIRED,readOnly,timeout_30</prop>
                <prop key="upgrade*">PROPAGATION_REQUIRES_NEW,ISOLATION_SERIALIZABLE</prop>
                <prop key="*">PROPAGATION_REQUIRED</prop>
            </props>
        </property>
    </bean>
  ```

#### TransactionAdvice와 차이점은?
- TransactionAdvice는 `RuntimeException` 예외에 대해선만 트랜잭션을 롤백시켰다. 체크 예외를 던지는 타깃에 사용한다면 문제가 될 수 있다
  - 모든 종류에 대한 예외를 던지게 되면 어떠한가? => 비즈니스 로직상의 예외를 나타내기 위해 타깃 오브젝트가 체크 예외를 던지는 경우 commit이 되어야 한다
  - 즉, 다시 말하면 트랜잭션 예외 상황을 설정하기 어려운 단점이 있다
- TransactionInterceptor는 두 가지 예외 처리 방식이 있다(TransactionAdivce 구현 내용과 같음)
  1) 런타임 예외가 발생하면 트랜잭션 롤백 
  2) 타깃 메서드가 런타임 예외가 아닌 체크 예외를 던지는 경우에는 예외상황으로 해석하지 않고 커밋한다
  - 기본 원칙을 따르지 않는 경우 `rollbackOn()` 메서드를 제공한다
  - 특정 체크 예외의 경우 롤백시키고, 런타임 예외에 대해 커밋시킬 수도 있다(개별적인 예외 설정이 가능하다)

#### 메서드 이름 패턴을 이용한 트랜잭션 속성 지정
  > PROPAGATION_NAME, ISOLATION_NAME, read-only, timeout_NNNN, -Exception1, +Exception2
  - `-Exception`: 체크 예외 중에서 롤백 대상을 추가한다
  - `+Exception`: 런타임 예외 중에서 롤백 시키지 않을 대상을 추가한다

#### tx 네임스페이스를 이용한 설정 방법
  - 관례상 조회 메서드는 `get` 또는 `find` 메서드를 사용한다 
  ```xml
    <tx:advice id="transactionAdvice" transaction-manager="platformTransactionManager">
          <tx:attributes>
              <tx:method name="get*" read-only="true" timeout="30"/>
              <tx:method name="upgrade*" propagation="REQUIRES_NEW" isolation="SERIALIZABLE"/>
              <tx:method name="*" />
          </tx:attributes>
      </tx:advice>
  ```

---

### 포인트커소가 트랜잭션 속성의 적용 전략

#### 트랜잭션 포인트컷 표현식은 타입패턴이나 빈 이름을 이용한다 
- 관례적으로 비즈니스 로직 서비스를 담당하는 클래스 이름은 `Service` 또는 `ServiceImpl`이다(인터페이스 타입 권장)
- 비즈니스 로직을 담고 있는 클래스라면 메소드 단위까지 세밀하게 포인트컷을 적용할 필요가 없다
- 읽기 전용 메서드에도 트래잭션을 적용하는 것이 좋다(읽기 전용 액세스 성능 향상)
- `execution()` 방식보다 `bean()` 표현식이 일정한 규칙이 없는 클래스나 인터페이스에 더 유용하다 


#### 공통된 메소드 이름 규칙을 통한 최소화의 트랜잭션 어드바이스와 속성을 적용한다 
- 단계적으로 트랜잭션 속성을 적용하는 것을 권장한다
  1) 디폴트 속성을 먼저 정의한다
  2) 조회(읽기 전용) 속성을 정의한다
  3) 일반화하기 적당하지 않은 특별한 트랜잭션 속성이 필요한 타깃 오브젝트에 대해서는 별도의 어드바이스와 포인트컷을 사용한다 
  
#### 프록시 방식 AOP는 같은 타깃 오브젝트 내의 메소드를 호출할 떄는 적용되지 않는다[⭐⭐⭐]
- 프록시 방식의 AOP에서 프록시를 통한 부가기능의 적용은 클라이언트 호출이 일어날 때만 가능하다 
  ```text
                  [1]
      클라이언트 ----------->  트랜잭션 프록시         ----------->      타깃
                       userService.delete()     ----------> userDao.delete()
                                                              [3]  ↓ (트랜잭션 부가기능 X)
                  [2]                                        userDao.update()
               ---------->                                              
                       userService.update()     -----------> userDao.update()                                        
  ```
- [1],[2] 방식은 클라이언트에서 트랜잭션 프록시를 호출하기 때문에 트랜잭션 경계 대상 설정이다 
- [3] 의 경우 클라이언트에서 호출한 경우가 아니기 때문에 프록시 객체가 아니다. 따라서 트랜잭션 부가기능을 지원하지 못한다 

--- 
### 트랜잭션 속성 적용

#### 트랜잭션 경계설정의 일원화
- 특정 계층의 경계를 트랜잭션 경계와 일치시키는 것이 바람직하다 
- 따라서, 서비스 계층을 트랜잭션 경계로 두었다면 데이터 액세스 계층(DAO)는 직접적으로 접근하지 못하게 하는것이 바람직하다(테스트 제외) 

