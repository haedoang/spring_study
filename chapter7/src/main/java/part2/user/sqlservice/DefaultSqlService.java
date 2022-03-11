package part2.user.sqlservice;

/**
 * author : haedoang
 * date : 2022/03/11
 * description :
 */
public class DefaultSqlService extends BasicSqlService {
    public DefaultSqlService() {
        setSqlReader(new JaxbXmlSqlReader());
        setSqlRegistry(new HashMapSqlRegistry());
    }
}
