package part2.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import part2.bean.Hello;
import part2.bean.Printer;
import part2.bean.StringPrinter;

/**
 * author : haedoang
 * date : 2022/06/19
 * description : 일반 빈 클래스
 */
public class HelloService {

    @Bean
    public Hello hello1() {
        final Hello hello = new Hello();
        hello.setName("Spring");
        hello.setPrinter(printer());
        return hello;
    }

    @Bean
    public Hello hello2() {
        final Hello hello = new Hello();
        hello.setName("Spring2");
        hello.setPrinter(printer());
        return hello;
    }

    @Bean
    public Printer printer() {
        return new StringPrinter();
    }
}
