//import java.io.*;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.util.List;
//import java.util.Properties;
//import java.sql.*;
//
//public class LoaderBatch {
//    private static final int BATCH_SIZE = 1000;
//    private static Connection con = null;
//    private static PreparedStatement stmt = null;
//
//    private static void openDB(Properties prop) {
//        try {
//            Class.forName("org.postgresql.Driver");
//        } catch (Exception e) {
//            System.err.println("Cannot find the Postgres driver. Check CLASSPATH.");
//            System.exit(1);
//        }
//        String url = "jdbc:postgresql://" + prop.getProperty("host") + "/" + prop.getProperty("database");
//        try {
//            con = DriverManager.getConnection(url, prop);
//            if (con != null) {
//                System.out.println("Successfully connected to the database "
//                        + prop.getProperty("database") + " as " + prop.getProperty("user"));
//                con.setAutoCommit(false);
//            }
//        } catch (SQLException e) {
//            System.err.println("Database connection failed");
//            System.err.println(e.getMessage());
//            System.exit(1);
//        }
//    }
//
//    public static void setPrepareStatement(Table_info info) {
//        try {
//            stmt = con.prepareStatement(info.getSql());
//        } catch (SQLException e) {
//            System.err.println("Insert statement failed");
//            System.err.println(e.getMessage());
//            closeDB();
//            System.exit(1);
//        }
//    }
//
//    private static void closeDB() {
//        if (con != null) {
//            try {
//                if (stmt != null) {
//                    stmt.close();
//                }
//                con.close();
//                con = null;
//            } catch (Exception ignored) {
//            }
//        }
//    }
//
//    private static Properties loadDBUser() {
//        Properties properties = new Properties();
//        try {
//            properties.load(new InputStreamReader(new FileInputStream("resources/dbUser.properties")));
//            return properties;
//        } catch (IOException e) {
//            System.err.println("can not find db user file");
//            throw new RuntimeException(e);
//        }
//    }
//
//    private static List<String> loadTXTFile(Table_info info) {
//        try {
//            return Files.readAllLines(Path.of(info.path));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private static void loadData(String line, Table_info info) {
//        String[] lineData = line.split(";");
//        int size = info.attributes.length;
//        if (con != null) {
//            try {
//                for (int i = 0; i < size; i++) {
//                    stmt.setString(i + 1, lineData[i]);
//                }
//            } catch (SQLException ex) {
//                throw new RuntimeException(ex);
//            }
//        }
//    }
//
//    public static void clearDataInTable() {
//        Statement stmt0;
//        if (con != null) {
//            try {
//                stmt0 = con.createStatement();
//                stmt0.executeUpdate("DROP TABLE IF EXISTS Contract_details;\n" +
//                        "DROP TABLE IF EXISTS Salesman;\n" +
//                        "DROP TABLE IF EXISTS Product_models;\n" +
//                        "DROP TABLE IF EXISTS Products;\n" +
//                        "DROP TABLE IF EXISTS Contract;\n" +
//                        "DROP TABLE IF EXISTS Enterprise;\n" +
//                        "DROP TABLE IF EXISTS Region;\n" +
//                        "DROP TABLE IF EXISTS Supply_center;");
//                con.commit();
//                stmt0.executeUpdate("CREATE TABLE IF NOT EXISTS Supply_center\n" +
//                        "(\n" +
//                        "    supply_center VARCHAR(50) PRIMARY KEY,\n" +
//                        "    director      VARCHAR(30) NOT NULL\n" +
//                        ");\n" +
//                        "\n" +
//                        "CREATE TABLE IF NOT EXISTS Region\n" +
//                        "(\n" +
//                        "    city_id SERIAL PRIMARY KEY,\n" +
//                        "    city    VARCHAR(100) NOT NULL,\n" +
//                        "    country VARCHAR(50)  NOT NULL\n" +
//                        "\n" +
//                        ");\n" +
//                        "\n" +
//                        "CREATE TABLE IF NOT EXISTS Enterprise\n" +
//                        "(\n" +
//                        "    company_id        SERIAL PRIMARY KEY,\n" +
//                        "    client_enterprise VARCHAR(100) NOT NULL,\n" +
//                        "    industry          VARCHAR(50),\n" +
//                        "    supply_center     VARCHAR(50),\n" +
//                        "    city_id           INTEGER NOT NULL,\n" +
//                        "    CONSTRAINT supply_by FOREIGN KEY (supply_center) REFERENCES Supply_center(supply_center),\n" +
//                        "    CONSTRAINT located FOREIGN KEY (city_id) REFERENCES Region(city_id)\n" +
//                        ");\n" +
//                        "\n" +
//                        "CREATE TABLE IF NOT EXISTS Contract\n" +
//                        "(\n" +
//                        "    contract_number VARCHAR(10) PRIMARY KEY,\n" +
//                        "    contract_date   DATE,\n" +
//                        "    enterprise      INTEGER,\n" +
//                        "    CONSTRAINT contract_enterprise_fk FOREIGN KEY (enterprise) REFERENCES Enterprise(company_id)\n" +
//                        ");\n" +
//                        "\n" +
//                        "CREATE TABLE IF NOT EXISTS Products\n" +
//                        "(\n" +
//                        "    product_code VARCHAR(7) PRIMARY KEY,\n" +
//                        "    product_name VARCHAR(50) NOT NULL\n" +
//                        ");\n" +
//                        "\n" +
//                        "CREATE TABLE IF NOT EXISTS Product_models\n" +
//                        "(\n" +
//                        "    model_id           SERIAL PRIMARY KEY,\n" +
//                        "    product_model_name VARCHAR(50) NOT NULL,\n" +
//                        "    product_code       VARCHAR(7),\n" +
//                        "    unit_price         INT         NOT NULL,\n" +
//                        "    CONSTRAINT belongs_to FOREIGN KEY (product_code) REFERENCES Products(product_code)\n" +
//                        ");\n" +
//                        "\n" +
//                        "CREATE TABLE IF NOT EXISTS Salesman\n" +
//                        "(\n" +
//                        "    salesman_id     SERIAL PRIMARY KEY,\n" +
//                        "    salesman_number INT,\n" +
//                        "    salesman_name   VARCHAR(50) NOT NULL,\n" +
//                        "    gender          VARCHAR(20),\n" +
//                        "    age             INT,\n" +
//                        "    mobile_number   VARCHAR,\n" +
//                        "    supply_center   VARCHAR(50),\n" +
//                        "    CONSTRAINT salesman_supply_center_fk FOREIGN KEY (supply_center) REFERENCES Supply_center(supply_center)\n" +
//                        ");\n" +
//                        "\n" +
//                        "CREATE TABLE IF NOT EXISTS Contract_details\n" +
//                        "(\n" +
//                        "    contract_id            VARCHAR(10) NOT NULL,\n" +
//                        "    model_id               INTEGER NOT NULL,\n" +
//                        "    salesman_id            INTEGER     NOT NULL,\n" +
//                        "    quantity               INT,\n" +
//                        "    estimated_deliver_date DATE,\n" +
//                        "    lodgement_date         DATE,\n" +
//                        "    PRIMARY KEY (contract_id, model_id),\n" +
//                        "    CONSTRAINT Contract_details_fk1 FOREIGN KEY (contract_id) REFERENCES Contract (contract_number),\n" +
//                        "    CONSTRAINT product_model_salesman_fk1 FOREIGN KEY (model_id) REFERENCES Product_models (model_id),\n" +
//                        "    CONSTRAINT product_model_salesman_fk2 FOREIGN KEY (salesman_id) REFERENCES Salesman (salesman_id)\n" +
//                        ");");
//                con.commit();
//                stmt0.close();
//            } catch (SQLException ex) {
//                throw new RuntimeException(ex);
//            }
//        }
//    }
//
//    public static void main(String[] args) {
//        Table_info[] table_infos = new Table_info[8];
//        CSVReader.setTables(table_infos);
//        Properties prop = loadDBUser();
//
//        // Empty target table
//        openDB(prop);
//        clearDataInTable();
//        closeDB();
//        List<String> lines = loadTXTFile(table_infos[1]);
//        int cnt = 0;
//        long start = System.currentTimeMillis();
//        openDB(prop);
//        setPrepareStatement(table_infos[1]);
//        try {
//            for (String line : lines) {
//                loadData(line, table_infos[1]);//do insert command
//                if (cnt % BATCH_SIZE == 0 && cnt > 0) {
//                    stmt.executeBatch();
//                    System.out.println("insert " + BATCH_SIZE + " data successfully!");
//                    stmt.clearBatch();
//                }
//                cnt++;
//            }
//
//            if (cnt % BATCH_SIZE != 0) {
//                stmt.executeBatch();
//                System.out.println("insert " + cnt % BATCH_SIZE + " data successfully!");
//
//            }
//            con.commit();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//        closeDB();
//        long end = System.currentTimeMillis();
//        System.out.println(cnt + " records successfully loaded");
//        System.out.println("Loading speed : " + (cnt * 1000L) / (end - start) + " records/s");
//
//    }
//}
//
