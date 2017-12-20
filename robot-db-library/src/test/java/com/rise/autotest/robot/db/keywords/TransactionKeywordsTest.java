package com.rise.autotest.robot.db.keywords;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.sql.SQLException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TransactionKeywordsTest {

    private TransactionKeywords transactionKeywords = new TransactionKeywords();
    private QueryKeywords queryKeywords = new QueryKeywords();
    private ConnectionKeywords connectionKeywords = new ConnectionKeywords();

    @BeforeAll
    void setup() {
        connectionKeywords.openConnection("db.properties");
        queryKeywords.executeQuery("create table if not exists temp(temp_id varchar(50))");
        queryKeywords.executeQuery("insert into temp values('1000')");
        queryKeywords.executeQuery("insert into temp values('1001')");
    }

    @AfterAll
    void cleanup() throws SQLException {
        connectionKeywords.closeAllConnections();
    }

    @Test
    void testTransactionCommit_WhenAutoCommitDisabled_ShouldNotCommit() throws SQLException {
        connectionKeywords.openConnection("db.properties");
        transactionKeywords.beginTransaction();
        queryKeywords.executeQuery("UPDATE temp SET temp_id='5000' WHERE temp_id='1000'");
        connectionKeywords.closeConnection();

        assertThat(queryKeywords.getRowCount("temp", "temp_id='5000'")).isEqualTo(0);
        assertThat(queryKeywords.getRowCount("temp", "temp_id='1000'")).isEqualTo(1);
    }

    @Test
    void testTransactionCommit_WhenAutoCommitDisabled_And_ExplicitCommit_ShouldCommit() throws SQLException {
        connectionKeywords.openConnection("db.properties");
        transactionKeywords.beginTransaction();
        queryKeywords.executeQuery("UPDATE temp SET temp_id='5001' WHERE temp_id='1001'");
        transactionKeywords.commitTransaction();
        connectionKeywords.closeConnection();

        assertThat(queryKeywords.getRowCount("temp", "temp_id='5001'")).isEqualTo(1);
        assertThat(queryKeywords.getRowCount("temp", "temp_id='1001'")).isEqualTo(0);

    }

    @Test
    void testTransactionRollback_WhenExplicitRollback_ShouldRollback() throws SQLException {
        connectionKeywords.openConnection("db.properties");
        transactionKeywords.beginTransaction();
        queryKeywords.executeQuery("UPDATE temp SET temp_id='5000' WHERE temp_id='1000'");
        transactionKeywords.rollbackTransaction();
        connectionKeywords.closeConnection();

        assertThat(queryKeywords.getRowCount("temp", "temp_id='5000'")).isEqualTo(0);
        assertThat(queryKeywords.getRowCount("temp", "temp_id='1000'")).isEqualTo(1);
    }

}