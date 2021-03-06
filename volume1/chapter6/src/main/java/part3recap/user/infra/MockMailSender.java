package part3recap.user.infra;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * packageName : part4.user.infra
 * fileName : MockMailSender
 * author : haedoang
 * date : 2022-02-28
 * description : 목 오브젝트
 */
public class MockMailSender implements MailSender {
    private final List<String> requests = new ArrayList<>();

    public List<String> getRequests() {
        return requests;
    }

    @Override
    public void send(SimpleMailMessage simpleMailMessage) throws MailException {
        requests.add(simpleMailMessage.getTo()[0]);
    }

    @Override
    public void send(SimpleMailMessage[] simpleMailMessages) throws MailException {

    }
}
