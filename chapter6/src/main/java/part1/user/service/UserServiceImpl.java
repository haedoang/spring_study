package part1.user.service;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import part1.user.dao.UserDao;
import part1.user.domain.Level;
import part1.user.domain.User;

import java.util.List;

/**
 * author : haedoang
 * date : 2022/02/27
 * description :
 */
public class UserServiceImpl implements UserService {
    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;

    private UserDao userDao;
    private MailSender mailSender;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }


// 비즈니스 로직과 트랜잭션로직의 분리. 서로 주고받는 것이 없기 때문에 메소드 분리가 가능함.
//    public void upgradeLevels() {
//        final TransactionStatus status =
//                this.transactionManager.getTransaction(new DefaultTransactionDefinition());
//
//        try {
//            upgradeLevelsInternal();
//            this.transactionManager.commit(status);
//        } catch (RuntimeException e) {
//            this.transactionManager.rollback(status);
//            throw e;
//        }
//    }

//    private void upgradeLevelsInternal() {
//        List<User> users = userDao.getAll();
//        for (User user : users) {
//            if (canUpgradeLevel(user)) {
//                upgradeLevel(user);
//            }
//        }
//    }

    public void upgradeLevels() {
        List<User> users = userDao.getAll();
        for (User user : users) {
            if (canUpgradeLevel(user)) {
                upgradeLevel(user);
            }
        }
    }

    protected void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
        sendUpgradeEMailSpring(user);
    }

    /**
     * MailException(RuntimeException wrapped)
     *
     * @param user
     */
    private void sendUpgradeEMailSpring(User user) {
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