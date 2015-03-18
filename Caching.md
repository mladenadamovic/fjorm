fjorm supports 3 caching strategies for tables:

1. Standard - only caching at the database level

2. `LazyCache` - once the row is read from the database, it will be kept, however complex queries will still go to the database. It keeps cached data in `HashTable` for performance purposes.

3. `FullCache` - on init, the whole content of the database will be read, and standard CRUD operation will use in-memory content. Complex SQL queries still go to the database.

To use `LazyCache` or `FullCache` annotate the Class with its annotations, i.e.
```
@FullCache
@TableName(table = "city")
public class CityInfo {

  @Id
  public int id;

  public String name;
  public String country;
  public double lat;
  public double lng;

  public String toPrintableName() {
    return name + ", " + country;
  }

}

```