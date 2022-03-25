package part6.user.exception;

/**
 * author : haedoang
 * date : 2022/03/21
 * description :
 */
public class SqlUpdateFailureException extends RuntimeException {
    public SqlUpdateFailureException(String message) {
        super(message);
    }
}
