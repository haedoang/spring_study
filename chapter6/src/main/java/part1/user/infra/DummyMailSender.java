package part1.user.infra;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * author : haedoang
 * date : 2022/02/26
 * description :
 */
public class DummyMailSender implements MailSender {
    private final List<String> requests = new ArrayList<>();

    @Override
    public void send(SimpleMailMessage simpleMailMessage) throws MailException {
        requests.add(simpleMailMessage.getTo()[0]);
    }

    @Override
    public void send(SimpleMailMessage[] simpleMailMessages) throws MailException {

    }
}
