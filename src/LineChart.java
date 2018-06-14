import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.ui.ApplicationFrame;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

public class LineChart extends ApplicationFrame implements WindowListener{

    private static String currency1, currency2;
    private static ArrayList<Tuple> currency1PricesList, currency2PricesList;

    private static DatabaseModel db;

    public LineChart(DatabaseModel db, String currency1, String currency2, ArrayList<Tuple> currency1PricesList, ArrayList<Tuple> currency2PricesList) {
        super("Cryptocurrency prices");

        this.db = db;
        this.currency1 = currency1;
        this.currency2 = currency2;
        this.currency1PricesList = currency1PricesList;
        this.currency2PricesList = currency2PricesList;

        JFreeChart lineChart = ChartFactory.createLineChart(
                currency1 + " vs " + currency2,
                "Date", "Price (USD)",
                createDataset(),
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(700, 600));
        setContentPane(chartPanel);

        // get a reference to the plot for further customisation...
        CategoryPlot plot = (CategoryPlot) lineChart.getPlot();
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);

    }

    private DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < currency1PricesList.size(); i++) {
            dataset.addValue(currency1PricesList.get(i).price, currency1, currency1PricesList.get(i).date);
            dataset.addValue(currency2PricesList.get(i).price, currency2, currency2PricesList.get(i).date);
        }
        return dataset;
    }

    @Override
    public void windowClosing(WindowEvent event) {
        saveDataPopUpQuestion();
    }

    public static void saveDataPopUpQuestion(){
        int dialogAnswer = JOptionPane.showConfirmDialog(
                null,
                "Do you want to save these records?");

        if (dialogAnswer == JOptionPane.YES_OPTION){

            // create a table with the 2 currencies and storing its name
            String tableName = db.createNewTable(currency1, currency2);

            // inserting data into the newly created table
            for (int i = 0; i < currency1PricesList.size(); i++){
                db.insert(
                        tableName,
                        currency1PricesList.get(i).date,
                        currency1PricesList.get(i).price,
                        currency2PricesList.get(i).price
                );
            }
            JOptionPane.showMessageDialog(
                    null,
                    "Data saved in database.db",
                    "Confimation",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }


}
