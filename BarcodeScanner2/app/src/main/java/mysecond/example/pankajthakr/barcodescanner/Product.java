package mysecond.example.pankajthakr.barcodescanner;

public class Product {
    private String Barcode;
    private String PName;
   private String Price;
    private String Describe;




    public Product(){

    }


    public Product(String b, String pn, String pd, String pr) {
        this.Barcode=b;
        this.PName=pn;
        this.Price=pr;
        this.Describe=pd;
    }

   @Override
    public String toString() {
        return " Product=" + PName + "\t" +
                " Price=" + Price + "\t"+
                " Description='" + Describe + '\'';
    }

    public String getBarcode() {
        return Barcode;
    }

    public void setBarcode(String barcode) {
        Barcode = barcode;
    }

    public String getPName() {
        return PName;
    }

    public void setPName(String PName) {
        this.PName = PName;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDescribe() {
        return Describe;
    }

    public void setDescribe(String describe) {
        Describe = describe;
    }

    public static Product valueOf(Product value) {
      return null;
    }
}