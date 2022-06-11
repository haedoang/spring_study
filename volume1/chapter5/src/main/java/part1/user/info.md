### 사용자 레벨 관리 기능 추가
  - 비즈니스 로직을 `service`로 구현한다. 
  - 타입을 매핑할 때에는 `enum`을 활용하도록 하자(type safety, 싱글톤 인스턴스)
  - 기능 별로 분리한다
    1) Domain: 도메인 객체에 대해 검증, Spring 사용 필요 없음
    2) Data Access Object: DB CRUD 검증.
    3) Service: 비즈니스 로직 검증. 
  - 매직 넘버 대신 상수를 사용한다
  - 테스트 시 경계값을 사용하여 테스트할 것
   

#### 비즈니스 로직에 변경이 일어나는 경우에는 ?
 - 이벤트를 한다고 해서 정책을 변경하는 로직이 추가 된다고 한다면 어떻게 할까?
 - 코드를 변경하고 이벤트가 종료될 시점에 다시 원래 코드로 수정하는 것은 번거롭고, 위험하다
 - 정책 인터페이스를 사용해보자
   ```java
    public intrerface UserLevelUpgradePolicy {
        boolean canUpgradeLevel(User part1.user);
        void upgradeLevel(User part1.user);
    }
    ``` 
   - 정책 인터페이스를 구현한 객체를 DI를 통해 유연하게 구현할 수 있다.