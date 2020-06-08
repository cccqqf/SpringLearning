package AOP;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.Advisor;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * Created by CCCQQF on 2020/6/8.
 */
public class  SubjectBeforeAdvice implements MethodBeforeAdvice {
    private static final Log logger= LogFactory.getLog(SubjectBeforeAdvice.class);

    @Override
    public void before(Method method, Object[] objects, Object o) throws Throwable {
        logger.info("在输出内容之前进行校验");
    }


    public static void main(String[] args) {
        Subject subject=new Subject();
        ProxyFactory weaver =new ProxyFactory(subject);
        //如果限制了pointcut就会对类里面的指定方法拦截，没有的话，就是所有的
        NameMatchMethodPointcut nameMatchMethodPointcut=new NameMatchMethodPointcut();
        nameMatchMethodPointcut.setMappedName("request");
        Advisor advisor=new DefaultPointcutAdvisor(nameMatchMethodPointcut,new SubjectBeforeAdvice());
        weaver.addAdvisor(advisor);
        Subject proxyObject=(Subject)weaver.getProxy();
        proxyObject.request();


        IRequestable impl=new RequestableImpl();
        ProxyFactory weaver1 =new ProxyFactory(impl);
        Advisor advisor1=new DefaultPointcutAdvisor(new SubjectBeforeAdvice());
        weaver1.addAdvisor(advisor1);
        IRequestable proxyObject1=(IRequestable)weaver1.getProxy();
        proxyObject1.request();
        proxyObject1.test();
    }



}


