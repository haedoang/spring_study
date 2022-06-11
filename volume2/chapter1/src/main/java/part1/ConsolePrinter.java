package part1;

/**
 * author : haedoang
 * date : 2022/06/11
 * description :
 */
public class ConsolePrinter implements Printer {
    @Override
    public void print(String message) {
        System.out.println(message);
    }
}
