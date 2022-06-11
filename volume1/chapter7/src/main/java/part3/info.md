## 서비스 추상화 적용
- JAXB 이외에 다양한 XML과 자바오브젝트를 매핑하는 기술이 있음 => 서비스 추상화 대상
- XML 파일을 더 다양한 소스에서 가져올 수 있게 한다 => 추상화 대상

### OXM 서비스 추상화
- XML과 자바 오브젝트 매핑 기술 => Object-XML Mapping
  - Castor XML: 설정파일이 필요 없는 인트로스펙션 모드를 지원하기도 하는 매우 간결하고 가벼운 바인딩 프레임워크
  - JiBX: 뛰어난 퍼포먼스를 자랑하는 XML 바인딩 기술
  - XmlBeans: 아파치 XML 프로젝트의 하나. XML의 정보셋을 효과적으로 제공
  - Xstream: 관례를 이용해서 설정이 없는 바인딩을 지원하는 XML 바인딩 기술

#### ORM 서비스 인터페이스
> org.springframework.oxm.Unmarshaller, Marshaller

#### JAXB, Castor 구현 테스트 
- OXM 서비스 추상화 구현 빈 설정
    ```xml
        <!-- CASTOR --> 
        <bean id="unmarshaller" class="org.springframework.oxm.castor.CastorMarshaller">
            <property name="mappingLocation" value="mapping.xml"/>
        </bean>
    ```
    ```xml
        <!-- JAXB -->
        <bean id="unmarshaller" class="org.springframework.oxm.jaxb.Jaxb2Marshaller">
            <property name="contextPath" value="part3.user.sqlservice.jaxb"/>
        </bean>
    ```

### OXM 서비스 추상화 적용

#### 멤버 클래스를 참조하는 통합 클래스
- OxmSqlService => Oxm 기능을 사용하는 두 객체를 강하게 결합하기 위해 내부 클래스로 사용
  ```text
                    (포함)                                            (interface)
     [ OxmSqlService -----> OxmSqlReader ] ------------------------> Unmarshaller
            |                                                             |       
            |                (interface)                                  ↓
            ---------------> SqlRegistry  -------------------------> 각 인터페이스의 구현 클래스를 빈으로 등록하고 
                                                                     OxmSqlService 프로퍼티가 참조하게 한다 
  ```

#### 위임을 이용한 BaseSqlService의 재사용
- BasicSqlService 재사용
  ```text
                          (interface)
            ----------->   SqlService  <-----------------
            |                                 (작업 위임) |                       
      OxmSqlService -------------------------->  BaseSqlService(⭐️) 
            |     ↘︎                           ↙        | OxmSqlService를 통해서 초기화된 오브젝트를 참조해서 동작
            |       ↘  ︎︎                     ↙          |
            |             OxmSqlReader                 |
            |                                          |
            |             (interface)                  |
            ----------->  SqlRegistry  <----------------
  ```


### 리소스 추상화
- XML 파일의 위치가 현재로서는 클래스패스 경로로 제한되어 있음

#### 리소스
- 스프링은 자바에 존재하는 일관성 ㅇ벗는 리소스 접근 API를 추상화하여 인터페이스 형태로 제공한다 `Resource`
  > org.springframework.core.io.Resource

#### 리소스 로더
- 문자열로 정의된 리소스를 실제 `Resource` 타입 오브젝트로 변환해주는 역할을 한다
  > org.springframework.core.io.ResourceLoader
  - 접두어 
    - `file:` : 파일 시스템  
    - `classpath:` : 클래스패스 
    - `없음` : ResourceLoader 구현에 따라 결정
    - `http:` : HTTP 프로토콜
  
#### Resource를 이용해 XML 파일 가져오기 
- classpath 접두어 
  ```xml
  <bean id="sqlService" class="part3.user.sqlservice.OxmSqlService">
    <property name="unmarshaller" ref="unmarshaller"/>
    <property name="sqlmap" value="classpath:/sqlmap.xml"/>
  </bean>
  ```
- file 접두어
  ```xml
  <bean id="sqlService" class="part3.user.sqlservice.OxmSqlService">
    <property name="unmarshaller" ref="unmarshaller"/>
    <property name="sqlmap" value="file:/opt/resources/sqlmap.xml"/>
  </bean>
  ```
  
- http 
  ```xml
  <bean id="sqlService" class="part3.user.sqlservice.OxmSqlService">
    <property name="unmarshaller" ref="unmarshaller"/>
    <property name="sqlmap" value="http://www.epril.com/resources/sqlmap.xml"/>
  </bean>
  ```
