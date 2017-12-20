package com.rise.autotest.robot.db.keywords;

import com.rise.autotest.robot.FailureException;
import com.rise.autotest.robot.FatalException;
import com.rise.autotest.robot.db.ConnectionManager;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.Autowired;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywordOverload;
import org.robotframework.javalib.annotation.RobotKeywords;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

@RobotKeywords
public class ValidationKeywords {



    @Autowired
    private QueryKeywords queryKeywords;

    /**
     * Verify table <b>tableName</b> exists in default schema. <br>
     *
     * @param tableName name of the table.
     */
    @RobotKeywordOverload
    public void tableMustExist(String tableName) {
        tableMustExist(tableName, null);
    }

    /**
     * Verify table <b>tableName</b> exists in the database schema <b>schemaName</b>. <br>
     *
     * @param tableName     name of the table.
     * @param schemaName    name of the schema. Optional, will check in default schema if not provided.
     */
    @RobotKeyword
    @ArgumentNames({"tableName", "schemaName=NONE"})
    public void tableMustExist(String tableName, String schemaName) {
        Connection con = ConnectionManager.instance().getCurrent();
        try {
            DatabaseMetaData metaData = con.getMetaData();
            ResultSet rs = metaData.getTables(null, (schemaName == null) ? null : schemaName.toUpperCase() ,
                    tableName.toUpperCase(), new String[]{"TABLE"});
            if(!rs.first()) {
                throw new FailureException(schemaName == null ?
                        String.format("Table '%s' expected to be exist, but not.", tableName) :
                        String.format("Table '%s' is expected to be exist in schema '%s', but not.", tableName, schemaName)
                );
            }
        } catch (SQLException e) {
            throw new FatalException("Error occurred while executing.", e);
        }
    }

    /**
     * Verify column <b>columnName</b> exists in table <b>tableName</b> in default schema.<br>
     *
     * @param tableName     name of the table.
     * @param columnName    name of the column.
     */
    @RobotKeywordOverload
    public void tableShouldContainColumn(String tableName, String columnName) {
        tableShouldContainColumn(tableName, columnName, null);
    }

    /**
     * Verify column <b>columnName</b> exists in table <b>tableName</b> in database schema <b>schemaName</b>. <br>
     *
     * @param tableName     name of the table.
     * @param columnName    name of the column.
     * @param schemaName    name of the schema.
     */
    @RobotKeyword
    @ArgumentNames({"tableName", "columnName", "schemaName=NONE"})
    public void tableShouldContainColumn(String tableName, String columnName, String schemaName) {
        Connection con = ConnectionManager.instance().getCurrent();
        try {
            DatabaseMetaData metaData = con.getMetaData();
            ResultSet rs = metaData.getColumns(null, (schemaName == null) ? null : schemaName.toUpperCase(),
                    tableName.toUpperCase(), columnName.toUpperCase());
            if(!rs.first()) {
                throw new FailureException(schemaName == null?
                        String.format("Column '%s' is expected to exist in table '%s', but not.", columnName, tableName ) :
                        String.format("Column '%s' is expected to exist in table '%s' and schema '%s', but not.", columnName, tableName, schemaName )
                );
            }
        } catch (SQLException e) {
            throw new FatalException("Error occurred while executing.", e);
        }
    }

    /**
     * Verify the table <b>tableName</b> is empty.<br>
     *
     * @param tableName     name of the table.
     */
    @RobotKeyword
    @ArgumentNames({"tableName"})
    public void tableMustBeEmpty(String tableName) {
        long rowCount = queryKeywords.getRowCount(tableName, null);
        if(rowCount > 0) {
            throw new FailureException(String.format("Table '%s' expected to be empty, but not.", tableName));
        }
    }

    /**
     * Verify number of rows in table <b>tableName</b> are equal to number of rows in table <b>anotherTable</b>.<br>
     *
     * @param tableName     name of the table.
     * @param anotherTable  name of the anothe table.
     */
    @RobotKeyword
    @ArgumentNames({"tableName", "anotherTableName"})
    public void tablesMustContainSameNumberOfRows(String tableName, String anotherTable) {
        long rowCount = queryKeywords.getRowCount(tableName, null);
        long otherRowCount = queryKeywords.getRowCount(anotherTable, null);
        String msg = String.format("Number of rows in table '%s' is expected to be equal to of table '%s'.",
                tableName, anotherTable);
        if(rowCount != otherRowCount) {
            throw new FailureException(msg);
        }
    }

    /**
     * Verify number of rows in table <b>tableName</b> is euqls to <b>expectedCount</b>.<br>
     *
     * @param tableName     Name of the table.
     * @param expectedCount         Expected count.
     */
    @RobotKeywordOverload
    public void numberOfRowsShouldBe(String tableName, Long expectedCount) {
        numberOfRowsShouldBe(tableName, expectedCount, null);
    }

    /**
     * Verify the number of rows in table <b>tableName</b> is equal to <b>count</b> when where clause<b>whereClause</b> is applied.<br>
     *
     * @param tableName     Name of the table.
     * @param expectedCount Expected count.
     * @param whereClause   Where Clause.
     */
    @RobotKeyword
    @ArgumentNames({"tableName", "expectedCount", "whereClause=NONE"})
    public void numberOfRowsShouldBe(String tableName, Long expectedCount, String whereClause) {

        long rowCount = queryKeywords.getRowCount(tableName, whereClause);
        if(rowCount != expectedCount) {
            throw new FailureException(
                    String.format("Number of rows in table '%s' expected to be '%d', but found to be '%d'",
                            tableName, expectedCount, rowCount)
            );
        }
    }

    /**
     * Verify the number of rows in table <b>tableName</b> are less than <b>expectedCount</b>. <br>
     *
     * @param tableName     Name of the table.
     * @param expectedCount Expected count.
     */
    @RobotKeywordOverload
    public void numberOfRowsShouldBeLessThan(String tableName, Long expectedCount) {
        numberOfRowsShouldBeLessThan(tableName, expectedCount, null);
    }

    /**
     * Verify the number of rows in table <b>tableName</b> are greater than <b>expectedCount</b>. <br>
     * @param tableName     Name of the table.
     * @param expectedCount Expected count.
     */
    @RobotKeywordOverload
    public void numberOfRowsShouldBeGreaterThan(String tableName, Long expectedCount) {
        numberOfRowsShouldBeGreaterThan(tableName, expectedCount, null);
    }

    /**
     * Verify the number of rows in table <b>tableName</b> are less than <b>expectedCount</b>
     * when where clause<b>whereClause</b> is applied.<br>
     *
     * @param tableName     Name of the table.
     * @param expectedCount Expected count.
     * @param whereClause   Where Clause.
     */
    @RobotKeyword
    @ArgumentNames({"tableName", "expectedCount", "whereClause=NONE"})
    public void numberOfRowsShouldBeLessThan(String tableName, Long expectedCount, String whereClause) {

        long rowCount = queryKeywords.getRowCount(tableName, whereClause);
        if(rowCount >= expectedCount) {
            throw new FailureException(
                    String.format("Number of rows in table '%s' expected to be less than '%d', but found to be '%d'",
                            tableName, expectedCount, rowCount)
            );
        }
    }

    /**
     * Verify the number of rows in table <b>tableName</b> are greater than <b>expectedCount</b>
     * when where clause<b>whereClause</b> is applied.<br>
     *
     * @param tableName     Name of the table.
     * @param expectedCount Expected count.
     * @param whereClause   Where Clause.
     */
    @RobotKeyword
    @ArgumentNames({"tableName", "expectedCount", "whereClause=NONE"})
    public void numberOfRowsShouldBeGreaterThan(String tableName, Long expectedCount, String whereClause) {
        long rowCount = queryKeywords.getRowCount(tableName, whereClause);
        if(rowCount <= expectedCount) {
            throw new FailureException(
                    String.format("Number of rows in table '%s' expected to be greater than '%d', but found to be '%d'",
                            tableName, expectedCount, rowCount)
            );
        }
    }

    public void setQueryKeywords(QueryKeywords queryKeywords) {
        this.queryKeywords = queryKeywords;
    }
}
