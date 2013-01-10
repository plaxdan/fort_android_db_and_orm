# Creating SQLite Database From Script Example

Basic example of creating an SQLite database from an SQL script in an Android application.

### Advantage
- With this approach you avoid duplicating the actual database file on the handset
  - (the sql file in this example is 400b, the equivalent SQLite DB is 5kb)

### Disadvantage
- If you have a large number of insert statements to execute then this approach could be slow
