package Transaction;


/**
 * 和org.springframework.transaction.support.TransactionSynchronizationManager类形似主要用来绑定连接信息
 * Created by CCCQQF on 2020/5/19.
 */
public class TransactionResourceManager{
    private static ThreadLocal resources=new ThreadLocal();
    public static Object getResource(){
        return resources.get();
    }
    public static void bindResources(Object resource){
        resources.set(resource);
    }
    public static Object unbindResources(){
        Object res=getResource();
        resources.set(null );
        return res;
    }


}
