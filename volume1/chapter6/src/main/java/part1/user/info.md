### 트랜잭션 코드의 분리 
- AOP의 적용 대상은 선언전 트랜잭션 기능이다. AOP를 활용하면 트랜잭션 경계설정을 보다 깔끔하게 설정할 수 있다
- AOP도입에 대한 이유에 대해 학습한다


#### 트랜잭션 코드의 분리
 - 비즈니스 로직과 트랜잭션 로직이 별개로 동작한다
   - solution1. 메소드로 분리한다
       ```text
          public void upgradeLevels() {
              final TransactionStatus status =
                      this.transactionManager.getTransaction(new DefaultTransactionDefinition());
    
              try {
                  upgradeLevelsInternal();
                  this.transactionManager.commit(status);
              } catch (RuntimeException e) {
                  this.transactionManager.rollback(status);
                  throw e;
              }
          }
    
          private void upgradeLevelsInternal() {
              List<User> users = userDao.getAll();
              for (User user : users) {
                  if (canUpgradeLevel(user)) {
                      upgradeLevel(user);
                  }
              }
   - solution2. DI를 이용해 클래스를 분리한다 => 트랜잭션 로직에서 비즈니스 로직을 호출하여 트랜잭션 경계를 설정
   ```text
        Client    ------------   UserService(interface)
   (UserServiceTest)                     |
                            ----------------------------
                            |                          |
                    UserServiceImpl               UserServiceTx 
                    (비즈니스 로직 담당)             (트랜잭션 로직 담당)
   ```
   - 비즈니스 로직을 담당하는 `userServiceImpl` 에서 트랜잭션에 대해 신경쓰지 않아도 된다(기술적인 내용 포함)
   - 트랜잭션 필요 여부도 비즈니스 로직에서 담당하지 않고, 트랜잭션 로직에서 담당한다(OCP)
   - 비즈니스 로직에 대해 테스트를 쉽게 만들 수 있다 => 다음 장에서 