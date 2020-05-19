package Transaction;

import org.springframework.transaction.*;
import org.springframework.transaction.support.DefaultTransactionStatus;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 简单的实现了一个基于PlatformTransactionManager的事务管理。
 * Created by CCCQQF on 2020/5/17.
 */
public class JdbcTransactionManager implements PlatformTransactionManager {
    private DataSource dataSource;

    public JdbcTransactionManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public TransactionStatus getTransaction(TransactionDefinition transactionDefinition) throws TransactionException {
        Connection connection;
        try {
            connection=dataSource.getConnection();
            TransactionResourceManager.bindResources(connection);
            return new DefaultTransactionStatus(connection,true,true,false,true,null);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new CannotCreateTransactionException("can't get connection for tx", e);
        }
    }

    @Override
    public void commit(TransactionStatus transactionStatus) throws TransactionException {
        Connection connection=(Connection)TransactionResourceManager.getResource();
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new TransactionSystemException("commit failed with SQLException",e);
        }finally {
            try{
                connection.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void rollback(TransactionStatus transactionStatus) throws TransactionException {
        Connection connection=(Connection) TransactionResourceManager.unbindResources();
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new UnexpectedRollbackException("rollback failed withSQLException",e);
        }finally {
            try{
                connection.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }

    }
}


