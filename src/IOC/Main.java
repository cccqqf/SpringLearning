package IOC;

import org.springframework.beans.PropertyEditorRegistrySupport;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.beans.factory.config.Scope;
import org.springframework.beans.factory.support.*;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.beans.PropertyEditorSupport;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by CCCQQF on 2020/5/19.
 */
public class Main {
    public static void main(String[] args) {
        //直接编码方式进行注册
        DefaultListableBeanFactory beanRegisterByCode=new DefaultListableBeanFactory();
        BeanFactory beanFactory=bindViaCode(beanRegisterByCode);
        NewsProvider provider= (NewsProvider) beanFactory.getBean("djNewsProvider");
        provider.getAndPersistNews();

        /**XML：先读文件然后加载到definition，然后注册到BeanFactory，然后绑定，就多了一步读。
         * 可以一步直接返回
         * return new XmlBeanFactory(new ClassPathResource("../news-config.xml"));
         */
        DefaultListableBeanFactory beanRegisterByXml=new DefaultListableBeanFactory();
        BeanFactory container=bindViaXMLFile(beanRegisterByXml);
        NewsProvider newsProvider=(NewsProvider)container.getBean("djNewsProvider");
        newsProvider.getAndPersistNews();




        //基于注释方法
        ApplicationContext context=new ClassPathXmlApplicationContext("file:D:/IntelliJ_Space/IdeaProjects/Spring/src/IOC/IOC.xml");
        NewsProvider provider1=(NewsProvider) context.getBean("newsProvider");
        provider1.getAndPersistNews();
        DateFoo foo=(DateFoo)context.getBean("dateFoo");
        System.out.println(foo.getDate());
    }

    private static BeanFactory bindViaCode(DefaultListableBeanFactory beanRegister) {
        //创建类的定义用于之后的注册，让BeanFactory中可以存在一个实例。
        AbstractBeanDefinition provider=new RootBeanDefinition(NewsProvider.class);
        AbstractBeanDefinition listenr=new RootBeanDefinition(DowJonesNewsListener.class);
        AbstractBeanDefinition persister=new RootBeanDefinition(DowJonesNewsPersister.class);

        //然后绑定（注册）到beanRegister容器中
        beanRegister.registerBeanDefinition("djNewsProvider",provider);
        beanRegister.registerBeanDefinition("djListener",listenr);
        beanRegister.registerBeanDefinition("djPersister",persister);

        //绑定依赖关系，可以通过setter或者构造方式
        ConstructorArgumentValues argumentValues=new ConstructorArgumentValues();
        argumentValues.addIndexedArgumentValue(0,listenr);
        argumentValues.addIndexedArgumentValue(1,persister);
        provider.setConstructorArgumentValues(argumentValues);
        //下面是setter的方法
        //MutablePropertyValues propertyValues = new MutablePropertyValues();
        //propertyValues.addPropertyValue(new PropertyValue("newsListener",listenr));
        //propertyValues.addPropertyValue(new PropertyValue("newPersistener",persister));
        //provider.setPropertyValues(propertyValues);
        // 绑定完成
        return beanRegister;
    }

    private static BeanFactory bindViaXMLFile(DefaultListableBeanFactory registry){
        XmlBeanDefinitionReader reader=new XmlBeanDefinitionReader(registry);
        reader.loadBeanDefinitions("file:D:/IntelliJ_Space/IdeaProjects/Spring/src/IOC/IOCXML.xml");
        return registry;
        //return new XmlBeanFactory(new ClassPathResource("./IOCXML.xml"));
    }


}
