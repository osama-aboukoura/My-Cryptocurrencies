import org.jfree.ui.ApplicationFrame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class DatabaseView extends JFrame {

    private DatabaseModel db;

    public DatabaseView(DatabaseModel db, String tableName) {
        super("Database");

        this.db = db;

        JPanel tablePanel = makeTablePanel(tableName);

        add(tablePanel);
        setVisible(true);


    }

    public JPanel makeTablePanel(String tableName){

        db.fetchAndFillDateAndPricesArrayLists("BitcoinCash_vs_Bitcoin");

        ArrayList<String> dates = db.getDates();
        ArrayList<String> currency1Prices = db.getCurrency1Prices();
        ArrayList<String> currency2Prices = db.getCurrency2Prices();

        JPanel tablePanel = new JPanel(new BorderLayout());
        JLabel tableTitle = new JLabel(tableName);
        tablePanel.add(tableTitle, BorderLayout.NORTH);

        String currency1Name = tableName.split("_vs_")[0];
        String currency2Name = tableName.split("_vs_")[1];
        String data[][] = new String[dates.size()][3];
        String column[] = {"Date", currency1Name, currency2Name};
        for (int i = 0; i < dates.size(); i++){
            data[i][0] = dates.get(i);
            data[i][1] = currency1Prices.get(i);
            data[i][2] = currency2Prices.get(i);
        }

        JTable jt = new JTable(data, column);
        JScrollPane sp = new JScrollPane(jt);
        tablePanel.add(sp, BorderLayout.CENTER);
        setSize(450, 400);

        return tablePanel;

    }
}
