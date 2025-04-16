package Types;

public class Contract {
    String contractNumber;
    String contractDate;
    int enterprise;
    String path = "src/resources/Contracts.txt";
    public Contract(String contractNumber, String contractDate, int enterprise) {
        this.contractNumber = contractNumber;
        this.contractDate = contractDate;
        this.enterprise = enterprise;
    }
}
