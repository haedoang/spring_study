package part6.user.sqlservice;

import part6.user.exception.SqlUpdateFailureException;

import java.util.Map;

/**
 * author : haedoang
 * date : 2022/03/21
 * description :
 */
public interface UpdatableSqlRegistry extends SqlRegistry {
    void updateSql(String key, String sql) throws SqlUpdateFailureException;

    void updateSql(Map<String, String> sqlmap) throws SqlUpdateFailureException;
}
