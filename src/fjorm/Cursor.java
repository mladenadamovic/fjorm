package fjorm;

import java.util.Iterator;

/**
 *
 * @author mladen
 */
public interface Cursor<T> extends Iterator<T> {
  
  public void close();
  
}
