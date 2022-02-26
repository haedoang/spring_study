### 서비스 추상화와 단일 책임 원칙
- 계층과 책임의 분리 
    ```text
            UserService             UserDao
                 |                     |
      TransactionManager(추상화)   DataSource(추상화)
                 |                     |
         JDBC, JTA, Connection Pooling, JNDI, WAS,DB
    
    ```

#### 단일 책임 원칙
- 하나의 모듈은 하나의 책임을 가져야 한다. 즉, 변경의 이유가 `하나`이어야 한다는 뜻이다.
- 추상화를 통해 단일 책임 원칙을 구현할 수 있다
  - UserService(TransactionManager => 트랜잭션 처리 위임, 비즈니스 로직만 수행)
  - UserDao(DataSource => Connection 등 DB 처리 위임, Data Access 로직만 수행)
- 변경이 필요할 때 수정의 대상이 명확해진다.
- 스프링은 추상화 객체를 DI를 통해 단일 책임 원칙을 지킬 수 있게 지원한다
- 다양한 디자인 패턴 등이 단일 책임 원칙을 지키도록 설계 하고 있다
- 이러한 디자인 패턴은 지속적인 학습과 노력이 필요하다(이론만 학습한다고 되는 것이 아님. 적용하려 노력할 것)