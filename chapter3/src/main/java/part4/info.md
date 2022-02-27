### 정리 
- UserDao에서 preparedStatment를 반환하는 컨텍스트를 메소드로 구현하였다.
- 이 컨텍스트를 외부에서(다른 Dao)도 사용할 수 있게 클래스를 분리할 수 있다 => `JdbcContext`
- 이렇게 생성된 클래스를 주입받는 것은 2가지 방법이 있다.
- 두 가지 모두 장/단점이 존재하기 때문에 적절하게 사용하면 될 것이다.

#### 1. 빈으로 등록하기
 - 장점
   - 빈으로 등록하면 싱글톤 객체로 사용이 가능하다
   - 의존 관계가 명확하게 드러난다
 - 단점
   - DI의 근본적인 원칙에 부합하지 않는다(인터페이스를 통한 의존성 주입이 아니기 떄문에 의존도가 높다)
 
#### 2. 수동으로 DI 사용하기
 - 빈이 수정자 메소드를 통해 DI 주입을 받을 떄 수동으로 클래스를 생성한다
 - 장점 
   - 의존 관계가 외부에 드러나지 않아서 DI 전략을 외부에 감출 수 있다
 - 단점 
   - 싱글톤으로 만들 수 없고, DI를 위한 부가적인 작업이 필요하다
   