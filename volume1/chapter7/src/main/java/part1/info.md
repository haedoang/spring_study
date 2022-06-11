## SQL과 DAO의 분리
- SQL 변경이 필요한 상황이 발생하면 SQL을 담고 있는 DAO 코드가 수정할 수 밖에 없다
- 따라서 SQL을 적절히 분리해 DAO 코드와 다른 파일이나 위치에 두고 관리하는 것이 좋다

### XML 설정을 이용한 분리 

#### 개별 SQL 프로퍼티 방식
- 쿼리 개수가 늘어날 경우 DI가 추가되는 단점이 있다
    ```xml
    <bean id="userDao" class="part1.user.dao.UserDaoJdbc">
            <property name="dataSource" ref="dataSource"/>
            <property name="sqlAdd"
              value="insert into users(id, name, password, level, login, recommend) values(?, ?, ?, ?, ?, ?)"/>
        </bean>
    ```
#### SQL 맵 프로퍼티 방식
- 문자열로 된 키 값을 사용하기 때문에 오타와 같은 실수가 있어도, 메소드가 실행되기 전까지 발견하기 어렵다
    ```xml
    <bean id="userDao" class="part1.user.dao.UserDaoJdbc">
            <property name="dataSource" ref="dataSource"/>
            <property name="sqlMap">
                <map>
                    <entry key="add"
                           value="insert into users(id, name, password, level, login, recommend) values(?, ?, ?, ?, ?, ?)"/>
                    <entry key="getAll" value="select * from users"/>
                    <entry key="get" value="select * from users where id = ?"/>
                    <entry key="getCount" value="select count(*) from users"/>
                    <entry key="update"
                           value="update users set name = ?, password = ?, level = ?, login = ?, recommend = ? where id = ?"/>
                    <entry key="delete" value="delete from users"/>
                </map>
            </property>
    </bean>
    ```
#### SQL 서비스 인터페이스 
- SQL 서비스를 인터페이스로 작성하여 UserDao와 독립적인 SQL 제공 서비스를 작성할 수 있다
    ```text
    public interface SqlService {
        String getSql(String key) throws SqlRetrievalFailureException; 
    }
    ```
#### 스프링 설정을 사용하는 단순 SQL 서비스
- userDao는 SQL 서비스를 주입받기 때문에 완전히 독립적이라고 볼 수 있다
- userDao의 수정 없이 SQL을 독립적으로 변경할 수 있는 구조이다
    ```xml
    <bean id="userDao" class="part1.user.dao.UserDaoJdbc">
        <property name="dataSource" ref="dataSource"/>
        <property name="sqlService" ref="sqlService" />
    </bean>
    
    <bean id="sqlService" class="part1.user.sqlservice.SimpleSqlService">
        <property name="sqlMap">
            <map>
                <entry key="add"
                       value="insert into users(id, name, password, level, login, recommend) values(?, ?, ?, ?, ?, ?)"/>
                <entry key="getAll" value="select * from users"/>
                <entry key="get" value="select * from users where id = ?"/>
                <entry key="getCount" value="select count(*) from users"/>
                <entry key="update"
                       value="update users set name = ?, password = ?, level = ?, login = ?, recommend = ? where id = ?"/>
                <entry key="delete" value="delete from users"/>
            </map>
        </property>
    </bean>
    ```