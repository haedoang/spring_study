### 메일 서비스 추상화
 - javax.mail 사용(javax.mail.jar)
     ```text
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
 - spring 메일 추상화 사용(springframework.context.support.jar)
   - EmailException으로 Wrapping 되어있다(RuntimeException)
    ```text
    private void sendUpgradeEMailSpring(User user) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom("haedoang@naver.com");
        mailMessage.setSubject("Upgrade 안내");
        mailMessage.setText(String.format("사용자님의 등급이 %s 로 업그레이드되었습니다.", user.getLevel().name()));

        mailSender.send(mailMessage);
    }
   
#### 추상화가 필요한 이유 ?
 - 테스트를 위해 메일 서버에 메일 발송을 요청하는 것은 비용이 든다
 - 실직적인 테스트는 메일 발송을 요청하는 것까지만으로도 충분하다

#### 테스트용 메일 발송 오브젝트
- 메일 발송 요청까지를 담당할 테스트 오브젝트 생성
- 아무 기능도 없지만 send()메서드를 실행하는 것까지를 담당한다
  ```java
  public class DummyMailSender implements MailSender {

      @Override
      public void send(SimpleMailMessage simpleMailMessage) throws MailException {

      }

      @Override
      public void send(SimpleMailMessage[] simpleMailMessages) throws MailException {

      }
  }
  ```
  
#### 테스트와 서비스 추상화
 - 메일 전송 서비스 추상화 구조
     ```text
                    UserService
                         |
                     MailSender
                 ________|_________
                |                 | 
         DummyMailService  JavaMailSericeImpl
              (dev)               |   
                               JavaMail         
                                (prod)
     ```
   
#### 테스트 대역
- 테스트 대역이란 테스트 환경을 만들어주기 위해 테스트 대상이 되는 오브젝트 기능에만 충실한 오브젝트.
- 테스트 대역의 종류와 특징
  - 대표적인 테스트 대역은 `테스트 스텁`이다
  - 테스트 스텁은 테스트 코드 내에 간접적으로 사용하며, DI등을 통해 미리 의존 오브젝트를 테스트 스텁으로 변경한다


#### 목 오브젝트
- 테스트 대상 오브젝트와 의존 오브젝트 사이에서 일어나는 일을 검증하도록 설계된 오브젝트
- 테스트하기 어려운 코드에 대해 목 오브젝트를 통한 검증할 수 있다
 ```java
 public class MockMailSender implements MailSender {
    private List<String> requests = new ArrayList<>();

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
 ```