package com.rise.autotest.robot.db.keywords;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import com.rise.autotest.robot.FatalException;
import com.rise.autotest.robot.db.ConnectionManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.sql.SQLException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ConnectionKeywordsTest {

    private ConnectionManager connectionManager = ConnectionManager.instance();
    private ConnectionKeywords connectionKeywords;

    private static final String CON_STRING = "jdbc:h2:mem:test";
    private static final String DRIVER_CLASS = "org.h2.Driver";
    private static final String USER = "sa";
    private static final String PASSWD = "sa";
    private static final String ALIAS = "alias";

    @BeforeAll
    void setup() {
        connectionKeywords = new ConnectionKeywords();
    }

    @BeforeEach
    void clean() throws SQLException {
        connectionManager.closeAll();
    }

    @Test
    void testOpenConnection_WithValidProperties_WithoutAlias_ShouldOpenConnection() throws SQLException {

        String sessionId = connectionKeywords.openConnection(DRIVER_CLASS, CON_STRING, USER, PASSWD, null);

        assertThat(connectionManager.size()).isEqualTo(1);
        assertThat(connectionManager.getCurrentSessionId()).isEqualTo(sessionId);
        assertThat(connectionManager.getCurrent()).isNotNull();
    }

    @Test
    void testOpenConnection_WithValidProperties_WithAlias_ShouldOpenConnectionWithGivenAlias() throws SQLException {

        String sessionWithAliasId = connectionKeywords.openConnection(DRIVER_CLASS, CON_STRING, USER, PASSWD, ALIAS);
        String sessionId = connectionKeywords.openConnection(DRIVER_CLASS, CON_STRING, USER, PASSWD, null);

        assertThat(connectionManager.size()).isEqualTo(2);
        assertThat(connectionManager.getCurrentSessionId()).isEqualTo(sessionId);
        assertThat(connectionManager.getCurrent()).isNotNull();
        connectionManager.switchConnection(sessionWithAliasId);
        assertThat(connectionManager.getCurrentSessionId()).isEqualTo(sessionWithAliasId);
    }

    @Test
    void testOpenConnection_WithInvalidDriverClass_ShouldThrowFatalException() throws SQLException {
        assertThatExceptionOfType(FatalException.class)
                .isThrownBy(() -> connectionKeywords.openConnection("DUMMY", CON_STRING, USER, PASSWD, null))
                .withMessage("Could not load driver class 'DUMMY'. Make sure, driver jar is added to classpath");

    }

    @Test
    void testOpenConnection_WithInvalidURL_ShouldThrowFatalException() throws SQLException {
        assertThatExceptionOfType(FatalException.class)
                .isThrownBy(() -> connectionKeywords.openConnection(DRIVER_CLASS, "DUMMY", USER, PASSWD, null))
                .withMessage("Error connecting to database: 'No suitable driver found for DUMMY'. "
                        + "Check the connection parameters: url->'DUMMY', user-> 'sa', password->'sa'");

    }

    @Test
    void testOpenConnection_WithValidPropertiesFromFile_ShouldOpenConnection() throws SQLException {
        String sessionId = connectionKeywords.openConnection("db.properties", null, null, null, null, false, null);

        assertThat(connectionManager.size()).isEqualTo(1);
        assertThat(connectionManager.getCurrentSessionId()).isEqualTo(sessionId);
        assertThat(connectionManager.getCurrent()).isNotNull();
    }

    @Test
    void testOpenConnection_WithInValidDriverPropertyFromFile_ShouldOpenConnection() throws SQLException {
        assertThatExceptionOfType(FatalException.class)
                .isThrownBy(() -> connectionKeywords.openConnection("db_invalid.properties", null, null, null, null, false, null))
                .withMessage("Could not load driver class 'org.h2.Driver1'. Make sure, driver jar is added to classpath");
    }

    @Test
    void testOpenConnection_WithValidPropertiesFromFile_ShouldOverrideExplicitProperties() throws SQLException {
        String sessionId = connectionKeywords.openConnection("db.properties", "DUMMY", "DUMMY", null, null, false, null);

        assertThat(connectionManager.size()).isEqualTo(1);
        assertThat(connectionManager.getCurrentSessionId()).isEqualTo(sessionId);
        assertThat(connectionManager.getCurrent()).isNotNull();
    }

    @Test
    void testCloseConnection_WhenNoOpenConnections_DoesNothing() throws SQLException {
        connectionKeywords.closeConnection();

    }

    @Test
    void testCloseConnection_WhenMultipleOpenConnections_ShouldCloseRecentlyOpened() throws SQLException {
        String sessionId = connectionKeywords.openConnection(DRIVER_CLASS, CON_STRING, USER, PASSWD, null);
        String anotherSessionId = connectionKeywords.openConnection(DRIVER_CLASS, CON_STRING, USER, PASSWD, null);

        connectionKeywords.closeConnection();
        assertThat(connectionManager.size()).isEqualTo(1);
        assertThat(connectionManager.get(anotherSessionId)).isNull();
        assertThat(connectionManager.getCurrentSessionId()).isEqualTo(sessionId);

    }

    @Test
    void testCloseAllConnections_WhenNoOpenConnections_DoesNothing() throws SQLException{
        connectionKeywords.closeAllConnections();
    }

    @Test
    void testCloseAllConnections_WhenMultipleOpenConnections_ShouldCloseAll() throws SQLException {
        String sessionId = connectionKeywords.openConnection(DRIVER_CLASS, CON_STRING, USER, PASSWD, null);
        String anotherSessionId = connectionKeywords.openConnection(DRIVER_CLASS, CON_STRING, USER, PASSWD, null);

        connectionKeywords.closeAllConnections();
        assertThat(connectionManager.size()).isEqualTo(0);
        assertThat(connectionManager.getCurrentSessionId()).isNull();
    }


}