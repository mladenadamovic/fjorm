# Introduction #

Cursors are the way to enable application to use data from the select query without loading all applicable rows in the memory.

Fjorm supports cursors with cursor(...) methods in Dao objects.


# Example #

This example iterates votes of images on Tralev.com website without loading them all in the memory. It prints the number of positive votes and negative votes in `image_vote` table:

```
      Dao<ImageVote> imageVoteDao = Dao.<ImageVote>getDao(ImageVote.class, TralevDaoProperties.getInstance());
      int positiveVotes = 0;
      int negativeVotes = 0;
      Iterator<ImageVote> imageVoteCursorAllTable = imageVoteDao.cursor("" /* you can put your where clause here */);
      while (imageVoteCursorAllTable.hasNext()) {
        ImageVote imageVote = imageVoteCursorAllTable.next();
        if (imageVote.vote > 0) {
          positiveVotes++;
        } else if (imageVote.vote < 0) {
          negativeVotes++;
        }
      }
      System.out.println("Positive votes = " + positiveVotes + ", negative votes = " + negativeVotes);
```

`Cursor` method supports the same parameters as `read` method and returns `iterator` for data which will be fetched one by one.

For example, to iterate only positive votes in `image_vote` table:
```
      Iterator<ImageVote> imageVoteCursorAllTable = imageVoteDao.cursor("vote > 0");
```



