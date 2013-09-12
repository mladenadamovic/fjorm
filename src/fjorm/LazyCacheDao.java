package fjorm;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author mladen
 */
class LazyCacheDao<T> extends Dao<T> {

  StandardDao<T> standardDao;
  Map<Object, T> idToElem = new HashMap<Object, T>();
  List<T> allElems = new ArrayList<T>();
  boolean usingMap = true;

  public LazyCacheDao(Class tclass, DaoProperties daoProperties) {
    synchronized (this) {
      if (standardDao == null) {
        standardDao = new StandardDao<T>(tclass, daoProperties);
        if (standardDao.idFieldName == null || standardDao.idFieldName.length() == 0) {
          usingMap = false;
        }
      }
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
  public List<T> read(String where, Object... params) throws SQLException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public T readByKey(Connection conn, Object key) throws SQLException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public T readByKey(Object key) throws SQLException {
    if (usingMap) {
      T elem = idToElem.get(key);
      if (elem == null) {
        elem = standardDao.readByKey(key);
        idToElem.put(key, elem);
      }
      return elem;
    } else {
      throw new UnsupportedOperationException("Not supported yet.");
    }
  }

  @Override
  public List<T> readAll() throws SQLException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public T create(T entity) throws SQLException {
    if (usingMap) {
      return standardDao.create(entity);
    } else {
      throw new UnsupportedOperationException("Not supported yet.");
    }
  }

  @Override
  public boolean update(T entity) throws SQLException {
    if (usingMap) {
      boolean result = standardDao.update(entity);
      idToElem.put(standardDao.getIdFromElem(entity), entity);
      return result;
    }
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean delete(T entity) throws SQLException {
    if (usingMap) {
      standardDao.update(entity);
      return (idToElem.remove(standardDao.getIdFromElem(entity)) != null);
    }
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean deleteByKey(Connection conn, Object key) throws SQLException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean deleteByKey(Object key) throws SQLException {
    if (usingMap) {
      standardDao.deleteByKey(key);
      return (idToElem.remove(key) != null);
    }
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public <K> Map<K, T> readAllAsMap() throws SQLException {
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

}
