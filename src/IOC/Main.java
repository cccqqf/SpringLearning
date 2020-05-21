package IOC;


import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConstructorArgumentValues;

import org.springframework.beans.factory.support.*;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * Created by CCCQQF on 2020/5/19.
 */
public class Main {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {


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




        ApplicationContext context1=new ClassPathXmlApplicationContext("file:D:/IntelliJ_Space/IdeaProjects/Spring/src/IOC/IOCXML.xml");
        DowJonesNewsListener dowJonesNewsListener1=(DowJonesNewsListener)context1.getBean("djNewsListener");
        System.out.println(dowJonesNewsListener1.getEncodedPassword());

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

//使用beanpostProcessor对Listener类实例化前进行修改
class PasswordDecodePostProcessor implements BeanPostProcessor{

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof PasswordDecodable){
            ((PasswordDecodable) bean).setDecodedPassword(decodePassword(((PasswordDecodable) bean).getEncodedPassword()));
        }
        return bean;
    }

    private String decodePassword(String encodedPassword) {
        // 实现解码逻辑
        return "解密后的pwd："+encodedPassword;
    }
}