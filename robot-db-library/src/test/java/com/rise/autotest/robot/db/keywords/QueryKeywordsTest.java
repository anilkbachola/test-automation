package com.rise.autotest.robot.db.keywords;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import com.rise.autotest.robot.FailureException;
import com.rise.autotest.robot.db.ConnectionManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.sql.SQLException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class QueryKeywordsTest {

    private QueryKeywords queryKeywords = new QueryKeywords();

    private ConnectionKeywords connectionKeywords = new ConnectionKeywords();
    private ConnectionManager connectionManager = ConnectionManager.instance();

    @BeforeAll
    void setup() {
        connectionKeywords.openConnection("db.properties");
        queryKeywords.executeQuery("create table if not exists users(user_id varchar(50), user_name varchar(50), email varchar(250))");
        queryKeywords.executeQuery("insert into users values('100', 'jamescooper', 'james.cooper@007.com')");
        queryKeywords.executeQuery("insert into users values('101', 'jamesbond', 'james.bond@007.com')");

        queryKeywords.executeQuery("create table if not exists temp(temp_id varchar(50))");

    }

    @AfterAll
    void cleanup() throws SQLException {
        connectionKeywords.closeAllConnections();
    }

    @Test
    void testRowCount_WithoutWhereClause_ShouldReturnNumberOfRowsInTable() {
        String tableName = "users";
        int rowCount = queryKeywords.getRowCount(tableName, null);
        assertThat(rowCount).isEqualTo(2);
    }

    @Test
    void testRowCount_WithWhereClause_ShouldReturnNumberOfRowsMatchingWhereClause() {
        String tableName = "users";
        String whereClause = "user_id='100'";
        int rowCount = queryKeywords.getRowCount(tableName, whereClause);
        assertThat(rowCount).isEqualTo(1);
    }

    @Test
    void testRowCount_NonExistingTable_ShouldThrowFailureException() {
        String nonExistingTable = "dummy";

        assertThatExceptionOfType(FailureException.class)
                .isThrownBy(() -> queryKeywords.getRowCount(nonExistingTable, null))
                .withMessageStartingWith("Error");
    }

    @Test
    void testRowCount_InvalidColumnsInWhereClause_ShouldThrowFailureException() {
        String nonExistingTable = "users";
        String whereClause = "xyz='100'";

        assertThatExceptionOfType(FailureException.class)
                .isThrownBy(() -> queryKeywords.getRowCount(nonExistingTable, whereClause))
                .withMessageStartingWith("Error executing query");
    }

    @Test
    void testRowCount_InvalidWhereClause_ShouldThrowFailureException() {
        String nonExistingTable = "users";
        String whereClause = "user_id='100' user_name='jamesbond'"; // no AND

        assertThatExceptionOfType(FailureException.class)
                .isThrownBy(() -> queryKeywords.getRowCount(nonExistingTable, whereClause))
                .withMessageStartingWith("Error executing query");
    }

    @Test
    void testExecuteQuery_ValidInsertQuery_ShouldSucceed() {
        String insertQuery = "insert into users(user_id, user_name, email) values('102', 'jamesbond1', 'james.bond1@007.com')";

        queryKeywords.executeQuery(insertQuery);
    }


    @Test
    void testExecuteQuery_ValidSelectQuery_ShouldSucceed() {
        String selectQuery = "SELECT user_id FROM users";

        queryKeywords.executeQuery(selectQuery);

    }

    @Test
    void testExecuteQuery_ValidUpdateQuery_ShouldSucceed() {
        String updateQuery = "UPDATE users SET user_name='cooper' WHERE user_id='100'";

        queryKeywords.executeQuery(updateQuery);
    }

    @Test
    void testExecuteQuery_ValidAlterQuery_ShouldSucceed() {
        String alterQuery = "ALTER TABLE users ADD(phone VARCHAR(50))";

        queryKeywords.executeQuery(alterQuery);
    }

    @Test
    void testExecuteQuery_InValidQuery_ShouldThrowFailureException() {
        String updateQuery = "Update dummy SET user_name='cooper' WHERE user_id='100'";

        assertThatExceptionOfType(FailureException.class).isThrownBy(() -> queryKeywords.executeQuery(updateQuery))
                .withMessageStartingWith("Error executing query");
    }

    @Test
    void testDeleteRows_WithWhereClause_ShouldSucceed() {
        queryKeywords.executeQuery("INSERT INTO temp VALUES('1000')");
        queryKeywords.executeQuery("INSERT INTO temp VALUES('1001')");
        queryKeywords.executeQuery("INSERT INTO temp VALUES('1002')");

        queryKeywords.deleteRows("temp","temp_id='1000'");

        assertThat(queryKeywords.getRowCount("temp", null)).isEqualTo(2);
    }

}