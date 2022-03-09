--## Use to run postgres db docker image
--# docker-compose -f stack.yml up

--#Create database and service accounts (login with super account in order to do this!)
CREATE database sportbuddydb;

CREATE USER sportbuddy_prod WITH PASSWORD 'sportbuddy';
CREATE USER sportbuddy_dev WITH PASSWORD 'sportbuddy';

--#Creating schemas with owners (select database)

CREATE SCHEMA sb_prod AUTHORIZATION sportbuddy_prod;
CREATE SCHEMA sb_dev AUTHORIZATION sportbuddy_dev;

GRANT CONNECT ON DATABASE sportbuddydb TO sportbuddy_dev;
GRANT CONNECT ON DATABASE sportbuddydb TO sportbuddy_prod;

GRANT ALL ON schema sb_dev TO sportbuddy_dev;
GRANT ALL ON schema sb_prod TO sportbuddy_prod;

GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA sb_dev TO sportbuddy_dev;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA sb_prod TO sportbuddy_prod;

-- #If necessary:
--GRANT USAGE ON SCHEMA sb_dev TO sportbuddy_dev;
--GRANT USAGE ON SCHEMA sb_prod TO sportbuddy_prod;