import java.io.*;

public class demo {
    public static void main(String[] args) {
        try {
            BufferedReader contract_detailsReader = new BufferedReader(new FileReader("resources/Contract_details.txt"));
            String line;
            //contract_details
            int i = 1;
            while ((line = contract_detailsReader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length != 6) System.out.println(i);
                i++;
            }


            System.out.println("SQL script has been written to resources/Data.sql");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}