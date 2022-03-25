package part6.user.sqlservice;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import part6.user.exception.SqlNotFoundException;
import part6.user.exception.SqlUpdateFailureException;

import javax.sql.DataSource;
import java.util.Map;

/**
 * author : haedoang
 * date : 2022/03/22
 * description :
 */
public class EmbeddedDbSqlRegistry implements UpdatableSqlRegistry {
    SimpleJdbcTemplate jdbcTemplate;
    TransactionTemplate transactionTemplate;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new SimpleJdbcTemplate(dataSource);
        transactionTemplate = new TransactionTemplate(
                new DataSourceTransactionManager(dataSource)
        );
    }

    @Override
    public void registerSql(String key, String sql) {
        jdbcTemplate.update("insert into sqlmap(key_, sql_) values(?,?)", key, sql);
    }

    @Override
    public String findSql(String key) throws SqlNotFoundException {
        try {
            return jdbcTemplate.queryForObject("select sql_ from sqlmap where key_ =?", String.class, key);
        } catch (EmptyResultDataAccessException e) {
            throw new SqlNotFoundException(String.format("%s 에 대한 SQL을 찾을 수 없습니다", key));
        }
    }

    @Override
    public void updateSql(String key, String sql) throws SqlUpdateFailureException {
        int affected = jdbcTemplate.update("update sqlmap set sql_ = ? where key_ = ?", sql, key);

        if (affected == 0) {
            throw new SqlUpdateFailureException(String.format("%s 에 대한 SQL을 찾을 수 없습니다", key));
        }
    }


    @Override
    public void updateSql(final Map<String, String> sqlmap) throws SqlUpdateFailureException {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                for (Map.Entry<String, String> entry : sqlmap.entrySet()) {
                    updateSql(entry.getKey(), entry.getValue());
                }
            }
        });
    }
}
