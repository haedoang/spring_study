package part4.user.sqlservice;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Unmarshaller;
import part4.user.exception.SqlRetrievalFailureException;
import part4.user.sqlservice.jaxb.SqlType;
import part4.user.sqlservice.jaxb.Sqlmap;

import javax.annotation.PostConstruct;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;

/**
 * author : haedoang
 * date : 2022/03/12
 * description :
 */
public class OxmSqlService implements SqlService {
    private final BasicSqlService basicSqlService = new BasicSqlService();
    private final OxmSqlReader oxmSqlReader = new OxmSqlReader();
    private SqlRegistry sqlRegistry = new HashMapSqlRegistry(); // default

    public void setUnmarshaller(Unmarshaller unmarshaller) {
        this.oxmSqlReader.setUnmarshaller(unmarshaller);
    }

//    public void setSqlmapFile(String sqlmapFile) {
//        this.oxmSqlReader.setSqlmapFile(sqlmapFile);
//    }

    public void setSqlRegistry(SqlRegistry sqlRegistry) {
        this.sqlRegistry = sqlRegistry;
    }

    public void setSqlmap(Resource sqlmap) {
        this.oxmSqlReader.setSqlmap(sqlmap);
    }

    @PostConstruct
    public void loadSql() {
        //this.oxmSqlReader.read(this.sqlRegistry);
        this.basicSqlService.setSqlReader(this.oxmSqlReader);
        this.basicSqlService.setSqlRegistry(this.sqlRegistry);

        this.basicSqlService.loadSql();
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
//        try {
//            return this.sqlRegistry.findSql(key);
//        } catch (SqlNotFoundException e) {
//            throw new SqlRetrievalFailureException(e);
//        }
        return this.basicSqlService.getSql(key);
    }

    private class OxmSqlReader implements SqlReader {
        private Resource sqlmap = new ClassPathResource("sqlmap.xml");
        private Unmarshaller unmarshaller;
//        private final static String DEFAULT_SQLMAP_FILE = "/sqlmap.xml";
//        private String sqlmapFile = DEFAULT_SQLMAP_FILE;

        public void setUnmarshaller(Unmarshaller unmarshaller) {
            this.unmarshaller = unmarshaller;
        }

//        public void setSqlmapFile(String sqlmapFile) {
//            this.sqlmapFile = sqlmapFile;
//        }

        public void setSqlmap(Resource sqlmap) {
            this.sqlmap = sqlmap;
        }

        @Override
        public void read(SqlRegistry registry) {
            try {
//                final Source source = new StreamSource(
//                        UserDao.class.getResourceAsStream(this.sqlmapFile)
//                );

                final StreamSource source = new StreamSource(sqlmap.getInputStream());

                final Sqlmap sqlmap = (Sqlmap) this.unmarshaller.unmarshal(source);
                for (SqlType sql : sqlmap.getSql()) {
                    sqlRegistry.registerSql(sql.getKey(), sql.getValue());
                }
            } catch (IOException e) {
//                throw new IllegalArgumentException(String.format("%s을 가져올 수 없습니다", this.sqlmapFile));
                throw new IllegalArgumentException(String.format("%s을 가져올 수 없습니다", this.sqlmap.getFilename()));
            }
        }
    }
}
