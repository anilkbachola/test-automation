package com.rise.autotest.robot.db;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class ConnectionManager {

    private static ConnectionManager connectionManager;
    private Map<UUID, ConnectionTuple> sessionIdConnectionMap = new HashMap<>();
    private Deque<UUID> sessionIdStack = new ArrayDeque<>();

    /**
     * private constructor to hide default constructor
     */
    private ConnectionManager() { }

    /**
     * initialize the instance of this class to be singleton
     * @return this instance {@code connectionManager}
     */
    public static synchronized ConnectionManager instance() {
        if(connectionManager == null) {
            synchronized (ConnectionManager.class) {
                if(connectionManager == null) {
                    connectionManager = new ConnectionManager();
                }
            }
        }
        return connectionManager;
    }

    public synchronized String register(Connection connection, String alias) {
        if(connection == null ) {
            throw new IllegalArgumentException("Connection should be instantiated before calling register");
        }
        UUID sessionId = newSessionId(alias);
        ConnectionTuple connectionTuple = new ConnectionTuple(sessionId, connection, alias);
        sessionIdStack.push(sessionId);
        sessionIdConnectionMap.put(sessionId, connectionTuple);
        return sessionId.toString();
    }

    /**
     * Return the current opened connection
     * @return current {@code WebDriver} instance
     */
    public Connection getCurrent() {
        if(sessionIdStack.isEmpty()) {
            return null;
        }
        ConnectionTuple connectionTuple = sessionIdConnectionMap.get(sessionIdStack.peek());
        return connectionTuple == null ? null : connectionTuple.getConnection();
    }

    /**
     * Get connection associated with a given sessionid or alias
     * @param sessionIdOrAlias  session id or alias
     * @return  connection. null if no connection found
     */
    public Connection get(String sessionIdOrAlias) {
        UUID sessionId = fromSessionIdOrAlias(sessionIdOrAlias);
        if(sessionId != null && sessionIdConnectionMap.containsKey(sessionId)) {
            return sessionIdConnectionMap.get(sessionId).getConnection();
        }
        return null;
    }

    /**
     * return session id associated with current open connection.
     * @return current session id
     */
    public String getCurrentSessionId() {
        return sessionIdStack.isEmpty() ? null : sessionIdStack.peek().toString();
    }

    /**
     * Close the current connection.
     * @throws SQLException failure to close the connection.
     */
    public void close() throws SQLException {
        if(!sessionIdStack.isEmpty()) {
            UUID sessionId = sessionIdStack.pop();
            Connection connection = sessionIdConnectionMap.get(sessionId).getConnection();
            connection.close();
            sessionIdConnectionMap.remove(sessionId);
        }
    }

    /**
     *  Close the connection associated with session id or alias
     * @param sessionIdOrAlias session id or alias
     * @throws SQLException sql exception
     */
    public void close(String sessionIdOrAlias) throws SQLException {
        UUID sessionId = fromSessionIdOrAlias(sessionIdOrAlias);
        if(sessionId != null && sessionIdConnectionMap.containsKey(sessionId)) {
            Connection connection = sessionIdConnectionMap.get(sessionId).getConnection();
            connection.close();
            sessionIdStack.remove(sessionId);
        }
    }


    /**
     * Close all web driver instances
     * @throws SQLException sql exception
     */
    public void closeAll() throws SQLException {
        Iterator<Map.Entry<UUID, ConnectionTuple>> iterator = sessionIdConnectionMap.entrySet().iterator();

        while(iterator.hasNext()) {
            Map.Entry<UUID, ConnectionTuple> entry = iterator.next();
            entry.getValue().getConnection().close();
            iterator.remove();
        }
        sessionIdStack.clear();
    }

    /**
     * Make another driver as current. Accepts alias or sessionId as argument
     * @param sessionIdOrAlias switch another connection based on session id or alias
     */
    public void switchConnection(String sessionIdOrAlias) {
        UUID sessionId = fromSessionIdOrAlias(sessionIdOrAlias);
        if(sessionId != null && sessionIdConnectionMap.containsKey(sessionId)) {
            sessionIdStack.remove(sessionId);
            sessionIdStack.push(sessionId);
        }
    }

    /**
     * returns the number of web driver sessions active
     * @return number of active driver sessions
     */
    public int size() {
        return sessionIdConnectionMap.size();
    }

    /**
     * cleanup the connection manager state
     * @throws SQLException sql exception
     */
    public void cleanup() throws SQLException {
        closeAll();
        sessionIdStack.clear();
        sessionIdConnectionMap.clear();
    }

    private UUID newSessionId(String alias) {
        return alias!= null && !alias.isEmpty() ? UUID.nameUUIDFromBytes(alias.getBytes()): UUID.randomUUID();
    }

    private UUID fromSessionIdOrAlias(String sessionIdOrAlias) {
        UUID sessionId = null;
        if(sessionIdOrAlias != null && !sessionIdOrAlias.isEmpty()) {
            try {
                sessionId = UUID.fromString(sessionIdOrAlias);
            } catch (IllegalArgumentException ex) {
                //ignore, passed value is an alias
                sessionId = UUID.nameUUIDFromBytes(sessionIdOrAlias.getBytes());
            }
        }
        return sessionId;
    }


    @Getter
    @AllArgsConstructor
    private static class ConnectionTuple {
        @NonNull
        private UUID sessionId;

        @NonNull
        private Connection connection;

        private String alias;
    }
}
