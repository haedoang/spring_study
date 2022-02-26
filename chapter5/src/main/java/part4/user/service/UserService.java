package part4.user.service;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import part4.user.dao.UserDao;
import part4.user.domain.Level;
import part4.user.domain.User;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;

/**
 * author : haedoang
 * date : 2022/02/23
 * description :
 */
public class UserService {
    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;

    private UserDao userDao;
    private PlatformTransactionManager transactionManager;
    private MailSender mailSender;

    public void setTransactionManager(PlatformTransactionManager platformTransactionManager) {
        this.transactionManager = platformTransactionManager;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void upgradeLevels() {
        final TransactionStatus status =
                this.transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            List<User> users = userDao.getAll();
            for (User user : users) {
                if (canUpgradeLevel(user)) {
                    upgradeLevel(user);
                }
            }
            this.transactionManager.commit(status);
        } catch (RuntimeException e) {
            this.transactionManager.rollback(status);
            throw e;
        }
    }

    protected void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
        //sendUpgradeEMail(user); javax
        sendUpgradeEMailSpring(user);
    }

    //javax
    private void sendUpgradeEMail(User user) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "mail.ksug.org");
        final Session session = Session.getInstance(props, null);

        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress("haedoang.@naver.com"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
            message.setSubject("Upgrade 안내");
            //message.setText("사용자님의 등급이 " + user.getLevel().name() + "로 업그레이드되었습니다.");
            message.setText(String.format("사용자님의 등급이 %s 로 업그레이드되었습니다.", user.getLevel().name()));
            Transport.send(message);
        } catch (AddressException e) {
            throw new RuntimeException(e);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * MailException(RuntimeException wrapped)
     *
     * @param user
     */
    private void sendUpgradeEMailSpring(User user) {
        //final JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        //mailSender.setHost("mail.server.com");

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom("haedoang@naver.com");
        mailMessage.setSubject("Upgrade 안내");
        mailMessage.setText(String.format("사용자님의 등급이 %s 로 업그레이드되었습니다.", user.getLevel().name()));


        mailSender.send(mailMessage);
    }

    private boolean canUpgradeLevel(User user) {
        Level currentLevel = user.getLevel();

        switch (currentLevel) {
            case BASIC:
                return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
            case SILVER:
                return (user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD);
            case GOLD:
                return false;
            default:
                throw new IllegalArgumentException(String.format("Unknown Level: %s", currentLevel));
        }
    }

    public void add(User user) {
        if (user.getLevel() == null) {
            user.setLevel(Level.BASIC);
        }
        userDao.add(user);
    }
}