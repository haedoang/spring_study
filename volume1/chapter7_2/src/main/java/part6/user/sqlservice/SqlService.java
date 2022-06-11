package part6.user.sqlservice;

import part6.user.exception.SqlRetrievalFailureException;

/**
 * author : haedoang
 * date : 2022/03/21
 * description :
 */
public interface SqlService {
    String getSql(String key) throws SqlRetrievalFailureException;
}
