
# --- !Ups

CREATE TABLE account (
  id        BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  email     VARCHAR(255) NOT NULL,
  password  VARCHAR(255) NOT NULL,
  salt      VARCHAR(255) NOT NULL,
  created   DATETIME NOT NULL
);

CREATE TABLE account_setting (
  id        BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  name      VARCHAR(255) NOT NULL,
  value     VARCHAR(255) NOT NULL,
  created   DATETIME NOT NULL
);
 
# --- !Downs

DROP TABLE account;
DROP TABLE account_setting;
