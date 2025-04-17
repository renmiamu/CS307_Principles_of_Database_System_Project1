import java.io.*;

public class SQLGenerator {
    public static void main(String[] args) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("resources/Data.sql"));
            BufferedReader supplyCenterReader = new BufferedReader(new FileReader("resources/supply_Center.txt"));
            BufferedReader regionReader = new BufferedReader(new FileReader("resources/Region.txt"));
            BufferedReader enterpriseReader = new BufferedReader(new FileReader("resources/Enterprise.txt"));
            BufferedReader contractReader = new BufferedReader(new FileReader("resources/Contract.txt"));
            BufferedReader productsReader = new BufferedReader(new FileReader("resources/Products.txt"));
            BufferedReader product_modelsReader = new BufferedReader(new FileReader("resources/Product_models.txt"));
            BufferedReader salesmanReader = new BufferedReader(new FileReader("resources/Salesman.txt"));
            BufferedReader contract_detailsReader = new BufferedReader(new FileReader("resources/Contract_details.txt"));

            writer.write("DROP TABLE IF EXISTS Contract_details;\n" +
                    "DROP TABLE IF EXISTS Salesman;\n" +
                    "DROP TABLE IF EXISTS Product_models;\n" +
                    "DROP TABLE IF EXISTS Products;\n" +
                    "DROP TABLE IF EXISTS Contract;\n" +
                    "DROP TABLE IF EXISTS Enterprise;\n" +
                    "DROP TABLE IF EXISTS Region;\n" +
                    "DROP TABLE IF EXISTS Supply_center;\n" +
                    "\n" +
                    "--entities\n" +
                    "CREATE TABLE IF NOT EXISTS Supply_center\n" +
                    "(\n" +
                    "    supply_center VARCHAR(50) PRIMARY KEY,\n" +
                    "    director      VARCHAR(30) NOT NULL\n" +
                    ");\n" +
                    "\n" +
                    "CREATE TABLE IF NOT EXISTS Region\n" +
                    "(\n" +
                    "    region_id INTEGER PRIMARY KEY,\n" +
                    "    city    VARCHAR(100) NOT NULL,\n" +
                    "    country VARCHAR(50)\n" +
                    "\n" +
                    ");\n" +
                    "\n" +
                    "CREATE TABLE IF NOT EXISTS Enterprise\n" +
                    "(\n" +
                    "    company_id        INTEGER PRIMARY KEY,\n" +
                    "    client_enterprise VARCHAR(100) NOT NULL,\n" +
                    "    industry          VARCHAR(50),\n" +
                    "    supply_center     VARCHAR(50),\n" +
                    "    region_id           INTEGER NOT NULL,\n" +
                    "    CONSTRAINT supply_by FOREIGN KEY (supply_center) REFERENCES Supply_center(supply_center),\n" +
                    "    CONSTRAINT located FOREIGN KEY (region_id) REFERENCES Region(region_id)\n" +
                    ");\n" +
                    "\n" +
                    "CREATE TABLE IF NOT EXISTS Contract\n" +
                    "(\n" +
                    "    contract_number VARCHAR(10) PRIMARY KEY,\n" +
                    "    contract_date   DATE,\n" +
                    "    enterprise      INTEGER,\n" +
                    "    CONSTRAINT contract_enterprise_fk FOREIGN KEY (enterprise) REFERENCES Enterprise(company_id)\n" +
                    ");\n" +
                    "\n" +
                    "CREATE TABLE IF NOT EXISTS Products\n" +
                    "(\n" +
                    "    product_code VARCHAR(7) PRIMARY KEY,\n" +
                    "    product_name VARCHAR(200) NOT NULL\n" +
                    ");\n" +
                    "\n" +
                    "CREATE TABLE IF NOT EXISTS Product_models\n" +
                    "(\n" +
                    "    model_id           INTEGER PRIMARY KEY,\n" +
                    "    product_model_name VARCHAR  NOT NULL,\n" +
                    "    product_code       VARCHAR(7),\n" +
                    "    unit_price         INT         NOT NULL,\n" +
                    "    CONSTRAINT belongs_to FOREIGN KEY (product_code) REFERENCES Products(product_code)\n" +
                    ");\n" +
                    "\n" +
                    "CREATE TABLE IF NOT EXISTS Salesman\n" +
                    "(\n" +
                    "    salesman_id     INTEGER PRIMARY KEY,\n" +
                    "    salesman_number INT,\n" +
                    "    salesman_name   VARCHAR(50) NOT NULL,\n" +
                    "    gender          VARCHAR(20),\n" +
                    "    age             INT,\n" +
                    "    mobile_number   VARCHAR,\n" +
                    "    supply_center   VARCHAR(50),\n" +
                    "    CONSTRAINT salesman_supply_center_fk FOREIGN KEY (supply_center) REFERENCES Supply_center(supply_center)\n" +
                    ");\n" +
                    "\n" +
                    "CREATE TABLE IF NOT EXISTS Contract_details\n" +
                    "(\n" +
                    "    contract_id            VARCHAR(10) NOT NULL,\n" +
                    "    model_id               INTEGER NOT NULL,\n" +
                    "    salesman_id            INTEGER     NOT NULL,\n" +
                    "    quantity               INT,\n" +
                    "    estimated_deliver_date DATE,\n" +
                    "    lodgement_date         DATE,\n" +
                    "    PRIMARY KEY (contract_id, model_id),\n" +
                    "    CONSTRAINT Contract_details_fk1 FOREIGN KEY (contract_id) REFERENCES Contract (contract_number),\n" +
                    "    CONSTRAINT product_model_salesman_fk1 FOREIGN KEY (model_id) REFERENCES Product_models (model_id),\n" +
                    "    CONSTRAINT product_model_salesman_fk2 FOREIGN KEY (salesman_id) REFERENCES Salesman (salesman_id)\n" +
                    ");\n");
            //supply center
            String line;
            while ((line = supplyCenterReader.readLine())!= null) {
                String[] parts = line.split(";");
                writer.write("INSERT INTO Supply_center (supply_center, director) VALUES ('" + parts[0] + "','" + parts[1] + "');\n");
            }

            //region
            while ((line = regionReader.readLine())!= null) {
                String[] parts = line.split(";");
                writer.write("INSERT INTO Region (region_id, city, country) VALUES (" + parts[0] + ",'" + parts[1] + "','" + parts[2] + "');\n");
            }

            //enterprise
            while ((line = enterpriseReader.readLine())!= null) {
                String[] parts = line.split(";");
                writer.write("INSERT INTO Enterprise (company_id, client_enterprise" +
                        ", industry, supply_center, region_id) VALUES " +
                        "(" + parts[0] + ",'" + parts[1] + "','" + parts[2]
                        + "','" + parts[3] + "'," + parts[4] + ");\n");
            }

            //contract
            while ((line = contractReader.readLine()) != null) {
                String[] parts = line.split(";");
                writer.write("INSERT INTO Contract (contract_number, contract_date" +
                        ", enterprise) VALUES ('" + parts[0] + "','" + parts[1] + "',"
                        + parts[2] + ");\n");
            }

            //products
            while ((line = productsReader.readLine()) != null) {
                String[] parts = line.split(";");
                writer.write("INSERT INTO Products (product_code, product_name) VALUES ('" + parts[0] + "','" + parts[1] + "');\n");
            }

            //product_models
            while ((line = product_modelsReader.readLine()) != null) {
                String[] parts = line.split(";");
                writer.write("INSERT INTO Product_models (model_id, product_model_name" +
                        ", product_code, unit_price) VALUES (" + parts[0] + ",'" + parts[1] + "','" + parts[2] + "'," + parts[3] + ");\n");
            }

            //salesman
            while ((line = salesmanReader.readLine()) != null) {
                String[] parts = line.split(";");
                writer.write("INSERT INTO Salesman (salesman_id, salesman_number" +
                        ", salesman_name, gender, age, mobile_number, supply_center) VALUES " +
                        "(" + parts[0] + "," + parts[1] + ",'" + parts[2] + "','" + parts[3] + "'," + parts[4] + ",'" + parts[5] + "','" + parts[6] + "');\n");
            }

            //contract_details
            while ((line = contract_detailsReader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 6){
                    writer.write("INSERT INTO Contract_details (contract_id, model_id" +
                            ", salesman_id, quantity, estimated_deliver_date, lodgement_date) VALUES " +
                            "('" + parts[0] + "'," + parts[1] + "," + parts[2] + "," + parts[3] + ",'" + parts[4] + "','" + parts[5] + "');\n");

                }
                else{
                    writer.write("INSERT INTO Contract_details (contract_id, model_id" +
                            ", salesman_id, quantity, estimated_deliver_date, lodgement_date) VALUES " +
                            "('" + parts[0] + "'," + parts[1] + "," + parts[2] + "," + parts[3] + ",'" + parts[4] + "'," + "NULL" + ");\n");
                }
            }
            writer.close();
            System.out.println("SQL script has been written to resources/Data.sql");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}