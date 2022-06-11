package part5;

import part5.user.sqlservice.ConcurrentHashMapSqlRegistry;
import part5.user.sqlservice.UpdatableSqlRegistry;

/**
 * author : haedoang
 * date : 2022/03/22
 * description :
 */
public class ConcurrentHashMapSqlRegistryTest extends AbstractUpdatableSqlRegistry {

    @Override
    protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
        return new ConcurrentHashMapSqlRegistry();
    }
}
