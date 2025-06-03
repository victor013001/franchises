CREATE TABLE franchise (
    id BIGINT NOT NULL AUTO_INCREMENT,
    uuid CHAR(36) NOT NULL UNIQUE,
    name VARCHAR(256) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE branch (
    id BIGINT NOT NULL AUTO_INCREMENT,
    uuid CHAR(36) NOT NULL UNIQUE,
    name VARCHAR(256) NOT NULL UNIQUE,
    franchise_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (franchise_id) REFERENCES franchise (id)
);

CREATE TABLE product (
    id BIGINT NOT NULL AUTO_INCREMENT,
    uuid CHAR(36) NOT NULL UNIQUE,
    name VARCHAR(256) NOT NULL UNIQUE,
    stock INT NOT NULL DEFAULT 0,
    branch_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (branch_id) REFERENCES branch (id)
);


-- REVERT
-- DROP TABLE IF EXISTS product;
-- DROP TABLE IF EXISTS branch;
-- DROP TABLE IF EXISTS franchise;
-- DELETE FROM flyway_schema_history WHERE version = '1.0';