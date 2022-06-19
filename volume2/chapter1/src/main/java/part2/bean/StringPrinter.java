package part2.bean;

/**
 * author : haedoang
 * date : 2022/06/11
 * description :
 */
public class StringPrinter implements Printer {
    private StringBuffer buffer = new StringBuffer();

    @Override
    public void print(String message) {
        this.buffer.append(message);
    }

    public String toString() {
        return this.buffer.toString();
    }
}
