package Types;

public class Product_models {
    int model_id;
    String product_model_name;
    String product_code;
    int unit_price;

    public Product_models(int model_id, String product_model_name, String product_code, int unit_price) {
        this.model_id = model_id;
        this.product_model_name = product_model_name;
        this.product_code = product_code;
        this.unit_price = unit_price;
    }
}
