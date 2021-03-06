## 애플리케이션 아키텍처

### 계층형 아키텍처
- 책임과 성격이 다른 것을 그룹화한 아키텍처
- ##### 아키텍처와 SoC
    - 애플리케이션을 구성하는 오브젝트들을 비슷한 성격과 책임을 가진 것들끼리 묶을 수 있다
    - 계층형 아키텍처, 멀티 티어 아키텍처, 3계층 애플리케이션 이라고 한다
- ##### 3계층 아키텍처와 수직 계층
    ```text
            클라이언트 < - > 프레젠테이션 계층 < - > 서비스 계층 < - > 데이터 액세스 계층 < - > DB/레거시
                            (웹 계층)         (매니저 계층)       (DAO 계층)
                            (UI 계층)        (비즈니스 로직 계층)  (EIS 계층)
                            (MVC 계층)  
    ```
    - 데이터 액세스 계층
        - DAO 계층, EIS 계층이라고 불린다
        - 외부 시스템을 호출해서 서비스를 이용하는 `infrastructure` 계층으로 따로 분류하기도 한다
        - 데이터 액세스 세분화 계층 구분
            - 수직적인 계층 구조 
            - `jdbcTemplate` 추상화를 위한 계층으로 사용하여 `JDBC`, `트랜잭션 추상화`의 간접 이용
            - 추상화 계층 구조를 통해 접근하도록 하는 것이 바람직하다 
        - 새로운 추상 계층이 추가되는 경우 애플리케이션 코드에 영향을 미치게 되니 주의해야 한다
    - 서비스 계층
        - 특별한 경우가 아니라면 추상화 수직 계층구조를 가질 필요가 없음
        - `infrasctructure` 계층에서 `service` 계층의 오브젝트를 호출하는 경우 ex) 스케줄링
        - 원칙적으로는 서비스 계층 코드가 서비스 계층 구현에 종속되면 안된다
        ```text
               서비스 계층         ----->  데이터 액세스 계층 
            (POJO 서비스 코드)                  
                 ↑  ↓                         ↓
                 기반 서비스 계층
                (트랜잭션, 보안, 리모팅, 메일, 메시징, 스케줄링 )
        ```
    - 프레젠테이션 계층
        - 가장 복잡한 구조
        - 자바에서 HTTP 프로토콜을 처리하는 가장 기본 엔진인 `서블릿 기술`을 바탕으로 한다

- ##### 계층형 아키텍처 설계의 원칙
    - 각 계층은 자신의 계층의 책임에만 충실해야 한다
    - 각 계층은 자신의 역할에만 충살해야 하고 자신과 관련된 기술이 아닌 다른 기술 API 사용을 삼가야 한다
    ```java
        public ResultSet findUsersByName(String name) throws SQLException //ng 
        public List<User> findUsersByName(String name) throws DataAccessException; //ok
    ```
    - 계층의 경계를 넘어갈 때에는 반드시 특정 계층에 종속ㅈ되지 않는 오브텍트 형태로 변환해야 한다
    - 계층 사이의 낮은 결합도를 깨뜨리지 않도록 설계햐아 한다(추상화)

### 애플리케이션 정보 아키텍처
- 엔터프라이즈 시스템은 동시에 많은 작업이 빠르게 수행돼야 하는 시스템이다. 
- 주요 상태정보는 `DB`나 메인프레임 같은 `EIS` 백엔드 시스템에 저장된다.

- ##### DB/SQL 중심의 로직 구현 방식
    - 하나의 업무 트랜잭션에 모든 계층의 코드가 종속되는 경향
    - 대용량 데이터를 다루면서 빠른 처리가 필요한 경우 일부 로직을 `PT/SQL` 저장 프로시저로 만들기도 한다
    - 개발이 쉬운 장점이 있으나 변화에 유연하지 못하다
    - 객체지향의 장점을 활용하지 못하며 확장성이 떨어진다
    - 테스트하기 힘들다

- ##### 거대한 서비스 계층 방식
    - 주요 로직을 서비스 계층의 코드에서 처리하는 방식
    - 비즈니스 로직이 복잡해지면 서비스 계층의 코드도 매우 복잡해지고 커진다
    - 애플리케이션 코드에 비즈니스 로직이 담겨있기 때문에 자바 코드를 활용할 수 있고 테스트가 수월하다
    - `DAO` 코드의 재사용성이 가능하다
    - 독립적인 개발이 가능하여 초기 개발 속도가 빠르다
    - `SQL`은 서비스 계층의 비즈니스 로직에 따라 만들어지므로 계층 간의 결합도가 높은 단점이 있다
    - 코드의 중복이 많아지기 쉽고 장기적으로 관리하기 어렵다

### 오브젝트 중심 아키텍처
- 도메인 모델을 반영하는 오브젝트 구조를 만들어서 계층 사이에서 전송하는 방식
- ##### 데이터와 오브젝트
    - 자바 레퍼런스 변수를 사용하여 RDB 테이블 간의 관계를 참조할 수 있다
    - `Map` 구조로는 순서와 변화에 영향을 취약한데 오브젝트 구조는 변화에 유연하다 
    - 자바 언어의 특성을 최대한 활용하여 정보를 가공할 수 있다

- ##### 도메인 오브젝트를 사용하는 코드
    ```java
        public int calcTotalOfProductPrice(Category cate) {
            int sum = 0;
            for (Product prod : cate.getProducts()) {
                sum += prod.getPrice();
            }
            return sum;
        }
    ```
    - 테스트가 수월하고 재사용성이 높다
    - 변화에 유연하다

- ##### 도메인 오브젝트 사용시 문제점
    - 최적화된 SQL에 비해 성능이 떨어진다
    - 조회 조건없는 오브젝트를 참조하는 경우 `NullPointerException` 이 발생할 수 있다
    - `지연 로딩`을 통해 문제점을 해결할 수 있다
    - `JPA`, `JDO`, `하이버네이트` 등 오브젝트 매핑 기술을 사용하는 것이 이상적이다

- ##### 빈약한 도메인 오브젝트 방식
    - 클래스는 `속성`과 `행위`의 조합이다
    - 도메인 오븢게트에 정보만 담겨 있고 정보를 활용하는 기능이 없는 경우 `빈약한 도메인 오브젝트` 라고 한다
    - 비즈니스 로직이 `서비스 계층`에 존재한다
    - 비즈니스 로직이 복잡하지 않다면 가장 만들기 쉬운 아키텍처 구조이다

- ##### 풍성한 도메인 오브젝트 방식
    - 풍성한 도메인 오브젝트, 영리한 도메인 오브젝트
    - 비즈니스 로직을 담고 있는 도메인 오브젝트를 말한다
    ```java
        public class Category {
            List<Product> products;

            public int calcTotalOfProductPrice() {
                int sum = 0;
                for (Product prod : this.products) {
                    sum += prod.getPrice();
                }
                return sum;
            }
        } 
    ```
    - 응집도가 높다
    - 비즈니스 로직이 간결하고 이해하기 쉽다

- ##### 도메인 계층 방식
    - 변경된 정보를 다시 `DB` 등에 반영하려면 서비스 계층 오브젝트의 부가적인 작업이 필요하다
    - 도메인 계층 역할과 비중을 극대화하기 위해서는 `풍성한 도메인 오브젝트` 방식으로는 부족하다
    - 도메인 오브젝트 들이 하나의 독립적인 계층을 이루어 계층 사이에 존재하는 방식
    - 기존 방식과 차이점
        1. 도메인 종속적인 비즈니스 로직 처리는 도메인 계층의 오브젝트에서 진행
        2. 도메인 오브젝트가 기존 데이터 액세스 계층이나 기반 계층의 기능을 직접 활용
    - 도메인 오브젝트는 싱글톤 빈이 아니기 때문에 DI를 사용하여 빈으로 등록해야 한다
    - 문제점 2가지
        1. 모든 계층에서 도메인 오브젝트를 사용하는 경우 비즈니스 로직 호출의 위험이 따름
            - `AspectJ 정책/표준 강제화` 사용
        2. 도메인 오브젝트는 도메인 계층에서만 사용하도록 한다
            - `DTO` 사용으로 도메인을 외부 계층으로부터 보호할 수 있다
            - `DTO`는 일회성이며 생명주기 또한 짧다 
            - `DTO`는 독립된 상태를 유지해야 하기 때문에 싱글톤이 될 수 없으며 빈으로 등록할 수 없다
    - 응집도가 매우 높기 때문에 단위 테스트를 작성하기 편리하다

- ##### DTO와 리포트 쿼리
    - 리포트 쿼리는 대표적인 DTO 사용이 필요한 예이다
    - DB의 쿼리 실행 결과를 담는 경우 DTO 또는 Map에 담아서 전달한다


### 스프링 애플리케이션을 위한 아키텍처 설계
- ##### 계층형 아키텍처
    - 3계층 구조는 스프링에서 사용하는 엔터프라이즈 애플리케이션에서 가장 많이 사용되는 구조다
    - 계층 간의 통합 또한 가능하다
    - 스프링은 MVC 패턴을 지원한다 
- ##### 정보 전송 아키텍처
    - 스프링 기본 기술에 가장 잘 맞고 쉽게 적용하는 방식은 오브젝트 중심 아키텍처의 도메인 오브젝트 방식이다
    - 빈약한 도메인 오브젝트 방식으로 시작하여 기능을 추가하는 방향으로 설계한다

- ##### 상태 관리와 빈 스코프
    - 아키텍처 설계에서 신경써야할 것은 `상태 관리`이다 
    - 서버 기반 애플리케이션은 기본적으로 `stateless` 방식이다
    - 스프링은 기본적으로 상태 유지되지 않는 빈과 오브젝트를 권장한다
    - 하지만 `Http Session`의 사용, `stateful` 애플리케이션 등 특징에 따라 얼마든지 사용할 수 있다

- ##### 서드파티 프레임워크, 라이브러리 적용
    - 스프링이 지원하는 기술이란?
        1. 해당 기술을 스프링의 DI 패턴을 따라 사용할 수 있는지 
        2. 스프링의 서비스가 추상화가 적용되었는지 
        3. 스프링이 지지하는 프로그래밍 모델을 적용하였는지
        4. 템플릿/콜백을 지원하는지 


        