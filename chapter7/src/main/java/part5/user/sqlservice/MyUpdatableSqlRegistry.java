package part5.user.sqlservice;

import part5.user.exception.SqlNotFoundException;
import part5.user.exception.SqlUpdateFailureException;

import java.util.Map;

/**
 * author : haedoang
 * date : 2022/03/21
 * description :
 */
public class MyUpdatableSqlRegistry implements UpdatableSqlRegistry {
    @Override
    public void registerSql(String key, String sql) {

    }

    @Override
    public String findSql(String key) throws SqlNotFoundException {
        return null;
    }

    @Override
    public void updateSql(String key, String sql) throws SqlUpdateFailureException {

    }

    @Override
    public void updateSql(Map<String, String> sqlmap) throws SqlUpdateFailureException {

    }
}
