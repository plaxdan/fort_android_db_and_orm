
CREATE TABLE contacts(
  id integer primary key,
  name text
);
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
INSERT INTO contacts VALUES(1,'The Hive');
INSERT INTO contacts VALUES(2,'Oskar Blues');
INSERT INTO contacts VALUES(3,'Pinball Jones');
INSERT INTO addresses VALUES(1,1,'117 E. Mountain','Suite 222','Fort Collins','Colorado',80524);
INSERT INTO addresses VALUES(2,2,'1800 Pike Road',NULL,'Longmont','Colorado',80501);
INSERT INTO addresses VALUES(3,2,'303 Main Street',NULL,'Lyons','Colorado',80540);
INSERT INTO addresses VALUES(4,3,'107 Linden Street',NULL,'Fort Collins','Colorado',80521);
