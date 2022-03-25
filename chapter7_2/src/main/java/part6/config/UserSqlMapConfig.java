package part6.config;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import part6.user.dao.UserDao;

/**
 * author : haedoang
 * date : 2022/03/25
 * description :
 */
public class UserSqlMapConfig implements SqlMapConfig {

    @Override
    public Resource getSqlMapResource() {
        return new ClassPathResource("sqlmap.xml", UserDao.class);
    }
}
