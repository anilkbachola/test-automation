package com.rise.autotest.robot.db.keywords;

import com.rise.autotest.robot.FailureException;
import com.rise.autotest.robot.db.ConnectionManager;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywordOverload;
import org.robotframework.javalib.annotation.RobotKeywords;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@RobotKeywords
public class QueryKeywords {

    private static final String COUNT_QUERY = "SELECT COUNT(*) FROM %s";
    private static final String COUNT_QUERY_WITH_WHERE = "SELECT COUNT(*) FROM %s WHERE %s";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM  %s";
    private static final String SELECT_ALL_QUERY_WITH_WHERE = "SELECT * FROM %s WHERE %s";
    private static final String DELETE_QUERY = "DELETE FROM %s";
    private static final String DELETE_QUERY_WITH_WHERE = "DELETE FROM %s WHERE %s";


    /**
     * Get number of rows in table <b>tableName</b>. Use optional where clause <b>whereClause</b> if provided. <br>
     *
     * @param tableName Name of the table.
     * @param whereClause   where clause.
     * @return  number of rows.
     */
    @RobotKeyword
    @ArgumentNames({"tableName", "whereClause=NONE"})
    public int getRowCount(String tableName, String whereClause) {
        return rowCount(tableName, whereClause);
    }

    /**
     * Execure the given <b>sql</b> command.<br>
     *
     * @param sql   sql to execute.
     */
    @RobotKeyword
    @ArgumentNames({"sql"})
    public void executeQuery(String sql) {
        Connection con = ConnectionManager.instance().getCurrent();
        try (PreparedStatement psmt = con.prepareStatement(sql)) {
            psmt.execute();
        } catch (SQLException e) {
            throw new FailureException(String.format("Error executing query '%s'", e.getMessage()),e );
        }
    }

    @RobotKeywordOverload
    @ArgumentNames({"tableName"})
    public void deleteRows(String tableName){
        deleteRows(tableName, null);
    }

    /**
     * Delete rows in the table <b>tableName</b>. Optionally use <b>whereClause</b> to limit the rows to be deleted.<br>
     *
     * @param tableName name of the table.
     * @param whereClause   where clause
     */
    @RobotKeyword
    @ArgumentNames({"tableName", "whereClause=NONE"})
    public void deleteRows(String tableName, String whereClause) {
        String sql = whereClause == null ? String.format(DELETE_QUERY, tableName) :
                String.format(DELETE_QUERY_WITH_WHERE, tableName, whereClause);

        Connection con = ConnectionManager.instance().getCurrent();
        try (PreparedStatement psmt = con.prepareStatement(sql)) {
            psmt.execute();
        } catch (SQLException e) {
            throw new FailureException(String.format("Error executing query '%s'", e.getMessage()),e );
        }
    }

    /**
     * Calls a stored procedure.<br>
     * @param procedure name of the procedure with optional params.
     * @return  true if success otherwise throws exception.
     */
    @RobotKeyword
    @ArgumentNames({"procedure"})
    public boolean callStoredProcedure(String procedure) {
        Connection con = ConnectionManager.instance().getCurrent();
        String callable = "{call "+procedure+"}";
        try (CallableStatement callableStatement = con.prepareCall(callable)){
            return callableStatement.execute();
        } catch (SQLException e) {
            throw new FailureException("Error calling the stored procedure.",e);
        }
    }

    private int rowCount(String tableName, String whereClause) {
        String selectStmt = whereClause == null ? String.format(COUNT_QUERY, tableName) :
                String.format(COUNT_QUERY_WITH_WHERE, tableName, whereClause);

        Connection con = ConnectionManager.instance().getCurrent();
        try (PreparedStatement psmt = con.prepareStatement(selectStmt, ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY)){
            ResultSet rs = psmt.executeQuery();
            rs.next();
            return rs.getInt(1) ;
        } catch (SQLException e) {
            //Count(*) is not supported, try with select *
            selectStmt = whereClause == null ? String.format(SELECT_ALL_QUERY, tableName) :
                    String.format(SELECT_ALL_QUERY_WITH_WHERE, tableName, whereClause);

            try (PreparedStatement psmt = con.prepareStatement(selectStmt, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY)){
                ResultSet rs = psmt.executeQuery();
                rs.last();
                return rs.getRow();
            } catch (SQLException ex) {
                throw new FailureException(String.format("Error executing query '%s'.", ex.getMessage()), e);
            }
        }
    }
}
