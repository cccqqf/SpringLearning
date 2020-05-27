package AOP;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * Created by CCCQQF on 2020/5/27.
 */
public class RequestCtrlCallback implements MethodInterceptor {
    private static final Log logger= LogFactory.getLog(RequestCtrlCallback.class);

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        if(method.getName().equals("request")){
            Date date=new Date();
            int hours=date.getHours();
            if(hours<6){
                logger.warn("service is not available now.");
                return null;
            }
            return methodProxy.invokeSuper(o,objects);
        }
        return null;
    }


    public static void main(String[] args) {
        Enhancer enhancer=new Enhancer();
        enhancer.setSuperclass(Subject.class);
        enhancer.setCallback(new RequestCtrlCallback());
        Subject proxy=(Subject)enhancer.create();
        proxy.request();
    }
}
