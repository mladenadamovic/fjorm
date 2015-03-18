# Introduction #

To instantiate Dao object, you have to pass a object which extends `DaoProperties` i.e.

```
Dao<ImageInfo> imageInfoDao = Dao.<ImageInfo>getDao(ImageInfo.class, TralevDaoProperties.getInstance());
```


Each class which extends `DaoProperties` has to implement three methods:
```
public abstract String getDbServer();
public abstract String getDbUser();
public abstract String getDbPass();
public abstract String getDriverName();
```

and that's all. Not at all difficult. No XML, you don't need to use java properties if you don't want to (but you an implement it using java properties if you want). Simple.

And this is an example:
```
public class TralevDaoProperties extends DaoProperties {

  static TralevDaoProperties dao = new TralevDaoProperties();
  public static TralevDaoProperties getInstance() {
    return dao;
  }


 static String dbserver= "jdbc:mysql://localhost/tralev?useUnicode=true&characterEncoding=utf-8&autoReconnect=true";
 static String dbuser = "root";
 static String dbpass = "Secret123";
 static String dbDriver = "com.mysql.jdbc.Driver";

  @Override
  public String getDbServer() {
    return dbserver;
  }

  @Override
  public String getDbUser() {
    return dbuser;
  }

  @Override
  public String getDbPass() {
    return dbpass;
  }

  @Override
  public String getDriverName() {
    return dbDriver;
  }

}
```

## Testing ##

We don't limit you to one particular way how to test applications written using fjorm.

There are few possibilities to consider:
  * you could create different subclasses of `DaoProperties` i.e. `ServerDaoProperties`, `LocalDaoProperties` and `TestDaoProperties` and inject `DaoProperties` with either static call i.e. with method `setDaoProperties` or otherwise
  * you could mock each Dao object (i.e. name it `MockDao`) and inject on aparticular place. You can use [EasyMock](http://easymock.org/) or [Guice](http://code.google.com/p/google-guice/wiki/GettingStarted) or other tool for mocking objects
  * you could use a properties file for different database server configurations
  * other way which you do like - we are not trying to limit you here, show some creativity on the way you like to do it