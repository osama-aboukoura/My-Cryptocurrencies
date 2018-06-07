public class Main {

    public static void main(String[] args) {

        Model model = new Model();
        View view = new View(model);

        view.setVisible(true);

        model.createNewTable();

        // test
        model.getCurrencyInfoFromWeb("bitcoin", 20180601, 20180607);
    }

}
