package IOC;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 业务代码用于演示IOC
 * Created by CCCQQF on 2020/5/19.
 */
@Component
//这里可以加bean的别名，如果不加默认的是newsProvider，如果开头两个字母都是大写就返回原类名，
// 其他的话会把第一个字符改成小写命名规则具体看java.beans.Introspector.decapitalize方法
public class NewsProvider {
    @Autowired
    private Listener newsListener;
    @Autowired
    private Persister newsPersister;


    public static String seq="160";

    public void getAndPersistNews()
    {
        System.out.println("seq="+seq);
        String[] newsIds = newsListener.getAvailableNewsIds();
        System.out.println(newsIds.length);
        //判空

        newsPersister.persistNews(newsIds);

    }

    //IOC通过构造方法注入
    public NewsProvider(Listener newsListener, Persister newsPersister) {
        this.newsListener = newsListener;
        this.newsPersister = newsPersister;
    }
    //通过setter注入
    public void setNewsListener(Listener newsListener) {
        this.newsListener = newsListener;
    }
    public void setNewsPersister(Persister newsPersister) {
        this.newsPersister = newsPersister;
    }
    //接口注入，比较麻烦
}
interface Listener{
    String[] getAvailableNewsIds();
}
interface Persister{
    void persistNews(String[] newsIds);
}
@Component
class DowJonesNewsListener implements Listener{

    @Override
    public String[] getAvailableNewsIds() {
        System.out.println("getAvailableNewsIds");
        return new String[0];
    }
}
@Component
class DowJonesNewsPersister implements Persister{

    @Override
    public void persistNews(String[] newsIds) {
        System.out.println("DowJonesNews");
    }

    public void getNewsBean(){
        System.out.println(this.toString());
    }
}


class fresh{
    static {
        NewsProvider.seq="189";
    }
}