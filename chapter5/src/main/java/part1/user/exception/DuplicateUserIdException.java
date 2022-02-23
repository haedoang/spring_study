package part1.user.exception;

/**
 * author : haedoang
 * date : 2022/02/20
 * description :
 */
public class DuplicateUserIdException extends RuntimeException {
    public DuplicateUserIdException(Throwable cause) {
        super(cause);
    }
}
