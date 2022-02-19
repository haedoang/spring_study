package part5.template;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * author : haedoang
 * date : 2022/02/19
 * description :
 */
public interface BufferedReaderCallback {
    Integer doSomethingWithReader(BufferedReader br) throws IOException;
}
