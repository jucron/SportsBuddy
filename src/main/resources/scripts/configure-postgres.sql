--## Use to run postgres db docker image, optional if you're not using a local mysqldb
--# docker-compose -f stack.yml up

--#Create Databases

CREATE database sportbuddydb;

--#Create database service accounts

CREATE USER sportbuddy_prod WITH PASSWORD 'sportbuddy';
CREATE USER sportbuddy_dev WITH PASSWORD 'sportbuddy';

CREATE SCHEMA sb_prod AUTHORIZATION sportbuddy_prod;
CREATE SCHEMA sb_dev AUTHORIZATION sportbuddy_dev;

--#For Docker environment:
--CREATE USER 'sfg_dev_user'@'%' IDENTIFIED BY 'guru';
--CREATE USER 'sfg_prod_user'@'%' IDENTIFIED BY 'guru';

--#Database grants

GRANT CONNECT ON DATABASE sportbuddydb TO sportbuddy_dev;
GRANT CONNECT ON DATABASE sportbuddydb TO sportbuddy_prod;

--GRANT USAGE ON SCHEMA sb_dev TO sportbuddy_dev;
--GRANT USAGE ON SCHEMA sb_prod TO sportbuddy_prod;

GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA sb_dev TO sportbuddy_dev;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA sb_prod TO sportbuddy_prod;

GRANT ALL ON schema sb_dev TO sportbuddy_dev;
GRANT ALL ON schema sb_prod TO sportbuddy_prod;

GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA sb_dev TO sportbuddy_dev;

--
--GRANT SELECT ON sfg_dev.* to 'sfg_dev_user'@'localhost';
--GRANT INSERT ON sfg_dev.* to 'sfg_dev_user'@'localhost';
--GRANT DELETE ON sfg_dev.* to 'sfg_dev_user'@'localhost';
--GRANT UPDATE ON sfg_dev.* to 'sfg_dev_user'@'localhost';
--GRANT SELECT ON sfg_prod.* to 'sfg_prod_user'@'localhost';
--GRANT INSERT ON sfg_prod.* to 'sfg_prod_user'@'localhost';
--GRANT DELETE ON sfg_prod.* to 'sfg_prod_user'@'localhost';
--GRANT UPDATE ON sfg_prod.* to 'sfg_prod_user'@'localhost';
--GRANT SELECT ON sfg_dev.* to 'sfg_dev_user'@'%';
--GRANT INSERT ON sfg_dev.* to 'sfg_dev_user'@'%';
--GRANT DELETE ON sfg_dev.* to 'sfg_dev_user'@'%';
--GRANT UPDATE ON sfg_dev.* to 'sfg_dev_user'@'%';
--GRANT SELECT ON sfg_prod.* to 'sfg_prod_user'@'%';
--GRANT INSERT ON sfg_prod.* to 'sfg_prod_user'@'%';
--GRANT DELETE ON sfg_prod.* to 'sfg_prod_user'@'%';
--GRANT UPDATE ON sfg_prod.* to 'sfg_prod_user'@'%';