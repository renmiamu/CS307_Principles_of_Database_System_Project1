package Loaders;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;
import java.sql.*;

public class Loader1Awful {
    private static Connection con = null;
    private static Statement stmt = null;

    private static void openDB(Properties prop) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {
            System.err.println("Cannot find the Postgres driver. Check CLASSPATH.");
            System.exit(1);
        }
        String url = "jdbc:postgresql://" + prop.getProperty("host") + "/" + prop.getProperty("database");
        try {
            con = DriverManager.getConnection(url, prop);
//            if (con != null) {
//                System.out.println("Successfully connected to the database "
//                        + prop.getProperty("database") + " as " + prop.getProperty("user"));
//            }
        } catch (SQLException e) {
            System.err.println("Database connection failed");
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    private static void closeDB() {
        if (con != null) {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                con.close();
                con = null;
            } catch (Exception ignored) {
            }
        }
    }

    private static Properties loadDBUser() {
        Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(new FileInputStream("resources/dbUser.properties")));
            return properties;
        } catch (IOException e) {
            System.err.println("can not find db user file");
            throw new RuntimeException(e);
        }
    }

    private static List<String> loadTXTFile() {
        try {
            return Files.readAllLines(Path.of("resources/Contract_details.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadData(String line) {
        String[] lineData = line.split(";");
        if (lineData[1].contains("'")) {
            lineData[1] = lineData[1].replace("'", "''");
        }
        String date;
        if (lineData.length < 6) date = "NULL";
        else date = '\'' + lineData[5] + '\'';
        String sql = String.format("INSERT INTO Contract_details (contract_id, model_id, salesman_id, quantity, estimated_deliver_date, lodgement_date) " +
                        "VALUES ('%s', %s, %s, %s, '%s', %s);",
                lineData[0], lineData[1], lineData[2], lineData[3], lineData[4], date);

        try {
            if (con != null) {
                stmt = con.createStatement();
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void clearDataInTable() {
        Statement stmt0;
        if (con != null) {
            try {
                stmt0 = con.createStatement();
                stmt0.executeUpdate("DROP TABLE IF EXISTS Contract_details;\n");
                stmt0.executeUpdate("CREATE TABLE IF NOT EXISTS Contract_details\n" +
                        "(\n" +
                        "    contract_id            VARCHAR(10) NOT NULL,\n" +
                        "    model_id               INTEGER     NOT NULL,\n" +
                        "    salesman_id            INTEGER     NOT NULL,\n" +
                        "    quantity               INT,\n" +
                        "    estimated_deliver_date DATE,\n" +
                        "    lodgement_date         DATE,\n" +
                        "    PRIMARY KEY (contract_id, model_id),\n" +
                        "    CONSTRAINT Contract_details_fk1 FOREIGN KEY (contract_id) REFERENCES Contract (contract_number),\n" +
                        "    CONSTRAINT product_model_salesman_fk1 FOREIGN KEY (model_id) REFERENCES Product_models (model_id),\n" +
                        "    CONSTRAINT product_model_salesman_fk2 FOREIGN KEY (salesman_id) REFERENCES Salesman (salesman_id)\n" +
                        ");");
                stmt0.close();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static void main(String[] args) {
        Properties prop = loadDBUser();
        List<String> lines = loadTXTFile();

        openDB(prop);
        clearDataInTable();
        closeDB();
        int cnt = 0;
        long start = System.currentTimeMillis();

        for (String line : lines) {
            openDB(prop);
            loadData(line);
            closeDB();
            cnt++;
            if (cnt % 1000 == 0) {
                System.out.println("Inserted " + cnt + " records...");
                break;
            }

        }

        closeDB();

        long end = System.currentTimeMillis();
        System.out.println(cnt + " records successfully loaded");
        System.out.println("Loading speed : " + (cnt * 1000L) / (end - start) + " records/s");
    }
}

