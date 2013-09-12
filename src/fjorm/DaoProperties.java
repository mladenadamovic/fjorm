package fjorm;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbcp.BasicDataSource;

/**
 * @author mladen
 */
public abstract class DaoProperties {

 public abstract String getDbServer();
 public abstract String getDbUser();
 public abstract String getDbPass();
 public abstract String getDriverName();

 BasicDataSource dataSource;
 public void initDataSource() {
   dataSource = new BasicDataSource();
    try {
      Class.forName(getDriverName());
    } catch (ClassNotFoundException ex) {
      Logger.getLogger(DaoProperties.class.getName()).log(Level.SEVERE, null, ex);
    }
    dataSource = new BasicDataSource();
    dataSource.setDriverClassName(getDriverName());
    dataSource.setUsername(getDbUser());
    dataSource.setPassword(getDbPass());
    dataSource.setUrl(getDbServer());
    dataSource.setMaxActive(64);
    dataSource.setMaxIdle(8);

    // added to try to fix CommunicationsException
    // The last packet sent successfully to the server was 70239 seconds ago, which  is longer than the server configured value of 'wait_timeout'.
    dataSource.setTimeBetweenEvictionRunsMillis(10000);
    dataSource.setRemoveAbandoned(true);
    dataSource.setTestOnBorrow(true);
    dataSource.setRemoveAbandonedTimeout(60);
    dataSource.setValidationQuery("SELECT 1");
 }

 public synchronized Connection createConnection() throws SQLException {
   if (dataSource == null) {
     initDataSource();
   }
   return dataSource.getConnection();
 }

 /*
  public boolean isInited = false;
  public synchronized Connection createConnection() throws SQLException {
    if (isInited == false) {
      try {
        Class.forName(getDriverName());
        isInited = true;
      } catch (ClassNotFoundException ex) {
        Logger.getLogger(DaoProperties.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    //Logger.getLogger(Configuration.class.getName()).info(getCallStackAsString() + " root for " + "Creating new connection at " + System.currentTimeMillis());
    long startGet = System.currentTimeMillis();
    Connection conn;
    try {
      conn = DriverManager.getConnection(getDbServer(), getDbUser(), getDbPass());
    } catch (SQLException exception) {
      System.gc();
      conn = DriverManager.getConnection(getDbServer(), getDbUser(), getDbPass());
    }
    //long endGet = System.currentTimeMillis();
    //Logger.getLogger(Configuration.class.getName()).info("needed time for creating new connection in ms = "
    //        + (endGet - startGet));
    return conn;
  }*/


}
