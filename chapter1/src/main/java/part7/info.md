### 정리
- 결합도가 낮은 의존 관계는 변화에 영향을 덜 받는다. (인터페이스에 대한 의존관계 설정을 사용하자)
- 의존 관계 주입과 의존 관계 검색을 통해 빈의 의존 관계를 설정할 수 있다

#### 의존관계 검색과 주입
 - 의존 관계 검색: 빈 컨테이너에서 빈을 찾는 방법 `ApplicationContext.getBean()` 
 - 의존 관계 주입: 객체에 의존 관계를 주입하는 방법(생성자, 수정자, 일반 주입)
 - 의존 관계 주입이 단순한고 깔끔하다.