package AOP;

/**
 * Created by CCCQQF on 2020/5/27.
 */
public class Subject {
    public void request(){
        System.out.println("rq in subject without implementint any interface");
    }
    public void test(){
        System.out.println("test in subject");
    }
}
