package part5.template;

/**
 * author : haedoang
 * date : 2022/02/19
 * description :
 */
public interface LineCallback<T> {
    T doSomethingWithLine(String line, T value);
}
