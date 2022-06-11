package part2.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import part2.user.dao.UserDao;
import part2.user.dao.strategy.DeleteAllStrategy;
import part2.user.dao.strategy.NewUserDao;
import part2.user.dao.strategy.StatementStrategy;


import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * author : haedoang
 * date : 2022/02/17
 * description :
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext-part2.xml")
public class NewUserDaoTest {

    @Autowired
    private NewUserDao newUserDao;

    @Test
    public void 클라이언트가_사용할_전략을_선택한다() throws SQLException {
        // given
        StatementStrategy stmt = new DeleteAllStrategy();

        // when
        newUserDao.deleteAll();

        // then
        assertThat(newUserDao.getCount(), is(0));
    }

}
