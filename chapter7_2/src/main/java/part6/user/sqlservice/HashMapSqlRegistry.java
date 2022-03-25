package part6.user.sqlservice;

import part6.user.exception.SqlNotFoundException;

import java.util.HashMap;
import java.util.Map;

/**
 * author : haedoang
 * date : 2022/03/21
 * description :
 */
public class HashMapSqlRegistry implements SqlRegistry {
    private Map<String, String> sqlMap = new HashMap<String, String>();

    @Override
    public void registerSql(String key, String sql) {
        this.sqlMap.put(key, sql);
    }

    @Override
    public String findSql(String key) throws SqlNotFoundException {
        String sql = sqlMap.get(key);

        if (sql == null) {
            throw new SqlNotFoundException(String.format("%s 에 대한 SQL을 찾을 수 없습니다", key));
        }
        return sql;
    }
}
