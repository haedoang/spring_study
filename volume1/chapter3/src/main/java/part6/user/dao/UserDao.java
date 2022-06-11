package part6.user.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import part6.user.domain.User;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * author : haedoang
 * date : 2022/02/19
 * description :
 */
public class UserDao {
    // org.springframework.jdbc.core.JdbcTemplate
    private JdbcTemplate jdbcTemplate;

    private RowMapper<User> userMapper =
            new RowMapper<User>() {
                @Override
                public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                    User user = new User();
                    user.setId(rs.getString("id"));
                    user.setName(rs.getString("name"));
                    user.setPassword(rs.getString("password"));
                    return user;
                }
            };

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void add(final User user) {
        this.jdbcTemplate.update(
                "insert into users(id, name, password) values(?, ?, ?)",
                user.getId(),
                user.getName(),
                user.getPassword()
        );
    }


    public User get(String id) {
        return this.jdbcTemplate.queryForObject(
                "select * from users where id = ?",
                new Object[]{id},
                this.userMapper

                // 공통화 분리
//                new RowMapper<User>() {
//                    @Override
//                    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
//                        User user = new User();
//                        user.setId(rs.getString("id"));
//                        user.setName(rs.getString("name"));
//                        user.setPassword(rs.getString("password"));
//                        return user;
//                    }
//                }
        );
    }

    public void deleteAll() {

        // jdbcTemplate 제공하는 콜백사용
        //        this.jdbcTemplate.update(
        //                new PreparedStatementCreator() {
        //                    @Override
        //                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        //                        return connection.prepareStatement("delete from users");
        //                    }
        //                }
        //        );

        // 내장 콜백 메서드 사용
        this.jdbcTemplate.update("delete from users");
    }

    public int getCount() {

        // jdbcTemplate에서 제공하는 콜백사용
        //        return this.jdbcTemplate.query(new PreparedStatementCreator() {
        //            @Override
        //            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        //                return connection.prepareStatement("select count(*) from users");
        //            }
        //        }, new ResultSetExtractor<Integer>() {
        //            @Override
        //            public Integer extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        //                resultSet.next();
        //                return resultSet.getInt(1);
        //            }
        //        });

        // 내장 콜백 메서드 사용
        return this.jdbcTemplate.queryForInt("select count(*) from users");
    }

    public List<User> getAll() {
        return this.jdbcTemplate.query(
                "select * from users order by id",
                this.userMapper
// 공통화 분리
//                new RowMapper<User>() {
//                    @Override
//                    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
//                        User user = new User();
//                        user.setId(rs.getString("id"));
//                        user.setName(rs.getString("name"));
//                        user.setPassword(rs.getString("password"));
//                        return user;
//                    }
//                }
        );
    }
}