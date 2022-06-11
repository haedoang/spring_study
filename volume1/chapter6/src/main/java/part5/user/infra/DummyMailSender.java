package part5.user.infra;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

/**
 * packageName : part5.user.infra
 * fileName : DummyMailSender
 * author : haedoang
 * date : 2022-03-05
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
