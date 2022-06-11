package part6.user.sqlservice;

import part6.user.exception.SqlNotFoundException;
import part6.user.exception.SqlRetrievalFailureException;
import part6.user.sqlservice.jaxb.SqlType;
import part6.user.sqlservice.jaxb.Sqlmap;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


public class XmlSqlService implements SqlService, SqlRegistry, SqlReader {
    private SqlReader sqlReader;
    private SqlRegistry sqlRegistry;
    private String sqlmapFile;
    private Map<String, String> sqlMap = new HashMap<String, String>();

    public void setSqlReader(SqlReader sqlReader) {
        this.sqlReader = sqlReader;
    }

    public void setSqlRegistry(SqlRegistry sqlRegistry) {
        this.sqlRegistry = sqlRegistry;
    }

    public void setSqlmapFile(String sqlmapFile) {
        this.sqlmapFile = sqlmapFile;
    }

    @PostConstruct
    public void loadSql() {
        this.sqlReader.read(this.sqlRegistry);
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        try {
            return this.sqlRegistry.findSql(key);
        } catch (SqlNotFoundException e) {
            throw new SqlRetrievalFailureException(e);
        }
    }


    @Override
    public void registerSql(String key, String sql) {
        sqlMap.put(key, sql);
    }

    @Override
    public String findSql(String key) throws SqlNotFoundException {
        String sql = sqlMap.get(key);

        if (sql == null) {
            throw new SqlNotFoundException(String.format("%s 에 대한 SQL을 찾을 수 없습니다", key));
        }
        return sql;
    }

    @Override
    public void read(SqlRegistry registry) {
        String contextPath = Sqlmap.class.getPackage().getName();
        try {
            JAXBContext context = JAXBContext.newInstance(contextPath);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            InputStream is = getClass().getResourceAsStream(this.sqlmapFile);
            Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(is);

            for (SqlType sql : sqlmap.getSql()) {
                registry.registerSql(sql.getKey(), sql.getValue());
            }

        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

}


/**
 * 책임을 분리하기 전 XmlSqlService
 */
//public class XmlSqlService implements SqlService {
//    private Map<String, String> sqlMap = new HashMap<String, String>();
//    private String sqlmapFile;
//
//    public void setSqlmapFile(String sqlmapFile) {
//        this.sqlmapFile = sqlmapFile;
//    }
//
//    @PostConstruct //객체 생성 이후 수행 로직 annotation-config 설정 필요
//    private void loadSql() {
//        String contextPath = Sqlmap.class.getPackage().getName();
//
//        try {
//            JAXBContext context = JAXBContext.newInstance(contextPath);
//            Unmarshaller unmarshaller = context.createUnmarshaller();
//            InputStream is = getClass().getResourceAsStream(this.sqlmapFile);
//            Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(is);
//
//            for (SqlType sql : sqlmap.getSql()) {
//                sqlMap.put(sql.getKey(), sql.getValue());
//            }
//
//        } catch (JAXBException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    public String getSql(String key) throws SqlRetrievalFailureException {
//        String sql = sqlMap.get(key);
//        if (sql == null) {
//            throw new part1.user.exception.SqlRetrievalFailureException(String.format("%s에 대한 SQL을 찾을 수 없습니다.", key));
//        }
//        return sql;
//    }
//}
