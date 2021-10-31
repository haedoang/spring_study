package user.dao;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

/**
 * packageName : user.dao
 * fileName : XmlConfigTest
 * author : haedoang
 * date : 2021/10/31
 * description :
 */
public class XmlConfigTest {
    public static void main(String[] args) {
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
        UserDao userDao = context.getBean("userDao", UserDao.class);
        System.out.println("userDao = " + userDao);
    }
}
