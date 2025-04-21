package Types;

public class Region {
    int region_id;
    String city;
    String country;
    public static String path = "resources/Region.txt";
    public Region(int region_id, String city, String country){
        this.region_id = region_id;
        this.city = city;
        this.country = country;
    }
}
