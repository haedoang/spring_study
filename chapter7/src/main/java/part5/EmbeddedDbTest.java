package part5;

import org.junit.After;
import org.junit.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import part5.user.exception.SqlUpdateFailureException;
import part5.user.sqlservice.EmbeddedDbSqlRegistry;
import part5.user.sqlservice.UpdatableSqlRegistry;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.fail;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;

/**
 * author : haedoang
 * date : 2022/03/21
 * description :
 */
public class EmbeddedDbTest extends AbstractUpdatableSqlRegistry {
    EmbeddedDatabase db;

    @Override
    protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
        db = new EmbeddedDatabaseBuilder()
                .setType(HSQL)
                .addScript("classpath:/sqlRegistrySchema.sql")
                .build();

        EmbeddedDbSqlRegistry embeddedDbSqlRegistry = new EmbeddedDbSqlRegistry();
        embeddedDbSqlRegistry.setDataSource(db);

        return embeddedDbSqlRegistry;
    }


    @After
    public void tearDown() {
        db.shutdown();
    }

    @Test
    public void transactionUpdate() {
        checkFindResult("SQL1", "SQL2", "SQL3");

        Map<String, String> sqlmap = new HashMap<String, String>();
        sqlmap.put("KEY1", "Modified");
        sqlmap.put("KEY9999!@#", "Modified9999");


        try {
            sqlRegistry.updateSql(sqlmap);
            fail();
        } catch (SqlUpdateFailureException e) {
        }

        checkFindResult("SQL1", "SQL2", "SQL3");
    }
//
//    @Test
//    public void initData() {
//        assertThat(template.queryForInt("select count(*) from sqlmap"), is(2));
//
//        List<Map<String, Object>> list = template.queryForList("select * from sqlmap order by key_");
//
//        assertThat((String) list.get(0).get("key_"), is("KEY1"));
//        assertThat((String) list.get(0).get("sql_"), is("SQL1"));
//        assertThat((String) list.get(1).get("key_"), is("KEY2"));
//        assertThat((String) list.get(1).get("sql_"), is("SQL2"));
//    }
//
//    @Test
//    public void insert() {
//        template.update("insert into sqlmap(key_, sql_) values(?,?)", "KEY3", "SQL3");
//
//        assertThat(template.queryForInt("select count(*) from sqlmap"), is(3));
//    }
}
