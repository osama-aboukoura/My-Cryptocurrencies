public class Main {

    public static void main(String[] args) {

        Model model = new Model();
        View view = new View(model);

        view.setVisible(true);

        model.createNewTable();

        model.insert("12-Apr-2010", 145.5);
        model.insert("14-Apr-2013", 105.5);

    }

}
