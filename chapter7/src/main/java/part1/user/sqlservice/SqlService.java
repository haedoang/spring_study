package part1.user.sqlservice;

import part1.user.exception.SqlRetrievalFailureException;

/**
 * author : haedoang
 * date : 2022/03/11
 * description :
 */
public interface SqlService {
    String getSql(String key) throws SqlRetrievalFailureException;
}
