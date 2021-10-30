package user.dao;

import common.db.ConnectionMaker;
import common.db.DConnectionMaker;
import user.domain.User;

import java.sql.*;

/**
 * packageName : user.dao
 * fileName : UserDao
 * author : haedoang
 * date : 2021/10/30
 * description : 관심사의 분리를 적용한 UserDao
 */
public class UserDao {
    private ConnectionMaker connectionMaker;

    //D사의 Connection 정보 주입 => 사용자에 따라 UserDao를 수정해야하는 문제가 생긴다.
    //public UserDao() {
    //    this.connectionMaker = new DConnectionMaker();
    //}

    //UserDao 커넥션 연결에 대한 설정을 클라이언트에게 넘긴다.
    public UserDao(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }

    public void add(User user) throws ClassNotFoundException, SQLException {
        Connection c = connectionMaker.makeConnection();

        String query = new StringBuilder("insert into users(id, name, password)")
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
        Connection c = connectionMaker.makeConnection();

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

    /**
     *  인터페이스를 적용하여 Connection 연결 시 의존성을 주입받게 하였다.
     *  그래도 발생하는 문제점은 UserDao 에서 해당 ConnectionMaker 클래스를 주입해주어야한다는 점이다.
     *  UserDao의 모든 코드는 ConnectionMaker 인터페이스 외에 어떠한 클래스와도 관계를 맺어서는 안된다.
     *
     *  해결 방법 ?
     *  의존성 주입을 클라이언트에게 넘기면 된다. UserDao 객체의 생성 시 클라이언트의 Connection 정보가 담긴 오브젝트를
     *  주입받는다면 UserDao 의 내용은 수정할 필요가 없어진다.
     *
     *  개방 폐쇄 원칙(Open-Closed Principle)을 잘 지키자!
     *  => 클래스와 모듈은 확장에는 열려 있어야하고 변경에는 닫혀있어야 한다. (SOLID 5원칙 중 하나)
     *  특징 1) 높은 응집도 : 변화가 일어날 때 해당 모듈에서 변하는 부분이 크다.
     *      2) 낮은 결합도 : 하나의 오브젝트가 변경이 일어날 때 관곌를 맺고 있는 다른 하나의 변경의 정도가 적음을 의미한다.
     *
     *  전략 패턴이란 ?
     *  =>  전략 패턴(strategy pattern) 또는 정책 패턴(policy pattern)은 실행 중에 알고리즘을 선택할 수 있게 하는 행위 소프트웨어 디자인 패턴이다.
     *  UserDao의 경우 N사와 D사의 오브젝트를 사용자에 따라(선택적) 주입받아 처리하기 떄문에 전략 패턴이 적용된 것이라 볼 수 있다.
     *  출처 : 위키백과
     * */
}
