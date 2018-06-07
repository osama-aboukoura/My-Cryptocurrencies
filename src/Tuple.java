/**
 * Associates the price of the currency with the date
 */
public class Tuple {

    public final String date;
    public final Double price;

    public Tuple(String date, Double price) {
        this.date = date;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Date: " + date + " - Price: " + price;
    }
}