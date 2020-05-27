package AOP;

/**
 * Created by CCCQQF on 2020/5/27.
 */
public class RequestableImpl implements IRequestable {
    @Override
    public void request() {
        System.out.println("request processed in RequestableImpl");
    }
}
