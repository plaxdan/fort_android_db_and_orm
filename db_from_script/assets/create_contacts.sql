
DROP TABLE contacts;
CREATE TABLE contacts(
  id integer primary key,
  name text
);
DROP TABLE addresses;
CREATE TABLE addresses(
  id integer primary key ,
  contact_id integer,
  addr1 text,
  addr2 text,
  city  text,
  state text,
  zip   text,
  FOREIGN KEY(contact_id) REFERENCES contacts(id)
);
INSERT INTO addresses VALUES(1,1,'117 E. Mountain','Suite 222','Fort Collins','Colorado',80524);
INSERT INTO contacts VALUES(1,'The Hive');