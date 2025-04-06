DROP TABLE IF EXISTS Product_model_salesman;
DROP TABLE IF EXISTS Product_models_product;
DROP TABLE IF EXISTS Enterprise_supply_center;
DROP TABLE IF EXISTS Enterprise_product;
DROP TABLE IF EXISTS Enterprise_region;
DROP TABLE IF EXISTS Salesman;

DROP TABLE IF EXISTS Supply_center;
DROP TABLE IF EXISTS Product_models;
DROP TABLE IF EXISTS Products;
DROP TABLE IF EXISTS Region;
DROP TABLE IF EXISTS Enterprise;

--entities
CREATE TABLE if not exists Enterprise
(
    company_id        SERIAL PRIMARY KEY,
    client_enterprise VARCHAR(100) NOT NULL,
    industry          VARCHAR(50)
);
CREATE TABLE if not exists Region
(
    city_id SERIAL PRIMARY KEY,
    country VARCHAR(50)  NOT NULL,
    city    VARCHAR(100) NOT NULL
);
CREATE TABLE if not exists Products
(
    product_code VARCHAR(7) PRIMARY KEY,
    product_name VARCHAR(50) NOT NULL
);
CREATE TABLE if not exists Supply_center
(
    supply_center VARCHAR(50) PRIMARY KEY,
    director      VARCHAR(30) NOT NULL
);
CREATE TABLE if not exists Product_models
(
    model_id           SERIAL PRIMARY KEY,
    product_model_name VARCHAR(50) NOT NULL,
    unit_price         INT         NOT NULL
);
CREATE TABLE if not exists Salesman
(
    salesman_number INT PRIMARY KEY,
    salesman_name   VARCHAR(50) NOT NULL,
    gender          VARCHAR(20),
    age             INT,
    mobile_number   varchar
);

--relations
CREATE TABLE if not exists Enterprise_region
(
    company_id INTEGER PRIMARY KEY,
    city_id    INTEGER NOT NULL,
    CONSTRAINT company_region_fk1 FOREIGN KEY (company_id) REFERENCES Enterprise (company_id),
    CONSTRAINT company_region_fk2 FOREIGN KEY (city_id) REFERENCES Region (city_id)
);

CREATE TABLE if not exists Enterprise_product
(
    company_id      INTEGER     NOT NULL,
    product_code    VARCHAR(7)  NOT NULL,
    contract_number VARCHAR(10) NOT NULL,
    contract_date   DATE,
    PRIMARY KEY (company_id, product_code),
    CONSTRAINT company_product_fk1 FOREIGN KEY (company_id) REFERENCES Enterprise (company_id),
    CONSTRAINT company_product_fk2 FOREIGN KEY (product_code) REFERENCES Products (product_code)
);
CREATE TABLE if not exists Enterprise_supply_center
(
    company_id    INTEGER PRIMARY KEY,
    supply_center VARCHAR(50) NOT NULL,
    CONSTRAINT company_supply_center_fk1 FOREIGN KEY (company_id) REFERENCES Enterprise (company_id),
    CONSTRAINT company_supply_center_fk2 FOREIGN KEY (supply_center) REFERENCES Supply_center (supply_center)
);
CREATE TABLE if not exists Product_models_product
(
    product_code VARCHAR(7) NOT NULL,
    model_id     INTEGER PRIMARY KEY,
    CONSTRAINT product_models_product_fk1 FOREIGN KEY (product_code) REFERENCES Products (product_code),
    CONSTRAINT product_models_product_fk2 FOREIGN KEY (model_id) REFERENCES Product_models (model_id)
);
CREATE TABLE if not exists Product_model_salesman
(
    model_id               INTEGER NOT NULL,
    salesman_number        INT     NOT NULL,
    quantity               INT,
    estimated_deliver_date DATE,
    lodgement_date         DATE,
    PRIMARY KEY (model_id, salesman_number),
    CONSTRAINT product_model_salesman_fk1 FOREIGN KEY (model_id) REFERENCES Product_models (model_id),
    CONSTRAINT product_model_salesman_fk2 FOREIGN KEY (salesman_number) REFERENCES Salesman (salesman_number)
);