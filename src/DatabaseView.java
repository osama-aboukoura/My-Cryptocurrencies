import org.jfree.ui.ApplicationFrame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class DatabaseView extends JFrame {

    private DatabaseModel db;

    public DatabaseView(DatabaseModel db) {
        super("Database");
        setLayout(new FlowLayout());
        setSize(520, 450);
        setMinimumSize(new Dimension(500, 450));
        setMaximumSize(new Dimension(500, Integer.MAX_VALUE));

        this.db = db;

        ArrayList<String> tableNames = db.getAllTableNamesFromDatabase();

        JPanel allTablesPanel = new JPanel(new GridLayout(tableNames.size(),1));
        for (int i = 0 ; i < tableNames.size(); i++){
            allTablesPanel.add(makeTablePanel(tableNames.get(i)));
        }

        JScrollPane scrollFrame = new JScrollPane(allTablesPanel);
        scrollFrame.setAutoscrolls(true);
        scrollFrame.setPreferredSize(new Dimension(500,450));
        add(scrollFrame);
        setVisible(true);

    }

    public JPanel makeTablePanel(String tableName){

        db.fetchAndFillDateAndPricesArrayLists(tableName);

        ArrayList<String> dates = db.getDates();
        ArrayList<String> currency1Prices = db.getCurrency1Prices();
        ArrayList<String> currency2Prices = db.getCurrency2Prices();

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setSize(450, 300);

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
        jt.setPreferredScrollableViewportSize(new Dimension(450,200));
        tablePanel.add(sp, BorderLayout.CENTER);

        return tablePanel;

    }
}
