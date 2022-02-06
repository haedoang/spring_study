### 정리
- Spring을 적용하여 오브젝트 팩토리 방식의 객체 생성에서 빈팩토리와 빈 컨테이너로 변경하였다.

#### 용어 정리 
- Bean : 스프링이 IoC 방식으로 관리하는 오브젝트.
- Bean Factory: 스프링의 IoC를 담당하는 핵심 컨테이너, 빈을 등록, 생성, 조회, 반환과 빈의 관리를 담당한다. 
- Application Context: 빈 팩토리를 확장한 IoC컨테이너. 빈 팩토리의 기능과 스프링이 제공하는 부가기능등을 담당한다.
- Configuration: IoC 컨테이너에 의해 관리되는 애플리케이션 오브젝트를 생성하고 구성할 때 사용ㄹ한다.

#### 어플리케이션 컨텍스트 동작 순서
1. Client: 빈 요청 (userDao)
2. Application Context: 빈 목록에서 빈 조회(getBean()) 
3. Bean Factory: 조회된 빈을 생성하고 빈 컨테이너에 등록한다
4. Client: 생성된 빈을 반환받는다.