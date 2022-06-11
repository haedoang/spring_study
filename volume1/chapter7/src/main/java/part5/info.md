## DI를 이용해 다양한 구현 방법 적용하기

### ConcurrentHashMap을 이용한 수정 가능 SQL 레지스트리
- CuncurrentHashMap: 동기화된 해시 데이터 조작에 최적화된 컬렉션 
  - 데이터 조작 시 전체 데이터에 대해 락을 걸지 않고, 조회는 락을 사용하지 않는다.
  - 멀티스레드 상에서 동시성 보장 
  
### 내장형 데이터베이스를 이용한 SQL 레지스트리 만들기
- 애플리케이션에 내장돼서 애플리케이션과 함께 시작되고 종료되는 DB를 말한다
- IO로 인해 발생하는 부하가 적기 때문에 성능이 뛰어나다

#### 스프링의 내장형 DB 지원 기능
- 내장형 DB 빌더를 제공하여 내장형 DB를 초기화한다
- `HSQL`, `H2`, `DERBY` 지원
- 내장형 DB는 애플리케이션 안에서 직접 DB 종료를 요청할 수도 있어야 한다
- `DataSource` 인터페이스를 상속하여 shutdown() 메소드를 추가한 `EmbeddedDatabase` 인터페이스를 사용한다

### 트랜잭션 적용

#### 코드를 이용한 트랜잭션 적용
- SimpleJdbcTemplate을 통해 JDBC를 처리하고 있으므로 스프링의 트랜잭션 추상화 서비스를 적용할 수 있다
  - `PlatformTransactionManager` 
  - `TransactionTemplate`: 템플릿/콜백 패턴의 트랜잭션
  ```java
  transactionTemplate.execute(new TransactionCallbackWithoutResult() {
      @Override
      protected void doInTransactionWithoutResult(TransactionStatus status) {
          for (Map.Entry<String, String> entry : sqlmap.entrySet()) {
              updateSql(entry.getKey(), entry.getValue());
          }
      }
  });
  ```


#### 내장형 DB의 트랜잭션 격리수준 
- HSQL 1.8버전 이하: READ_UNCOMMITTED 
- HSQL 1.9 이상, H2, Derby : READ_COMMITTED
- `READ_UNCOMMITTED`: 한 트랜잭션이 종료되기 전에 변경한 정보를 잃어버렸는데 해당 트랜잭션이 롤백돼버리면 실제로는 DB에 반영되지 않은 유효하지 않은 데이터가 사용된다