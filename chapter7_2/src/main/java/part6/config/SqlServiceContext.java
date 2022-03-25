package part6.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.castor.CastorMarshaller;
import part6.user.sqlservice.EmbeddedDbSqlRegistry;
import part6.user.sqlservice.OxmSqlService;
import part6.user.sqlservice.SqlRegistry;
import part6.user.sqlservice.SqlService;

import javax.sql.DataSource;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;

/**
 * author : haedoang
 * date : 2022/03/25
 * description :
 */
@Configuration
public class SqlServiceContext {

    @Autowired
    private SqlMapConfig sqlMapConfig; // appContext bean

    @Bean
    public SqlService sqlService() {
        final OxmSqlService oxmSqlService = new OxmSqlService();
        oxmSqlService.setUnmarshaller(unmarshaller());
        oxmSqlService.setSqlRegistry(sqlRegistry());

        oxmSqlService.setSqlmap(sqlMapConfig.getSqlMapResource());
        return oxmSqlService;
    }

    @Bean
    public SqlRegistry sqlRegistry() {
        final EmbeddedDbSqlRegistry embeddedDbSqlRegistry = new EmbeddedDbSqlRegistry();
        embeddedDbSqlRegistry.setDataSource(embeddedDatabase());

        return embeddedDbSqlRegistry;
    }

    @Bean
    public Unmarshaller unmarshaller() {
        final CastorMarshaller castorMarshaller = new CastorMarshaller();
        castorMarshaller.setMappingLocation(new ClassPathResource("mapping.xml"));

        return castorMarshaller;
    }
//    @Resource
//    DataSource embeddedDatabase;

    @Bean
    public DataSource embeddedDatabase() {
        return new EmbeddedDatabaseBuilder()
                .setName("embeddedDatabase")
                .setType(HSQL)
                .addScript("classpath:sqlRegistrySchema.sql")
                .build();
    }
}
