package IOC;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by CCCQQF on 2020/5/20.
 */
public class CustomScopeTest {
    public static void main(String[] args) {
        DefaultListableBeanFactory registry=new DefaultListableBeanFactory();
        registry.registerScope("thread",new ThreadScope());
        BeanFactory container=bindViaXMLFile(registry);
        Thread thread1=new Thread(() -> {
            NewsProvider newsProvider=(NewsProvider)container.getBean("djNewsProvider");
            System.out.println(newsProvider.toString());
        });
        Thread thread2=new Thread(() -> {
            NewsProvider newsProvider=(NewsProvider)container.getBean("djNewsProvider");
            System.out.println(newsProvider.toString());
        });
        thread1.start();
        thread2.start();
    }

    private static BeanFactory bindViaXMLFile(BeanDefinitionRegistry registry){
        XmlBeanDefinitionReader reader=new XmlBeanDefinitionReader(registry);
        reader.loadBeanDefinitions("file:D:/IntelliJ_Space/IdeaProjects/Spring/src/IOC/IOCXML.xml");
        return (BeanFactory) registry;
        //return new XmlBeanFactory(new ClassPathResource("./IOCXML.xml"));
    }
}
class ThreadScope implements Scope {
    private final ThreadLocal threadScope= ThreadLocal.withInitial(() -> new HashMap<>());

    @Override
    public Object get(String s, ObjectFactory<?> objectFactory) {
        Map scope=(HashMap)threadScope.get();
        Object o=scope.get(s);
        if(o==null){
            o=objectFactory.getObject();
            scope.put(s,o);
        }
        return o;
    }

    @Override
    public Object remove(String s) {
        Map scope=(HashMap)threadScope.get();
        return scope.remove(s);
    }

    @Override
    public void registerDestructionCallback(String s, Runnable runnable) {
    }

    @Override
    public Object resolveContextualObject(String s) {
        return null;
    }

    @Override
    public String getConversationId() {
        return null;
    }
}
