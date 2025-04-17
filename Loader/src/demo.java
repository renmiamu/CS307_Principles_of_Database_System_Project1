import java.io.*;

public class demo {
    public static void main(String[] args) {
        try {
            BufferedReader contract_detailsReader = new BufferedReader(new FileReader("resources/Contract.txt"));
            String line;
            //contract_details
            int i = 0;
              while ((line = contract_detailsReader.readLine()) != null) {
                i++;
            }
            System.out.println(i);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}