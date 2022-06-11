package part3.user.exception;

/**
 * author : haedoang
 * date : 2022/03/11
 * description :
 */
public class SqlNotFoundException extends RuntimeException {
    public SqlNotFoundException(String message) {
        super(message);
    }
}
