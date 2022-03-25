package part6.user.sqlservice;

import part6.user.exception.SqlNotFoundException;

/**
 * author : haedoang
 * date : 2022/03/21
 * description :
 */
public interface SqlRegistry {
    // sql을 등록한다
    void registerSql(String key, String sql);

    // sql을 검색한다
    String findSql(String key) throws SqlNotFoundException;
}
