package com.rise.autotest.robot.db.keywords;

import com.rise.autotest.robot.FailureException;
import com.rise.autotest.robot.db.ConnectionManager;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

import java.sql.Connection;
import java.sql.SQLException;

@RobotKeywords
public class TransactionKeywords {

    /**
     * begin a new transaction, sets the auto-commit=false to start a new transaction.
     */
    @RobotKeyword
    public void beginTransaction() {
        try {
            ConnectionManager.instance().getCurrent().setAutoCommit(false);
        } catch (SQLException e) {
            throw new FailureException("Could not start a new transaction.", e);
        }
    }

    /**
     * Call 'commit' on the current transaction.
     */
    @RobotKeyword
    public void commitTransaction() {
        try {
            ConnectionManager.instance().getCurrent().commit();
        } catch (SQLException e) {
            throw new FailureException("Could not commit the transaction.", e);
        }
    }

    /**
     * Calls 'rollback' on the current transaction.
     */
    @RobotKeyword
    public void rollbackTransaction() {

        try {
            ConnectionManager.instance().getCurrent().rollback();
        } catch (SQLException e) {
            throw new FailureException("Could not rollback the transaction.",e );
        }
    }

    /**
     * Sets the desired transaction isolation level.<br>
     *
     * <table>
     * <tr>
     * <td>Isolation Level</td><td>Transactions</td><td>Dirty Reads</td><td>Non-repetable Reads</td><td>Phantom Reads</td>
     * <td>TRANSACTION_NONE</td><td>Not supported</td><td>N/A</td><td>N/A</td><td>N/A</td>
     * <td>TRANSACTION_READ_COMMITTED</td><td>Supported</td><td>Prevented</td><td>Allowed</td><td>Allowed</td>
     * <td>TRANSACTION_READ_UNCOMMITTED</td><td>Supported</td><td>Allowed</td><td>Allowed</td><td>Allowed</td>
     * <td>TRANSACTION_REPEATABLE_READ</td><td>Supported</td><td>Prevented</td><td>Prevented</td><td>Allowed</td>
     * <td>TRANSACTION_SERIALIZABLE</td><td>Supported</td><td>Prevented</td><td>Prevented</td><td>Prevented</td>
     * </tr>
     * </table>
     *
     */
    @RobotKeyword
    @ArgumentNames({"isolationLevel"})
    public void setTransactionIsolation(String isolationLevel) {
        int isolation;
        switch (isolationLevel.toUpperCase()){
            case "TRANSACTION_READ_COMMITTED":
                isolation = Connection.TRANSACTION_READ_COMMITTED;
                break;
            case "TRANSACTION_READ_UNCOMMITTED":
                isolation = Connection.TRANSACTION_READ_UNCOMMITTED;
                break;
            case "TRANSACTION_REPEATABLE_READ":
                isolation = Connection.TRANSACTION_REPEATABLE_READ;
                break;
            case "TRANSACTION_SERIALIZABLE":
                isolation = Connection.TRANSACTION_SERIALIZABLE;
                break;
            default:
                isolation = Connection.TRANSACTION_NONE;
                break;
        }
        try {
            ConnectionManager.instance().getCurrent().setTransactionIsolation(isolation);
        } catch (SQLException e) {
            throw new FailureException("Could not set the transaction isolation level successfully", e);
        }
    }

}