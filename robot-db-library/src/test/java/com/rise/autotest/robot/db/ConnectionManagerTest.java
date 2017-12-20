package com.rise.autotest.robot.db;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

class ConnectionManagerTest {
    private ConnectionManager connectionManager = ConnectionManager.instance();

    private static final String ALIAS = "Test";
    private static final String ANOTHER_ALIAS = "AnotherTest";

    @BeforeEach
    void clean() throws SQLException {
        connectionManager.closeAll();
    }

    @Test
    void testRegister_WhenConnectionNull_ShouldThrowException() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> connectionManager.register(null, ALIAS))
                .withMessage("Connection should be instantiated before calling register");
    }

    @Test
    void testRegister_WhenAliasNull_ShouldRegisterConnection() {
        Connection connection = mock(Connection.class);
        String sessionId = connectionManager.register(connection, null);
        assertThat(sessionId).isNotEmpty();
    }

    @Test
    void testRegister_WhenAliasNotNull_SessionShouldMatchWithAlias() {
        Connection connection = mock(Connection.class);
        String sessionId = connectionManager.register(connection, ALIAS);
        assertThat(sessionId).isNotEmpty();
        assertThat(UUID.nameUUIDFromBytes(ALIAS.getBytes()).toString()).isEqualTo(sessionId);
    }

    @Test
    void testRegister_WhenSuccess_SessionShouldMatchGetCurrentSession() {
        Connection connection = mock(Connection.class);
        String sessionId = connectionManager.register(connection, ALIAS);
        assertThat(sessionId).isNotEmpty();
        assertThat(connectionManager.getCurrentSessionId()).isEqualTo(sessionId);
    }

    @Test
    void testGetCurrent_WhenAtLeastOneConnectionRegistered_ShouldReturnCurrentConnection() {
        Connection connection = mock(Connection.class);
        String sessionId = connectionManager.register(connection, ALIAS);

        Connection anotherConnection = mock(Connection.class);
        String anotherSessionId = connectionManager.register(anotherConnection, ANOTHER_ALIAS);

        assertThat(connectionManager.getCurrent()).isNotNull();
        assertThat(connectionManager.getCurrent()).isEqualTo(anotherConnection);
    }

    @Test
    void testGetCurrent_WhenNoConnectionRegistered_ShouldReturnNull() {
        assertThat(connectionManager.getCurrent()).isNull();
    }

    @Test
    void testGetCurrentSession_WhenAtLeastOneConnectionRegistered_ShouldReturnCurrentSession() {
        Connection connection = mock(Connection.class);
        String sessionId = connectionManager.register(connection, ALIAS);

        assertThat(connectionManager.getCurrentSessionId()).isNotNull();
    }

    @Test
    void testGetCurrentSession_WhenNoConnectionRegistered_ShouldReturnNull() {
        assertThat(connectionManager.getCurrentSessionId()).isNull();
    }

    @Test
    void testClose_WhenAtLeastOneConnectionRegistered_ShouldCloseCurrentConnection() throws SQLException{
        Connection connection = mock(Connection.class);
        String sessionId = connectionManager.register(connection, ALIAS);

        Connection anotherConnection = mock(Connection.class);
        String anotherSessionId = connectionManager.register(anotherConnection, ANOTHER_ALIAS);

        connectionManager.close();

        verifyZeroInteractions(connection);
        verify(anotherConnection, times(1)).close();
        assertThat(connectionManager.size()).isEqualTo(1);
    }

    @Test
    void testCloseAll_WhenAtLeastOneConnectionRegistered_ShouldCloseAll() throws SQLException {
        Connection connection = mock(Connection.class);
        String sessionId = connectionManager.register(connection, ALIAS);

        Connection anotherConnection = mock(Connection.class);
        String anotherSessionId = connectionManager.register(anotherConnection, ANOTHER_ALIAS);

        connectionManager.closeAll();

        verify(connection, times(1)).close();
        verify(anotherConnection, times(1)).close();
        assertThat(connectionManager.size()).isEqualTo(0);
        assertThat(connectionManager.getCurrentSessionId()).isNull();
    }

    @Test
    void testSize_WhenNoConnectionRegistered_ShouldReturnZero() {
        assertThat(connectionManager.size()).isEqualTo(0);
    }

    @Test
    void testSize_WhenAtLeastOneConnectionRegistered_ShouldReturnCorrectSize() {
        Connection connection = mock(Connection.class);
        String sessionId = connectionManager.register(connection, ALIAS);
        assertThat(connectionManager.size()).isEqualTo(1);
    }

    @Test
    void testSize_WhenAtLeastOneConnectionRegistered_And_ConnectionClosed_ShouldReturnCorrectSize()  throws SQLException {
        Connection connection = mock(Connection.class);
        String sessionId = connectionManager.register(connection, ALIAS);

        Connection anotherConnection = mock(Connection.class);
        String anotherSessionId = connectionManager.register(anotherConnection, ANOTHER_ALIAS);

        connectionManager.close();
        assertThat(connectionManager.size()).isEqualTo(1);
    }
}