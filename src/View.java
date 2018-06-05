import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

public class View extends JFrame {

    private Model model;
    private Controller controller;

    public View(Model model, Controller controller) {

        this.model = model;
        this.controller = controller;

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
        JComboBox currency1ComboBox = new JComboBox(allCurrencies);
        JComboBox currency2ComboBox = new JComboBox(allCurrencies);
        JComboBox periodComboBox = new JComboBox(periods);
        middlePanel.add(currency1Label);
        middlePanel.add(currency1ComboBox);
        middlePanel.add(currency2Label);
        middlePanel.add(currency2ComboBox);
        middlePanel.add(periodLabel);
        middlePanel.add(periodComboBox);

        // bottom panel containing the submit button
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(new EmptyBorder(10,10,10,10));
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(controller);
        bottomPanel.add(submitButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
        add(middlePanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

}
