package Loaders;

import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class Loader5Batch {
    private static final int BATCH_SIZE = 1000;
    private static Connection con = null;
    private static PreparedStatement stmt = null;

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
            if (con != null) {
                System.out.println("Successfully connected to the database "
                        + prop.getProperty("database") + " as " + prop.getProperty("user"));
                con.setAutoCommit(false);
            }
        } catch (SQLException e) {
            System.err.println("Database connection failed");
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void setPrepareStatement() {
        try {
            stmt = con.prepareStatement("INSERT INTO Contract_details (contract_id, model_id, salesman_id, quantity, estimated_deliver_date, lodgement_date)  " +
                    "VALUES (?,?,?,?,?,?);");
        } catch (SQLException e) {
            System.err.println("Insert statement failed");
            System.err.println(e.getMessage());
            closeDB();
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
            return Files.readAllLines(Path.of("resources/Formatted_contract_details.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadData(String line) {
        String[] lineData = line.split(";");
        if (con != null) {
            try {
                stmt.setString(1, lineData[0]);
                stmt.setInt(2, Integer.parseInt(lineData[1]));
                stmt.setInt(3, Integer.parseInt(lineData[2]));
                stmt.setInt(4, Integer.parseInt(lineData[3]));
                stmt.setDate(5, Date.valueOf((lineData[4])));
                if (lineData.length == 6) stmt.setDate(6, Date.valueOf(lineData[5]));
                else stmt.setNull(6, Types.DATE);
                stmt.addBatch();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
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

        // Empty target table
        openDB(prop);
        clearDataInTable();

        int cnt = 0;
        long start = System.currentTimeMillis();
        setPrepareStatement();
        try {
            for (String line : lines) {
                loadData(line);//do insert command
                cnt++;
                if (cnt % BATCH_SIZE == 0) {
                    stmt.executeBatch();
                    System.out.println("insert " + cnt + " data successfully!");
                    stmt.clearBatch();
                }
            }

            // Ensure that any remaining batch is executed
            if (cnt % BATCH_SIZE != 0) {
                stmt.executeBatch();
                System.out.println("insert " + (cnt % BATCH_SIZE) + " data successfully!");
            }
            con.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        closeDB();
        long end = System.currentTimeMillis();
        System.out.println(cnt + " records successfully loaded");
        System.out.println("Loading speed : " + (cnt * 1000L) / (end - start) + " records/s");

    }
}
