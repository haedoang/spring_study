package part2.user.sqlservice;

import part2.user.exception.SqlRetrievalFailureException;

/**
 * author : haedoang
 * date : 2022/03/11
 * description :
 */
public interface SqlService {
    String getSql(String key) throws SqlRetrievalFailureException;
}
