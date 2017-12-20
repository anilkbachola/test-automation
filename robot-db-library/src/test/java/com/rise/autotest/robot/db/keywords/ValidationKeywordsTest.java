package com.rise.autotest.robot.db.keywords;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import com.rise.autotest.robot.FailureException;
import com.rise.autotest.robot.db.ConnectionManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.sql.SQLException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ValidationKeywordsTest {

    private ConnectionKeywords connectionKeywords = new ConnectionKeywords();
    private ConnectionManager connectionManager = ConnectionManager.instance();
    private ValidationKeywords validationKeywords = new ValidationKeywords();
    private QueryKeywords queryKeywords = new QueryKeywords();

    @BeforeAll
    void setup() {
        connectionKeywords.openConnection("db.properties");
        queryKeywords.executeQuery("create table if not exists temp(temp_id varchar(50))");
        queryKeywords.executeQuery("create schema if not exists temp_schema AUTHORIZATION SA");
        queryKeywords.executeQuery("create table if not exists temp_schema.temp_users(temp_id varchar(50))");
        queryKeywords.executeQuery("create table if not exists count_table(id varchar(50))");

        validationKeywords.setQueryKeywords(queryKeywords);
        queryKeywords.deleteRows("count_table");
    }

    @AfterAll
    void cleanup() throws SQLException {
        connectionKeywords.closeAllConnections();
    }

    @Test
    void testTableMustExist_ExistingTable_DefaultSchema_ShouldSucceed() {
        validationKeywords.tableMustExist("temp");
    }

    @Test
    void testTableMustExist_ExistingTable_ExplicitSchema_ShouldSucceed() {
        validationKeywords.tableMustExist("temp_users","temp_schema");
    }

    @Test
    void testTableMustExist_ExistingTable_InvalidSchema_ShouldThrowFailureException() {
        assertThatExceptionOfType(FailureException.class).isThrownBy(
                () -> validationKeywords.tableMustExist("temp_users", "dummy_schema")
        ).withMessage("Table 'temp_users' is expected to be exist in schema 'dummy_schema', but not.");
    }

    @Test
    void testTableMustExist_NonExistingTable_ExplicitSchema_ShouldThrowFailureException() {
        assertThatExceptionOfType(FailureException.class).isThrownBy(
                () -> validationKeywords.tableMustExist("dummy_table", "temp_schema")
        ).withMessage("Table 'dummy_table' is expected to be exist in schema 'temp_schema', but not.");
    }

    @Test
    void testTableMustExist_NonExistingTable_DefaultSchema_ShouldThrowFailureException() {
        assertThatExceptionOfType(FailureException.class).isThrownBy(
                () -> validationKeywords.tableMustExist("dummy_table")
        ).withMessage("Table 'dummy_table' expected to be exist, but not.");
    }

    @Test
    void tableTableShouldContainColumn_NonExistingTable_ShouldThrowFailureException() {
        assertThatExceptionOfType(FailureException.class).isThrownBy(
                () -> validationKeywords.tableShouldContainColumn("dummy_table","dummy_column")
        ).withMessage("Column 'dummy_column' is expected to exist in table 'dummy_table', but not.");
    }

    @Test
    void tableTableShouldContainColumn_ExistingTable_And_NonExistingColumn_ShouldThrowFailureException() {
        assertThatExceptionOfType(FailureException.class).isThrownBy(
                () -> validationKeywords.tableShouldContainColumn("temp","dummy_column")
        ).withMessage("Column 'dummy_column' is expected to exist in table 'temp', but not.");
    }

    @Test
    void tableTableShouldContainColumn_ExistingTable_And_Column_DefaultSchema_ShouldSucceed() {
        validationKeywords.tableShouldContainColumn("temp","temp_id");
    }

    @Test
    void tableTableShouldContainColumn_ExistingTable_And_Column_ExplicitSchema_ShouldSucceed() {
        validationKeywords.tableShouldContainColumn("temp_users","temp_id","temp_schema");
    }

    @Test
    void testTableMustBeEmpty_EmptyTable_ShouldSucceed() {
        queryKeywords.executeQuery("create table if not exists empty_table(id varchar(50))");
        validationKeywords.tableMustBeEmpty("empty_table");
    }

    @Test
    void testTableMustBeEmpty_NonEmptyTable_ShouldThrowFailureException() {
        queryKeywords.executeQuery("create table if not exists nonempty_table(id varchar(50))");
        queryKeywords.executeQuery("INSERT INTO nonempty_table VALUES('1000')");

        assertThatExceptionOfType(FailureException.class).isThrownBy(
                () -> validationKeywords.tableMustBeEmpty("nonempty_table")
        ).withMessage("Table 'nonempty_table' expected to be empty, but not.");
    }


    @Test
    void testTablesMustContainSameNumberOfRows_WhenExistingTablesHavingSameNumberOfRows_ShouldSucceed() {
        queryKeywords.executeQuery("create table if not exists table1(id varchar(50))");
        queryKeywords.executeQuery("create table if not exists table2(id varchar(50))");
        queryKeywords.executeQuery("INSERT INTO table1 VALUES('1000')");
        queryKeywords.executeQuery("INSERT INTO table2 VALUES('2000')");

        validationKeywords.tablesMustContainSameNumberOfRows("table1", "table2");
    }

    @Test
    void testTablesMustContainSameNumberOfRows_WhenExistingTablesNotHavingSameNumberOfRows_ShouldThrowFailureException() {
        queryKeywords.executeQuery("create table if not exists table1(id varchar(50))");
        queryKeywords.executeQuery("create table if not exists table2(id varchar(50))");
        queryKeywords.executeQuery("INSERT INTO table1 VALUES('3000')");
        assertThatExceptionOfType(FailureException.class).isThrownBy(
                () -> validationKeywords.tablesMustContainSameNumberOfRows("table1", "table2")
        ).withMessage("Number of rows in table 'table1' is expected to be equal to of table 'table2'.");
    }

    @Test
    void testTablesMustContainSameNumberOfRows_WhenNonExistingTables_ShouldThrowFailureException() {
        assertThatExceptionOfType(FailureException.class).isThrownBy(
                () -> validationKeywords.tablesMustContainSameNumberOfRows("dummy_table1", "dummy_table2")
        ).withMessageStartingWith("Error executing query");
    }

    @Test
    void testNumberOfRowsShouldBe_WithoutWhereClause_WhenCountMatches_ShouldSucceed() {
        queryKeywords.executeQuery("INSERT INTO count_table VALUES('3000')");
        validationKeywords.numberOfRowsShouldBe("count_table", 1L);
        queryKeywords.deleteRows("count_table");
    }

    @Test
    void testNumberOfRowsShouldBe_WithoutWhereClause_WhenCountDoesNotMatch_ShouldThrowFailureException() {
        queryKeywords.executeQuery("INSERT INTO count_table VALUES('3000')");

        assertThatExceptionOfType(FailureException.class).isThrownBy(
                () -> validationKeywords.numberOfRowsShouldBe("count_table", 2L)
        ).withMessage("Number of rows in table 'count_table' expected to be '2', but found to be '1'");
        queryKeywords.deleteRows("count_table");
    }

    @Test
    void testNumberOfRowsShouldBe_WithWhereClause_WhenCountMatches_ShouldSucceed() {
        queryKeywords.executeQuery("INSERT INTO count_table VALUES('3000')");
        queryKeywords.executeQuery("INSERT INTO count_table VALUES('4000')");
        validationKeywords.numberOfRowsShouldBe("count_table", 1L, "id='3000'");
        queryKeywords.deleteRows("count_table");
    }

    @Test
    void testNumberOfRowsShouldBe_WithWhereClause_WhenCountDoesNotMatch_ShouldThrowFailureException() {
        queryKeywords.executeQuery("INSERT INTO count_table VALUES('3000')");
        queryKeywords.executeQuery("INSERT INTO count_table VALUES('4000')");

        assertThatExceptionOfType(FailureException.class).isThrownBy(
                () -> validationKeywords.numberOfRowsShouldBe("count_table", 2L,"id='3000'")
        ).withMessage("Number of rows in table 'count_table' expected to be '2', but found to be '1'");
        queryKeywords.deleteRows("count_table");
    }

    @Test
    void testNumberOfRowsShouldBeLessThan_WithoutWhereClause_CountLesserThanGiven_ShouldSucceed() {
        queryKeywords.executeQuery("INSERT INTO count_table VALUES('3000')");
        queryKeywords.executeQuery("INSERT INTO count_table VALUES('4000')");
        validationKeywords.numberOfRowsShouldBeLessThan("count_table", 3L);
        queryKeywords.deleteRows("count_table");
    }

    @Test
    void testNumberOfRowsShouldBeLessThan_WithoutWhereClause_CountGreaterThanGiven_ShouldThrowFailureException() {
        queryKeywords.executeQuery("INSERT INTO count_table VALUES('3000')");
        queryKeywords.executeQuery("INSERT INTO count_table VALUES('4000')");

        assertThatExceptionOfType(FailureException.class).isThrownBy(
                () -> validationKeywords.numberOfRowsShouldBeLessThan("count_table", 1L)
        ).withMessage("Number of rows in table 'count_table' expected to be less than '1', but found to be '2'");
        queryKeywords.deleteRows("count_table");
    }
    @Test
    void testNumberOfRowsShouldBeLessThan_WithWhereClause_CountLesserThanGiven_ShouldSucceed() {
        queryKeywords.executeQuery("INSERT INTO count_table VALUES('3000')");
        queryKeywords.executeQuery("INSERT INTO count_table VALUES('4000')");
        validationKeywords.numberOfRowsShouldBeLessThan("count_table",2L, "id='3000'");
        queryKeywords.deleteRows("count_table");
    }

    @Test
    void testNumberOfRowsShouldBeLessThan_WithWhereClause_CountGreaterThanGiven_ShouldThrowFailureException() {
        queryKeywords.executeQuery("INSERT INTO count_table VALUES('3000')");
        queryKeywords.executeQuery("INSERT INTO count_table VALUES('3000')");
        queryKeywords.executeQuery("INSERT INTO count_table VALUES('4000')");

        assertThatExceptionOfType(FailureException.class).isThrownBy(
                () -> validationKeywords.numberOfRowsShouldBeLessThan("count_table",1L, "id='3000'")
        ).withMessage("Number of rows in table 'count_table' expected to be less than '1', but found to be '2'");
        queryKeywords.deleteRows("count_table");
    }



    @Test
    void testNumberOfRowsShouldBeGreaterThan_WithoutWhereClause_CountGreaterThanGiven_ShouldSucceed() {
        queryKeywords.executeQuery("INSERT INTO count_table VALUES('3000')");
        queryKeywords.executeQuery("INSERT INTO count_table VALUES('4000')");
        validationKeywords.numberOfRowsShouldBeGreaterThan("count_table", 1L);
        queryKeywords.deleteRows("count_table");
    }

    @Test
    void testNumberOfRowsShouldBeGreaterThan_WithoutWhereClause_CountLesserThanGiven_ShouldThrowFailureException() {
        queryKeywords.executeQuery("INSERT INTO count_table VALUES('3000')");
        queryKeywords.executeQuery("INSERT INTO count_table VALUES('4000')");

        assertThatExceptionOfType(FailureException.class).isThrownBy(
                () -> validationKeywords.numberOfRowsShouldBeGreaterThan("count_table", 2L)
        ).withMessage("Number of rows in table 'count_table' expected to be greater than '2', but found to be '2'");
        queryKeywords.deleteRows("count_table");
    }
    @Test
    void testNumberOfRowsShouldBeGreaterThan_WithWhereClause_CountGreaterThanGiven_ShouldSucceed() {
        queryKeywords.executeQuery("INSERT INTO count_table VALUES('3000')");
        queryKeywords.executeQuery("INSERT INTO count_table VALUES('3000')");
        queryKeywords.executeQuery("INSERT INTO count_table VALUES('4000')");
        validationKeywords.numberOfRowsShouldBeGreaterThan("count_table",1L, "id='3000'");
        queryKeywords.deleteRows("count_table");
    }

    @Test
    void testNumberOfRowsShouldBeGreaterThan_WithWhereClause_CountLesserThanGiven_ShouldThrowFailureException() {
        queryKeywords.executeQuery("INSERT INTO count_table VALUES('3000')");
        queryKeywords.executeQuery("INSERT INTO count_table VALUES('4000')");

        assertThatExceptionOfType(FailureException.class).isThrownBy(
                () -> validationKeywords.numberOfRowsShouldBeGreaterThan("count_table",1L, "id='3000'")
        ).withMessage("Number of rows in table 'count_table' expected to be greater than '1', but found to be '1'");
        queryKeywords.deleteRows("count_table");
    }
}