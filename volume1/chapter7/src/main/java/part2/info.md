## 인터페이스의 분리와 자기참조 빈

### XML 파일 매핑

#### JAXB(Java Architecture for XML Binding)

- java.xml.bind
- xml 문서정보를 거의 동일한 구조의 오브젝트로 직접 매핑해준다
- DOM과 차이점
    - DOM : XML 정보를 자바의 리플렉션 API를 사용하여 조작하는 것처럼 `간접적으로 접근`한다
    - JAXB : XML의 정보를 그대로 담고 있는 오브젝트 트리구조를 만들어준다
- XML 문서의 구조를 정의한 스키마를 이용해서 매핑할 오브젝트의 클래스까지 자동으로 만들어주는 컴파일러를 제공한다 `xjc`

#### SQL 맵을 위한 스키마 작성과 컴파일
- SQL 맵 XML 문서
  ```xml
  <sqlMap>
      <sql key="update">update users set name = ?, password = ?, level = ?, login = ?, recommend = ? where id = ?</sql>
      <sql key="delete">delete from users"</sql>
  </sqlMap>     
  ```

- SQL 맵 문서에 대한 스키마(sqlmap.xsd)
  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <schema xmlns="http://www.w3.org/2001/XMLSchema"
          targetNamespace="http://www.epril.com/sqlmap"
          xmlns:tns="http://www.epril.com/sqlmap" elementFormDefault="qualified">
  
      <element name="sqlmap">
          <complexType>
              <sequence>
                  <element name="sql" maxOccurs="unbounded" type="tns:sqlType" />
              </sequence>
          </complexType>
      </element>
      <complexType name="sqlType">
          <simpleContent>
              <extension base="string">
                  <attribute name="key" use="required" type="string" />
              </extension>
          </simpleContent>
      </complexType>
  </schema>
  ```
- JAXB 컴파일를 사용하여 스키마파일을 XML 문서 바인딩용 클래스로 생성하기
  > xjc -p par2.user.sqlservice.jaxb sqlmap.xsd -d src 
  - -p args[0]: 생성할 클래스의 패키지
  - -p args[1]: 변환할 스키마 파일 
  - -d args[0]: 생성된 파일이 저장될 위치. 소스 폴더에 추가한다

#### 언마샬링
- XML 문서를 읽어서 자바의 오브젝트로 변환하는 것을 JAXB에서 `언마샬링(unmarshalling)`이라 한다. <-> `마샬링(marshalling)`

### XML 파일을 이용하는 SQL 서비스

#### SQL 맵 XML 파일
- sqlmap.xml
  ```xml
  <?xml version="1.0" encoding="utf-8" ?>
  <sqlmap xmlns="http://www.epril.com/sqlmap"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://www.epril.com/sqlmap ../../../sqlmap.xsd">
      <sql key="add">insert into users(id, name, password, level, login, recommend) values(?, ?, ?, ?, ?, ?)</sql>
      <sql key="getAll">select * from users</sql>
      <sql key="get">select * from users where id = ?</sql>
      <sql key="getCount">select count(*) from users</sql>
      <sql key="update">update users set name = ?, password = ?, level = ?, login = ?, recommend = ? where id = ?</sql>
      <sql key="delete">delete from users</sql>
  </sqlmap>
  ```
  
#### XML SQL 서비스
- sqlmap.xml에 있는 SQL 내용을 DAO에 제공해주는 SqlService 구현 클래스를 만든다
- 어느 시점에서 JAXB를 사용해서 XML 문서를 가져올지 생각해봐야 한다 


### 빈의 초기화 작업
- 스프링은 빈 오브젝트를 생성하고 DI 작업을 수행해서 프로피터를 모두 주입해준 뒤 미리 지정한 메소드를 호출할 수 있다. `@PostConstruct`
  > <context: annotation-config />
- `@PostContruct` 는 생성자와는 달리 프로퍼티까지 모두 준비된 후에 실행된다 

### 변화를 위한 준비: 인터페이스 분리 

#### 책임에 따른 인터페이스 정의
1. SQL 정보를 외부로부터 읽어들이는 것 `SqlReader`
2. 읽어온 SQL을 보관해두고 있다가 필요할 때 제공해주는 것 `SqlRegisty`

### 자기참조 빈으로 시작하기
#### 다중 인터페이스 구현과 간접 참조
  ```text
                      SqlService
                          |
      -------------------------------------------
      |                   |                     |                     
   SqlReader  <----  XmlSqlService ---->  SqlRegistry
      ↑                                 ↗       ↑   
   JaxbSqlReader---------------------↗    HashMapSqlRegistry
  ```
- XmlSqlService가 `SqlReader`, `SqlService`, `SqlRegistry`를 직접 구현하여 자기 참조 빈으로 사용할 수 있다

#### 자기참조 빈 설정
- 자주 사용하는 패턴은 아니다. 하위 구현체가 필요하지 않은 경우 고려해 볼 수 있다
  ```xml
    <bean id="sqlService" class="part2.user.sqlservice.XmlSqlService">-->
        <property name="sqlmapFile" value="/sqlmap.xml"/>
        <property name="sqlReader" ref="sqlService"/>
        <property name="sqlRegistry" ref="sqlService"/>
    </bean>
  ```
### 디폴트 의존관계

#### 확장 가능한 기반 클래스
- `BaseSqlService`는 `SqlReader`, `SqlRegistry` 인터페이스를 확장하도록 `protected` 로 선언하였다
- `JaxbXmlSqlReader`, `HashMapSQlRegisty` 는 하위 구현체이며, 확장에 자유롭다라는 것을 보여주는 코드이다

#### 디폴트 의존 관계를 갖는 빈 만들기
- `DefaultSqlService` 는 의존 관계가 없을 경우 빈을 생성한다
- 참조하는 객체들의 DI까지 고려해야 하기 때문에 `정적 상수`를 사용하여 기본 값을 지정해주었다 `JaxbSqlReader` 
- 디폴트 의존 오브젝트는 생성자에서 디폴트 의존 오브젝트를 다 만든다는 단점(불필요한 객체 생성)이 있지만, 사용에 장점이 더 크다