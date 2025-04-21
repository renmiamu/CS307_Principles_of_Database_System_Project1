package Types;

public class SupplyCenter {
    String supply_center;
    String director;
    public static String path = "resources/supply_Center.txt";
    public SupplyCenter(String supply_center, String director){
        this.supply_center = supply_center;
        this.director = director;
    }
}
