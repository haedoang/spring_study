package part5.study.pointcut;

/**
 * author : haedoang
 * date : 2022/03/06
 * description :
 */
public class Target implements TargetInterface {
    @Override
    public void hello() {

    }

    @Override
    public void hello(String a) {

    }

    @Override
    public int minus(int a, int b) throws RuntimeException {
        return 0;
    }

    @Override
    public int plus(int a, int b) {
        return 0;
    }

    public void method() {
    }

    public static void main(String[] args) throws NoSuchMethodException {
        System.out.println(Target.class.getMethod("minus", int.class, int.class));
    }
}
