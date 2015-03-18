For list of supported operations you can look up in [Dao interface source code](https://code.google.com/p/fjorm/source/browse/src/fjorm/Dao.java).

Some examples (let's learn from examples):

### Example for Create and readFirst ###
```
  public static boolean writeLikesForUser(int id, int vote, String email) {
    try {
      Dao<ImageVote> imageVoteDao = Dao.<ImageVote>getDao(ImageVote.class, TralevDaoProperties.getInstance());
      ImageVote existingVote = imageVoteDao.readFirst("image_id = ? and username = ?", id, email);
      ImageVote imageVote = new ImageVote();
      if (existingVote != null) {
        imageVote = existingVote;
      }
      imageVote.image_id = id;
      imageVote.month = DateTimeUtils.getMonth();
      imageVote.year = DateTimeUtils.getYear();
      imageVote.username = email;
      imageVote.vote = vote;
      if (existingVote != null) {
        imageVoteDao.update(imageVote);
      } else {
        imageVoteDao.create(imageVote);
      }
      return true;
    } catch (SQLException ex) {
      Logger.getLogger(ImageVoteUtils.class.getName()).log(Level.SEVERE, null, ex);
    }
    return false;
  }

```


## Example for delete and for deleteByKey ##
```
  public static boolean deleteImage(int id) {
    Logger.getLogger(ImageUtils.class.getName()).log(Level.INFO, "started dequying all...");
    ImageCacheCreatorRunnable.getInstanceAndRunIfNotRunned().dequeueAll(id);
    Logger.getLogger(ImageUtils.class.getName()).log(Level.INFO, "finished dequying all...");
    Dao<ImageVote> imageVoteDao = Dao.<ImageVote>getDao(ImageVote.class, TralevDaoProperties.getInstance());
    try {
      imageVoteDao.delete("image_id = ?", id);
    } catch (SQLException ex) {
      Logger.getLogger(CaptureServlet.class.getName()).log(Level.SEVERE, null, ex);
      return false;
    }
    Dao<ImageInfo> imageInfoDao = Dao.<ImageInfo>getDao(ImageInfo.class, TralevDaoProperties.getInstance());
    String filename = ImageUtils.getOrigFilename(id);
    File file = new File(filename);
    boolean result = true;
    if (file.exists()) {
      result = file.delete();
      deleteNonOriginalCopiesOfImage(id);
    }
    Logger.getLogger(ImageUtils.class.getName()).log(Level.INFO, "finished deleting non original copies...");
    try {
      imageInfoDao.deleteByKey(id);
      Logger.getLogger(ImageUtils.class.getName()).log(Level.INFO, "finished deleting imageInfo by key");
      return result;
    } catch (SQLException ex) {
      Logger.getLogger(ImageUtils.class.getName()).log(Level.SEVERE, null, ex);
      return false;
    }
  }
```

## Examples for read ##

```
//getting images which are not moderated
imageInfoDao.read("moderator_score = 0 order by id asc limit " + limit);
```

```
// getting images which user did like
imageInfoDao.read(" inner join image_vote on image_info.id = image_vote.image_id " + 
              "where image_vote.username = ? and image_vote.vote = 1 order by image_vote.id desc limit 1000", email);
```

```
//getting images which user did upload
imageInfoDao.read(" where uploader_username = ? order by id desc limit 1000", email);
```

```
//get recent uploads by user. Recent uploads are still not reviewed by moderator
imageInfoDao.read(" where uploader_username = ? and moderator_score = 0 order by id desc limit 100", email);
```


```
///get images near given lat,lng
imageInfoDao.read("where lat > ? and lat < ? and lng > ? and lng < ? limit 2000", lat - 0.2, lat + 0.2, lng - 0.2, lng + 0.2);
```

## Examples for readAll ##
```
   List<CityInfo> cityInfos = citiesInfoDao.readAll();
```

## Examples for update ##
```
  public static boolean setModeratorScore(int id, int moderator_score) {
    try {
      Dao<ImageInfo> imageInfoDao = Dao.<ImageInfo>getDao(ImageInfo.class, TralevDaoProperties.getInstance());
      ImageInfo imageInfo = imageInfoDao.readByKey(id);
      if (imageInfo == null) {
        throw new RuntimeException("no image_info for id " + id);
      }
      imageInfo.moderator_score = moderator_score;
      imageInfoDao.update(imageInfo);
      return true;
    } catch (SQLException ex) {
      Logger.getLogger(ImageUtils.class.getName()).log(Level.SEVERE, null, ex);
      return false;
    }
  }
```


```
  public static boolean writeLikesForUser(int id, int vote, String email) {
    try {
      Dao<ImageVote> imageVoteDao = Dao.<ImageVote>getDao(ImageVote.class, TralevDaoProperties.getInstance());
      ImageVote existingVote = imageVoteDao.readFirst("image_id = ? and username = ?", id, email);
      ImageVote imageVote = new ImageVote();
      if (existingVote != null) {
        imageVote = existingVote;
      }
      imageVote.image_id = id;
      imageVote.month = DateTimeUtils.getMonth();
      imageVote.year = DateTimeUtils.getYear();
      imageVote.username = email;
      imageVote.vote = vote;
      if (existingVote != null) {
        imageVoteDao.update(imageVote);
      } else {
        imageVoteDao.create(imageVote);
      }
      return true;
    } catch (SQLException ex) {
      Logger.getLogger(ImageVoteUtils.class.getName()).log(Level.SEVERE, null, ex);
    }
    return false;
  }
```