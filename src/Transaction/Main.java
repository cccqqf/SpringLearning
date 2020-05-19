package Transaction;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.TransactionStatus;

import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 如果使用注释@Transaction，用于分离事务管理代码和业务代码，业务管理代码去获取有@Transaction注释的method，
 * 然后创建要给TransactionTemplate，根据@Transaction的属性设置去设置这个TransactionTemplate，最后调用transactionTemplate.execute（），
 * 在execute中调用业务代码。
 * Created by CCCQQF on 2020/5/17.
 */
public class Main {
    public static void main(String[] args) {
        MysqlDataSource dataSource=new MysqlDataSource();
        dataSource.setServerName("localhost");
        dataSource.setPort(3306);
        dataSource.setDatabaseName("student");
        dataSource.setUser("root");
        dataSource.setPassword("160104400106");
        TransactionTemplate transactionTemplate=new TransactionTemplate(new DataSourceTransactionManager(dataSource));
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                    Connection conn=DataSourceUtils.getConnection(dataSource);
                Statement stmt1 = null;
                Statement stmt2 = null;
                Statement stmt3 = null;
                try {
                    stmt1 = conn.createStatement();
                    stmt2 = conn.createStatement();
                    stmt3 = conn.createStatement();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                Object savePoint=null;
                try {
                    stmt1.execute("insert into student(Sno,Sname) value('777','崔奇峰')");
                    savePoint=transactionStatus.createSavepoint();
                    stmt2.execute("insert into student(Sno,Sname)  value  ('222','崔奇峰')");

                    stmt3.execute("insert into student(Sno,Sname)value('222','崔奇峰')");
                } catch (SQLException e) {
                    e.printStackTrace();
                    transactionStatus.rollbackToSavepoint(savePoint);
                }finally {
                    transactionStatus.releaseSavepoint(savePoint);
                }


            }
        });
    }
}

