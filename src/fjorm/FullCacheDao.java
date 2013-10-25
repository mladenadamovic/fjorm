package fjorm;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mladen
 */
public class FullCacheDao<T> extends Dao<T> {


  StandardDao<T> standardDao;
  Map<Object, T> idToElem = new HashMap<Object, T>();
  List<T> allElems = new ArrayList<T>();
  boolean usingMap = true;

  public FullCacheDao(Class tclass, DaoProperties daoProperties) {
    synchronized (this) {
      if (standardDao == null) {
        standardDao = new StandardDao<T>(tclass, daoProperties);
        if (standardDao.idFieldName == null || standardDao.idFieldName.length() == 0) {
          resetUsingList();
        } else {
          resetUsingMap();
        }
      }
    }
  }

  private void resetUsingList() {
    usingMap = false;
    try {
      allElems = standardDao.readAll();
    } catch (SQLException ex) {
      Logger.getLogger(FullCacheDao.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private void resetUsingMap() {
    usingMap = true;
    try {
      for (T elem : standardDao.readAll()) {
        Object id = standardDao.getIdFromElem(elem);
        idToElem.put(id, elem);
      }
    } catch (SQLException ex) {
      Logger.getLogger(FullCacheDao.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  @Override
  public List<T> read(Connection conn, String where, Object... params) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public List<T> readAll(Connection conn) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public T create(Connection conn, T entity) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean update(Connection conn, T entity) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean delete(Connection conn, T entity) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public T readByKey(Connection conn, Object key) throws SQLException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public List<T> read(String where, Object... params) throws SQLException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public T readByKey(Object key) throws SQLException {
    if (usingMap) {
      return idToElem.get(key);
    }
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public List<T> readAll() throws SQLException {
    if (usingMap) {
      List<T> result = new ArrayList<T>();
      result.addAll(idToElem.values());
      return result;
    } else {
      return allElems;
    }
  }

  @Override
  public T create(T entity) throws SQLException {
    Logger.getLogger(this.getClass().getName()).info("Warning: this operation is slow");
    if (usingMap) {
      T result = standardDao.create(entity);
      resetUsingMap();
      return result;
    } else {
      T result = standardDao.create(entity);
      resetUsingList();
      return result;
    }
  }

  @Override
  public boolean update(T entity) throws SQLException {
    if (usingMap) {
      boolean result = standardDao.update(entity);
      idToElem.put(standardDao.getIdFromElem(entity), entity);
      return result;
    } else {
      Logger.getLogger(this.getClass().getName()).info("Warning: this operation is slow");
      boolean result = standardDao.update(entity);
      resetUsingList();
      return result;
    }
  }

  @Override
  public boolean delete(T entity) throws SQLException {
    if (usingMap) {
      boolean result = standardDao.delete(entity);
      idToElem.remove(standardDao.getIdFromElem(entity));
      return result;
    } else {
      Logger.getLogger(this.getClass().getName()).info("Warning: this operation is slow");
      boolean result = standardDao.delete(entity);
      resetUsingList();
      return result;
    }
  }

  @Override
  public boolean deleteByKey(Connection conn, Object key) throws SQLException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean deleteByKey(Object key) throws SQLException {
    if (usingMap) {
      boolean result = standardDao.deleteByKey(key);
      idToElem.remove(key);
      return result;
    } else {
      Logger.getLogger(this.getClass().getName()).info("Warning: this operation is slow");
      boolean result = standardDao.deleteByKey(key);
      resetUsingList();
      return result;
    }
  }

  @Override
  public <K> Map<K, T> readAllAsMap() throws SQLException {
    if (usingMap) {
      return (Map<K, T>) idToElem;
    }
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean create(Connection conn, Collection<T> entityCol) throws SQLException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public int delete(Connection conn, String where, Object... params) throws SQLException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public int delete(String where, Object... params) throws SQLException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Cursor<T> cursor(Connection conn, String where, Object... params) throws SQLException {
    throw new UnsupportedOperationException("No point in using cursor over data which are in cache, use read instead."); 
  }

  @Override
  public Cursor<T> cursor(String where, Object... params) throws SQLException {
    throw new UnsupportedOperationException("No point in using cursor over data which are in cache, use read instead."); 
  }

}
