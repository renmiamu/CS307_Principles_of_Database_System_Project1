import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;
import java.sql.*;

public class mysql_loader {
    private static Connection con = null;
    private static Statement stmt = null;

    private static void openDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); 
        } catch (Exception e) {
            System.err.println("Cannot find the MySQL driver. Check CLASSPATH.");
            System.exit(1);
        }
        // 修改连接URL格式
        String url = "jdbc:mysql://localhost:3306/proj1";
        try {
            con = DriverManager.getConnection(url, "root", "shhl1022");
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


    private static List<String> loadTXTFile() {
        try {
            return Files.readAllLines(Path.of("Loader/resources/Data.sql"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadData(String line) {
        if (line.isEmpty()) return;
        if (!line.startsWith("INSERT")) return;
        String sql = line;
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
    Statement stmt0 = null;
    if (con != null) {
        try {
            stmt0 = con.createStatement();
            
            // 禁用外键检查
            stmt0.execute("SET FOREIGN_KEY_CHECKS = 0");
            
            // 按依赖顺序删除表（从子表到父表）
            String[] dropTables = {
                "DROP TABLE IF EXISTS Contract_details",
                "DROP TABLE IF EXISTS Salesman",
                "DROP TABLE IF EXISTS Product_models",
                "DROP TABLE IF EXISTS Products",
                "DROP TABLE IF EXISTS Contract",
                "DROP TABLE IF EXISTS Enterprise",
                "DROP TABLE IF EXISTS Region",
                "DROP TABLE IF EXISTS Supply_center"
            };
            
            for (String sql : dropTables) {
                stmt0.executeUpdate(sql);
            }
            
            // 重新启用外键检查
            stmt0.execute("SET FOREIGN_KEY_CHECKS = 1");
            
            // 按依赖顺序创建表（从父表到子表）
            stmt0.executeUpdate("CREATE TABLE IF NOT EXISTS Supply_center (" +
                "supply_center VARCHAR(50) PRIMARY KEY, " +
                "director VARCHAR(30) NOT NULL" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");
            
            stmt0.executeUpdate("CREATE TABLE IF NOT EXISTS Region (" +
                "region_id INT PRIMARY KEY, " +
                "city VARCHAR(100) NOT NULL, " +
                "country VARCHAR(50)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");
            
            stmt0.executeUpdate("CREATE TABLE IF NOT EXISTS Enterprise (" +
                "company_id INT PRIMARY KEY, " +
                "client_enterprise VARCHAR(100) NOT NULL, " +
                "industry VARCHAR(50), " +
                "supply_center VARCHAR(50), " +
                "region_id INT NOT NULL, " +
                "CONSTRAINT supply_by FOREIGN KEY (supply_center) " +
                "REFERENCES Supply_center (supply_center) ON DELETE CASCADE ON UPDATE CASCADE, " +
                "CONSTRAINT located FOREIGN KEY (region_id) " +
                "REFERENCES Region (region_id) ON DELETE CASCADE ON UPDATE CASCADE" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");
            
            stmt0.executeUpdate("CREATE TABLE IF NOT EXISTS Contract (" +
                "contract_number VARCHAR(10) PRIMARY KEY, " +
                "contract_date DATE, " +
                "enterprise INT, " +
                "CONSTRAINT contract_enterprise_fk FOREIGN KEY (enterprise) " +
                "REFERENCES Enterprise (company_id) ON DELETE CASCADE ON UPDATE CASCADE" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");
            
            stmt0.executeUpdate("CREATE TABLE IF NOT EXISTS Products (" +
                "product_code VARCHAR(7) PRIMARY KEY, " +
                "product_name VARCHAR(200) NOT NULL" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");
            
            stmt0.executeUpdate("CREATE TABLE IF NOT EXISTS Product_models (" +
                "model_id INT PRIMARY KEY, " +
                "product_model_name VARCHAR(255) NOT NULL, " +
                "product_code VARCHAR(7), " +
                "unit_price INT NOT NULL, " +
                "CONSTRAINT belongs_to FOREIGN KEY (product_code) " +
                "REFERENCES Products (product_code) ON DELETE CASCADE ON UPDATE CASCADE" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");
            
            stmt0.executeUpdate("CREATE TABLE IF NOT EXISTS Salesman (" +
                "salesman_id INT PRIMARY KEY, " +
                "salesman_number INT, " +
                "salesman_name VARCHAR(50) NOT NULL, " +
                "gender VARCHAR(20), " +
                "age INT, " +
                "mobile_number VARCHAR(20), " +
                "supply_center VARCHAR(50), " +
                "CONSTRAINT salesman_supply_center_fk FOREIGN KEY (supply_center) " +
                "REFERENCES Supply_center (supply_center) ON DELETE CASCADE ON UPDATE CASCADE" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");
            
            stmt0.executeUpdate("CREATE TABLE IF NOT EXISTS Contract_details (" +
                "contract_id VARCHAR(10) NOT NULL, " +
                "model_id INT NOT NULL, " +
                "salesman_id INT NOT NULL, " +
                "quantity INT, " +
                "estimated_deliver_date DATE, " +
                "lodgement_date DATE, " +
                "PRIMARY KEY (contract_id, model_id), " +
                "CONSTRAINT Contract_details_fk1 FOREIGN KEY (contract_id) " +
                "REFERENCES Contract (contract_number) ON DELETE CASCADE ON UPDATE CASCADE, " +
                "CONSTRAINT product_model_salesman_fk1 FOREIGN KEY (model_id) " +
                "REFERENCES Product_models (model_id) ON DELETE CASCADE ON UPDATE CASCADE, " +
                "CONSTRAINT product_model_salesman_fk2 FOREIGN KEY (salesman_id) " +
                "REFERENCES Salesman (salesman_id) ON DELETE CASCADE ON UPDATE CASCADE" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");
            
            stmt0.close();
        } catch (SQLException ex) {
            try {
                if (stmt0 != null) {
                    stmt0.execute("SET FOREIGN_KEY_CHECKS = 1"); // 确保恢复外键检查
                }
            } catch (SQLException e) {
                // 忽略此异常
            }
            throw new RuntimeException("数据库操作失败: " + ex.getMessage(), ex);
        }
    }
}

    public static void main(String[] args) {
        List<String> lines = loadTXTFile();

        // Empty target table
        openDB();
        clearDataInTable();

        int cnt = 0;
        long start = System.currentTimeMillis();
        for (String line : lines) {
            loadData(line);//do insert command
            cnt++;
            if (cnt % 1000 == 0) {
                System.out.println("insert " + 1000 + " data successfully!");
            }
        }
        closeDB();
        long end = System.currentTimeMillis();
        System.out.println(cnt + " records successfully loaded");
        System.out.println("running time: "+(end-start)/1000);
        System.out.println("Loading speed : " + (cnt * 1000L) / (end - start) + " records/s");
    }
}

