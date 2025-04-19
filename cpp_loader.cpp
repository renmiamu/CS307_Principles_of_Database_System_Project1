#include <pqxx/pqxx> // 包含 libpqxx 头文件
#include <iostream>
#include <fstream>



std::string file_path = "Loader/resources/Data.sql";

void load_data(pqxx::connection& C, const std::string& file, int& cnt){
    // Open the SQL file
    std::ifstream sql_file(file);
    if (!sql_file) {
        std::cerr << "Can't open SQL file" << std::endl;
        return;
    }

    std::string line;
    while (std::getline(sql_file, line)){
        if (line.rfind("INSERT", 0) == 0){
            // Start a transaction
            pqxx::work W(C);
            // Execute the SQL command
            W.exec(line);
            // Commit the transaction
            W.commit();
            cnt++;
        }
    }
}
void drop_and_create_table(pqxx::connection& C){
    std::string drop_table=R"(DROP TABLE IF EXISTS Contract_details;
    DROP TABLE IF EXISTS Salesman;
    DROP TABLE IF EXISTS Product_models;
    DROP TABLE IF EXISTS Products;
    DROP TABLE IF EXISTS Contract;
    DROP TABLE IF EXISTS Enterprise;
    DROP TABLE IF EXISTS Region;
    DROP TABLE IF EXISTS Supply_center)";
    std::string create_table=R"(CREATE TABLE IF NOT EXISTS Supply_center
                            (
                                supply_center VARCHAR(50) PRIMARY KEY,
                                director      VARCHAR(30) NOT NULL
                            );

                            CREATE TABLE IF NOT EXISTS Region
                            (
                                region_id INTEGER PRIMARY KEY,
                                city    VARCHAR(100) NOT NULL,
                                country VARCHAR(50)

                            );

                            CREATE TABLE IF NOT EXISTS Enterprise
                            (
                                company_id        INTEGER PRIMARY KEY,
                                client_enterprise VARCHAR(100) NOT NULL,
                                industry          VARCHAR(50),
                                supply_center     VARCHAR(50),
                                region_id           INTEGER NOT NULL,
                                CONSTRAINT supply_by FOREIGN KEY (supply_center) REFERENCES Supply_center(supply_center),
                                CONSTRAINT located FOREIGN KEY (region_id) REFERENCES Region(region_id)
                            );

                            CREATE TABLE IF NOT EXISTS Contract
                            (
                                contract_number VARCHAR(10) PRIMARY KEY,
                                contract_date   DATE,
                                enterprise      INTEGER,
                                CONSTRAINT contract_enterprise_fk FOREIGN KEY (enterprise) REFERENCES Enterprise(company_id)
                            );

                            CREATE TABLE IF NOT EXISTS Products
                            (
                                product_code VARCHAR(7) PRIMARY KEY,
                                product_name VARCHAR(200) NOT NULL
                            );

                            CREATE TABLE IF NOT EXISTS Product_models
                            (
                                model_id           INTEGER PRIMARY KEY,
                                product_model_name VARCHAR  NOT NULL,
                                product_code       VARCHAR(7),
                                unit_price         INT         NOT NULL,
                                CONSTRAINT belongs_to FOREIGN KEY (product_code) REFERENCES Products(product_code)
                            );

                            CREATE TABLE IF NOT EXISTS Salesman
                            (
                                salesman_id     INTEGER PRIMARY KEY,
                                salesman_number INT,
                                salesman_name   VARCHAR(50) NOT NULL,
                                gender          VARCHAR(20),
                                age             INT,
                                mobile_number   VARCHAR,
                                supply_center   VARCHAR(50),
                                CONSTRAINT salesman_supply_center_fk FOREIGN KEY (supply_center) REFERENCES Supply_center(supply_center)
                            );

                            CREATE TABLE IF NOT EXISTS Contract_details
                            (
                                contract_id            VARCHAR(10) NOT NULL,
                                model_id               INTEGER NOT NULL,
                                salesman_id            INTEGER     NOT NULL,
                                quantity               INT,
                                estimated_deliver_date DATE,
                                lodgement_date         DATE,
                                PRIMARY KEY (contract_id, model_id),
                                CONSTRAINT Contract_details_fk1 FOREIGN KEY (contract_id) REFERENCES Contract (contract_number),
                                CONSTRAINT product_model_salesman_fk1 FOREIGN KEY (model_id) REFERENCES Product_models (model_id),
                                CONSTRAINT product_model_salesman_fk2 FOREIGN KEY (salesman_id) REFERENCES Salesman (salesman_id)
                            );)";
    pqxx::work txn(C);
    txn.exec(drop_table);
    txn.exec(create_table);
    txn.commit();    
}

void normal_insert(){
    try {
        pqxx::connection C(
            "dbname=proj1 "
            "user=proj "
            "password=123456 "
            "hostaddr=127.0.0.1 "
            "port=5432"
        );

        // 2. 检查连接是否成功
        if (C.is_open()) {
            std::cout << "✅ 成功连接到数据库: " << C.dbname() << std::endl;
            
        } else {
            std::cerr << "❌ 连接失败！" << std::endl;
        }

        drop_and_create_table(C);
        int cnt=0;

        auto start = std::chrono::high_resolution_clock::now();
        load_data(C, file_path, cnt);
        std::cout<<"Successfully Load"<<std::endl;
        auto end = std::chrono::high_resolution_clock::now();
        auto duration = std::chrono::duration_cast<std::chrono::milliseconds>(end - start);
        auto result_time = 1.0 * duration.count() / 1000;
        std::cout << "Execution time: " << result_time << " s" << std::endl;
        auto records_per_second=cnt/result_time;
        std::cout<<"records per second: " <<records_per_second<< " records/s"<<std::endl;

    } catch (const std::exception &e) {
        std::cerr << e.what() << std::endl;
        return;
    }
}




int main(){
    normal_insert();
    return 0;
}
