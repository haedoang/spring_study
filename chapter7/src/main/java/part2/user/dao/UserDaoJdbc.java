package part2.user.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import part2.user.domain.Level;
import part2.user.domain.User;
import part2.user.sqlservice.SqlService;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * fileName : UserDaoJdbc
 * author : haedoang
 * date : 2022-03-11
 * description :
 */
public class UserDaoJdbc implements UserDao {

    private SqlService sqlService;

    public void setSqlService(SqlService sqlService) {
        this.sqlService = sqlService;
    }

    private JdbcTemplate jdbcTemplate;

    private final RowMapper<User> userMapper =
            new RowMapper<User>() {
                @Override
                public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                    User user = new User();
                    user.setId(rs.getString("id"));
                    user.setName(rs.getString("name"));
                    user.setPassword(rs.getString("password"));
                    user.setLevel(Level.valueOf(rs.getInt("level")));
                    user.setLogin(rs.getInt("login"));
                    user.setRecommend(rs.getInt("recommend"));
                    return user;
                }
            };

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void add(final User user) {
        this.jdbcTemplate.update(
                sqlService.getSql("add"),
                user.getId(),
                user.getName(),
                user.getPassword(),
                user.getLevel().intValue(),
                user.getLogin(),
                user.getRecommend()
        );
    }

    @Override
    public User get(String id) {
        return this.jdbcTemplate.queryForObject(
                sqlService.getSql("get"),
                new Object[]{id},
                this.userMapper
        );
    }

    @Override
    public void deleteAll() {
        this.jdbcTemplate.update(sqlService.getSql("delete"));
    }

    @Override
    public int getCount() {
        return this.jdbcTemplate.queryForInt(sqlService.getSql("getCount"));
    }

    @Override
    public List<User> getAll() {
        return this.jdbcTemplate.query(
                sqlService.getSql("getAll"),
                this.userMapper
        );
    }

    @Override
    public void update(User user) {
        this.jdbcTemplate.update(
                sqlService.getSql("update"),
                user.getName(),
                user.getPassword(),
                user.getLevel().intValue(),
                user.getLogin(),
                user.getRecommend(),
                user.getId()
        );
    }
}
