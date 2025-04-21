package Types;

public class Enterprise {
    int company_id;
    String client_enterprise;
    String industry;
    String supply_center;
    int region_id;
    public static String path = "resources/Enterprise.txt";
    public Enterprise(int company_id, String client_enterprise, String industry,
                      String supply_center, int region_id){
        this.company_id = company_id;
        this.client_enterprise = client_enterprise;
        this.industry = industry;
        this.supply_center = supply_center;
        this.region_id = region_id;
    }
}
