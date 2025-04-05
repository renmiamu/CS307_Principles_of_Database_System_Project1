DROP TABLE IF EXISTS sales;
DROP TABLE IF EXISTS belong;
DROP TABLE IF EXISTS supply;
DROP TABLE IF EXISTS production;
DROP TABLE IF EXISTS location;
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
    mobile_number   INT
);

--relations
CREATE TABLE if not exists location
(
    company_id INTEGER NOT NULL,
    city_id    INTEGER NOT NULL,
    PRIMARY KEY (company_id, city_id),
    CONSTRAINT fk_company_location FOREIGN KEY (company_id) REFERENCES companies (company_id),
    CONSTRAINT fk_region_location FOREIGN KEY (city_id) REFERENCES regions (city_id)
);

CREATE TABLE if not exists production
(
    company_id      INTEGER     NOT NULL,
    product_code    VARCHAR(7)  NOT NULL,
    contract_number VARCHAR(10) NOT NULL,
    contract_date   DATE,
    PRIMARY KEY (company_id, product_code),
    CONSTRAINT fk_company_production FOREIGN KEY (company_id) REFERENCES companies (company_id),
    CONSTRAINT fk_product_production FOREIGN KEY (product_code) REFERENCES products (product_code)
);
CREATE TABLE if not exists supply
(
    company_id    INTEGER     NOT NULL,
    supply_center VARCHAR(50) NOT NULL,
    PRIMARY KEY (company_id, supply_center),
    CONSTRAINT fk_company_supply FOREIGN KEY (company_id) REFERENCES companies (company_id),
    CONSTRAINT fk_supply_center FOREIGN KEY (supply_center) REFERENCES supply_center (supply_center)
);
CREATE TABLE if not exists belong
(
    product_code VARCHAR(7)  NOT NULL,
    model_id     VARCHAR(20) NOT NULL,
    PRIMARY KEY (product_code, model_id),
    CONSTRAINT fk_product_belong FOREIGN KEY (product_code) REFERENCES products (product_code),
    CONSTRAINT fk_model_belong FOREIGN KEY (model_id) REFERENCES product_models (model_id)
);
CREATE TABLE if not exists sales
(
    model_id               VARCHAR(20) NOT NULL,
    salesman_number        INT         NOT NULL,
    quantity               INT,
    estimated_deliver_date DATE,
    lodgement_date         DATE,
    PRIMARY KEY (model_id, salesman_number),
    CONSTRAINT fk_model_model FOREIGN KEY (model_id) REFERENCES product_models (model_id),
    CONSTRAINT fk_salesman_sales FOREIGN KEY (salesman_number) REFERENCES salesman (salesman_number)
);