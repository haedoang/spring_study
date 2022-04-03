## 정리
- 스프링 DI, 서비스 추상화 등을 으용ㅇ해 새로운 SQL 서비스 기능을 설계하고 개발한 뒤 점진적으로 확장, 발전하는 방법을 알아봤다
- 스프링이 제공해주는 내장 기능에만 만족하지 말고 **스프링의 기반 기술을 자유자재로 활용할 수 있도록 다양한 시도**를 해야 한다
- SQL처럼 변경될 수 있는 텍스트로 된 정보는 외부 리소스에 담아두고 가져오게 만들면 편리하다
  - sqlmap.xml
- 성격이 다른 코드가 섞 있는클래스라면 먼저 인터페이스를 정의해서 인터페이스 별로 분리하는게 좋다.
  - 빈의 참조는 인터페이스를 통해 DI하여 느슨한 객체 연관 관계를 가져 확장성을 높여야 한다
- 자주 사용되는 의존 오브젝트는 디폴트로 정의해두면 편리하다
  - DefaultSqlService
- XML과 오브젝트 매핑은 스프링의 OXM 추상화 기능을 활용한다
- 특정 의존 오브젝트를 고정시켜 기능을 특화하려면 멤버 클래스로 만드는 것이 편리하다 <br/>
  기존에 만들어진 기능과 중복되는 경우 위임을 통해 중복을 제거하는 게 좋다
  - BaseSqlService
- 외부의 파일이나 리소스를 사용하는 코드에서는 스프링의 리소스 추상화와 리소스 로더를 사용한다
  - `org.springframework.core.io.Resource`
  - `org.springframework.core.io.ClassPathResource`
- DI를 의식하면서 코드를 작성하면 객체지향 설계에 도움이 된다
- DI에는 인터페이스를 사용한다. 인터페이스를 사용하면 인터페이스 분리 원칙을 잘 지키는데 도움이 된다
- 클라이언트에 따라서 인터페이스를 분리할 때 새로운 인터페이스를 만드는 방법과 인터페이스를 상속하는 방법 두 가지를 사용할 수 있다
- 애플리케이션에 내장하는 DB를 사용할 떄는 스프링의 내장형 DB 추상화 기능과 전용 태그를 사용하면 편리하다
  - `org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder`
  