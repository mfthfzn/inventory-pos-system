CREATE DATABASE manajemen_inventaris;

USE manajemen_inventaris;

CREATE TABLE users (
	email VARCHAR(255) NOT NULL,
    password VARCHAR(255)NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    middle_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    role VARCHAR(30) NOT NULL,
    PRIMARY KEY(email)
) ENGINE innodb;

CREATE TABLE token_sessions (
    email VARCHAR(255) NOT NULL PRIMARY KEY,
    token VARCHAR(36) NOT NULL,
    expired_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_users_token_sessions 
        FOREIGN KEY (email) 
        REFERENCES users(email)
) ENGINE=InnoDB;

DESC users;
DESC token_sessions;

TRUNCATE users;
TRUNCATE token_sessions;

DROP TABLE users;
DROP TABLE token_sessions;

INSERT INTO users VALUE ('eko@gmail.com', 'rahasia', 'Eko', 'Kurniawan', 'Khannedy', 'CASHIER');

SELECT * FROM users;
SELECT * FROM token_sessions;