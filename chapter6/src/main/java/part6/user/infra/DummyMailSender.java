package part6.user.infra;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

/**
 * fileName : DummyMailSender
 * author : haedoang
 * date : 2022-03-06
 * description : Dev용 dummy mailSender
 */
public class DummyMailSender implements MailSender {

    @Override
    public void send(SimpleMailMessage simpleMailMessage) throws MailException {
        System.out.println("invoke");
    }

    @Override
    public void send(SimpleMailMessage[] simpleMailMessages) throws MailException {

    }
}
