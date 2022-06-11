package part3.study.spring.oxm;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Unmarshaller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import part3.user.sqlservice.jaxb.SqlType;
import part3.user.sqlservice.jaxb.Sqlmap;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


/**
 * author : haedoang
 * date : 2022/03/12
 * description :
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/OxmTest-context.xml")
public class OxmTest {
    @Autowired
    Unmarshaller unmarshaller;

    @Test
    public void unmarshallSqlMap() throws IOException {
        Source xmlSource = new StreamSource(
                getClass().getResourceAsStream("/sqlmap.xml")
        );

        final Sqlmap sqlmap = (Sqlmap) this.unmarshaller.unmarshal(xmlSource);

        final List<SqlType> sqlList = sqlmap.getSql();

        assertThat(sqlList.size(), is(6));
        assertThat(sqlList.get(0).getKey(), is("add"));
        assertThat(sqlList.get(1).getKey(), is("getAll"));
        assertThat(sqlList.get(2).getKey(), is("get"));
        assertThat(sqlList.get(3).getKey(), is("getCount"));
        assertThat(sqlList.get(4).getKey(), is("update"));
        assertThat(sqlList.get(5).getKey(), is("delete"));
    }


}
