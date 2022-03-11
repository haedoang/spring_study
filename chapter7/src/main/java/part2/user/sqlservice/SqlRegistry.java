package part2.user.sqlservice;

import part2.user.exception.SqlNotFoundException;

/**
 * author : haedoang
 * date : 2022/03/11
 * description :
 */
public interface SqlRegistry {
    // sql을 등록한다
    void registerSql(String key, String sql);

    // sql을 검색한다
    String findSql(String key) throws SqlNotFoundException;
}
