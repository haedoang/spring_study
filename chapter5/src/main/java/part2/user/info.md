### 트랜잭션 서비스 추상화

#### 트랜잭션 경계에 대한 설정이 없이는 예외가 발생했을 때 문제가 일어날 수 있다
- SQL 작업 중 예외가 발생하여 스케줄링이 종료된 경우 기존 업데이트 된 데이터를 롤백할지 판단을 해야 한다
- `jdbcTempalte` 은 `auto commit`을 default로 하고 있기 때문에 트랜잭션의 경계를 구현해주어야 한다
  ```text
       service -> dao(jdbcTemplate) -> DB
               <- (Connection 생성)  <-                                      
  ```
- JDBCTemplate은 Connection을 생성하여 메서드 호출 종료 시점에 connection을 반환한다

#### 비즈니스 로직 내의 트랜잭션 설정하기
- case1. 비즈니스 로직을 jdbcTempalte을 사용하는 메서드로 합친다
  - 비즈니스 성격과 책임의 분리가 제대로 이루어지지 않는 단점이 발생한다
- case2. 비즈니스 로직은 분리하되 Connection을 공유한다
  - 트랜잭션 경계 내의 jdbcTemplate 메서드들에게 모두 `connection` 을 공유해야 한다
  - `connection` 을 사용하지 않는 JPA나 하이버네이트로 변경할 경우 구현 방식이 변경되어야 함(추상화 필요성)

#### 트랜잭션 동기화
- TransactionSynchronizations(트랜잭션 동기화 저장소) 사용하기
  ```text
  public void upgradeLevelsTx() throws Exception {
     TransactionSynchronizationManager.initSynchronization();
     Connection c = DataSourceUtils.getConnection(dataSource);
     c.setAutoCommit(false);
    
     try {
         List<User> users = userDao.getAll();
         for (User user : users) {
             if (canUpgradeLevel(user)) {
                  upgradeLevel(user);
             }
         }
         c.commit();
     } catch (Exception e) {
         c.rollback();
         throw e;
     } finally {
         DataSourceUtils.releaseConnection(c, dataSource);
         TransactionSynchronizationManager.unbindResource(this.dataSource);
         TransactionSynchronizationManager.clearSynchronization();
     }
  }
- Connection 파라미터 전달 필요가 없음
- 트랜잭션 동기화 작업으로 멀티스레드 환경에서도 안전하다
- 동작 방식
  1. Service에서 Connection을 생성한다
  2. 생성한 Connection을 트랜잭션 동기화 저장소에 저장해둔 후 Auto Commit을 `false` 로 설정한다
  3. dao의 메서드를 호출하여 내부의 jdbcTemplate 메서드가 실행된다
  4. jdbcTemplate메서드는 트랜잭션 동기화 저장소에 connection 오브젝트 존재여부를 파악한다
  5. connection을 가져와서 SQL QUERY를 수행하고 connection 닫지 않은 채로 작업을 마친다
  6. 다음 jdbcTemplate메서드가 4-5번의 작업을 반복한다
  7. 모든 작업이 끝났으면 userService는 `commit()`으로 트랜잭션을 종료한다

#### 트랜잭션 서비스 추상화
- 기술과 환경 종속적인 트랜잭션 경계설정 코드 
  - `TransactionSynchronizations`은 로컬 트랜잭션(하나의 DB)에서 동작한다
  - 글로벌 트랜잭션에 대한 분산 트랜잭션 관리(JTA)
    - JTA(Java Transaction API)는 여러개의 DB 또는 메시징 서버에 대해 트랜잭션을 관리한다(11장에서 학습)
    ```text
    InitialContext ctx = new InitialContext();
    UserTransaction tx = (UserTransaction)ctx.lookup(USER_TX_JNDI_NAME);
  
    tx.begin();
    Connection c = dataSource.getConnection(); //jndi로 가져온 dataSource
    try {
        ...
        tx.commit();
    } catch (Exception e) {
        tx.rollback();
    } finally {
        c.close();
    }
    ```
    - connection 사용으로 인해 JDBC와 마찬가지로 기술 환경 종속적이다
    - UserService가 UserDaoJdbc, UserDaoHibernate 를 의존하게 됨
    

- 스프링의 트랜잭션 추상화 API
  ```text
   public void upgradeLevelsTxAbstract() {
          final TransactionStatus status =
                  this.transactionManager.getTransaction(new DefaultTransactionDefinition());
  
          try {
              List<User> users = userDao.getAll();
              for (User user : users) {
                  if (canUpgradeLevel(user)) {
                      upgradeLevel(user);
                  }
              }
              this.transactionManager.commit(status);
          }
          catch (RuntimeException e) {
              this.transactionManager.rollback(status);
              throw e;
          }
      }
  ```
  ```text
  인터페이스 구조 
                   PlatformTransactionManager 
       DataSourceTxManager            HibernateTxManager
  JDBC/Connection, JTA/UserTrsaaction       Hibernate/Trsancation
  ```
- 스프링의 DI를 통해 트랜잭션 매니저를 추상화하여 유연하게 사용이 가능하다