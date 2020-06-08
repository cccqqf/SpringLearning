package AOP;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;

/**
 * Created by CCCQQF on 2020/6/8.
 */
public class ServiceControlRequestableProxy implements IRequestable {
    private static final Log logger= LogFactory.getLog(IRequestable.class);
    IRequestable requestable;
    @Override
    public void request() {
        Date date=new Date();
        int hours=date.getHours();
        if(hours<6){
            logger.warn("service is not available now.");
        }
        logger.info("service is in available now.");
        requestable.request();
    }

    @Override
    public void test() {
        requestable.test();
    }

    public ServiceControlRequestableProxy(IRequestable requestable) {
        this.requestable = requestable;
    }

    public static Log getLogger() {
        return logger;
    }

    public IRequestable getRequestable() {
        return requestable;
    }

    public void setRequestable(IRequestable requestable) {
        this.requestable = requestable;
    }

    //动态代理示例
    public static void main(String[] args) {
        IRequestable target = new RequestableImpl();
        IRequestable iRequestable =new ServiceControlRequestableProxy(target);
        iRequestable.request();
    }
}
