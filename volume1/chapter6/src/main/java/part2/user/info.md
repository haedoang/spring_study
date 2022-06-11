### 고립된 단위 테스트
- 가장 좋은 테스트 방법은 가능한 작은 단위로 쪼개서 테스트 하는 것이다
- 테스트 단위가 작아야 테스트의 의도나 내용이 분명해지고, 만들기도 쉬워진다.

#### 복잡한 의존관계 속의 테스트
- 아래처럼 3개의 의존관계를 가지고 있는 UserService는 테스트에 대한 비용이 크다
- 특정 기능만을 테스트하는 경우 테스트가 많이 비효율적일 것이다.
    ```text
        
        UserServiceTest  ------ UserService ---- UserDaoJdbc --> XXDataSource --> DB 
                                            ---- TransactionManager ↗
                                            ---- JavaMailSenderImpl --> JavaMail --> MailServer
    ```

#### 테스트 대상 오브젝트 고립시키기
- 테스트의 대상이 환경이나, 외부서버, 다른 클래스의 콛드에 종속되고 영향을 받지 않도록 고립시켜야 한다
- `MockMailServer`, `MockUserDao` 클래스가 그 역할을 하고 있다
  - MockUserDao: DB 역할을 대신 수행하는 목 오브젝트
  - MockMailServer: MailServer 역할을 대신 수행하는 목 오브젝트


#### 단위 테스트와 통합 테스트 
- 단위테스트: 의존 오브젝트나 외부의 리소스를 사용하지 않도록 `고립`시켜 테스트하는 것
- 통합테스트: 외부의 DB나 파일, 서비스 등의 리소스가 참여하는 테스트
- 항상 단위 테스트를 먼저 고려하도록 한다(테스트는 성능이 중요하기 때문이다)
- 외부 리소스를 사용해야할 때는 통합테스트로 만든다
- DAO테스트는 DB라는 외부 리소스를 사용하기 때문에 통합테스트로 분류한다
- 스프링 테스트 컨텍스트는 통합테스트이다

#### 목 프레임워크
- Mockito 프레임워크 
  - 직관적이고 편리하다
  - 호출되었을때(when), 리턴할 값(thenReturn)을 지정할 수 있다
  - 메서드가 몇번 호출되었는지(verify(mockUserDao), times(2)).update(any(User.class)) 확인할 수 있다