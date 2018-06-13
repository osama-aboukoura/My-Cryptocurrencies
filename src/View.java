import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.border.EmptyBorder;

public class View extends JFrame {

    public static Database db = new Database();

    private Model model;
    private JComboBox currency1ComboBox, currency2ComboBox, periodComboBox;

    public View(Model model) {

        this.model = model;

        setTitle("My Cryptocurrencies");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(430, 300));
        setResizable(false);
        initWidgets();

    }

    private void initWidgets() {

        // top panel containing the title and the message
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("My Cryptocurrencies", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Garamond", Font.BOLD, 24));
        titleLabel.setBorder(new EmptyBorder(10,10,10,10));
        topPanel.add(titleLabel,BorderLayout.NORTH);
        JLabel messageLabel = new JLabel("<html><p>Choose any two cryptocurrencies from the lists and click submit to view a graph of their historical data</p></html>");
        messageLabel.setBorder(new EmptyBorder(10,10,10,10));
        topPanel.add(messageLabel,BorderLayout.CENTER);

        // middle panel containing the drop down lists and their labels
        JPanel middlePanel = new JPanel(new GridLayout(3,2));
        middlePanel.setBorder(new EmptyBorder(20,40,10,40));
        JLabel currency1Label = new JLabel("First Cryptocurrency:");
        JLabel currency2Label = new JLabel("Second Cryptocurrency:");
        JLabel periodLabel = new JLabel("Select the Period:");
        String[] allCurrencies = model.getListOfCryptocurrencies();
        String[] periods = {"Last 7 days", "Last 14 days", "Last 30 days"};

        currency1ComboBox = new JComboBox(allCurrencies);
        currency2ComboBox = new JComboBox(allCurrencies);
        periodComboBox = new JComboBox(periods);

        middlePanel.add(currency1Label);
        middlePanel.add(currency1ComboBox);
        middlePanel.add(currency2Label);
        middlePanel.add(currency2ComboBox);
        middlePanel.add(periodLabel);
        middlePanel.add(periodComboBox);

        // bottom panel containing the submit button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBorder(new EmptyBorder(10,10,10,10));
        JButton submitButton = new JButton("Submit");
        JButton viewDataButton = new JButton("View Saved Data");
        submitButton.setName("submitButton");
        viewDataButton.setName("viewDataButton");

        Controller controller = new Controller();
        submitButton.addActionListener(controller);
        viewDataButton.addActionListener(controller);
        bottomPanel.add(viewDataButton);
        bottomPanel.add(submitButton);

        add(topPanel, BorderLayout.NORTH);
        add(middlePanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private class Controller implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            // casting the action event into a JButton
            JButton pressedButton = (JButton) e.getSource();

            if (pressedButton.getName().equals("submitButton")) {

                int dateTo = model.getCurrentTimeStamp();
                int dateFrom = model.getStartTimeStampFrom(periodComboBox.getSelectedItem().toString());

                String currency1Name = model.prepareCurrencyName(currency1ComboBox.getSelectedItem().toString());
                String currency2Name = model.prepareCurrencyName(currency2ComboBox.getSelectedItem().toString());

                ArrayList<Tuple> currency1PricesList = model.getCurrencyInfoFromWeb(currency1Name, dateFrom, dateTo);
                ArrayList<Tuple> currency2PricesList = model.getCurrencyInfoFromWeb(currency2Name, dateFrom, dateTo);

                LineChart chart = new LineChart(db, currency1Name, currency2Name, currency1PricesList, currency2PricesList);
                chart.pack();
                chart.setVisible(true);

            } else {
                // test data
                db.selectAllRowsInTable("BitcoinCash_vs_Bitcoin");
            }
        }
    }
}
