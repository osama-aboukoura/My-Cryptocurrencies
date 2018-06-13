import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

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

        Collections.reverse(date_price_list);

        return date_price_list;
    }

    public int getCurrentTimeStamp(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate now = LocalDate.now();
        return Integer.parseInt(formatter.format(now));
    }

    public int getStartTimeStampFrom(String numberOfDaysAgo){
        // this regex will extract the number from a string like 'Last 14 days'
        int days = Integer.parseInt(numberOfDaysAgo.replaceAll("[^0-9]", ""));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate now = LocalDate.now().minusDays(days);
        return Integer.parseInt(formatter.format(now));
    }
}
