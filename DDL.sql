DROP TABLE IF EXISTS product_model_salesman;
DROP TABLE IF EXISTS product_models_product;
DROP TABLE IF EXISTS company_supply_center;
DROP TABLE IF EXISTS company_product;
DROP TABLE IF EXISTS company_region;

DROP TABLE IF EXISTS salesman;
DROP TABLE IF EXISTS product_models;
DROP TABLE IF EXISTS supply_center;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS regions;
DROP TABLE IF EXISTS companies;
--entities
CREATE TABLE if not exists companies
(
    company_id   SERIAL PRIMARY KEY,
    company_name VARCHAR(100) NOT NULL,
    industry     VARCHAR(50)
);
CREATE TABLE if not exists regions
(
    city_id SERIAL PRIMARY KEY,
    country VARCHAR(50)  NOT NULL,
    city    VARCHAR(100) NOT NULL
);
CREATE TABLE if not exists products
(
    product_code VARCHAR(7) PRIMARY KEY,
    product_name VARCHAR(50) NOT NULL
);
CREATE TABLE if not exists supply_center
(
    supply_center VARCHAR(50) PRIMARY KEY,
    director      VARCHAR(30) NOT NULL
);
CREATE TABLE if not exists product_models
(
    model_id           VARCHAR(20) PRIMARY KEY,
    product_model_name VARCHAR(50) NOT NULL,
    unit_price         INT         NOT NULL
);
CREATE TABLE if not exists salesman
(
    salesman_number INT PRIMARY KEY,
    salesman_name   VARCHAR(50) NOT NULL,
    gender          VARCHAR(20),
    age             INT,
    mobile_number   varchar
);

--relations
CREATE TABLE if not exists company_region
(
    company_id INTEGER PRIMARY KEY ,
    city_id    INTEGER NOT NULL,
    CONSTRAINT company_region_fk1 FOREIGN KEY (company_id) REFERENCES companies (company_id),
    CONSTRAINT company_region_fk2 FOREIGN KEY (city_id) REFERENCES regions (city_id)
);

CREATE TABLE if not exists company_product
(
    company_id      INTEGER     NOT NULL,
    product_code    VARCHAR(7)  NOT NULL,
    contract_number VARCHAR(10) NOT NULL,
    contract_date   DATE,
    PRIMARY KEY (company_id, product_code),
    CONSTRAINT company_product_fk1 FOREIGN KEY (company_id) REFERENCES companies (company_id),
    CONSTRAINT company_product_fk2 FOREIGN KEY (product_code) REFERENCES products (product_code)
);
CREATE TABLE if not exists company_supply_center
(
    company_id    INTEGER     PRIMARY KEY ,
    supply_center VARCHAR(50) NOT NULL,
    CONSTRAINT company_supply_center_fk1 FOREIGN KEY (company_id) REFERENCES companies (company_id),
    CONSTRAINT company_supply_center_fk2 FOREIGN KEY (supply_center) REFERENCES supply_center (supply_center)
);
CREATE TABLE if not exists product_models_product
(
    product_code VARCHAR(7)  NOT NULL,
    model_id     VARCHAR(20) PRIMARY KEY ,
    CONSTRAINT product_models_product_fk1 FOREIGN KEY (product_code) REFERENCES products (product_code),
    CONSTRAINT product_models_product_fk2 FOREIGN KEY (model_id) REFERENCES product_models (model_id)
);
CREATE TABLE if not exists product_model_salesman
(
    model_id               VARCHAR(20) NOT NULL,
    salesman_number        INT         NOT NULL,
    quantity               INT,
    estimated_deliver_date DATE,
    lodgement_date         DATE,
    PRIMARY KEY (model_id, salesman_number),
    CONSTRAINT product_model_salesman_fk1 FOREIGN KEY (model_id) REFERENCES product_models (model_id),
    CONSTRAINT product_model_salesman_fk2 FOREIGN KEY (salesman_number) REFERENCES salesman (salesman_number)
);