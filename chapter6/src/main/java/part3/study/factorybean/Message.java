package part3.study.factorybean;

/**
 * author : haedoang
 * date : 2022/02/27
 * description :
 */
public class Message {
    String text;

    private Message(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static Message newMessage(String text) {
        return new Message(text);
    }
}
