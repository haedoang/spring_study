### 정리 
 - 스프링의 JdbcTemplate


#### JdbcTemplate
 - update(): PreparedStatementCreator 타입의 콜백 사용, 내장 콜백으로 sql만 사용 가능 
 - queryForInt(): 조회 시 resultSet을 활용하는 메서드. PreparesStatementCreator, ResultSetExtractor 콜백 사용
 - queryForObject(): 객체 반환 메서드. rowMapper를 사용한다.(자동으로 ResultSet의 next()를 실행한다), 단일 로우를 예상한다
 - query(): 다중 로우를 예상한다. 제네릭스를 활용하여 다양한 타입을 반환할 수 있게 사용한다


### 3장 정리
 - 공유 리소스의 반환(AutoCloseable 구현 객체)이 필요한 코드는 try/catch/finally 블록으로 관리할 것
 - 일부 기능이 바뀌는 코드가 존재한 경우 전략 패턴을 적용한다. 
   - 바뀌지 않는 부분은 `컨텍스트`, 바뀌는 부분은 `전략`, 인터페이스를 통해 전략을 유연하게 변경한다
 - 같은 애플리케이션 내에서 다양한 전략을 사용한다면 `컨텍스트`를 이용하는 클라이언트 메서드에서 `전략`을 정의하고 제공한다
 - 클라이언트 메서드 내에 익명내부 클래스를 사용하여 전략 오브젝트를 구현할 수 있다
 - 컨텍스트가 하나 이상의 클라이언트 오브젝트에서 사용된다면 클래스를 분리해서 공유 한다 
 - 스프링에서 지원하는 템플릿/콜백 패턴은 중복 코드를 줄이고, 재활용이 용이하다
 - 템플릿은 하나 이상의 콜백을 사용할 수 있고, 하나의 콜백을 여러 번 호출 할 수 있다.
 - jdbc를 사용할 것이라면 Spring 의 JdbcTemplate을 사용하자

