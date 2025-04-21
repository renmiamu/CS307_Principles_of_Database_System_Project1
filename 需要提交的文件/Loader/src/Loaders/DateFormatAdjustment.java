package Loaders;

import java.io.*;

public class DateFormatAdjustment {
    public static void main(String[] args) {
        File inputFile = new File("resources/Contract_details.txt");
        File outputFile = new File("resources/Formatted_contract_details.txt");

        try (
                BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] lineData = line.split(";");
                if (lineData.length < 5) {
                    writer.write(line);
                    writer.newLine();
                    continue;
                }

                lineData[4] = lineData[4].replace('/', '-');

                if (lineData.length == 6 && !lineData[5].equalsIgnoreCase("NULL") && !lineData[5].isBlank()) {
                    lineData[5] = lineData[5].replace('/', '-');
                }

                writer.write(String.join(";", lineData));
                writer.newLine();
            }

            System.out.println("formatted ：" + outputFile.getPath());

        } catch (IOException e) {
            System.err.println("failed：" + e.getMessage());
        }
    }
}
