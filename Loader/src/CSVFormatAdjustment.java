import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVFormatAdjustment {
    public static void main(String[] args) {
        String filePath = "D:\\2025_Spring\\Database\\Projects\\Project1\\output25S.csv";
        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("\"Hong Kong, Macao and Taiwan regions of China\"")){
                    line = line.replace("\"Hong Kong, Macao and Taiwan regions of China\"",
                            "Hong Kong and Macao and Taiwan regions of China");
                }else if (line.contains("Hong Kong, Macao and Taiwan regions of China")) {
                    line = line.replace("Hong Kong, Macao and Taiwan regions of China",
                            "Hong Kong and Macao and Taiwan regions of China");
                }
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String l : lines) {
                bw.write(l);
                bw.newLine();
            }
            System.out.println("formatted！");
        } catch (IOException e) {
            System.err.println("Error writing CSV file: " + e.getMessage());
        }
    }
}
