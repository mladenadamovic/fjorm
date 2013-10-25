package fjorm;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author mladen
 */
public abstract class Dao<T> {

  static Map<Class, Dao> cache = new HashMap<Class, Dao>();

  public static synchronized  <ST> Dao<ST> getDao(Class tclass, DaoProperties daoProperties) {
    if (cache.get(tclass) != null) {
      return (Dao<ST>) cache.get(tclass);
    }
    Dao<ST> result;
    if (tclass.isAnnotationPresent(LazyCache.class)) {
      result = new LazyCacheDao<ST>(tclass, daoProperties);
    } else if (tclass.isAnnotationPresent(FullCache.class)) {
      result = new FullCacheDao<ST>(tclass, daoProperties);
    } else {
      result = new StandardDao<ST>(tclass, daoProperties);
    }
    cache.put(tclass, result);
    return result;
  }

  public synchronized static void clearDaoCache() {
    cache.clear();
  }

  public abstract List<T> read(String where, Object... params) throws SQLException;

  public T readFirst(String where, Object... params) throws SQLException {
    List<T> candidates = read(where, params);
    if (candidates == null || candidates.size() == 0) {
      return null;
    }
    return candidates.get(0);
  }

  public abstract List<T> read(Connection conn, String where, Object... params) throws SQLException;

  public abstract T readByKey(Connection conn, Object key) throws SQLException;

  public abstract T readByKey(Object key) throws SQLException;

  public abstract List<T> readAll(Connection conn) throws SQLException;

  public abstract List<T> readAll() throws SQLException;

  public abstract T create(Connection conn,T entity) throws SQLException;

  public abstract boolean create(Connection conn,Collection<T> entityCol) throws SQLException;

  public abstract T create(T entity) throws SQLException;

  public abstract boolean update(Connection conn, T entity) throws SQLException;

  public abstract boolean update(T entity) throws SQLException;

  public abstract boolean delete(Connection conn, T entity) throws SQLException;
  
  /**
  * @return either (1) the row count for SQL Data Manipulation Language (DML) statements
  *         or (2) 0 for SQL statements that return nothing
  * @param conn
  * @param where
  * @param params
  * @return
  * @throws SQLException 
  */
  public abstract int delete(Connection conn, String where, Object... params) throws SQLException;

  /**
  * @return either (1) the row count for SQL Data Manipulation Language (DML) statements
  *         or (2) 0 for SQL statements that return nothing
  * @param where
  * @param params
  * @return
  * @throws SQLException 
  */
  public abstract int delete(String where, Object... params) throws SQLException;

  public abstract boolean delete(T entity) throws SQLException;

  public abstract boolean deleteByKey(Connection conn, Object key) throws SQLException;

  public abstract boolean deleteByKey(Object key) throws SQLException;

  public abstract <K> Map<K, T> readAllAsMap() throws SQLException;
  
  public abstract Cursor<T> cursor(Connection conn, String where, Object... params) throws SQLException;

  public abstract Cursor<T> cursor(String where, Object... params) throws SQLException;

}
