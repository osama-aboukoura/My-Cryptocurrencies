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


}
