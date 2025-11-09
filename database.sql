CREATE DATABASE manajemen_inventaris;

USE manajemen_inventaris;

CREATE TABLE users (
	email VARCHAR(255) NOT NULL PRIMARY KEY,
    password VARCHAR(255)NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    middle_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    role VARCHAR(30) NOT NULL
) ENGINE innodb;

DESC users;

INSERT INTO users VALUE ('eko@gmail.com', 'rahasia', 'Eko', 'Kurniawan', 'Khannedy', 'CASHIER');

SELECT * FROM users;