# DB As Asset

This is an example of providing an Android application with a pre-populated SQLite database by copying a database file from the assets directory to 

    /data/data/$MY_APP/$MY_DATABASE 

### Advantage
- The advantage of this approach is that copying the entire database file as a byte stream is relatively fast compared to executing multiple SQL insert statements

### Disadvantage
- This approach means that your DB file exists on the handset twice (once in the assets dir and once in the /data dir)
