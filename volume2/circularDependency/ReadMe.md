## 스프링 순환 참조 

- Spring 생성자 Dependency Injection 도중 객체와 객체 사이에서 참조가 발생하는 경우 순환 참조 오류가 발생한다
  - `BeanCurrentlyInCreationException` 예외 발생


### 다양 순환 참조 해결 방법

#### 1. @Lazy 
- 순환 참조가 발생하는 빈의 지연 초기화를 사용하는 방식
- `@Lazy`가 없는 빈은 즉시 초기화가 발생하며 존재하는 빈의 경우 호출에 의해 초기화된다


#### 2. Setter Injection 
- 스프링 문서에서 추천하는 방식
- 빈을 생성하지만 필요할 때까지 DI가 발생하지 않기 때문에 순환참조가 발생하지 않는다

#### 3. @PostConstruct
- `@PostConstruct` 생성자 호출 이후의 객체 initialization 작업을 수행할 수 있는 애너테이션
- 참조 방식을 해당 애너테이션 내에서 수행하는 방법
- cf) `@PostConstruct` vs `InitializingBean` 
  - `@PostConstruct`는 초기화 메서드 이전에 호출된다
  - `@PostConstruct` -> `@afterPropertiesSet` -> `init-method` 순으로 호출

#### 4. ApplicationContextAware, InitializingBean 
- 빈 내에서 직접 context의 빈을 추출하는 방식. DI를 수동으로 설정한다
- 개인적으로 코드 가독성이 좋지 않은 것 같다


