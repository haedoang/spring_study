### 정리

#### @RunWith
 - 스프링의 테스트 컨텍스트 컨텍스트 확장 기능을 추가할 수 있다.
 - `@RunWith(SpringJUnit4ClassRunner.class` JUnit 확장 기능을 추가하는 옵션

#### @ContextConfiguration 
 - 테스트 컨텍스트가 `공유`할 applicationContext의 위치를 지정할 수 있다.

#### 테스트 메소드간 컨텍스트 공유
 - 이전 장에서 JUnit은 메소드가 격리된 어플리케이션 컨텍스트를 가진다고 학습하였다.
 - 컨텍스트를 공유하게 되면 매 테스트 시 컨텍스트를 생성하지 않아도 되서 테스트 코드가 빠르게 수행된다
 - `@ContextConfiguration` 어노테이션을 사용한다
 - 메소드 간/클래스 간 컨텍스트 공유를 허용한다
 
#### @Autowired
 - 해당 어노테이션은 컨텍스트가 빈을 자동으로 찾아서 생성해준다. 
 - 스프링 어플리케이션 컨텍스트는 초기화 시 자신도 빈으로 등록한다
 - 따라서 스프링 어플리케이션 컨텍스트 내의 빈들을 `@Autowired`로 DI를 받을 수 있다.

#### 테스트 코드에 의한 DI
 - 컨텍스트 내에서 생성된 빈을 사용하지 않고 강제로 변경할 수 있다.
 - 테스트 내에서 빈을 직접 생성한 경우 컨텍스트에 정의된 빈 대신 생성할 수 있다.
 - `@DirtiesContext` 로 컨텍스트 내 빈이 강제로 변경되었음을 명시해주어야 한다
 - `빈의 의존관계를 강제로 변경하는것`은 문제를 발생할 수 있다.

#### 별도의 DI 설정
 - ApplicationContext를 분리하는 방법을 사용한다
 - `@ContextConfiguration` 내 사용할 컨텍스트를 지정하여 사용한다

#### 스프링 컨테이너 없이 테스트 사용하기 
 - 빈을 직접 주입해주는 방법으로 테스트 수행 시 직접 DI를 해주는 방법이다
 - 직접 등록하는 방법이므로 속도가 빠르다
 - 복잡한 오브젝트 연관관계가 있을 경우 사용이 좋지 않은 단점이 있다.

### 정리
 - 다양한 테스트 코드 작성 방법을 학습하였다
 - 우선적으로 스프링 컨테이너 없이 작성이 가능한 경우 해당 방법을 사용하자