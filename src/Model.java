import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.*;

import java.io.IOException;
import java.util.ArrayList;

public class Model {

    public String[] getListOfCryptocurrencies() {
        ArrayList<String> currencyNames = new ArrayList<String>();
        String tableOfCurrencies_html = "https://coinmarketcap.com/";
        Document doc = null;
        try {
            doc = Jsoup.connect(tableOfCurrencies_html).get();
            Elements tableElements = doc.select("table");
            Elements tableRowElements = tableElements.select("tr");
            for (int i = 0; i < tableRowElements.size(); i++) {
                if (i != 0) { // skipping the row header
                    Element row = tableRowElements.get(i);
                    Elements rowItems = row.select("td");
                    Elements links = rowItems.get(1).select("a");
                    currencyNames.add(links.get(1).text());
                }
            }
        } catch (IOException e) {
            System.out.println("Error- couldn't retrieve list of currency names!");
        }

        // converting ArrayList to array
        String[] arrayToReturn = new String[currencyNames.size()];
        for (int i = 0; i<currencyNames.size(); i++){
            arrayToReturn[i] = currencyNames.get(i);
        }
        return arrayToReturn;
    }

    private Connection connectToDatabase() {
        // SQLite connection string
        String url = "jdbc:sqlite:database.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void createNewTable() {
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS currenciesTable (datestamp TEXT, currencyPrice REAL);";

        try (Connection conn = this.connectToDatabase();
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insert(String datestamp, double currencyPrice) {
        String sql = "INSERT INTO currenciesTable(datestamp,currencyPrice) VALUES(?,?)";

        try (Connection conn = this.connectToDatabase();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, datestamp);
            pstmt.setDouble(2, currencyPrice);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * takes the name of the currency and a date range and
     * returns the currency price on each day within the given period
     * @param currencyName
     * @param from of the form yyyymmdd
     * @param to of the form yyyymmdd
     * @return an ArrayList of (date, price) tuples.
     */
    public ArrayList<Tuple> getCurrencyInfoFromWeb(String currencyName, int from, int to){
        ArrayList<Tuple> date_price_list = new ArrayList<Tuple>();

        String html = "https://coinmarketcap.com/currencies/"+currencyName+"/historical-data/?start="+from+"&end="+to;
        Document doc = null;
        try {
            doc = Jsoup.connect(html).get();
            Elements tableElements = doc.select("table");
            Elements tableRowElements = tableElements.select("tr");

            for (int i = 0; i < tableRowElements.size(); i++) {
                if (i != 0) { // skipping the row header
                    Element row = tableRowElements.get(i);
                    Elements rowItems = row.select("td");
                    String date = rowItems.get(0).text();
                    double price = Double.parseDouble(rowItems.get(4).text());
                    date_price_list.add(new Tuple(date, price));
                }
            }
        } catch (IOException e) {
            System.out.println("Error- couldn't retrieve currency data");
        }

        for (int i = 0 ; i < date_price_list.size(); i++){
            System.out.println(date_price_list.get(i));
        }

        return date_price_list;
    }

}
