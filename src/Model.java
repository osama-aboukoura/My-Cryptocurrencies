import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
}
