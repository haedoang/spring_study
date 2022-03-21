package part5.user.sqlservice;

import part5.user.exception.SqlNotFoundException;
import part5.user.exception.SqlUpdateFailureException;
import part5.user.sqlservice.UpdatableSqlRegistry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * author : haedoang
 * date : 2022/03/21
 * description :
 */
public class ConcurrentHashMapSqlRegistry implements UpdatableSqlRegistry {
    private Map<String, String> sqlMap = new ConcurrentHashMap<String, String>();

    @Override
    public void registerSql(String key, String sql) {
        sqlMap.put(key, sql);
    }

    @Override
    public String findSql(String key) throws SqlNotFoundException {
        String sql = sqlMap.get(key);
        if (sql == null) {
            throw new SqlNotFoundException(String.format("%s 를 이용해서 SQL을 찾을 수 없습니다.", key));
        }
        return sql;
    }

    @Override
    public void updateSql(String key, String sql) throws SqlUpdateFailureException {
        if (sqlMap.get(key) == null) {
            throw new SqlUpdateFailureException(String.format("%s 를 이용해서 SQL을 찾을 수 없습니다.", key));
        }

        sqlMap.put(key, sql);
    }

    @Override
    public void updateSql(Map<String, String> updateSqlMap) throws SqlUpdateFailureException {
        for (Map.Entry<String, String> entry : updateSqlMap.entrySet()) {
            updateSql(entry.getKey(), entry.getValue());
        }
    }
}
