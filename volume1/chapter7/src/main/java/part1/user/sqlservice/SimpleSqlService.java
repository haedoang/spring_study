package part1.user.sqlservice;

import part1.user.exception.SqlRetrievalFailureException;

import java.util.Map;

/**
 * author : haedoang
 * date : 2022/03/11
 * description :
 */
public class SimpleSqlService implements SqlService {
    private Map<String, String> sqlMap;

    public void setSqlMap(Map<String, String> sqlMap) {
        this.sqlMap = sqlMap;
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        final String sql = sqlMap.get(key);

        if(sql == null) {
            throw new SqlRetrievalFailureException(String.format("%s에 대한 SQL을 찾을 수 없습니다.", key));
        }

        return sql;
    }
}
