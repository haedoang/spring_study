package part4.user.exception;

/**
 * author : haedoang
 * date : 2022/03/21
 * description :
 */
public class SqlNotFoundException extends RuntimeException {
    public SqlNotFoundException(String message) {
        super(message);
    }
}
