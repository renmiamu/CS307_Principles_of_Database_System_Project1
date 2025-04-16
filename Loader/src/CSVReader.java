import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import Types.*;

public class CSVReader {
    public static void main(String[] args) {
        String filePath = "D:\\2025_Spring\\Database\\Projects\\Project1\\output25S.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            try{
                BufferedWriter supplyCenterWriter = new BufferedWriter(new FileWriter("resources/supply_Center.txt"));
                BufferedWriter regionWriter = new BufferedWriter(new FileWriter("resources/Region.txt"));
                BufferedWriter enterpriseWriter = new BufferedWriter(new FileWriter("resources/Enterprise.txt"));
                BufferedWriter contractWriter = new BufferedWriter(new FileWriter("resources/Contract.txt"));
                BufferedWriter productsWriter = new BufferedWriter(new FileWriter("resources/Products.txt"));
                BufferedWriter product_modelsWriter = new BufferedWriter(new FileWriter("resources/Product_models.txt"));
                BufferedWriter SalesmanWriter = new BufferedWriter(new FileWriter("resources/Salesman.txt"));
                BufferedWriter Contract_detailsWriter = new BufferedWriter(new FileWriter("resources/Contract_details.txt"));

                Set<String> supply_centers = new HashSet<>();
                int supply_center_index = 2;
                int director_index = 14;

                HashMap<String, Integer> regions = new HashMap<>();
                Set<String> cities = new HashSet<>();
                Set<String> countries = new HashSet<>();
                int city_index = 4;
                int country_index = 3;
                int region_id = 1;

                HashMap<String, Integer> enterprises = new HashMap<>();
                Set<String> enterprise_names = new HashSet<>();
                int enterprise_index = 1;
                int enterprise_id = 1;
                int industry_index = 5;

                Set<String> contract_numbers = new HashSet<>();
                int contract_index = 0;
                int contract_date_index = 11;

                Set<String> product_codes = new HashSet<>();
                int product_code_index = 6;
                int product_name_index = 7;

                HashMap<String, Integer> product_models = new HashMap<>();
                int model_id = 1;
                int model_name_index = 8;
                int model_price_index = 9;

                Set<String> salesman_names_and_num = new HashSet<>();
                HashMap<String, Integer> salesman_ids = new HashMap<>();
                int salesman_id = 1;
                int salesman_number_index = 16;
                int salesman_name_index = 15;
                int geder_index = 17;
                int age_index = 18;
                int phone_index = 19;

                Set<String> contract_details = new HashSet<>();
                int quantaty_index = 10;
                int delivery_date_index = 12;
                int logement_index = 13;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    //SupplyCenter.txt
                    String supply_center = values[supply_center_index];
                    String director = values[director_index];
                    if (!supply_centers.contains(supply_center)) {
                        supply_centers.add(supply_center);
                        supplyCenterWriter.write(supply_center + ";" + director);
                        supplyCenterWriter.write('\n');
                        supplyCenterWriter.flush();
                    }
                    //Region.txt
                    String city_name = values[city_index];
                    String country_name = values[country_index];
                    Region region;
                    if (city_name.equals("")){
                        if (!countries.contains(country_name)) {
                            countries.add(country_name);
                            region = new Region(region_id, city_name, country_name);
                            regions.put(city_name + country_name, region_id);
                            regionWriter.write( region_id++ + ";" + ";" + country_name);
                            regionWriter.write('\n');
                            regionWriter.flush();
                        }
                    }
                    else{
                        if (!cities.contains(city_name)) {
                            cities.add(city_name);
                            region = new Region(region_id, city_name, country_name);
                            regions.put(city_name + country_name, region_id);
                            regionWriter.write(region_id++ + ";" + city_name + ";" + country_name);
                            regionWriter.write('\n');
                            regionWriter.flush();
                        }
                    }
                    //Enterprise.txt
                    String enterprise_name = values[enterprise_index];
                    if (enterprise_name.contains("'")){
                        enterprise_name = enterprise_name.replace("'", "''");
                    }
                    String industry = values[industry_index];
                    if (!enterprise_names.contains(enterprise_name)){
                        Enterprise enterprise = new Enterprise(enterprise_id, enterprise_name,
                                industry, supply_center, regions.get(city_name + country_name));
                        enterprise_names.add(enterprise_name);
                        enterprises.put(enterprise_name, enterprise_id);
                        enterpriseWriter.write(enterprise_id + ";" + enterprise_name + ";"
                        + industry + ";" + supply_center + ";" + regions.get(city_name + country_name));
                        enterpriseWriter.write('\n');
                        enterpriseWriter.flush();
                        enterprise_id ++;
                    }
                    //Contract.txt
                    String contract_number = values[contract_index];
                    String contract_date = values[contract_date_index];
                    if (!contract_numbers.contains(contract_number)) {
                        contract_numbers.add(contract_number);
                        contractWriter.write(contract_number + ";" + contract_date +
                                ";" + enterprises.get(enterprise_name));
                        contractWriter.write('\n');
                        contractWriter.flush();
                    }
                    //Product.txt
                    String product_code = values[product_code_index];
                    String product_name = values[product_name_index];
                    if (product_name.contains("'")) product_name = product_name.replace("'", "''");
                    if (!product_codes.contains(product_code)){
                        product_codes.add(product_code);
                        Products product = new Products(product_code, product_name);
                        productsWriter.write(product_code + ";" + product_name);
                        productsWriter.write('\n');
                        productsWriter.flush();
                    }
                    //Product_models.txt
                    String model_name = values[model_name_index];
                    if (model_name.contains("'")) model_name = model_name.replace("'",",,");
                    String model_price = values[model_price_index];
                    if (!product_models.containsKey(model_name)){
                        product_models.put(model_name, model_id);
                        product_modelsWriter.write(model_id + ";"
                                + model_name + ";" + product_code + ";" + model_price);
                        product_modelsWriter.write('\n');
                        product_modelsWriter.flush();
                        model_id ++;
                    }
                    //Salesman.txt
                    String salesman_name = values[salesman_name_index];
                    String salesman_number = values[salesman_number_index];
                    String gender = values[geder_index];
                    String age = values[age_index];
                    String phone = values[phone_index];
                    if (!salesman_names_and_num.contains(salesman_name + salesman_number)){
                        salesman_names_and_num.add(salesman_name + salesman_number);
                        salesman_ids.put(salesman_name + salesman_number, salesman_id);
                        SalesmanWriter.write(salesman_id + ";" + salesman_number + ";"
                                + salesman_name + ";" + gender + ";" + age + ";" + phone + ";" +supply_center);
                        SalesmanWriter.write('\n');
                        SalesmanWriter.flush();
                        salesman_id ++;
                    }
                    //Contract_details.txt
                    String quantity = values[quantaty_index];
                    String delivery_date = values[delivery_date_index];
                    String logement = values[logement_index];
                    int md_id = product_models.get(model_name);
                    if (!contract_details.contains(contract_number + md_id + delivery_date)){
                        contract_details.add(contract_number + md_id + delivery_date);
                        Contract_detailsWriter.write(contract_number + ";" + md_id + ";"
                                + salesman_ids.get(salesman_name + salesman_number) + ";"
                                + quantity + ";" + delivery_date + ";" + logement);
                        Contract_detailsWriter.write('\n');
                        Contract_detailsWriter.flush();
                    }
                }

                supplyCenterWriter.flush();
                regionWriter.flush();
                enterpriseWriter.flush();
                contractWriter.flush();
                productsWriter.flush();
                product_modelsWriter.flush();
                SalesmanWriter.flush();
                Contract_detailsWriter.flush();

                supplyCenterWriter.close();
                regionWriter.close();
                enterpriseWriter.close();
                contractWriter.close();
                productsWriter.close();
                product_modelsWriter.close();
                SalesmanWriter.close();
                Contract_detailsWriter.close();
            } catch (IOException e) {
                System.err.println("Error writing txt file: " + e.getMessage());
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }

    }
}
