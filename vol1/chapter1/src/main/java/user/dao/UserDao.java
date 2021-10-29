package user.dao;

import user.domain.User;

import java.sql.*;

/**
 * packageName : user.dao
 * fileName : UserDao
 * author : haedoang
 * date : 2021/10/29
 * description : 상속을 통해 확장할 Users 도메인에 접근할 User 추상 클래스
 */
public abstract class UserDao {
    public abstract Connection getConnection() throws ClassNotFoundException, SQLException;

    public void add(User user) throws ClassNotFoundException, SQLException {
        Connection c = getConnection();

        String query = new StringBuilder("insert into users(id, name, password) ")
                                    .append(" values (?,?,?)").toString();

        PreparedStatement ps = c.prepareStatement(query);
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        ps.executeUpdate();

        ps.close();
        c.close();
    }


    public User get(String id) throws ClassNotFoundException, SQLException {
        Connection c = getConnection();

        String query = new StringBuilder("select * from users where id = ?").toString();
        PreparedStatement ps = c.prepareStatement(query);
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();
        rs.next();
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));

        rs.close();
        ps.close();
        c.close();

        return user;

    }

    protected void hookMethod() {
        System.out.println("훅 메소드는 선택적으로 오버라이드가능하다.");
    }

    /***
     *  상속을 통해 공통 코드를 분리하였다.
     *  그리고 추상메소드와 훅메소드를 통해 확장성을 높였다.
     *
     *  하지만, 자바언어는 다중상속을 허용하지 않는다. 만약, 커넥션 연결에 대한 상속이외의 용도로 dao가 설계되어있을 경우에 사용할 수가 없게 된다.
     *   또한, 상속을 통한 상하위 클래스의 관계의 밀접 또한 단점이다. 서브클래스는 슈퍼클래스의 기능을 직접 사용할 수 있기 때문에
     *        슈퍼클래스 내부의 변경이 있을 시 하위 클래스도 변경이 필요할 수 있다. (결합도를 낮추도록 해야 유지보수가 용이하다)
     *  DB커넥션을 연결하는 코드를 다른 DAO 클래스에서 사용을 못하는 것도 단점임
     */

}
