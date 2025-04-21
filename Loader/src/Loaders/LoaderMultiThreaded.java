package Loaders;

import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.concurrent.*;

public class LoaderMultiThreaded {

    private static final int THREAD_COUNT = 4;
    private static final int BATCH_SIZE = 1000;
    private static final String FILE_PATH = "resources/Formatted_contract_details.txt";

    public static void main(String[] args) {
        List<String> lines = loadFile(FILE_PATH);
        Properties prop = loadDBUser();

        clearDataInTable(prop);
        disableTrigger(prop);

        int total = lines.size();
        int chunkSize = (total + THREAD_COUNT - 1) / THREAD_COUNT;
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        long start = System.currentTimeMillis();
        List<Future<Integer>> futures = new ArrayList<>();

        for (int i = 0; i < THREAD_COUNT; i++) {
            int from = i * chunkSize;
            int to = Math.min((i + 1) * chunkSize, total);
            List<String> chunk = lines.subList(from, to);
            futures.add(executor.submit(new InsertWorker(chunk, prop, i)));
        }

        executor.shutdown();
        int totalInserted = 0;
        for (Future<Integer> f : futures) {
            try {
                totalInserted += f.get();
            } catch (Exception e) {
                throw new RuntimeException("thread execution failed", e);
            }
        }

        enableTrigger(prop);

        long end = System.currentTimeMillis();
        System.out.println("insert " + totalInserted + " data successfully!");
        System.out.println("Loading speed : " + (totalInserted * 1000L / (end - start)) + " records/s");
    }

    private static List<String> loadFile(String path) {
        try {
            return Files.readAllLines(Path.of(path));
        } catch (IOException e) {
            throw new RuntimeException("failed to open file", e);
        }
    }

    private static Properties loadDBUser() {
        Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(new FileInputStream("resources/dbUser.properties")));
            return properties;
        } catch (IOException e) {
            System.err.println("cannot load database");
            throw new RuntimeException(e);
        }
    }

    private static void clearDataInTable(Properties prop) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://" + prop.getProperty("host") + "/" + prop.getProperty("database");
            try (Connection con = DriverManager.getConnection(url, prop)) {
                Statement stmt = con.createStatement();
                stmt.executeUpdate("DROP TABLE IF EXISTS Contract_details;");
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Contract_details\n" +
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
                System.out.println("table Contract_details has been cleared");
            }
        } catch (Exception e) {
            throw new RuntimeException("failed to clear table", e);
        }
    }

    private static void disableTrigger(Properties prop) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://" + prop.getProperty("host") + "/" + prop.getProperty("database");
            try (Connection con = DriverManager.getConnection(url, prop)) {
                Statement stmt = con.createStatement();
                stmt.executeUpdate("ALTER TABLE Contract_details DISABLE TRIGGER ALL;");
                System.out.println("trigger disabled");
            }
        } catch (Exception e) {
            throw new RuntimeException("failed to disable triggers", e);
        }
    }

    private static void enableTrigger(Properties prop) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://" + prop.getProperty("host") + "/" + prop.getProperty("database");
            try (Connection con = DriverManager.getConnection(url, prop)) {
                Statement stmt = con.createStatement();
                stmt.executeUpdate("ALTER TABLE Contract_details ENABLE TRIGGER ALL;");
                System.out.println("trigger enabled");
            }
        } catch (Exception e) {
            throw new RuntimeException("failed to enable triggers", e);
        }
    }

    static class InsertWorker implements Callable<Integer> {
        private final List<String> chunk;
        private final Properties prop;
        private final int threadId;

        InsertWorker(List<String> chunk, Properties prop, int threadId) {
            this.chunk = chunk;
            this.prop = prop;
            this.threadId = threadId;
        }

        @Override
        public Integer call() {
            int count = 0;
            Connection con = null;
            PreparedStatement stmt = null;

            try {
                Class.forName("org.postgresql.Driver");
                String url = "jdbc:postgresql://" + prop.getProperty("host") + "/" + prop.getProperty("database");
                con = DriverManager.getConnection(url, prop);
                con.setAutoCommit(false);

                stmt = con.prepareStatement(
                        "INSERT INTO Contract_details (contract_id, model_id, salesman_id, quantity, estimated_deliver_date, lodgement_date) VALUES (?, ?, ?, ?, ?, ?)");

                for (String line : chunk) {
                    String[] lineData = line.split(";");
                    if (lineData.length < 5) continue;

                    stmt.setString(1, lineData[0]);
                    stmt.setInt(2, Integer.parseInt(lineData[1]));
                    stmt.setInt(3, Integer.parseInt(lineData[2]));
                    stmt.setInt(4, Integer.parseInt(lineData[3]));
                    stmt.setDate(5, java.sql.Date.valueOf(lineData[4]));

                    if (lineData.length == 6 && !lineData[5].isBlank() && !lineData[5].equalsIgnoreCase("null")) {
                        stmt.setDate(6, java.sql.Date.valueOf(lineData[5]));
                    } else {
                        stmt.setNull(6, Types.DATE);
                    }

                    stmt.addBatch();
                    count++;

                    if (count % BATCH_SIZE == 0) {
                        stmt.executeBatch();
                        stmt.clearBatch();
                        System.out.println("insert " + count + " data successfully!");
                    }
                }

                if (count % BATCH_SIZE != 0) {
                    stmt.executeBatch();
                    System.out.println("insert " + (count % BATCH_SIZE) + " data successfully!");
                }

                con.commit();
                return count;

            } catch (Exception e) {
                System.err.println("thread " + threadId + " insertion failed" + e.getMessage());
                e.printStackTrace();
                return 0;
            } finally {
                try {
                    if (stmt != null) stmt.close();
                    if (con != null) con.close();
                } catch (Exception ignored) {
                }
            }
        }
    }
}
