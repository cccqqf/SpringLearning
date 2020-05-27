package AOP;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Date;

/**
 * 使用动态代理创建代理对象
 * Created by CCCQQF on 2020/5/27.
 */
public class RequestCtrlInvocationHandler implements InvocationHandler {
    private static final Log logger= LogFactory.getLog(RequestCtrlInvocationHandler.class);
    private Object target;

    public RequestCtrlInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(method.getName().equals("request")){
            Date date=new Date();
            int hours=date.getHours();
            if(hours<6){
                logger.warn("service is not available now.");
                return null;
            }
            return method.invoke(target,args);
        }
        return null;
    }


    public static void main(String[] args) {
       IRequestable requestable=(IRequestable) Proxy.newProxyInstance(RequestableImpl.class.getClassLoader(),
               new Class[]{IRequestable.class},new RequestCtrlInvocationHandler(new RequestableImpl()));
       requestable.request();

    }
}
