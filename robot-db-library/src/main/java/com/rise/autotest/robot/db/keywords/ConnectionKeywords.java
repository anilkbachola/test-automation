package com.rise.autotest.robot.db.keywords;

import com.rise.autotest.robot.FatalException;
import com.rise.autotest.robot.db.ConnectionManager;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywordOverload;
import org.robotframework.javalib.annotation.RobotKeywords;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Robot keywords to manage database connections.
 */
@RobotKeywords
public class ConnectionKeywords {

    private List<String> conProps = Arrays.asList("db.driverClass","db.url","db.username","db.password");

    @RobotKeywordOverload
    public String openConnection(String driverClass, String url, String username, String password, String alias) {
        return openConnection(null, driverClass, url, username, password, true, alias);
    }

    @RobotKeywordOverload
    public String openConnection(String driverClass, String url, String username, String password) {
        return openConnection(null, driverClass, url, username, password, true, null);
    }

    @RobotKeywordOverload
    public String openConnection(String driverClass, String url, String username, String password,
            String alias, boolean readOnly) {
        return openConnection(null, driverClass, url, username, password, readOnly, alias);
    }

    @RobotKeywordOverload
    public String openConnection(String configFileName) {
        return openConnection(configFileName, null, null, null, null, true, null);
    }

    @RobotKeywordOverload
    public String openConnection(String configFileName, boolean readOnly) {
        return openConnection(configFileName, null, null, null, null, readOnly, null);
    }

    @RobotKeywordOverload
    public String openConnection(String configFileName, boolean readOnly, String alias) {
        return openConnection(configFileName, null, null, null, null, readOnly, alias);
    }

    @RobotKeywordOverload
    public String openConnection(String configFileName, String alias) {
        return openConnection(configFileName, null, null, null, null, true, alias);
    }

    /**
     * Opens a <b>readOnly</b> connection to the database specified by the <b>url</b> connection string
     * Uses the provided <b>username</b> and <b>password</b>. Assigns the optional <b>alias</b> to the connection.
     * <br>
     * If, <b>configFile</b> is provided, an attemp will be made to load connection properties from the file.
     * Values from the property will be used even if the explicit properties are provided.
     * <br>
     * An exception will be thrown, if config file not found or could not read and also if not all required properties
     * are available in the config file.
     * <br>
     * Uses the Java JDBC to open connections, so should work with any JDBC compatible databases.
     * The corresponding jdbc driver/version for the database should be added to the classpath along with this library
     * and also the fully qualified driver class name <b>driverClass</b> should be passed along.
     *
     * <br>
     * JDBC Driver Examples(not a full list):
     * <table summary="">
     *     <tr><td>Database</td><td>URL</td><td>Driver Class</td></tr>
     *     <tr><td>MySQL</td><td>jdbc:mysql://HOST/DATABASE</td><td>com.mysql.jdbc.Driver</td></tr>
     *     <tr><td>PostgreSQL</td><td>jdbc:postgresql://HOST/DATABASE</td><td>org.postgresql.Driver</td></tr>
     *     <tr><td>SQLServer</td><td>jdbc:microsoft:sqlserver://HOST:1433;DatabaseName=DATABASE</td><td>com.microsoft.jdbc.sqlserver.SQLServerDriver</td></tr>
     *     <tr><td>DB2</td><td>jdbc:as400://HOST/DATABASE;</td><td>com.ibm.as400.access.AS400JDBCDriver</td></tr>
     * </table>
     *
     * @param configFile the config file with connection properties.
     * @param driverClass   the fully qualified driver class name for the database
     * @param url   jdbc connection string
     * @param username  database user
     * @param password  database password
     * @param  readOnly if true, opens a readonly db connection. default value is falase.
     * @param alias an alias to be used for this connection. Optional
     * @return sessionId aka connection id for this connection.
     * @throws FatalException if connection could not be opened.
     */
    @RobotKeyword
    @ArgumentNames({"configFile=NONE", "driverClass=NONE", "url=NONE", "username=NONE", "password=NONE", "readOnly=true", "alias=NONE"})
    public String openConnection(String configFile, String driverClass, String url, String username, String password, boolean readOnly,
            String alias ) {

        if(configFile != null) {
            Properties props = loadProperties(configFile);
            driverClass = props.getProperty("db.driverClass");
            url = props.getProperty("db.url");
            username = props.getProperty("db.username");
            password = props.getProperty("db.password");
            alias = props.getProperty("db.alias");
            if(props.getProperty("db.readOnly") != null) {
                readOnly = Boolean.valueOf(props.getProperty("db.readOnly"));
            }
        }

        try {
            Class.forName(driverClass);
            Connection connection = DriverManager.getConnection(url, username, password);
            connection.setReadOnly(readOnly);
            return ConnectionManager.instance().register(connection, alias);
        } catch (ClassNotFoundException e) {
            throw new FatalException(
                    String.format("Could not load driver class '%s'. Make sure, driver jar is added to classpath",
                            driverClass),e);
        } catch (SQLException e) {
            throw new FatalException(
                    String.format("Error connecting to database: '%s'. Check the connection parameters: url->'%s', user-> '%s', password->'%s'",
                            e.getMessage(), url, username, password), e);
        }
    }


    /**
     * Close the recently opened connection
     * @throws SQLException sql exception
     */
    @RobotKeywordOverload
    public void closeConnection() throws SQLException{
        closeConnection(null);
    }

    /**
     * Close the connection with the specified <b>sessionIdOrAlias</b>. <br>
     * Closes the connection only if it is found and not already closed.
     *
     * @param sessionIdOrAlias  the session id or aliad of previously opened connection.
     * @throws SQLException sql exception
     */
    @RobotKeyword
    @ArgumentNames({"sessionIdOrAlias=NONE"})
    public void closeConnection(String sessionIdOrAlias) throws SQLException {
        if(sessionIdOrAlias == null) {
            ConnectionManager.instance().close();
        } else {
            ConnectionManager.instance().close(sessionIdOrAlias);
        }
    }


    /**
     * Close all opened connections.
     * @throws SQLException sql exception
     */
    @RobotKeyword
    public void closeAllConnections() throws SQLException {
       ConnectionManager.instance().closeAll();
    }


    private Properties loadProperties(String configFileName) {
        Properties props = new Properties();

        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        try ( InputStream is = loader.getResourceAsStream(configFileName)) {
            props.load(is);
        } catch (IOException e) {
            throw new FatalException(String.format("Could not read property file '%s'", configFileName), e);
        }

        if(!props.stringPropertyNames().containsAll(conProps)){
            throw new FatalException(String.format("Not all required connection properties provided in '%s'. "
                    + "Required properties are: %s. ", configFileName, Arrays.toString(conProps.toArray())));
        }
        return props;
    }
}