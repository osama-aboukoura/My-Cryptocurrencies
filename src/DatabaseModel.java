import java.sql.*;
import java.util.ArrayList;

public class DatabaseModel {

    private ArrayList<String> dates;
    private ArrayList<String> currency1Prices;
    private ArrayList<String> currency2Prices;

    /**
     * Creates a new database if database doesn't exist,
     * otherwise connects to the existing database.
     * @return
     */
    private Connection connectToDatabase() {
        String url = "jdbc:sqlite:database.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    /**
     * Creates a new table in the database with 3 columns
     * 1st col for date, the 2nd and 3rd for prices of 2 given currencies
     * @param currency1 String
     * @param currency2 String
     * @return returns the name of the created table
     */
    public String createNewTable(String currency1, String currency2) {

        // removing dashes as they cause errors in sql statements
        currency1 = currency1.replaceAll("-", "");
        currency2 = currency2.replaceAll("-", "");

        String tableName = currency1 + "_vs_" + currency2;

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName
                + " (datestamp TEXT, " + currency1 + " REAL, " + currency2 + " REAL);";

        try (Connection conn = this.connectToDatabase();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        // clearing the table so we don't have duplicate records if function called again with same parameters
        deleteAllRecordsInTable(tableName);

        return tableName;
    }

    /**
     * takes the name of the table, the date and price of the 2 currencies
     * and inserts a single row in the given table in the database with the given data
     * @param tableName String
     * @param datestamp String
     * @param currency1 Double
     * @param currency2 Double
     */
    public void insert(String tableName, String datestamp, double currency1, double currency2) {

        String sql_insert = "INSERT INTO " + tableName + " VALUES(?,?,?)";

        try (Connection conn = this.connectToDatabase();
             PreparedStatement pstmt = conn.prepareStatement(sql_insert)) {
            pstmt.setString(1, datestamp);
            pstmt.setDouble(2, currency1);
            pstmt.setDouble(3, currency2);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * deletes all records from a given table
     * @param tableName String
     */
    private void deleteAllRecordsInTable(String tableName){

        String sql_delete = "DELETE FROM " + tableName ;

        try (Connection conn = this.connectToDatabase();
             PreparedStatement pstmt = conn.prepareStatement(sql_delete)) {
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void fetchAndFillDateAndPricesArrayLists(String tableName){

        resetAllArrays();

        String sql = "SELECT * FROM " + tableName;

        try (Connection conn = this.connectToDatabase();
             Statement stmt  = conn.createStatement();
             ResultSet result = stmt.executeQuery(sql)){

            while (result.next()) {
                dates.add(result.getString(1));
                currency1Prices.add(result.getString(2));
                currency2Prices.add(result.getString(3));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @return ArrayList<String> of Dates
     */
    public ArrayList<String> getDates() {
        return dates;
    }

    /**
     * @return ArrayList<String> of currency1 prices
     */
    public ArrayList<String> getCurrency1Prices() {
        return currency1Prices;
    }

    /**
     * @return ArrayList<String> of currency2 prices
     */
    public ArrayList<String> getCurrency2Prices() {
        return currency2Prices;
    }

    /**
     * initialise dates and prices arrays
     */
    public void resetAllArrays(){
        dates = new ArrayList<String>();;
        currency1Prices = new ArrayList<String>();;
        currency2Prices = new ArrayList<String>();;
    }

    /**
     * @return ArrayList<String> of database table names
     */
    public ArrayList<String> getAllTableNamesFromDatabase(){

        ArrayList<String> tableNames = new ArrayList<String>();

        String sql = "SELECT * FROM sqlite_master WHERE type = 'table'";
        try (Connection conn = this.connectToDatabase();
             Statement stmt  = conn.createStatement();
             ResultSet result = stmt.executeQuery(sql)){
            while (result.next()) {
                tableNames.add(result.getString("name")); // returns table name
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return tableNames;
    }

    public void dropTable(String tableName){
        String sql_drop = "DROP TABLE IF EXISTS " + tableName + ";";

        try (Connection conn = this.connectToDatabase();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql_drop);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
