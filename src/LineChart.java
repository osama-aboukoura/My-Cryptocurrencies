import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.ApplicationFrame;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.util.ArrayList;

public class LineChart extends ApplicationFrame {

    private String currency1, currency2;
    private ArrayList<Tuple> currency1PricesList, currency2PricesList;

    public LineChart(String currency1, String currency2, ArrayList<Tuple> currency1PricesList, ArrayList<Tuple> currency2PricesList) {
        super("Cryptocurrency prices");

        this.currency1 = currency1;
        this.currency2 = currency2;
        this.currency1PricesList = currency1PricesList;
        this.currency2PricesList = currency2PricesList;

        JFreeChart lineChart = ChartFactory.createLineChart(
                currency1 + " vs " + currency2,
                "Date", "Price",
                createDataset(),
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 350));
        setContentPane(chartPanel);
    }

    private DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < currency1PricesList.size(); i++) {
            dataset.addValue(currency1PricesList.get(i).price, currency1, currency1PricesList.get(i).date);
        }

        return dataset;
    }

    /**
     * test function to be deleted
     */
    public static void main(String[] args) {

        // testing data
        Model model = new Model();
        int dateTo = model.getCurrentTimeStamp();
        int dateFrom = model.getStartTimeStampFrom("7");
        ArrayList<Tuple> currency1PricesList = model.getCurrencyInfoFromWeb("Dash", dateFrom, dateTo);
        ArrayList<Tuple> currency2PricesList = model.getCurrencyInfoFromWeb("Litecoin", dateFrom, dateTo);

        LineChart chart = new LineChart("Dash", "Litecoin", currency1PricesList, currency2PricesList);

        chart.pack();
        chart.setVisible(true);

    }

}
