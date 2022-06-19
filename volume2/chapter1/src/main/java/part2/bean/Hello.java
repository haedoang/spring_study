package part2.bean;


/**
 * author : haedoang
 * date : 2022/06/11
 * description :
 */
public class Hello {
    String name;
    Printer printer;

    public String sayHello() {
        return "Hello " + name;
    }

    public void print() {
        this.printer.print(sayHello());
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrinter(Printer printer) {
        this.printer = printer;
    }

    public String getName() {
        return name;
    }

    public Printer getPrinter() {
        return printer;
    }
}
